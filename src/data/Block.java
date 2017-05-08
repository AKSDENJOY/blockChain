package data;

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

    private int blockNumber;//4字节
    private int recordCount;//3字节

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

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
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
}
