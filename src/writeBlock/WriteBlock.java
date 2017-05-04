package writeBlock;

import data.Block;

import java.io.*;

import static data.dataInfo.blocks;
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
//        write();
    }

    private void write() throws FileNotFoundException {
        System.out.println("write");
        if (blocks.size()<11){
            return;
        }
        BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(location));
        //开始写先解决别的问题，然后再来进行。

    }
}
