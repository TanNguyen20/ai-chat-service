package com.tannguyen.ai.model.secondary;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "student_ush")
public class StudentUSH extends StudentBase{
}
