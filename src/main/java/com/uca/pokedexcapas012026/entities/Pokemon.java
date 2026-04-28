package com.uca.pokedexcapas012026.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pokemon")
@Data
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "level")
    private int level;

    @Column(name = "weakness")
    private String weakness;

}
