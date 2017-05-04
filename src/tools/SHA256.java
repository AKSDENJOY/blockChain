package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by EnjoyD on 2017/4/24.
 */
public class SHA256 {
    private MessageDigest digest;
    private SHA256(){
        try {
            digest=MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private static class INSTANCE{
        private static volatile SHA256 instance=new SHA256();

    }
    public static SHA256 getInstance(){
        return INSTANCE.instance;
    }
    public byte[] sha256(byte[] source){
        return digest.digest(source);
    }
}
