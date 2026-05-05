package com.uca.pokedexcapas012026.utils;

import com.uca.pokedexcapas012026.dto.request.PokemonDTORequest;
import com.uca.pokedexcapas012026.dto.response.PokemonDTOResponse;
import com.uca.pokedexcapas012026.entities.Pokemon;

public class PokemonMapper {

    public static Pokemon toEntity(PokemonDTORequest pokemonDTORequest){
        return Pokemon.builder()
                .name(pokemonDTORequest.full_name())
                .level(pokemonDTORequest.level())
                .type(pokemonDTORequest.type())
                .weakness(pokemonDTORequest.weakness())
                .build();
    }

    public static PokemonDTOResponse toResponse(Pokemon pokemon){
        return new PokemonDTOResponse(
                pokemon.getName(),
                pokemon.getLevel()
        );
    }
}
