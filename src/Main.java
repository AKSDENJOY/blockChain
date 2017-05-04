import CreatRecord.CreatRecord;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECPoint;

import static data.dataInfo.ECNAME;
import static tools.toByte.hexStringToByteArray;

/**
 * Created by EnjoyD on 2017/4/27.
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
//        DataInputStream in=new DataInputStream(new FileInputStream("./key"));
//        String first=in.readUTF();
//        String second=in.readUTF();
//        String third=in.readUTF();
//        String fouth=in.readUTF();
//        String fifth=in.readUTF();
//        System.out.println(first);
//        in.close();
//        ECPrivateKeyImpl privateKey=new ECPrivateKeyImpl(new BigInteger(first,16), ECUtil.getECParameterSpec(null,ECNAME));
//        ECPublicKeyImpl publicKey=new ECPublicKeyImpl(new ECPoint(new BigInteger(second,16),new BigInteger(third,16)),ECUtil.getECParameterSpec(null,ECNAME));
//        byte[] sign=hexStringToByteArray(fouth);
//        Signature signature=Signature.getInstance("SHA1withECDSA","SunEC");
//        signature.initVerify(publicKey);
//        signature.update(fifth.getBytes(StandardCharsets.UTF_8));
//        System.out.println(signature.verify(sign));

    }
}
