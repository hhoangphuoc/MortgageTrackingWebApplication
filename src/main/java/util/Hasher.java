package util;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Hasher {
    private static Hasher instance = new Hasher();
    public static Hasher getInstance(){
        return instance;
    }
    private static final int iterations = 1;
    private static final int memoryKB = 1024*256;
    private static final int parallelism = 4;
    private static final int outputLength = 32;
    private final byte[] pepper = "3B327CFBE6134C7F9CA9EA48894B1419".getBytes();
    
    public Hasher(){
    }
    public byte[] hash(String password, byte[] salt){
        var start = System.currentTimeMillis();
        byte[] hash = hashWithSha512(password, salt);
        var end = System.currentTimeMillis();
        System.out.println("hashing took: " + (end - start) + "ms");
        return hash;
    }
    private byte[] hashWithArgon2(String password, byte[] salt){
        
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13) // 19
                .withIterations(iterations)
                .withMemoryAsKB(memoryKB)
                .withParallelism(parallelism)
                .withSalt(salt);
        
                Argon2BytesGenerator gen = new Argon2BytesGenerator();
        gen.init(builder.build());
        byte[] result = new byte[outputLength];
        gen.generateBytes(stringToBytes(password), result, 0, result.length);
        
        return result;
    }
    private byte[] hashWithSha512(String password, byte[] salt){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(salt);
        return md.digest(stringToBytes(password) );
    }
    private byte[] stringToBytes(String str){
        return str.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] generateSalt(){
        var rand = new SecureRandom();
        var salt = new byte[16];
        rand.nextBytes(salt);
        return salt;
    }
}
