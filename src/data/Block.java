package data;

import static tools.toByte.intToByte;
import static tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2017/4/18.
 */
public class Block {
    private byte lastHash[];//32 bytes
    private byte Merkle[];// 32 bytes
    private byte time[];//4bytes
    private byte difficulty;//1 bytes难度目标，
    private byte nonce[];//4 bytes

    private byte data[];

    private byte[] blockNumber;//3字节
    private byte[] recordCount;//2字节


    public Block(){

    }

    public byte[] getLastHash() {
        return lastHash;
    }

    public void setLastHash(byte[] lastHash) {
        this.lastHash = lastHash;
    }

    public byte[] getMerkle() {
        return Merkle;
    }

    public void setMerkle(byte[] merkle) {
        Merkle = merkle;
    }

    public byte[] getTime() {
        return time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }

    public byte getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public void setNonce(byte[] nonce) {
        this.nonce = nonce;
    }

    public byte[] getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        byte[]tem=intToByte(blockNumber);
        byte[]result=new byte[3];
        System.arraycopy(tem,1,result,0,3);
        this.blockNumber = result;
    }

    public int getBlockByteNum(){
        return 32+32+4+4+1;
    }

    @Override
    public String toString() {
        return "num:"+blockNumber+"\n"+
                "nonce:"+byteToInt(nonce)+"\n"+
                "diff:"+(difficulty&0xff)+"\n"+
                "time:"+byteToInt(time);

    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        byte[]tem=intToByte(recordCount);
        byte[]result=new byte[2];
        System.arraycopy(tem,2,result,0,2);
        this.recordCount = result;
    }

    public byte[] getBlockDatas(){
        int i=lastHash.length+Merkle.length+time.length+1+nonce.length+blockNumber.length+recordCount.length+data.length;
        byte result[]=new byte[2+i];
        byte tem[]=intToByte(i);
        System.arraycopy(tem,2,result,0,2);
        System.arraycopy(lastHash,0,result,2,lastHash.length);
        System.arraycopy(Merkle,0,result,2+lastHash.length,Merkle.length);
        System.arraycopy(time,0,result,2+lastHash.length+Merkle.length,time.length);
        result[2+lastHash.length+Merkle.length+time.length]=difficulty;
        System.arraycopy(nonce,0,result,2+lastHash.length+Merkle.length+time.length+1,nonce.length);
        System.arraycopy(blockNumber,0,result,2+lastHash.length+Merkle.length+time.length+1+nonce.length,blockNumber.length);
        System.arraycopy(recordCount,0,result,2+lastHash.length+Merkle.length+time.length+1+nonce.length+blockNumber.length,recordCount.length);
        System.arraycopy(data,0,result,2+lastHash.length+Merkle.length+time.length+1+nonce.length+blockNumber.length+recordCount.length,data.length);
        return result;
    }
}
