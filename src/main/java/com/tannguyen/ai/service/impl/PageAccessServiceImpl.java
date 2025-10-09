package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.CrudSetDTO;
import com.tannguyen.ai.dto.PageAccessDTO;
import com.tannguyen.ai.dto.request.UpsertPageRequestDTO;
import com.tannguyen.ai.enums.RoleName;
import com.tannguyen.ai.model.embeddable.PageRolePermissionId;
import com.tannguyen.ai.model.primary.PageAccess;
import com.tannguyen.ai.model.primary.PageRolePermission;
import com.tannguyen.ai.model.primary.Role;
import com.tannguyen.ai.repository.primary.PageAccessRepository;
import com.tannguyen.ai.repository.primary.PageRolePermissionRepository;
import com.tannguyen.ai.repository.primary.RoleRepository;
import com.tannguyen.ai.service.inf.PageAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageAccessServiceImpl implements PageAccessService {
    private final PageAccessRepository pageRepo;
    private final PageRolePermissionRepository permRepo;
    private final RoleRepository roleRepo;

    /* ---------- Query ---------- */

    @Transactional(readOnly = true)
    public List<PageAccessDTO> list() {
        List<PageAccess> pages = pageRepo.findAll();
        Map<Long, List<PageRolePermission>> permsByPage = new HashMap<>();
        permRepo.findAll().forEach(p -> {
            permsByPage.computeIfAbsent(p.getPage().getId(), k -> new ArrayList<>()).add(p);
        });

        return pages.stream().map(p -> toDto(p, permsByPage.getOrDefault(p.getId(), List.of()))).toList();
    }

    @Transactional(readOnly = true)
    public Page<PageAccessDTO> listPagination(Pageable pageable) {
        Page<PageAccess> page = pageRepo.findAll(pageable);

        // collect ids from current page
        Set<Long> pageIds = page.getContent().stream()
                .map(PageAccess::getId)
                .collect(Collectors.toSet());

        // fetch only relevant permissions; add this derived query in your repo
        List<PageRolePermission> perms = permRepo.findByPageIdIn(pageIds);

        Map<Long, List<PageRolePermission>> permsByPage = perms.stream()
                .collect(Collectors.groupingBy(p -> p.getPage().getId()));

        // Page#map keeps all paging metadata intact
        return page.map(p ->
                toDto(p, permsByPage.getOrDefault(p.getId(), List.of()))
        );
    }

    /* ---------- Create / Update ---------- */

    @Transactional
    public PageAccessDTO create(UpsertPageRequestDTO req) {
        PageAccess page = PageAccess.builder()
                .url(req.url())
                .description(req.description())
                .name(req.name())
                .icon(req.icon())
                .build();
        page = pageRepo.save(page);
        upsertPermissions(page, req.rolePermissions());
        return get(page.getId());
    }

    @Transactional
    public PageAccessDTO update(Long id, UpsertPageRequestDTO req) {
        PageAccess page = pageRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Page not found id=" + id));
        page.setUrl(req.url());
        page.setName(req.name());
        page.setIcon(req.icon());
        page.setDescription(req.description());
        pageRepo.save(page);
        // replace permissions for the page:
        List<PageRolePermission> existing = permRepo.findByPage(page);
        permRepo.deleteAll(existing);
        upsertPermissions(page, req.rolePermissions());
        return get(page.getId());
    }

    @Transactional
    public void delete(Long id) {
        PageAccess page = pageRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Page not found id=" + id));
        permRepo.deleteAll(permRepo.findByPage(page));
        pageRepo.delete(page);
    }

    @Transactional(readOnly = true)
    public PageAccessDTO get(Long id) {
        PageAccess page = pageRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Page not found id=" + id));
        List<PageRolePermission> perms = permRepo.findByPage(page);
        return toDto(page, perms);
    }

    /* ---------- Helpers ---------- */

    private void upsertPermissions(PageAccess page, Map<String, CrudSetDTO> map) {
        if (map == null || map.isEmpty()) return;

        for (Map.Entry<String, CrudSetDTO> e : map.entrySet()) {
            RoleName rn = RoleName.valueOf(e.getKey());
            Role role = roleRepo.findByName(rn)
                    .orElseThrow(() -> new IllegalArgumentException("Role missing in DB: " + rn));

            CrudSetDTO c = e.getValue();
            PageRolePermission prp = PageRolePermission.builder()
                    .id(new PageRolePermissionId(page.getId(), role.getId()))
                    .page(page)
                    .role(role)
                    .canCreate(c.create())
                    .canRead(c.read())
                    .canUpdate(c.update())
                    .canDelete(c.delete())
                    .build();

            permRepo.save(prp);
        }
    }

    private PageAccessDTO toDto(PageAccess page, List<PageRolePermission> perms) {
        Map<String, CrudSetDTO> roleMap = new LinkedHashMap<>();
        for (PageRolePermission p : perms) {
            String roleName = p.getRole().getName().name();
            roleMap.put(roleName, new CrudSetDTO(p.isCanCreate(), p.isCanRead(), p.isCanUpdate(), p.isCanDelete()));
        }
        return new PageAccessDTO(page.getId(), page.getUrl(), page.getDescription(), roleMap, page.getName(), page.getIcon());
    }
}
