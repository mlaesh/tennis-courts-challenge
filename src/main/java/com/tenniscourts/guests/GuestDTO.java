package com.tenniscourts.guests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Data
public class GuestDTO {

    @NotNull
    private String name;
    private Long id;
}
