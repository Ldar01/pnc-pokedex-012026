package com.uca.pokedexcapas012026.controller;
import com.uca.pokedexcapas012026.entities.Pokemon;
import com.uca.pokedexcapas012026.service.PokedexService;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pokedex/pokemon")
public class PokedexController {

    private final PokedexService pokedexService;

    public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;

    }

    @GetMapping("/{id}")
    public Pokemon testPokemon(@PathVariable @PathParam("id") Integer id) {
        return pokedexService.findPokemonById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> createPokemon(@RequestBody Pokemon pokemon) {
        pokedexService.createPokemon(pokemon);
        return ResponseEntity.ok("Pokemon has been created");
    }

}
