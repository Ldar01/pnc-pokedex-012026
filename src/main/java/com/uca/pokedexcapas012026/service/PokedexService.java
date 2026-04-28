package com.uca.pokedexcapas012026.service;

import com.uca.pokedexcapas012026.entities.Pokemon;
import com.uca.pokedexcapas012026.repository.PokedexRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PokedexService {

    @Autowired
    private PokedexRepository pokedexRepository;

    public void createPokemon(Pokemon pokemon){
        pokedexRepository.save(pokemon);
    }

    public Pokemon findPokemonById(int id){
        return pokedexRepository.findById(id).get();
    }

    public void deletePokemonById(int id){
        pokedexRepository.deleteById(id);
    }

    public List<Pokemon> findAllPokemon(){
        return pokedexRepository.findAll();
    }

    public void updatePokemon(Pokemon pokemon){
        pokedexRepository.save(pokemon);
    }
}
