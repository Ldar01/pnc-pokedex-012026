# 📖 Pokédex API — pokedex-capas-012026

API REST construida con **Spring Boot** que implementa arquitectura de **N-Capas**
(Controller -> Service -> Repository -> BD) para gestionar información de Pokémon.

---

## 🏗️ Arquitectura del proyecto

```text
controller/
  └── PokedexController.java      -> Endpoints HTTP y respuestas

service/
  └── PokedexService.java         -> Lógica de negocio

repository/
  └── PokedexRepository.java      -> Acceso a datos con Spring Data JPA

entities/
  └── Pokemon.java                -> Entidad JPA mapeada a la tabla pokemon

dto/
  ├── GeneralResponse.java        -> Wrapper de respuestas exitosas
  ├── request/
  │   └── PokemonDTORequest.java  -> DTO de entrada con validaciones
  └── response/
      └── PokemonDTOResponse.java -> DTO de salida (name, level)

utils/
  └── PokemonMapper.java          -> Conversión DTO <-> Entity

exception/
  ├── PokemonNotFound.java        -> Excepción de negocio (404)
  ├── ApiError.java               -> Estructura de errores
  └── GlobalExceptionHandler.java -> Manejo global de excepciones (@RestControllerAdvice)
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

## 🗃️ Entidad `Pokemon`

Mapeada a la tabla `pokemon` en PostgreSQL.
Anotaciones Lombok: `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.

| Campo | Tipo Java | Columna BD | Descripción |
|---|---|---|---|
| `id` | `int` | `id` | Clave primaria autogenerada |
| `name` | `String` | `name` | Nombre del Pokémon |
| `type` | `String` | `type` | Tipo del Pokémon |
| `level` | `int` | `level` | Nivel del Pokémon |
| `weakness` | `String` | `weakness` | Debilidad del Pokémon |

---

## 📦 DTOs

### `PokemonDTORequest` (entrada)

| Campo | Tipo | Restricción |
|---|---|---|
| `full_name` | `String` | `@NotNull` — "El nombre no debe de ser nulo" |
| `type` | `String` | `@NotNull` — "El tipo no debe de ser nulo" |
| `level` | `int` | `@Min(1)` — "El nivel no puede ser menor que 1" |
| `weakness` | `String` | Sin restricción |

### `PokemonDTOResponse` (salida)

| Campo | Tipo | Descripción |
|---|---|---|
| `name` | `String` | Nombre del Pokémon |
| `level` | `int` | Nivel del Pokémon |

### `GeneralResponse` — respuesta exitosa

```json
{
  "data": { },
  "message": "Texto descriptivo"
}
```

### `ApiError` — respuesta de error

```json
{
  "message": "Pokemon not found with id 5",
  "code": 404,
  "timestamp": "2026-05-12"
}
```

---

## 🛡️ Manejo de excepciones (`GlobalExceptionHandler`)

| Excepción | HTTP Status | Descripción |
|---|---|---|
| `PokemonNotFound` | `404 Not Found` | Pokémon no encontrado por ID |
| `MethodArgumentNotValidException` | `400 Bad Request` | Validación de `@Valid` fallida |

---

## 📂 Repositorio `PokedexRepository`

Extiende `JpaRepository<Pokemon, Integer>` y además incluye:

| Método | Descripción |
|---|---|
| `findByType(String type)` | Busca todos los Pokémon por tipo |

---

## 🌐 Endpoints disponibles

Base URL: `http://localhost:8080/pokedex/pokemon`

| Método | Ruta | Descripción | OK | Error |
|---|---|---|---|---|
| `GET` | `/` | Lista todos los Pokémon | `200` | - |
| `GET` | `/{id}` | Busca Pokémon por ID | `200` | `404` |
| `POST` | `/` | Crea un Pokémon | `200` | `400` |
| `PUT` | `/{id}` | Actualiza por ID | `200` | `400`, `404` |
| `DELETE` | `/{id}` | Elimina por ID | `200` | - |

---

## 📥 Ejemplos de uso

### `POST` — Crear Pokémon

```bash
curl -X POST http://localhost:8080/pokedex/pokemon \
  -H "Content-Type: application/json" \
  -d '{"full_name": "Charmander", "type": "Fuego", "level": 5, "weakness": "Agua"}'
```

Respuesta:
```json
{
  "data": {"full_name": "Charmander", "type": "Fuego", "level": 5, "weakness": "Agua"},
  "message": "Pokemon has been created"
}
```

### `GET /{id}` — Buscar por ID

```bash
curl http://localhost:8080/pokedex/pokemon/1
```

Respuesta (solo `name` y `level` via `PokemonDTOResponse`):
```json
{
  "data": {"name": "Charmander", "level": 5},
  "message": "Pokemon found with id: 1"
}
```

Si el ID no existe → `404`:
```json
{"message": "Pokemon not found with id 1", "code": 404, "timestamp": "2026-05-12"}
```

### `PUT /{id}` — Actualizar

```bash
curl -X PUT http://localhost:8080/pokedex/pokemon/1 \
  -H "Content-Type: application/json" \
  -d '{"full_name": "Charmeleon", "type": "Fuego", "level": 16, "weakness": "Agua"}'
```

### `DELETE /{id}` — Eliminar

```bash
curl -X DELETE http://localhost:8080/pokedex/pokemon/1
```

---

## 📋 Métodos del Service

| Método | Entrada | Salida |
|---|---|---|
| `createPokemon(PokemonDTORequest)` | DTO | `void` |
| `findAllPokemon()` | — | `List<Pokemon>` |
| `findPokemonById(int id)` | `int` | `PokemonDTOResponse` |
| `updatePokemon(int id, PokemonDTORequest)` | `int` + DTO | `void` |
| `deletePokemonById(int id)` | `int` | `void` |

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

Script SQL (si `ddl-auto: update` no crea la tabla):

```sql
CREATE TABLE pokemon (
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(100),
    type     VARCHAR(50),
    level    INTEGER,
    weakness VARCHAR(50)
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

- `findPokemonById` usa `orElseThrow(() -> new PokemonNotFound(...))` — devuelve `404` controlado
- `updatePokemon` verifica existencia antes de guardar — lanza `PokemonNotFound` si no existe
- `GlobalExceptionHandler` maneja `PokemonNotFound` (404) y `MethodArgumentNotValidException` (400)
- Validaciones en `PokemonDTORequest` con `@NotNull` y `@Min`
- Controller usa `@AllArgsConstructor` — inyección por constructor sin `@Autowired`

---

## 🐛 Pendientes / bugs conocidos

| Severidad | Ubicación | Descripción |
|---|---|---|
| 🔴 Alta | `PokedexController.deletePokemon()` | Tras el `delete`, llama `findPokemonById(id)` que ya no existe — lanza `PokemonNotFound` |
| 🟡 Media | `PokedexService.findAllPokemon()` | Devuelve `List<Pokemon>` (entidad) en lugar de `List<PokemonDTOResponse>` — inconsistente con `GET /{id}` |
| 🟡 Baja | `PokedexService.java` | `import org.springframework.beans.factory.annotation.Autowired` no se usa — limpiar import |

---

## 👩‍💻 Autor
Proyecto educativo de arquitectura N-Capas con Spring Boot — 2026.
por Ing. Luisa Arévalo (ldarevalo@uca.edu.sv)