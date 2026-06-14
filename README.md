# 📖 Pokédex API — pokedex-capas-012026

API REST construida con **Spring Boot** que implementa arquitectura de **N-Capas**
(Controller -> Service -> Repository -> BD) para gestionar Pokémon y Entrenadores.

---

## 🏗️ Arquitectura del proyecto

```text
controller/
  ├── PokedexController.java      -> Endpoints HTTP de Pokémon
  ├── TrainerController.java      -> Endpoints HTTP de Entrenador
  └── AuthController.java         -> Endpoints de autenticación (login)

service/
  ├── PokedexService.java         -> Lógica de negocio de Pokémon
  ├── TrainerService.java         -> Lógica de negocio de Entrenador
  └── AuthService.java            -> Lógica de autenticación

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
  │   ├── TrainerDTORequest.java  -> DTO entrada Entrenador
  │   └── LoginRequest.java       -> DTO entrada login
  └── response/
      ├── PokemonDTOResponse.java -> DTO salida Pokémon (name, level)
      └── TrainerDTOResponse.java -> DTO salida Entrenador (con lista de Pokémon)

security/
  ├── JwtUtil.java                -> Generación y validación de JWT
  ├── JwtAuthFilter.java          -> Filtro de autenticación JWT
  └── JwtAuth.java                -> Configuración de seguridad

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
| Spring Security | Incluido en Spring Boot |
| Bean Validation | Incluido en Spring Boot |
| Lombok | Última estable |
| PostgreSQL | Driver `42.x` |
| JWT (jjwt) | 0.12.6 |
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
{ "data": {}, "message": "Texto descriptivo" }
```

```json
{ "message": "Detalle", "code": 404, "timestamp": "2026-05-17", "errors": {} }
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

## 🔐 Seguridad y JWT

### Configuración JWT

El proyecto usa **JSON Web Tokens (JWT)** para autenticación stateless.

**Variables de entorno requeridas** (`application.yml`):

```yaml
jwt:
  secret: ${JWT_SECRET}      # Clave secreta en Base64 (mínimo 256 bits)
  expiration: 60000          # Tiempo de expiración en ms (1 minuto)
```

### Componente `JwtUtil`

| Método | Descripción |
|---|---|
| `generateToken(Authentication)` | Genera un JWT firmado con la información del usuario |
| `validateToken(String token)` | Valida firma y expiración del token |
| `getUsernameFromToken(String token)` | Extrae el username del token |
| `getKey()` | Genera la clave secreta HMAC desde Base64 |

### Uso de JWT

**1. Generar clave secreta Base64:**

```powershell
# Genera una clave aleatoria de 256 bits en Base64
[Convert]::ToBase64String([System.Text.Encoding]::UTF8.GetBytes("tu_secreto_muy_largo_de_al_menos_32_caracteres"))
```

**2. Configurar variable de entorno:**

```powershell
# Windows PowerShell
$env:JWT_SECRET="dHVfc2VjcmV0b192ZXJ5X2xhcmdvX2RlX2FsX21lbm9zXzMyX2NhcmFjdGVyZXM="

# Linux/Mac
export JWT_SECRET="dHVfc2VjcmV0b192ZXJ5X2xhcmdvX2RlX2FsX21lbm9zXzMyX2NhcmFjdGVyZXM="
```

**3. Usar el token en peticiones:**

```bash
curl -H "Authorization: Bearer <tu-token-jwt>" \
     http://localhost:8080/api/protected-endpoint
```

### Mejoras en JWT (0.12.6)

- ✅ API moderna sin métodos deprecated
- ✅ Uso de `SecretKey` en lugar de `Key` genérico
- ✅ Parser con `.verifyWith()` y `.build()`
- ✅ Builder con métodos sin `set` (`.subject()`, `.issuedAt()`, `.expiration()`)
- ✅ Manejo robusto de excepciones en validación

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

jwt:
  secret: ${JWT_SECRET}      # Variable de entorno con clave Base64
  expiration: 60000          # 1 minuto (en milisegundos)
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

**Prerrequisitos:** 
- Java 21
- PostgreSQL en `localhost:5432`
- Base de datos `pokedex` creada

**Pasos:**

1. **Configurar variable de entorno JWT:**

```powershell
# Windows PowerShell
$env:JWT_SECRET="dHVfc2VjcmV0b192ZXJ5X2xhcmdvX2RlX2FsX21lbm9zXzMyX2NhcmFjdGVyZXM="
```

2. **Clonar y ejecutar:**

```powershell
git clone <url-del-repositorio>
cd pokedex-capas-012026
gradlew.bat bootRun
```

Disponible en: `http://localhost:8080`

---


## ✅ Mejoras ya implementadas

### Arquitectura y Patrones
- Arquitectura N-Capas (Controller → Service → Repository → Entity)
- Separación de DTOs (Request/Response) y Entities
- Mappers dedicados para conversión DTO ↔ Entity
- Relación `@ManyToMany` entre `Trainer` y `Pokemon` con tabla intermedia `trainer_pokemon`

### Validación y Manejo de Errores
- Manejo de errores con `orElseThrow(() -> new PokemonNotFound(...))`
- `updatePokemon` valida existencia antes de guardar
- `GlobalExceptionHandler` con `@RestControllerAdvice` para `404` y `400`
- Validaciones en request con `@Valid`, `@NotNull`, `@Min`
- `ApiError` incluye campo `errors` con todos los campos inválidos

### Seguridad (JWT)
- JWT 0.12.6 (API moderna sin deprecated)
- Autenticación stateless con Spring Security
- Generación y validación de tokens JWT
- Uso de `SecretKey` con HMAC-SHA256
- Configuración por variables de entorno

### Repositorios
- `PokedexRepository` con `existsByName` y `findByName` para buscar Pokémon por nombre
- `TrainerRepository` con CRUD básico mediante `JpaRepository`

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

## 📚 Documentación adicional

- **[JWT-MODERNIZATION.md](JWT-MODERNIZATION.md)** — Guía completa sobre la actualización de JWT de 0.11.x a 0.12.x, diferencias de API, ejemplos de uso y mejores prácticas

---

## 👩‍💻 Autor
Proyecto educativo de arquitectura N-Capas con Spring Boot — 2026.
por Ing. Luisa Arévalo (ldarevalo@uca.edu.sv)