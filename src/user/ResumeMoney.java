package user;

import joy.aksd.ECC.ECC;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPoint;
import java.util.Scanner;

import static joy.aksd.data.dataInfo.PORT;
import static joy.aksd.data.dataInfo.ROOTIP;
import static joy.aksd.data.protocolInfo.CONSUMEMONEY;
import static joy.aksd.tools.toByte.hexStringToByteArray;
import static joy.aksd.tools.toByte.intToByte;

public class ResumeMoney {

    public static void main(String[] args) throws IOException {
        int selfMoney = QuerySelfMoney.process();
        int consumedMoney = getConsumedMoney(selfMoney);
        if (consumedMoney <= 0) {
            System.out.println("don't consume any money");
            System.exit(1);
        }

        try (Socket socket = new Socket(ROOTIP, PORT)) {
            byte[] messageTobeSent = getMessageToBeSent(consumedMoney);
            OutputStream out = socket.getOutputStream();
            out.write(CONSUMEMONEY);
            out.write(intToByte(messageTobeSent.length));
            out.write(messageTobeSent);
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("error in get");
        }
    }

    private static int getConsumedMoney(int selfMoney)  {
        Scanner sc = new Scanner(System.in);
        System.out.println("please input money you want to consume(if you want to end the process ,intput 0):");
        while (sc.hasNext()) {
            int tem = Integer.parseInt(sc.nextLine());
            if (tem == 0) {
                return -1;
            } else if (tem > selfMoney) {
                System.out.println("your account has no enough money!,please reinput:");
            } else
                return tem;
        }
        return -1;

    }

    public static byte[] getMessageToBeSent(int consumedMoney) throws IOException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
        File f = new File("./privateMoneyFile");
        if (!f.exists())
            System.exit(1);
        DataInputStream in = new DataInputStream(new FileInputStream(f));
        byte[] privateKeyOfByteArray = hexStringToByteArray(in.readUTF());
        byte[] publicKeyXOfByteArray = hexStringToByteArray(in.readUTF());
        byte[] publicKeyYOfByteArray = hexStringToByteArray(in.readUTF());
        ECPrivateKey privateKey = new ECPrivateKeyImpl(new BigInteger(1, privateKeyOfByteArray), ECC.spec);
        ECPublicKey publicKey = new ECPublicKeyImpl(new ECPoint(new BigInteger(1, publicKeyXOfByteArray), new BigInteger(1, publicKeyYOfByteArray)), ECC.spec);

        Signature signature = Signature.getInstance("SHA1withECDSA", "SunEC");
        signature.initSign(privateKey);
        signature.update(intToByte(consumedMoney));
        byte[] signatureResult = signature.sign();
        //公钥加签名打包  4字节int(consumedMoney)+publickeyx+publickeyy+signature
        byte[] messageToBeSent = new byte[4 + signatureResult.length + publicKeyXOfByteArray.length + publicKeyYOfByteArray.length];
        System.arraycopy(intToByte(consumedMoney), 0, messageToBeSent, 0, 4);
        System.arraycopy(publicKeyXOfByteArray, 0, messageToBeSent, 4, publicKeyXOfByteArray.length);
        System.arraycopy(publicKeyYOfByteArray, 0, messageToBeSent, 4 + publicKeyXOfByteArray.length, publicKeyYOfByteArray.length);
        System.arraycopy(signatureResult, 0, messageToBeSent, 4 + publicKeyXOfByteArray.length + publicKeyYOfByteArray.length, signatureResult.length);

//        signature.initVerify(publicKey);
//        signature.update(intToByte(consumedMoney));
//        System.out.println(signature.verify(signatureResult));

        return messageToBeSent;

    }
}
