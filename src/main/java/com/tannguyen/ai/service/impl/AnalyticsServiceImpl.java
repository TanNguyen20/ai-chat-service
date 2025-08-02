package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.AnalyticsRequestDTO;
import com.tannguyen.ai.dto.response.AnalyticsResponseDTO;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.Analytics;
import com.tannguyen.ai.model.Role;
import com.tannguyen.ai.model.User;
import com.tannguyen.ai.repository.AnalyticsRepository;
import com.tannguyen.ai.repository.RoleRepository;
import com.tannguyen.ai.repository.UserRepository;
import com.tannguyen.ai.service.inf.AnalyticsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Override
    public List<AnalyticsResponseDTO> getAnalyticsList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User not found"));
        Set<Role> roles = user.getRoles();
        List<Analytics> analyticsList = analyticsRepository.findAllByUsersContainingOrRolesIn(user, roles);

        return analyticsList.stream().map(AnalyticsResponseDTO::from).toList();
    }

    @Override
    public AnalyticsResponseDTO getAnalyticsById(Long id) {
        Analytics analytics = analyticsRepository.findById(id).orElseThrow(() -> new NotFoundException("Analytics not found"));
        return AnalyticsResponseDTO.from(analytics);
    }

    @Override
    public void addAnalytics(AnalyticsRequestDTO request) {
        List<User> users = userRepository.findAllByUsernameIn(request.getUsers());
        Set<Role> roles = roleRepository.findAllByNameIn(request.getRoles());

        Analytics analytics = new Analytics();
        analytics.setUsers(users);
        analytics.setDashboardId(request.getDashboardId());
        analytics.setDashboardHost(request.getDashboardHost());
        analytics.setDashboardTitle(request.getDashboardTitle());
        analytics.setRoles(roles);

        analyticsRepository.save(analytics);
    }

    @Override
    public void deleteAnalytics(Long id) {
        analyticsRepository.deleteById(id);
    }
}