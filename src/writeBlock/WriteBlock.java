package writeBlock;

import data.Block;

import java.io.*;
import java.nio.channels.FileChannel;

import static data.dataInfo.blocks;
import static data.dataInfo.identifedRecord;
import static data.dataInfo.location;

/**
 * Created by EnjoyD on 2017/4/20.
 */
public class WriteBlock {
    private Block block;
    public WriteBlock(Block block){
        this.block=block;
    }
    public void start() throws FileNotFoundException {
        write();
    }

    private void write() throws FileNotFoundException {
        System.out.println("write");
        RandomAccessFile file=new RandomAccessFile(location,"rw");
        try {
            file.seek(file.length());
            file.write(block.getBlockDatas());
            file.close();
        }catch (IOException e) {
            System.out.println("error in writeBlock");
        }
    }
}
