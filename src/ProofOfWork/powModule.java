package ProofOfWork;

import data.Block;

import java.security.NoSuchAlgorithmException;

import static data.dataInfo.*;
import static tools.toByte.intToByte;
import static tools.toInt.byteToInt;

/** 工作量证明模块
 * Created by EnjoyD on 2017/4/10.
 */
public class powModule {
    public void start(Block block) throws NoSuchAlgorithmException, InterruptedException {
        findNonceAndTime(block);
    }

    public static void findNonceAndTime(Block block) throws NoSuchAlgorithmException, InterruptedException {
        byte target[]=getTarget(block.getDifficulty());
        byte[] lashHash=block.getLastHash();
        byte[] merkle=block.getMerkle();
        byte difficulty=block.getDifficulty();
        byte[] tem=new byte[block.getBlockByteNum()];
        System.arraycopy(lashHash,0,tem,0,lashHash.length);
        System.arraycopy(merkle,0,tem,lashHash.length,merkle.length);
        tem[lashHash.length+merkle.length+4]=difficulty;
        for (int i=0;true;i++){
            if (Thread.currentThread().isInterrupted())
                Thread.currentThread().sleep(1);
            if (interuptPOW)
                Thread.currentThread().interrupt();
            if (i==Integer.MAX_VALUE)
                i=0;
            byte[]time=intToByte(getUnixTime());
            byte[]nonce=intToByte(i);
            System.arraycopy(time,0,tem,lashHash.length+merkle.length,time.length);
            System.arraycopy(nonce,0,tem,lashHash.length+merkle.length+time.length+1,nonce.length);
            if (isRight(SHA256x.digest(tem),target)){
                block.setNonce(nonce);
                block.setTime(time);
                if (blocks.size()>cacheBlockCount)//缓存最近cacheBlockCount个区块
                    blocks.remove(0);
                blocks.addLast(block);
                timeRecord.add(byteToInt(time));
                block.setBlockNumber(num++);
                return;
            }
        }

    }
    /**
     * 判断是否满足目标
     *
     * @param hash 要判断的值
     * @param target 目标值
     * @return 正确与否
     */
    private static boolean isRight(byte[] hash, byte[] target) {
        for (int i = 0; i < target.length - 1; i++) {
            if (hash[i] != target[i])
                return false;
        }
        if (target[target.length - 1] == -1) {
            if (hash[target.length - 1] < 0 || hash[target.length - 1] > 16)//0x0f
                return false;
        } else {
            if (hash[target.length - 1] != target[target.length - 1])//0x00
                return false;
        }
        return true;

    }

    /**
     * 根据难度求目标
     * 难度值表示二进制中前多少位是0
     * @param difficulty 难度值
     * @return 难度前缀
     */
    private static byte[] getTarget(byte difficulty) {
        int num=difficulty&0xff;
        int tem1=num/8;
        int tem2=num%8;
        byte[] target;
        if (tem2==0){
            target=new byte[tem1];
            init(target);
        }
        else {
            target=new byte[tem1+1];
            init(target);
            target[tem1]=-1;
        }
        return target;


    }

    private static void init(byte[] target) {
        for(int i=0;i<target.length;i++){
            target[i]=0x00;
        }
    }
    //region test
//    public static void main(String[] args) {
//
//        try {
//            creatFirstBlock.start();
//        } catch (NoSuchAlgorithmException e) {
//            System.out.println("error on first");
//        }
//
//        while (true) {
//            CreatBlock creatBlock = new CreatBlock();
//            Block block = null;
//            try {
//                block = creatBlock.start();
//            } catch (NoSuchAlgorithmException e) {
//                System.out.println("error on secone");
//            }
//
//            powModule powModule = new powModule();
//            try {
//                powModule.start(block);
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//            System.out.println(block);
//            System.out.println("------");
//        }
//    }
    //endregion


}
