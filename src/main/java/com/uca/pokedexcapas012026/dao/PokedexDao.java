package com.uca.pokedexcapas012026.dao;

import com.uca.pokedexcapas012026.entities.Pokemon;

import java.util.List;

public interface PokedexDao {
    List<Pokemon> findAll();
    Pokemon findById(int id);
    void save(Pokemon pokemon);
    void update(Pokemon pokemon);
    void deleteById(int id);
}
