package com.uca.pokedexcapas012026.repository;

import com.uca.pokedexcapas012026.entities.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokedexRepository extends JpaRepository<Pokemon, Integer> {
    List<Pokemon> findByType(String type);
}
