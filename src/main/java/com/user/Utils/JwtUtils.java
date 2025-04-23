package com.user.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.Dto.UserCredentialDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtUtils
{


    // It's used to sign the JWT, ensuring the integrity and authenticity of the token.
    private static final String BASE64_SECRET="rajkumarsecretkeyforjwttokenmustbe32byte!!!";



    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Decoders.BASE64.decode(...): Decodes the Base64 string to a byte array.
    // Keys.hmacShaKeyFor(...): Converts the byte array into a Key object suitable for HMAC-SHA algorithms.
    // This Key will be used to sign the JWT using HS512.


    private static final long TOKEN_VALIDITY=1000 * 60 * 60 * 10;
    // 10 HRS VALIDITY Expressed in milliseconds (1000 ms * 60 sec * 60 min * 10 hrs).

    private static final ObjectMapper objectMapper= new ObjectMapper();
    // Jackson's ObjectMapper is used to convert Java objects to JSON strings.
    // Useful for storing complex objects (like UserCredential) in token claims.




    // generate JWT Token
//    public static String generateToken(UserCredentialDto userCredential) throws JsonProcessingException {
//        String json= objectMapper.writeValueAsString(userCredential);
//        // Serializes the UserCredential object into a JSON string.
//        // Example: {"username":"raj","role":"admin"}
//
//        Map<String, String > claims= new HashMap<>();
//        claims.put("credential", json);
//        /*
//          A Map of claims is created with a single entry:
//          Key: "credential"
//          Value: the serialized JSON of the user credential
//        */
//
//        Date now= new Date();
//        Date expiration= new Date(now.getTime()+TOKEN_VALIDITY);
//
//        /*
//        now: current time
//        expiration: current time + 10 hours
//        Used to define when the token was issued and when it will expire
//
//         */
//
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(expiration)
//                .signWith(SECRET_KEY)
//                .compact();
//
//        /*
//            Creates the JWT:
//                           .setClaims(claims): Adds your custom data.
//                           .setIssuedAt(now): Time of token creation.
//                           .setExpiration(expiration): Time when token expires.
//                          .signWith(SECRET_KEY): Uses the secret key and algorithm HS512 to digitally sign the token.
//
//            .compact(): Returns the final token string.
//         */
//    }

    // parse JWT token and extract claims

    public static String generateToken(UserCredentialDto userCredential) throws JsonProcessingException {
        if (userCredential == null) {
            throw new IllegalArgumentException("UserCredential cannot be null");
        }

        // Initialize ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Serialize user credential to JSON
        String json = objectMapper.writeValueAsString(userCredential);

        // Set claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("credential", json);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + TOKEN_VALIDITY); // e.g., 10 hours

        // Sign and return JWT
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }


    public static UserCredentialDto parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Object userCredentialJson = claims.get("credential");
            if(userCredentialJson==null){
                System.out.println("Credential claim is missing.");
            }
            System.out.println("Parsed Credential: " + userCredentialJson);
            return objectMapper.readValue(userCredentialJson.toString(), UserCredentialDto.class);

            /*
                Jwts.parserBuilder() → Creates a secure parser builder for JWTs (introduced in JJWT 0.11.0+).
                .setSigningKey(SECRET_KEY) → Sets the secret key to validate the token's signature.
                .build() → Builds the parser
                .parseClaimsJws(token) → Parses the JWT token and validates the signature and expiration
                .getBody() → Extracts the actual claims (payload) from the token (in your case, includes a "credential" key).

                Object userCredentialJson = claims.get("credential");
                            -->    Retrieves the "credential" field from the token's claims.
                            -->    This was originally stored as a JSON string when the token was generated.
             */

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static boolean validateToken(String token)
    {
        try {
            Jwts.parserBuilder()
                    .setSigningKey( SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
            /*
                Jwts.parser() -> Creates a JWT parser instance to validate the token.

                .setSigningKey( SECRET_KEY)--> Tells the parser to verify the JWT token's signature using your SECRET_KEY.
                                                        (SecretKey) is a typecast — it ensures the key is a SecretKey (required by verifyWith()).
                                                         If your key is already a SecretKey, the cast is safe.
                                                         This step ensures that the token was not tampered with.

                .build() --> Finalizes the configuration and builds the parser object.

                 .parseClaimsJws(token);; --> Parses the JWT token passed as a string.
                                               Validates the signature and checks expiration.
                                               If the token is invalid (wrong signature, expired, malformed), it will throw an exception.

                return true; -->  The token passed all checks → it's valid → return true.

             */
        }
        catch (JwtException | IllegalArgumentException e){
            e.getMessage();
            return false;

        }

    }
}
