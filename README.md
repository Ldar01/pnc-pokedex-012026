# 📖 Pokédex API — pokedex-capas-012026

API REST construida con **Spring Boot** que implementa arquitectura de **N-Capas**
(Controller -> Service -> Repository -> BD) para gestionar Pokémon y Entrenadores.

---

## 🏗️ Arquitectura del proyecto

```text
controller/
  ├── PokedexController.java      -> Endpoints HTTP de Pokémon
  └── TrainerController.java      -> Endpoints HTTP de Entrenador

service/
  ├── PokedexService.java         -> Lógica de negocio de Pokémon
  └── TrainerService.java         -> Lógica de negocio de Entrenador

repository/
  ├── PokedexRepository.java      -> Acceso a datos de Pokémon
  └── TrainerRepository.java      -> Acceso a datos de Entrenador

entities/
  ├── Pokemon.java                -> Entidad JPA (tabla pokemon)
  └── Trainer.java                -> Entidad JPA (tabla trainer + trainer_pokemon)

dto/
  ├── GeneralResponse.java        -> Wrapper de respuestas exitosas
  ├── request/
  │   ├── PokemonDTORequest.java  -> DTO entrada Pokémon (con validaciones)
  │   └── TrainerDTORequest.java  -> DTO entrada Entrenador
  └── response/
      ├── PokemonDTOResponse.java -> DTO salida Pokémon (name, level)
      └── TrainerDTOResponse.java -> DTO salida Entrenador (con lista de Pokémon)

utils/
  ├── PokemonMapper.java          -> Conversión Pokemon DTO <-> Entity
  └── TrainerMapper.java          -> Conversión Trainer DTO <-> Entity

exception/
  ├── PokemonNotFound.java        -> Excepción 404 de Pokémon
  ├── ApiError.java               -> Estructura de respuesta de error
  └── GlobalExceptionHandler.java -> Manejo global (@RestControllerAdvice)
```

---

## 🛠️ Stack tecnológico

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.0.5 |
| Spring Data JPA | Incluido en Spring Boot |
| Bean Validation | Incluido en Spring Boot |
| Lombok | Última estable |
| PostgreSQL | Driver `42.x` |
| Gradle | Wrapper incluido |

---

## 🗃️ Entidades

### `Pokemon`

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Clave primaria autogenerada |
| `name` | `String` | Nombre del Pokémon |
| `type` | `String` | Tipo del Pokémon |
| `level` | `int` | Nivel del Pokémon |
| `weakness` | `String` | Debilidad del Pokémon |

### `Trainer`

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | Clave primaria autogenerada |
| `name` | `String` | Nombre del entrenador |
| `age` | `Integer` | Edad del entrenador |
| `pokemons` | `List<Pokemon>` | Pokémon del entrenador (`@ManyToMany`) |

### Relación entre entidades

```
Trainer  *──────────*  Pokemon
         trainer_pokemon
         (trainer_id, pokemon_id)
```

Un `Trainer` puede tener muchos `Pokemon` y un `Pokemon` puede pertenecer a muchos `Trainer` (`@ManyToMany` con tabla intermedia `trainer_pokemon`).

---

## 📦 DTOs

### `TrainerDTORequest` (entrada)

| Campo | Tipo | Descripción |
|---|---|---|
| `name` | `String` | Nombre del entrenador |
| `age` | `int` | Edad del entrenador |

### `TrainerDTOResponse` (salida)

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` | ID del entrenador |
| `name` | `String` | Nombre del entrenador |
| `age` | `int` | Edad del entrenador |
| `pokemons` | `List<PokemonDTOResponse>` | Pokémon asociados |

### `PokemonDTORequest` (entrada)

| Campo | Tipo | Restricción |
|---|---|---|
| `full_name` | `String` | `@NotNull` |
| `type` | `String` | `@NotNull` |
| `level` | `int` | `@Min(1)` |
| `weakness` | `String` | Sin restricción |

### `PokemonDTOResponse` (salida)

| Campo | Tipo | Descripción |
|---|---|---|
| `name` | `String` | Nombre del Pokémon |
| `level` | `int` | Nivel del Pokémon |

### `GeneralResponse` / `ApiError`

```json
// Respuesta exitosa
{ "data": { }, "message": "Texto descriptivo" }

// Respuesta de error
{ "message": "Detalle", "code": 404, "timestamp": "2026-05-17", "errors": { } }
```

---

## 📂 Repositorios

### `PokedexRepository`

| Método | Descripción |
|---|---|
| `findByType(String type)` | Busca Pokémon por tipo |
| `existsByName(String name)` | Verifica si existe un Pokémon por nombre |
| `findByName(String name)` | Busca un Pokémon por nombre |

### `TrainerRepository`

Extiende `JpaRepository<Trainer, Integer>` — CRUD básico por defecto.

---

## 🌐 Endpoints — Pokémon

Base URL: `http://localhost:8080/pokedex/pokemon`

| Método | Ruta | Descripción | OK | Error |
|---|---|---|---|---|
| `GET` | `/` | Lista todos los Pokémon | `200` | - |
| `GET` | `/{id}` | Busca Pokémon por ID | `200` | `404` |
| `POST` | `/` | Crea un Pokémon | `200` | `400` |
| `PUT` | `/{id}` | Actualiza por ID | `200` | `400`, `404` |
| `DELETE` | `/{id}` | Elimina por ID | `200` | - |

## 🌐 Endpoints — Entrenador

Base URL: `http://localhost:8080/pokedex/trainer`

| Método | Ruta | Descripción | OK | Error |
|---|---|---|---|---|
| `POST` | `/` | Crea un entrenador | `200` | - |
| `POST` | `/{idTrainer}/pokemon` | Agrega un Pokémon existente a un entrenador | `200` | `400` |

---

## 📥 Ejemplos de uso — Trainer

### Crear entrenador

```bash
curl -X POST http://localhost:8080/pokedex/trainer \
  -H "Content-Type: application/json" \
  -d '{"name": "Ash", "age": 10}'
```

### Agregar Pokémon a un entrenador

```bash
curl -X POST http://localhost:8080/pokedex/trainer/1/pokemon \
  -H "Content-Type: application/json" \
  -d '{"full_name": "pikachu", "type": "Electrico", "level": 5, "weakness": "Tierra"}'
```

> ⚠️ El Pokémon debe **existir previamente** en la BD. El servicio busca por `full_name` (en minúsculas).

---

## 📋 Métodos del Service

### `PokedexService`

| Método | Entrada | Salida |
|---|---|---|
| `createPokemon(PokemonDTORequest)` | DTO | `void` |
| `findAllPokemon()` | — | `List<Pokemon>` |
| `findPokemonById(int id)` | `int` | `PokemonDTOResponse` |
| `updatePokemon(int id, PokemonDTORequest)` | `int` + DTO | `void` |
| `deletePokemonById(int id)` | `int` | `void` |

### `TrainerService`

| Método | Entrada | Salida |
|---|---|---|
| `createTrainer(TrainerDTORequest)` | DTO | `void` |
| `savePokemonToTrainer(PokemonDTORequest, int)` | DTO + `idTrainer` | `void` |

---

## ⚙️ Configuración de base de datos

`src/main/resources/application.yml`:

```yaml
spring:
  application:
    name: pokedex-capas-012026
  datasource:
    url: jdbc:postgresql://localhost:5432/pokedex
    username: postgres
    password: root
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

Script SQL para crear las tablas manualmente (si `ddl-auto: update` no las crea):

```sql
CREATE TABLE pokemon (
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100),
    type     VARCHAR(50),
    level    INTEGER,
    weakness VARCHAR(50)
);

CREATE TABLE trainer (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100),
    age  INTEGER
);

CREATE TABLE trainer_pokemon (
    trainer_id INTEGER REFERENCES trainer(id),
    pokemon_id INTEGER REFERENCES pokemon(id),
    PRIMARY KEY (trainer_id, pokemon_id)
);
```

---

## 🚀 Ejecución local

**Prerrequisitos:** Java 21, PostgreSQL en `localhost:5432`, base de datos `pokedex` creada.

```powershell
git clone <url-del-repositorio>
cd pokedex-capas-012026
gradlew.bat bootRun
```

Disponible en: `http://localhost:8080`

---

## ✅ Mejoras ya implementadas

- Manejo de errores con `orElseThrow(() -> new PokemonNotFound(...))`
- `updatePokemon` valida existencia antes de guardar
- `GlobalExceptionHandler` con `@RestControllerAdvice` para `404` y `400`
- Validaciones en request con `@Valid`, `@NotNull`, `@Min`
- `ApiError` incluye campo `errors` con todos los campos inválidos
- Relación `@ManyToMany` entre `Trainer` y `Pokemon` con tabla intermedia `trainer_pokemon`
- `PokedexRepository` con `existsByName` y `findByName` para buscar Pokémon por nombre

---

## 🐛 Pendientes / bugs conocidos

| Severidad | Ubicación | Descripción |
|---|---|---|
| 🔴 Alta | `PokedexController.deletePokemon()` | Tras el `delete`, llama `findPokemonById(id)` — lanza `PokemonNotFound` porque ya no existe |
| 🟡 Media | `TrainerService.savePokemonToTrainer()` | La lógica de `existsByName` está invertida — lanza error si el Pokémon **existe** en vez de si **no existe** |
| 🟡 Media | `TrainerController` | No usa `ResponseEntity<GeneralResponse>` — respuestas inconsistentes con el resto de la API |
| 🟡 Media | `PokedexService.findAllPokemon()` | Devuelve `List<Pokemon>` (entidad) en lugar de `List<PokemonDTOResponse>` |
| 🟢 Baja | `PokedexService.java` | Import `@Autowired` sin usar |

---

## 👩‍💻 Autor
Proyecto educativo de arquitectura N-Capas con Spring Boot — 2026.
por Ing. Luisa Arévalo (ldarevalo@uca.edu.sv)