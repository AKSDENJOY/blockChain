package firstBlock;

import data.Block;
import writeBlock.WriteBlock;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static ProofOfWork.powModule.findNonceAndTime;
import static data.dataInfo.*;


/**
 * Created by EnjoyD on 2017/4/20.
 */
public class creatFirstBlock {
    public static void start() throws NoSuchAlgorithmException, FileNotFoundException {
        if (blocks.size()==0){
            Block block=new Block();
            block.setMerkle(new byte[32]);
//            block.setTime(intToByte((int) (System.currentTimeMillis()/1000)));
            byte[]lastHash=SHA256x.digest("EnjoyTheDeath".getBytes(StandardCharsets.UTF_8));
            block.setLastHash(lastHash);
            block.setDifficulty((byte) 0x14);
            block.setBlockNumber(num);
            getNonceAndTime(block);
            block.setRecordCount(0);
            block.setData(new byte[0]);
            new WriteBlock(block).start();
            indexBlock.add((long) 0);
        }
    }

    private static void getNonceAndTime(Block block) throws NoSuchAlgorithmException {
        try {
            findNonceAndTime(block);
        } catch (InterruptedException e) {
            System.out.println("error in firstBlock");
        }
    }


}
