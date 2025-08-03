package com.tannguyen.ai.service.impl;

import com.tannguyen.ai.dto.request.AnalyticsConfigRequestDTO;
import com.tannguyen.ai.dto.response.AnalyticsConfigResponseDTO;
import com.tannguyen.ai.exception.NotFoundException;
import com.tannguyen.ai.model.AnalyticsConfig;
import com.tannguyen.ai.repository.AnalyticsConfigRepository;
import com.tannguyen.ai.service.inf.AnalyticsConfigService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalyticsConfigServiceImpl implements AnalyticsConfigService {

    private AnalyticsConfigRepository repository;

    @Override
    public void create(AnalyticsConfigRequestDTO requestDTO) {
        AnalyticsConfig entity = new AnalyticsConfig();
        entity.setHostname(requestDTO.getHostname());
        entity.setUsername(requestDTO.getUsername());
        entity.setPassword(requestDTO.getPassword());

        repository.save(entity);
    }

    @Override
    public AnalyticsConfigResponseDTO getById(Long id) {
        AnalyticsConfig analyticsConfig = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return AnalyticsConfigResponseDTO.from(analyticsConfig);
    }

    @Override
    public List<AnalyticsConfigResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(AnalyticsConfigResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Long id, AnalyticsConfigRequestDTO requestDTO) {
        AnalyticsConfig entity = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not found analytics config"));

        entity.setHostname(requestDTO.getHostname());
        entity.setUsername(requestDTO.getUsername());
        entity.setPassword(requestDTO.getPassword());

        repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Not found analytics config");
        }
        repository.deleteById(id);
    }
}