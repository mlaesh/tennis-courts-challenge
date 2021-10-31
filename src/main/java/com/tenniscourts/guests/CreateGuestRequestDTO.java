package com.tenniscourts.guests;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Data
public class CreateGuestRequestDTO {

    @NotNull
    private String name;
    private Long id;
}
