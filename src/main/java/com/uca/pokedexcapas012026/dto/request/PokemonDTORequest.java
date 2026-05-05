package com.uca.pokedexcapas012026.dto.request;

import lombok.Builder;

@Builder
public record PokemonDTORequest(
        String full_name,
        String type,
        int level,
        String weakness
) {
}
