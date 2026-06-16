package com.uca.pokedexcapas012026.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pokemon")
@Data
@Builder // Patron de diseño Builder
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos nuestros atributos
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
