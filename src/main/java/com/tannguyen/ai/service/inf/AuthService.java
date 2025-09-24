package com.tannguyen.ai.service.inf;

import com.tannguyen.ai.dto.request.AuthRequestDTO;
import com.tannguyen.ai.dto.response.AuthResponseDTO;

public interface AuthService {
    void register(String username, String password);

    AuthResponseDTO login(AuthRequestDTO request);
}
