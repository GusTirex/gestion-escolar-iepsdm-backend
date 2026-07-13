package com.sdm.gestion_escolar_backend.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Genera y valida los tokens JWT que autentican a cada usuario.
 * La clave secreta y la duracion se configuran por variables de entorno
 * (JWT_SECRET y JWT_EXPIRATION_MS); hay valores por defecto para desarrollo.
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expiracionMs;

    public JwtService(
            @Value("${jwt.secret:clave-de-desarrollo-iepsdm-cambiar-en-produccion-0123456789}") String secret,
            @Value("${jwt.expiration-ms:43200000}") long expiracionMs) { // 12 horas por defecto
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = expiracionMs;
    }

    /** Crea un token firmado con el usuario como subject y los datos extra como claims. */
    public String generarToken(String usuario, Map<String, Object> claims) {
        Date ahora = new Date();
        return Jwts.builder()
                .claims(claims)
                .subject(usuario)
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs))
                .signWith(key)
                .compact();
    }

    /** Valida la firma y la expiracion; devuelve los claims o lanza si es invalido. */
    public Claims validar(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
