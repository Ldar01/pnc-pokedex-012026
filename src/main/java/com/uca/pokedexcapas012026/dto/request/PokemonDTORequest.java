package com.uca.pokedexcapas012026.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PokemonDTORequest(

        @NotNull(message = "El nombre no debe de ser nulo")
        String full_name,

        @NotNull(message = "El tipo no debe de ser nulo")
        String type,

        @Min(value=1, message = "El nivel no puede ser menor que 1")
        int level,

        String weakness
) {
}
