package com.uca.pokedexcapas012026.controller;

import com.uca.pokedexcapas012026.dto.request.PokemonDTORequest;
import com.uca.pokedexcapas012026.dto.request.TrainerDTORequest;
import com.uca.pokedexcapas012026.repository.TrainerRepository;
import com.uca.pokedexcapas012026.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("pokedex/trainer")
public class TrainerController {

    private final TrainerService trainerService;

    @PostMapping
    public void createTrainer(@RequestBody TrainerDTORequest trainer) {
        trainerService.createTrainer(trainer);
    }

    @PostMapping("/{idTrainer}/pokemon")
    public void addPokemonToTrainer(@RequestBody PokemonDTORequest pokemon, @PathVariable int idTrainer){
        trainerService.savePokemonToTrainer(pokemon, idTrainer);
    }

}
