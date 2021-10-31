package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.persistence.BaseEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
@Builder
public class TennisCourt extends BaseEntity<Long> {

    @Column
    @NotNull
    private String name;
}
