# 🐳 Docker Setup - Pokedex

## Opción 1: Build manual + Docker (Dockerfile corregido)

1. Generar el JAR:
```powershell
.\gradlew clean bootJar
```

2. Levantar con Docker Compose:
```powershell
docker compose up --build
```

## Opción 2: Multi-stage build (recomendado)

Sin necesidad de ejecutar `gradlew` antes:

```powershell
docker compose -f docker-compose.multistage.yml up --build
```

## Comandos útiles

**Levantar en segundo plano:**
```powershell
docker compose up -d --build
```

**Ver logs:**
```powershell
docker compose logs -f app
```

**Detener y eliminar:**
```powershell
docker compose down
```

**Detener y eliminar volúmenes (limpieza completa):**
```powershell
docker compose down -v
```

## Variables de entorno

Edita `JWT_SECRET` en `docker-compose.yml` antes de producción:
```yaml
JWT_SECRET: tu-secreto-muy-largo-y-aleatorio-aqui
```

## Endpoints

- App: http://localhost:8080
- PostgreSQL: localhost:5432

