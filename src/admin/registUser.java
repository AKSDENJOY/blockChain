package admin;

import CreatRecord.CreatRecord;
import data.Record;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import static data.dataInfo.ECNAME;
import static data.dataInfo.PORT;
import static data.dataInfo.ROOTIP;
import static data.protocolInfo.REGISTER;

/**
 * Created by EnjoyD on 2017/5/3.
 */
public class registUser {
    public static void main(String[] args) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException {
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

        DataOutputStream file=new DataOutputStream(new FileOutputStream("./key"));
        file.writeUTF(privateKey);
        file.writeUTF(publicKeyX);
        file.writeUTF(publicKeyY);
        file.close();
        System.out.println("saved");
        Record record=new CreatRecord().registRecord(pubKey,priKey);
        Socket socket=new Socket(ROOTIP,PORT);
        OutputStream out=null;
        out=socket.getOutputStream();
        out.write(REGISTER);
        out.write(record.getBytesData());
        out.close();
        System.out.println("regist over");

    }
}
