package joy.aksd.ECC;

import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.*;

import static joy.aksd.data.dataInfo.ECNAME;
import static joy.aksd.tools.toByte.hexStringToByteArray;

/**
 * Created by EnjoyD on 2017/4/25.
 */
public class ECC {

    public static ECParameterSpec spec=initSpec("secp160r2", "1.3.132.0.30", 1, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC73", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFAC70", "B4E134D3FB59EB8BAB57274904664D5AF50388BA", "52DCB034293A117E1F4FF11B30F7199D3144CE6D", "FEAFFEF2E331F296E071FA0DF9982CFEA7D43F2E", "0100000000000000000000351EE786A818F3A1A16B", 1);

    private static ECParameterSpec initSpec(String var0, String var1, int var2, String var3, String var4, String var5, String var6, String var7, String var8, int var9) {
        BigInteger var11 = bi(var3);
        Object var12;
        var12 = new ECFieldFp(var11);
        EllipticCurve var13 = new EllipticCurve((ECField)var12, bi(var4), bi(var5));
        ECPoint var14 = new ECPoint(bi(var6), bi(var7));
        return new ECParameterSpec(var13,var14,bi(var8),var9);
    }
    private static BigInteger bi(String var0) {
        return new BigInteger(var0, 16);
    }


    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg;
        kpg=KeyPairGenerator.getInstance("EC","SunEC");
        ECGenParameterSpec ecsp;
        ecsp=new ECGenParameterSpec(ECNAME);
        kpg.initialize(ecsp);

        KeyPair keyPair=kpg.generateKeyPair();
        ECPrivateKeyImpl priKey= (ECPrivateKeyImpl) keyPair.getPrivate();
        ECPublicKeyImpl pubKey= (ECPublicKeyImpl) keyPair.getPublic();

        ECPrivateKeyImpl ecPrivateKey=new ECPrivateKeyImpl(priKey.getS(), ECC.spec);
        System.out.println(ecPrivateKey.equals(priKey));
        ECPublicKeyImpl ecPublicKey=new ECPublicKeyImpl(new ECPoint(pubKey.getW().getAffineX(),pubKey.getW().getAffineY()),ECC.spec);
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
