package CreatBlock;

import data.Block;
import data.Record;
import firstBlock.creatFirstBlock;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static data.dataInfo.*;

/** 创建区块
 * Created by EnjoyD on 2017/4/18.
 */
public class CreatBlock {

    public Block start() throws NoSuchAlgorithmException {
        Block block=new Block();
        //获取前一区块哈希 32字节
        block.setLastHash(getLashHashValue());
        //获取默克尔树并填充blockdata 32字节
        block.setMerkle(generateMerkle(block));
        //获取难度值
        block.setDifficulty(getDifficulty(blocks.getLast().getDifficulty()));
        //返回为完成的block
        return block;
    }

    private byte getDifficulty(byte difficulty) {
        if (timeRecord.size()==adjustCount){
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

    private byte[] generateMerkle(Block block) {
        //从池中取记录 打包成默克尔树这个地方需要进行全局操作，比如取记录
        ArrayDeque<byte []> result=new ArrayDeque<>();
        int bytes=0;
        //取出纪录
        synchronized (identifedRecord) {
            if (identifedRecord.size() == 0)
                return new byte[32];
            Iterator<Record> it = identifedRecord.iterator();
            int i = 0;
            while (i++ < merkleTreeLimitation && it.hasNext()) {
                byte []tem=it.next().getBytesData();
                bytes+=tem.length;
                result.add(tem);
                it.remove();
            }
        }
        //填充block 的data数据
        byte BlockData[]=new byte[bytes];
        bytes=0;
        for (byte[] tem : result) {
            System.arraycopy(tem, 0, BlockData, bytes, tem.length);
            bytes += tem.length;
        }
        block.setData(BlockData);
        //开始计算merkle tree root
        MessageDigest digest= null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        for (int i=0;i<result.size();i++){
            byte tem[]=result.removeFirst();
            tem=digest.digest(tem);
            result.addLast(tem);
        }
        while (result.size()!=1){
            ArrayDeque<byte []> temResult=new ArrayDeque<>();
            while (!result.isEmpty()){
                byte []left=result.removeFirst();
                byte []right=null;
                try {
                    right = result.removeFirst();
                }catch (NoSuchElementException e){}
                if (right==null){
                    temResult.addLast(digest.digest(left));
                }
                else {
                    byte []tem=new byte[left.length+right.length];
                    System.arraycopy(left,0,tem,0,left.length);
                    System.arraycopy(right,0,tem,left.length,right.length);
                    temResult.addLast(digest.digest(tem));
                }
            }
            result=temResult;
        }
        return result.getFirst();
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
        Block block=new Block();
        Record record=new Record();
        record.setLockScript(new byte[32]);
        record.setMac(new byte[6]);
        record.setTime(new byte[4]);
        record.setOrderStamp(new byte[4]);
        record.setUnLockScript(new byte[85]);
        identifedRecord.add(record);
        identifedRecord.add(record);
        identifedRecord.add(record);
        identifedRecord.add(record);
        identifedRecord.add(record);

        new CreatBlock().generateMerkle(block);
    }

}
