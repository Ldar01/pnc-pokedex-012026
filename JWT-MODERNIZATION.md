# 🔐 Modernización de JWT en Spring Boot

## ✅ Problema resuelto

El error que veías:
```
JwtUtil.java uses or overrides a deprecated API.
```

Se debía a que estabas usando **jjwt 0.11.3** con métodos deprecated.

---

## 🔄 Cambios realizados

### 1) Actualización de dependencias

**Antes** (`build.gradle`):
```gradle
implementation("io.jsonwebtoken:jjwt-api:0.11.3")
runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.3")
runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.3")
```

**Ahora** (`build.gradle`):
```gradle
implementation("io.jsonwebtoken:jjwt-api:0.12.6")
runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
```

### 2) API moderna en `JwtUtil.java`

#### ❌ Antes (métodos deprecated 0.11.x)

```java
// Generar token
String token = Jwts.builder()
        .setSubject(username)          // ❌ Deprecated
        .setIssuedAt(now)              // ❌ Deprecated
        .setExpiration(expiryDate)     // ❌ Deprecated
        .signWith(getKey())
        .compact();

// Parsear token
String username = Jwts.parser()
        .setSigningKey(getKey())       // ❌ Deprecated
        .parseClaimsJws(token)         // ❌ Deprecated
        .getBody()
        .getSubject();
```

#### ✅ Ahora (API moderna 0.12.x)

```java
// Generar token
return Jwts.builder()
        .subject(username)              // ✅ Moderno
        .issuedAt(now)                  // ✅ Moderno
        .expiration(expiryDate)         // ✅ Moderno
        .signWith(getKey())
        .compact();

// Parsear token
Claims claims = Jwts.parser()
        .verifyWith(getKey())           // ✅ Moderno
        .build()                        // ✅ Requerido
        .parseSignedClaims(token)       // ✅ Moderno
        .getPayload();

return claims.getSubject();
```

---

## 📚 Diferencias clave: 0.11.x → 0.12.x

| Concepto | Versión 0.11.x (deprecated) | Versión 0.12.x (moderna) |
|---|---|---|
| **Builder - Subject** | `.setSubject(...)` | `.subject(...)` |
| **Builder - Issued At** | `.setIssuedAt(...)` | `.issuedAt(...)` |
| **Builder - Expiration** | `.setExpiration(...)` | `.expiration(...)` |
| **Parser - Signing Key** | `.setSigningKey(...)` | `.verifyWith(...).build()` |
| **Parser - Parse** | `.parseClaimsJws(...)` | `.parseSignedClaims(...)` |
| **Claims** | `.getBody()` | `.getPayload()` |
| **Key Type** | `Key` (genérico) | `SecretKey` (específico) |

---

## 🔑 Cómo funciona JWT en Spring Boot moderno

### 1) **Generar un token** (`generateToken`)

```java
public String generateToken(Authentication authentication) {
    String username = authentication.getName();
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + Long.parseLong(expirationTime));

    return Jwts.builder()
            .subject(username)           // Quién es el usuario
            .issuedAt(now)               // Cuándo se creó
            .expiration(expiryDate)      // Cuándo expira
            .signWith(getKey())          // Firma con clave secreta
            .compact();                  // Genera el string JWT
}
```

**Flujo:**
1. Obtienes el username del `Authentication`
2. Defines fecha de creación y expiración
3. Construyes el JWT con claims (subject, dates)
4. Firmas con tu clave secreta
5. Lo devuelves como string

### 2) **Validar un token** (`validateToken`)

```java
public boolean validateToken(String token) {
    try {
        Jwts.parser()
                .verifyWith(getKey())    // Verifica con tu clave
                .build()                 // Construye el parser
                .parseSignedClaims(token);  // Parsea y valida
        return true;
    } catch (Exception e) {
        return false;                    // Si falla, token inválido
    }
}
```

**Flujo:**
1. Intentas parsear el token con tu clave
2. Si la firma coincide y no expiró → válido
3. Si algo falla (firma incorrecta, expirado, etc.) → inválido

### 3) **Extraer información** (`getUsernameFromToken`)

```java
public String getUsernameFromToken(String token) {
    Claims claims = Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();               // Obtiene los claims

    return claims.getSubject();          // Extrae el username
}
```

**Flujo:**
1. Parseas el token
2. Extraes el `payload` (los claims)
3. Obtienes el `subject` (username)

---

## 🔐 Clave secreta en Base64

```java
public SecretKey getKey() {
   return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
}
```

**¿Por qué Base64?**
- Tu `application.yml` define: `jwt.secret: ${JWT_SECRET}`
- Esa variable debe estar en Base64
- `Decoders.BASE64.decode()` la convierte a bytes
- `Keys.hmacShaKeyFor()` genera la clave HMAC

**Generar una clave Base64:**
```bash
# Genera 256 bits (32 bytes) en Base64
echo -n "tu_secreto_muy_largo_de_al_menos_32_caracteres" | base64
```

O en Java:
```java
String secret = Encoders.BASE64.encode("tu_secreto".getBytes());
System.out.println(secret);
```

---

## 🛡️ Mejoras de seguridad aplicadas

### ✅ Manejo de excepciones en `validateToken`

**Antes:**
```java
public boolean validateToken(String token) {
    Jwts.parser()
            .setSigningKey(getKey())
            .parse(token);
    return true;  // ❌ Si falla, lanza excepción
}
```

**Ahora:**
```java
public boolean validateToken(String token) {
    try {
        Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
        return true;
    } catch (Exception e) {
        return false;  // ✅ Captura cualquier error
    }
}
```

### ✅ Uso de `SecretKey` en lugar de `Key`

**Antes:**
```java
public Key getKey() { ... }
```

**Ahora:**
```java
public SecretKey getKey() { ... }  // ✅ Tipo específico
```

---

## 🧪 Cómo probar JWT

### 1) Endpoint para generar token

```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(), 
            request.getPassword()
        )
    );
    
    String token = jwtUtil.generateToken(auth);
    return ResponseEntity.ok(new JwtResponse(token));
}
```

### 2) Usar el token en peticiones

```bash
curl -H "Authorization: Bearer TU_TOKEN_AQUI" \
     http://localhost:8080/api/protected
```

### 3) Filtro JWT (ejemplo básico)

```java
String token = request.getHeader("Authorization");
if (token != null && token.startsWith("Bearer ")) {
    token = token.substring(7);
    
    if (jwtUtil.validateToken(token)) {
        String username = jwtUtil.getUsernameFromToken(token);
        // Autenticar usuario...
    }
}
```

---

## 📊 Comparativa de versiones

| Aspecto | 0.11.x | 0.12.x |
|---|---|---|
| **API** | Setters deprecated | Builder moderno |
| **Parsing** | `parseClaimsJws` | `parseSignedClaims` |
| **Verificación** | `setSigningKey` | `verifyWith().build()` |
| **Type Safety** | `Key` genérico | `SecretKey` específico |
| **Excepciones** | Menos claras | Más específicas |

---

## ✅ Checklist de actualización

- [x] Actualizar `jjwt` a 0.12.6
- [x] Reemplazar `.setSubject()` → `.subject()`
- [x] Reemplazar `.setIssuedAt()` → `.issuedAt()`
- [x] Reemplazar `.setExpiration()` → `.expiration()`
- [x] Reemplazar `.setSigningKey()` → `.verifyWith().build()`
- [x] Reemplazar `.parseClaimsJws()` → `.parseSignedClaims()`
- [x] Reemplazar `.getBody()` → `.getPayload()`
- [x] Cambiar `Key` → `SecretKey`
- [x] Agregar try-catch en `validateToken()`
- [x] Compilar sin warnings

---

## 🚀 Resultado final

```bash
.\gradlew.bat clean build

> BUILD SUCCESSFUL
```

✅ **Sin warnings de deprecated API**  
✅ **Sin errores de compilación**  
✅ **JWT funcional con la API moderna**

---

## 📖 Referencias

- **jjwt 0.12.x docs**: https://github.com/jwtk/jjwt#install
- **Migration guide**: https://github.com/jwtk/jjwt/blob/master/CHANGELOG.md
- **Spring Security + JWT**: https://spring.io/guides/tutorials/spring-security-and-angular-js/

