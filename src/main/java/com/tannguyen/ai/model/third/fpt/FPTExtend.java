package com.tannguyen.ai.model.third.fpt;

import com.tannguyen.ai.model.third.base.FPTBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@Table(name = "fpt_extend")
public class FPTExtend extends FPTBase {
}
