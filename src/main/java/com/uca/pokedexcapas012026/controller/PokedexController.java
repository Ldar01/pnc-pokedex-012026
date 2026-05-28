package com.uca.pokedexcapas012026.controller;
import com.uca.pokedexcapas012026.dto.GeneralResponse;
import com.uca.pokedexcapas012026.dto.request.PokemonDTORequest;
import com.uca.pokedexcapas012026.service.PokedexService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pokedex/pokemon")
@AllArgsConstructor
public class PokedexController {

    private final PokedexService pokedexService;

    // Injeccion de dependencias por constructor
    /*public PokedexController(PokedexService pokedexService) {
        this.pokedexService = pokedexService;

    }*/

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GeneralResponse> findAll(){
        return ResponseEntity.ok(GeneralResponse.builder()
                        .data(pokedexService.findAllPokemon())
                        .message("All pokemons found")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getPokemonById(@PathVariable int id) {
        return ResponseEntity.ok(GeneralResponse.builder()
                        .data(pokedexService.findPokemonById(id))
                        .message("Pokemon found with id: " + id)
                .build());
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createPokemon(@Valid @RequestBody PokemonDTORequest pokemon) {
        pokedexService.createPokemon(pokemon);
        return ResponseEntity.ok(GeneralResponse.builder()
                        .data(pokemon)
                        .message("Pokemon has been created")
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> updatePokemon(@PathVariable int id,@Valid @RequestBody PokemonDTORequest pokemon) {
        pokedexService.updatePokemon(id,pokemon);
        return ResponseEntity.ok(GeneralResponse.builder()
                        .data(pokemon)
                        .message("Pokemon has been updated")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deletePokemon(@PathVariable int id) {
        pokedexService.deletePokemonById(id);
        return ResponseEntity.ok(GeneralResponse.builder()
                .data(pokedexService.findPokemonById(id))
                .message("Pokemon has been deleted")
                .build());
    }

}
