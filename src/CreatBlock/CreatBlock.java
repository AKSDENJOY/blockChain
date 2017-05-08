package CreatBlock;

import data.Block;
import firstBlock.creatFirstBlock;

import java.security.NoSuchAlgorithmException;

import static data.dataInfo.*;

/**
 * Created by EnjoyD on 2017/4/18.
 */
public class CreatBlock {
//region old version 有错误
//    private byte[]nonce;
//    private byte[] lastHashValue;
//    private byte[]hashValue;
//    private byte difficulty;
//    public CreatBlock(powModule powModule){
//        this.nonce=powModule.getNonce();
//        this.lastHashValue= powModule.getLahsHashValue();
//        this.hashValue=powModule.getHashValue();
//        this.difficulty=powModule.getDifficulty();
//    }
//    public Block start(){
//        byte[] merkle=creatMerkle();
//        int time=getTime();
//
//
//        return new Block(lastHashValue,merkle,time,difficulty,nonce);
//    }
//
//
//    public static void main(String[] args) {
//
//    }
//
//
//
//    private byte[] creatMerkle() {
//        if (dataInfo.verifyRecord1.size()==0){//先进行此种测试
//            byte []Merkle=new byte[32];
//            for (int i=0;i<Merkle.length;i++){
//                Merkle[i]=0;
//            }
//            return Merkle;
//        }
//        return new byte[0];
//    }
//
//    public int getTime() {
//        return (int) (System.currentTimeMillis()/1000);
//    }
    //endregion
    public Block start() throws NoSuchAlgorithmException {
        Block block=new Block();
        //获取前一区块哈希 32字节
        block.setLastHash(getLashHashValue());
        //获取默克尔树 32字节
        block.setMerkle(generateMerkle());
        //获取难度值
        block.setDifficulty(getDifficulty(blocks.getLast().getDifficulty()));
        //返回为完成的block
        return block;
    }

    private byte getDifficulty(byte difficulty) {
        if (timeRecord.size()==11){
            int avgTime=getAvgTime();
            byte Diff=difficulty;
            if (avgTime>exceptTime){
                Diff=decrease(difficulty);
            }
            if (avgTime<exceptTime){
                Diff=increase(difficulty);
            }
            timeRecord.clear();
            System.out.println("-----------------------------");
            System.out.println("avgtime is "+avgTime);
            System.out.println("-----------------------------");
            return Diff;
        }
        else {
            return difficulty;
        }
    }

    private byte increase(byte difficulty) {
        int i=difficulty&0xff;
        i+=4;
        return (byte) i;
    }

    private byte decrease(byte difficulty) {
        int i=difficulty&0xff;
        i-=4;
        return (byte) i;

    }

    private int getAvgTime() {
        int result=0;
        for (int i=1;i<timeRecord.size();i++){
            result+=(timeRecord.get(i)-timeRecord.get(i-1));
        }
        return result/(timeRecord.size()-1);

    }

    private byte[] generateMerkle() {
        //从池中取记录 打包成默克尔树这个地方需要进行全局操作，比如取记录

        return new byte[32];//暂时这样

    }

    private byte[] getLashHashValue() throws NoSuchAlgorithmException {
        Block block=blocks.getLast();
        byte[] lashHash=block.getLastHash();
        byte[] merkle=block.getMerkle();
        byte[] time=block.getTime();
        byte difficulty=block.getDifficulty();
        byte[]nonce=block.getNonce();
        byte[] tem=new byte[lashHash.length+merkle.length+time.length+1+nonce.length];
        System.arraycopy(lashHash,0,tem,0,lashHash.length);
        System.arraycopy(merkle,0,tem,lashHash.length,merkle.length);
        System.arraycopy(time,0,tem,lashHash.length+merkle.length,time.length);
        tem[lashHash.length+merkle.length+time.length]=difficulty;
        System.arraycopy(nonce,0,tem,lashHash.length+merkle.length+time.length+1,nonce.length);
//        MessageDigest digest = SHA256.getInstance();
        byte []result=SHA256x.digest(tem);
        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        creatFirstBlock.start();
        CreatBlock creatBlock=new CreatBlock();

        Block block=creatBlock.start();
        System.out.println(block);
    }

}
