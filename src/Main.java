import CreatRecord.CreatRecord;
import data.Block;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECPoint;

import static data.dataInfo.ECNAME;
import static data.dataInfo.location;
import static tools.toByte.hexStringToByteArray;
import static tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2017/4/27.
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
        RandomAccessFile randomAccessFile=new RandomAccessFile(location, "r");
        long location=0;
        while (randomAccessFile.getFilePointer()!=randomAccessFile.length()){
            byte []count=new byte[2];
            randomAccessFile.read(count);
            count=new byte[byteToInt(count)];
            randomAccessFile.read(count);
            Block block=new Block(count);
            System.out.println(block);
        }
        randomAccessFile.close();

    }
}
