package joy.aksd.tools;

import joy.aksd.data.Block;

import java.io.IOException;
import java.io.RandomAccessFile;

import static joy.aksd.data.dataInfo.indexBlock;
import static joy.aksd.data.dataInfo.location;
import static joy.aksd.tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2017/5/15.
 */
public class readData {
    public static Block readSpecificBlock(int i) throws IOException {
        RandomAccessFile randomAccessFile=new RandomAccessFile(location, "r");
        long location=indexBlock.get(i);
        randomAccessFile.seek(location);
        byte []count=new byte[2];
        randomAccessFile.read(count);
        count=new byte[byteToInt(count)];
        randomAccessFile.read(count);
        Block block=new Block(count);
        randomAccessFile.close();
        System.out.println(block);
        return block;
    }

    public static void main(String[] args) {
        try {
            readSpecificBlock(39);
        } catch (IOException e) {
            System.out.println("error");
        }
    }
}
