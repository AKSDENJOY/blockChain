import joy.aksd.data.Block;
import joy.aksd.data.Record;

import java.io.*;
import java.security.*;

import static joy.aksd.data.dataInfo.*;
import static joy.aksd.tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2017/4/27.
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException {

        recoverFromDisk();
    }
    //region recoverFromDisktest
    public static void recoverFromDisk() throws IOException {
        DataInputStream in=new DataInputStream(new FileInputStream(location));
        byte tem[];
        while (true){
            //读取区块
            tem=new byte[2];
            in.read(tem);
            int byteCount=byteToInt(tem);
            if (byteCount==0)
                break;
            //复原区块
            tem=new byte[byteCount];
            in.read(tem);
            Block block=new Block(tem);
            System.out.println(block);
            System.out.println(byteToInt(block.getRecordCount()));
            //读取纪录
            byte blockData[]=block.getData();
            int x=0;
            for (int i=0;i<byteToInt(block.getRecordCount());i++){
                tem=new byte[2];
                System.arraycopy(blockData,x,tem,0,2);
                x+=2;
                tem=new byte[byteToInt(tem)];
                System.arraycopy(blockData,x,tem,0,tem.length);
                x+=tem.length;
                Record record=new Record(tem);
                System.out.println(record);
            }
        }
        in.close();
        System.out.println("end");
    }
    //endregion
}
