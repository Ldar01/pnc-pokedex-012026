package com.uca.pokedexcapas012026.controller;
import com.uca.pokedexcapas012026.entities.Pokemon;
import com.uca.pokedexcapas012026.service.PokedexService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pokedex/pokemon")
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;

    }

    @GetMapping
    public List<Pokemon> findAll(){
        return pokedexService.findAllPokemon();
    }

    @GetMapping("/{id}")
    public Pokemon getPokemonById(@PathParam("id") int id) {
        return pokedexService.findPokemonById(id);
    }

    @PostMapping
    public ResponseEntity<String> createPokemon(@RequestBody Pokemon pokemon) {
        pokedexService.createPokemon(pokemon);
        return ResponseEntity.ok("Pokemon has been created");
    }

    @PutMapping
    public ResponseEntity<String> updatePokemon(@RequestBody Pokemon pokemon) {
        pokedexService.updatePokemon(pokemon);
        return ResponseEntity.ok("Pokemon has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePokemon(@PathVariable int id) {
        pokedexService.deletePokemonById(id);
        return ResponseEntity.ok("Pokemon has been deleted");
    }

}
