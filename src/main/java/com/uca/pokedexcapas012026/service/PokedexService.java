package com.uca.pokedexcapas012026.service;

import com.uca.pokedexcapas012026.dto.request.PokemonDTORequest;
import com.uca.pokedexcapas012026.dto.response.PokemonDTOResponse;
import com.uca.pokedexcapas012026.entities.Pokemon;
import com.uca.pokedexcapas012026.exception.PokemonNotFound;
import com.uca.pokedexcapas012026.repository.PokedexRepository;
import com.uca.pokedexcapas012026.utils.PokemonMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PokedexService {

    private PokedexRepository pokedexRepository;

    public void createPokemon(PokemonDTORequest pokemon){
        if (pokedexRepository.existsByName(pokemon.full_name().toLowerCase())){
            throw new IllegalArgumentException("Pokemon with name " + pokemon.full_name() + " already exists");
        }

        pokedexRepository.save(PokemonMapper.toEntity(pokemon));
    }

    public PokemonDTOResponse findPokemonById(int id){
        return PokemonMapper.toResponse(pokedexRepository.findById(id).orElseThrow(
                () -> new PokemonNotFound("Pokemon not found with id " + id)
        ));
    }

    public void deletePokemonById(int id){
        pokedexRepository.deleteById(id);
    }

    public List<Pokemon> findAllPokemon(){
        return pokedexRepository.findAll();
    }

    public void updatePokemon(int id, PokemonDTORequest pokemon){
        Pokemon pokemonToUpdate = PokemonMapper.toEntity(pokemon);
        if (pokedexRepository.existsById(id)){
            pokemonToUpdate.setId(id);
        }else{
            throw new PokemonNotFound("Pokemon not found with id " + id);
        }
        pokedexRepository.save(pokemonToUpdate);
    }
}
