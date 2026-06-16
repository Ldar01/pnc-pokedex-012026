package com.uca.pokedexcapas012026.dto.response;

import java.util.List;

public record TrainerDTOResponse(
        int id,
        int age,
        String name,
        List<PokemonDTOResponse> pokemons
) {
}
