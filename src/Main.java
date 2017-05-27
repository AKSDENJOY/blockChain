import CreatRecord.CreatRecord;
import data.Block;
import data.Record;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.ECPoint;

import static data.dataInfo.*;
import static tools.toByte.hexStringToByteArray;
import static tools.toInt.byteToInt;
import static tools.toString.byteToString;

/**
 * Created by EnjoyD on 2017/4/27.
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {
//        RandomAccessFile randomAccessFile=new RandomAccessFile(location, "r");
//        long location=0;
//        while (randomAccessFile.getFilePointer()!=randomAccessFile.length()){
//            byte []count=new byte[2];
//            randomAccessFile.read(count);
//            count=new byte[byteToInt(count)];
//            randomAccessFile.read(count);
//            Block block=new Block(count);
//            System.out.println(block);
//        }
//        randomAccessFile.close();
//        recoverFromDisk();
    }
    //region recoverFromDisktest
//    public static void recoverFromDisk() throws IOException {
//        DataInputStream in=new DataInputStream(new FileInputStream(location));
//        long index=0;
//        byte tem[];
//        while (true){
//            //读取区块
//            tem=new byte[2];
//            in.read(tem);
//            int byteCount=byteToInt(tem);
//            if (byteCount==0)
//                break;
//            //建立索引
//            indexBlock.add(index);
//            tem=new byte[byteCount];
//            in.read(tem);
//            index+=(2+tem.length);
//            //复原区块
//            Block block=new Block(tem);
//            //添加区块缓存
//            blocks.addLast(block);
//            //添加time
//            if (timeRecord.size()==adjustCount)
//                timeRecord.clear();
//            timeRecord.add(byteToInt(block.getTime()));
//            //读取纪录
//            byte blockData[]=block.getData();
//            int x=0;
//            for (int i=0;i<byteToInt(block.getRecordCount());i++){
//                tem=new byte[2];
//                System.arraycopy(blockData,x,tem,0,2);
//                x+=2;
//                tem=new byte[byteToInt(tem)];
//                System.arraycopy(blockData,x,tem,0,tem.length);
//                x+=tem.length;
//                Record record=new Record(tem);
//                //添加未使用纪录
//                verifyRecord2.put(byteToString(record.getLockScript()),record);
//            }
//        }
//        in.close();
//        System.out.println("end");
//    }
    //endregion
}
