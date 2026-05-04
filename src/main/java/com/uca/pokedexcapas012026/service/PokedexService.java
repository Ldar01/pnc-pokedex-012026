package com.uca.pokedexcapas012026.service;

import com.uca.pokedexcapas012026.dao.PokedexDao;
import com.uca.pokedexcapas012026.entities.Pokemon;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PokedexService {

    public PokedexDao pokedexDao;

    public void createPokemon(Pokemon pokemon){
        pokedexDao.save(pokemon);
    }

    public Pokemon findPokemonById(int id){
        return pokedexDao.findById(id);
    }

    public void deletePokemonById(int id){
        pokedexDao.deleteById(id);
    }

    public List<Pokemon> findAllPokemon(){
        return pokedexDao.findAll();
    }

    public void updatePokemon(Pokemon pokemon){
        pokedexDao.save(pokemon);
    }
}
