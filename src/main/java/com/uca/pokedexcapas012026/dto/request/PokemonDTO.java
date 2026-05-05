package com.uca.pokedexcapas012026.dto.request;

public record PokemonDTO(
        String name,
        String type,
        int level,
        String weakness
) {
}
