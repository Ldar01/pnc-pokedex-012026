package com.uca.pokedexcapas012026.dto.request;

import lombok.Builder;

@Builder
public record TrainerDTORequest(
        int age,
        String name
) { }
