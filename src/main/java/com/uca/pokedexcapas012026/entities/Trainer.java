package com.uca.pokedexcapas012026.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @OneToMany(mappedBy = "trainer",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Pokemon> pokemons;
}
