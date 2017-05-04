package firstBlock;

import data.Block;
import tools.SHA256;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static ProofOfWork.powModule.findNonceAndTime;
import static data.dataInfo.*;
import static tools.toByte.intToByte;
import static tools.toInt.byteToInt;


/**
 * Created by EnjoyD on 2017/4/20.
 */
public class creatFirstBlock {
    public static void start() throws NoSuchAlgorithmException {
        if (blocks.size()==0){
            Block block=new Block();
            block.setMerkle(new byte[32]);
//            block.setTime(intToByte((int) (System.currentTimeMillis()/1000)));
            SHA256 sha= SHA256.getInstance();
            byte[]lastHash=sha.sha256("EnjoyTheDeath".getBytes(StandardCharsets.UTF_8));
            block.setLastHash(lastHash);
            block.setDifficulty((byte) 0x0c);
            block.setBlockNumber(num);
            getNonceAndTime(block);

        }
    }

    private static void getNonceAndTime(Block block) throws NoSuchAlgorithmException {
        findNonceAndTime(block);
    }


}
