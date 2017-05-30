package ECC;

import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPoint;

import static joy.aksd.data.dataInfo.ECNAME;
import static joy.aksd.tools.toByte.hexStringToByteArray;

/**
 * Created by EnjoyD on 2017/4/25.
 */
public class ECC {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg;
        kpg=KeyPairGenerator.getInstance("EC","SunEC");
        ECGenParameterSpec ecsp;
        ecsp=new ECGenParameterSpec(ECNAME);
        kpg.initialize(ecsp);

        KeyPair keyPair=kpg.generateKeyPair();
        ECPrivateKeyImpl priKey= (ECPrivateKeyImpl) keyPair.getPrivate();
        ECPublicKeyImpl pubKey= (ECPublicKeyImpl) keyPair.getPublic();

        ECPrivateKeyImpl ecPrivateKey=new ECPrivateKeyImpl(priKey.getS(), ECUtil.getECParameterSpec(null, ECNAME));
        System.out.println(ecPrivateKey.equals(priKey));
        ECPublicKeyImpl ecPublicKey=new ECPublicKeyImpl(new ECPoint(pubKey.getW().getAffineX(),pubKey.getW().getAffineY()),ECUtil.getECParameterSpec(null,ECNAME));
        System.out.println(ecPublicKey.equals(pubKey));

        String tems=String.format("%040x",priKey.getS());
        System.out.println(String.format("%050x",priKey.getS()));
        System.out.println(tems);
        System.out.println(priKey.getS().toByteArray().length);

        BigInteger bigInteger=new BigInteger(tems,16);
        BigInteger bigInteger1=new BigInteger(1,hexStringToByteArray(tems));

        System.out.println(bigInteger);
        System.out.println(bigInteger1);

        String privateKey=String.format("%040x",priKey.getS());
        String publicKeyX=String.format("%040x",pubKey.getW().getAffineX());
        String publicKeyY=String.format("%040x",pubKey.getW().getAffineY());
        //签名
        String s="my name is wy";
        Signature e=Signature.getInstance("SHA1withECDSA","SunEC");
        e.initSign(priKey);
        byte[] ss=s.getBytes(StandardCharsets.UTF_8);
        e.update(ss);
        byte []result=e.sign();
        String reS=new BigInteger(1,result).toString(16).toLowerCase();
        System.out.println("signature is : 0x"+reS);
        System.out.println("signature length is "+ result.length);
        //验证
        Signature v=Signature.getInstance("SHA1withECDSA","SunEC");
        v.initVerify(pubKey);
        v.update(ss);
        boolean r=v.verify(result);
        System.out.println(r);

        DataOutputStream file=new DataOutputStream(new FileOutputStream("./key"));
        file.writeUTF(privateKey);
        file.writeUTF(publicKeyX);
        file.writeUTF(publicKeyY);
//        file.writeUTF(reS);
//        file.writeUTF(s);
        file.close();
        System.out.println("saved");

    }

}
