package com.gimapp.sportkeeper.infra

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil(@Value("\${security.jwt.secret:changemechangemechangemechangeme}") secret: String) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())
    fun generate(username: String, minutes: Long = 60): String =
        Jwts.builder().setSubject(username)
            .setIssuedAt(Date()).setExpiration(Date(System.currentTimeMillis()+minutes*60*1000))
            .signWith(key, SignatureAlgorithm.HS256).compact()
    fun parse(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject
}
