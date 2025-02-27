package com.example.sksb.domain.member.service;


import com.example.sksb.domain.member.entity.Member;
import com.example.sksb.global.app.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthTokenService {
    public String genToken(Member member, long expireSeconds) {
        Claims claims = Jwts
                .claims()
                .add("id", member.getId() + "")
                .add("username", member.getUsername())
                .add("authorities", member.getAuthoritiesAsStringList())
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * expireSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKey())
                .compact();
    }

    public String genRefreshToken(Member member) {
        return genToken(member, 60 * 60 * 24 * 365 * 1); // 1년
    }

    public String genAccessToken(Member member) {
        return genToken(member, 60 * 10); // 10분
    }

    public Map<String, Object> getDataFrom(String token) {
        Claims payload = Jwts.parser()
                .setSigningKey(AppConfig.getJwtSecretKey())
                .build()
                .parseClaimsJws(token)
                .getPayload();

        return Map.of(
                "id", Integer.parseInt(payload.get("id", String.class)),
                "username", payload.get("username", String.class),
                "authorities", payload.get("authorities", List.class)
        );
    }

    public boolean validataToken(String token) {
        try{
            Jwts.parser().setSigningKey(AppConfig.getJwtSecretKey()).build().parseClaimsJwt(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}