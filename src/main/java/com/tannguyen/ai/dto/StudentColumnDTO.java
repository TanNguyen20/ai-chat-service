package com.tannguyen.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentColumnDTO {
    /**
     * Field name in the entity (used as accessorKey in frontend)
     */
    private String id;

    /**
     * Column label / title to show in the table header
     */
    private String label;

    /**
     * Data type of the column (e.g., text, enum, date)
     */
    private String type;

    /**
     * Whether this column is sortable
     */
    private boolean sortable;
}