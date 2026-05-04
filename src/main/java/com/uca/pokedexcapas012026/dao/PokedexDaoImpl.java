package com.uca.pokedexcapas012026.dao;

import com.uca.pokedexcapas012026.entities.Pokemon;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor // Para hacer constructor
public class PokedexDaoImpl implements PokedexDao {

    public EntityManager entityManager;

    @Override
    public List<Pokemon> findAll() {
        return entityManager.createQuery("from Pokemon", Pokemon.class).getResultList();
    }

    @Override
    public Pokemon findById(int id) {
        return entityManager.find(Pokemon.class, id);
    }

    @Override
    public void save(Pokemon pokemon) {
        entityManager.persist(pokemon);
    }

    @Override
    public void update(Pokemon pokemon) {
        entityManager.merge(pokemon);
    }

    @Override
    public void deleteById(int id) {
        Pokemon pokemon = findById(id);
        if (pokemon != null) {
            entityManager.remove(pokemon);
        }
    }
}
