package zju.vipa.aix.container.utils;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;


/**
 * @author sontal
 * @version 1.0
 * @date 2020/2/26 15:49
 */
public class JwtUtil {

    private static String secret;

    private static JWTVerifier jwtVerifier;


    static{
        secret = PropertyUtil.getProperty("jwt.secret","vipa-dev");
        jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
    }


    public static String createJWT(String id, long ttlMillis){
        String token="";
        JWTCreator.Builder builder = JWT.create().withAudience(id);

        if(ttlMillis >= 0){
            builder.withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis));
        }

        token= builder.sign(Algorithm.HMAC256(secret));
        return token;
    }

    public static String decodeClinetId(String token) throws JWTVerificationException{
        try {
            String clientId = JWT.decode(token).getAudience().get(0);
            return clientId;
        }catch (Exception e){
            ExceptionUtils.handle(e);
            return null;
        }

    }


    public static boolean verify(String token) {
        try {
            jwtVerifier.verify(token);
            return true;
        }catch (Exception e){
            ExceptionUtils.handle(e,"Token:"+token);
            return false;
        }
    }

}
