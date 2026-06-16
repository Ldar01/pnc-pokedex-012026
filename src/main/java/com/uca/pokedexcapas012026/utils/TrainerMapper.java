package com.uca.pokedexcapas012026.utils;

import com.uca.pokedexcapas012026.dto.request.TrainerDTORequest;
import com.uca.pokedexcapas012026.entities.Trainer;

public class TrainerMapper {

    public static TrainerDTORequest toDTO(Trainer trainer){
        return TrainerDTORequest.builder()
                .name(trainer.getName())
                .age(trainer.getAge())
                .build();
    }

    public static Trainer toEntity(TrainerDTORequest dto){
        return Trainer.builder()
                .name(dto.name())
                .age(dto.age())
                .build();
    }

}
