package firstBlock;

import data.Block;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import static ProofOfWork.powModule.findNonceAndTime;
import static data.dataInfo.*;


/**
 * Created by EnjoyD on 2017/4/20.
 */
public class creatFirstBlock {
    public static void start() throws NoSuchAlgorithmException {
        if (blocks.size()==0){
            Block block=new Block();
            block.setMerkle(new byte[32]);
//            block.setTime(intToByte((int) (System.currentTimeMillis()/1000)));
            byte[]lastHash=SHA256x.digest("EnjoyTheDeath".getBytes(StandardCharsets.UTF_8));
            block.setLastHash(lastHash);
            block.setDifficulty((byte) 0x0c);
            block.setBlockNumber(num);
            getNonceAndTime(block);
            indexBlock.add((long) 0);

        }
    }

    private static void getNonceAndTime(Block block) throws NoSuchAlgorithmException {
        findNonceAndTime(block);
    }


}
