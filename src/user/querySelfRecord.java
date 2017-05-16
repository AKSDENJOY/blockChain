package user;

import data.Record;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECPoint;

import static data.dataInfo.ECNAME;
import static data.dataInfo.PORT;
import static data.dataInfo.ROOTIP;
import static data.protocolInfo.SELFQUERY;
import static tools.toByte.hexStringToByteArray;
import static tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2017/5/15.
 */
public class querySelfRecord {

    public void querySelfRecord(){
        //get lockScript
        byte[] lockScript = null;
        try {
            lockScript=getLockScript();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //start query
        try {
            startQuery(lockScript);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startQuery(byte[] lockScript) throws IOException {
        Socket socket=new Socket(ROOTIP,PORT);
        InputStream in=socket.getInputStream();
        OutputStream out=socket.getOutputStream();
        out.write(SELFQUERY);
        out.write(lockScript);
        try {
            while (true) {
                byte[] tem = new byte[2];
                in.read(tem);
                tem = new byte[byteToInt(tem)];
                in.read(tem);
                Record record=new Record(tem);
                System.out.println(record);
            }
        }catch (Exception e){
            ;
        }
        return;
    }

    private byte[] getLockScript() throws IOException, NoSuchAlgorithmException {
        byte []lockScript=getLockScript();
        DataInputStream in = new DataInputStream(new FileInputStream("./key"));
        in.readUTF();
        String second = in.readUTF();
        String third = in.readUTF();
        in.close();
        byte []x=hexStringToByteArray(second);
        byte []y=hexStringToByteArray(third);
        byte[] result = new byte[x.length + y.length];
        System.arraycopy(x, 0, result, 0, x.length);
        System.arraycopy(y, 0, result, x.length, y.length);
        return MessageDigest.getInstance("SHA-256").digest(result);

    }

}
