package com.uca.pokedexcapas012026.service;

import com.uca.pokedexcapas012026.dto.request.PokemonDTORequest;
import com.uca.pokedexcapas012026.dto.request.TrainerDTORequest;
import com.uca.pokedexcapas012026.dto.response.TrainerDTOResponse;
import com.uca.pokedexcapas012026.entities.Pokemon;
import com.uca.pokedexcapas012026.entities.Trainer;
import com.uca.pokedexcapas012026.repository.PokedexRepository;
import com.uca.pokedexcapas012026.repository.TrainerRepository;
import com.uca.pokedexcapas012026.utils.TrainerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final PokedexRepository pokedexRepository;

    public void createTrainer(TrainerDTORequest trainer){
        trainerRepository.save(TrainerMapper.toEntity(trainer));
    };


    public void savePokemonToTrainer(PokemonDTORequest pokemon, int idTrainer){
        if (!pokedexRepository.existsByName(pokemon.full_name().toLowerCase())){
            throw new IllegalArgumentException("Pokemon with name " + pokemon.full_name() + " already exists");
        }
        //Obtenemos nuestro pokemon
        Pokemon pokemonEntity = pokedexRepository.findByName(pokemon.full_name().toLowerCase());

        //El entrenador existe
        Trainer trainer = trainerRepository.findById(idTrainer)
                .orElseThrow(() -> new IllegalArgumentException("Trainer with id " + idTrainer + " not found"));

        //Agregamos el pokemon al entrenador
        trainer.getPokemons().add(pokemonEntity);

        trainerRepository.save(trainer);

    }
}
