import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.io.*;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import static joy.aksd.data.dataInfo.ECNAME;
import static joy.aksd.tools.toByte.hexStringToByteArray;

/**
 * Created by EnjoyD on 2018/1/2.
 */
public class generateMoneyKey {
    public static void main(String[] args) {
        File tem =new File("./privateMoneyFile");
        try {
            if (tem.exists()){
                System.out.println("已存在秘钥文件");
                DataInputStream in=new DataInputStream(new FileInputStream(tem));
                String first=in.readUTF();
                String second =in.readUTF();
                String third = in.readUTF();
                in.close();
                System.out.println(first);
                System.out.println(second);
                System.out.println(third);
                byte []x=hexStringToByteArray(second);
                byte []y=hexStringToByteArray(third);
                byte[] result = new byte[x.length + y.length];
                System.arraycopy(x, 0, result, 0, x.length);
                System.arraycopy(y, 0, result, x.length, y.length);
                result=MessageDigest.getInstance("SHA-256").digest(result);
                System.out.println(result.length);
                System.exit(1);
            }
        }catch (Exception e){
            System.out.println("read error");
        }
        try {
            System.out.println("start generate your private key");
            KeyPairGenerator kpg;
            kpg=KeyPairGenerator.getInstance("EC","SunEC");
            ECGenParameterSpec ecsp;
            ecsp=new ECGenParameterSpec(ECNAME);
            kpg.initialize(ecsp);

            KeyPair keyPair=kpg.generateKeyPair();
            ECPrivateKeyImpl priKey= (ECPrivateKeyImpl) keyPair.getPrivate();
            ECPublicKeyImpl pubKey= (ECPublicKeyImpl) keyPair.getPublic();

            String privateKey=String.format("%040x",priKey.getS());
            String publicKeyX=String.format("%040x",pubKey.getW().getAffineX());
            String publicKeyY=String.format("%040x",pubKey.getW().getAffineY());

            DataOutputStream file=new DataOutputStream(new FileOutputStream(tem));
            file.writeUTF(privateKey);
            file.writeUTF(publicKeyX);
            file.writeUTF(publicKeyY);
            file.close();

        }catch (Exception e){
            System.err.println("generate money key error ");
            System.exit(1);
        }
    }
}
