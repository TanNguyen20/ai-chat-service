package com.tannguyen.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FPTColumnDTO {
    private String id;
    private String label;
    private String type;
    private boolean sortable;
}