package ProofOfWork;

import CreatBlock.CreatBlock;
import data.Block;
import firstBlock.creatFirstBlock;
import tools.SHA256;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import static data.dataInfo.*;
import static tools.toByte.intToByte;
import static tools.toByte.intToOneByte;
import static tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2017/4/10.
 */
public class powModule {
    //region old version

    //此模块状态值：nonce及找到的hashvalue
//    private byte[] nonce;//找到的nonce值
//    private byte[] hashValue;//找到的hashValue
//    private byte[] lahsHashValue;//上一块hashValue
//    private byte difficulty;//目标值前的0的个数（二进制，总计256位，因此一个字节足以）
//    private byte[] timeStamp;
//
//    //region Getter and Setter
//    public byte[] getNonce() {
//        return nonce;
//    }
//
//    public void setNonce(byte[] nonce) {
//        this.nonce = nonce;
//    }
//
//    public byte[] getHashValue() {
//        return hashValue;
//    }
//
//    public void setHashValue(byte[] hashValue) {
//        this.hashValue = hashValue;
//    }
//
//    public byte[] getLahsHashValue() {
//        return lahsHashValue;
//    }
//
//    public void setLahsHashValue(byte[] lahsHashValue) {
//        this.lahsHashValue = lahsHashValue;
//    }
//
//    public byte getDifficulty() {
//        return difficulty;
//    }
//
//    public void setDifficulty(byte difficulty) {
//        this.difficulty = difficulty;
//    }
//
//    //endregion
    //region
//
//    public static int numOfBlock = 0;
//
//    /**
//     * 目标字节数组，表示目标的二进制位中前多少位为0
//     */
//    public static byte[] target;
//
//
//    /**
//     * 初始hash及运算中的上一区块的hash值。
//     */
//    public static byte[] lashHash;
//
//    /**
//     * 记录当前区块的前十区块的产生时间，用来评估难度值。
//     */
//    public static ArrayList<Double> time = new ArrayList<>();
//
//
//    public void start(Block block) throws NoSuchAlgorithmException {
//        initLashHash();
////        first 获得难度值
//        getdifficulty();
////        second 开始寻找符合难度值的hash值
//        startHash();
//    }
//
//    static {
//        initTarget();
//        if (blocks.size()==0){
//            MessageDigest digest = null;
//            try {
//                digest = MessageDigest.getInstance("SHA-256");
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//            lashHash = digest.digest("Enjoy The Death".getBytes(StandardCharsets.UTF_8));
//        }
//    }
//
//    public static void main(String args[]) throws NoSuchAlgorithmException {
//        powModule powModule = new powModule();
//
//        initLashHash();
//        initTarget();
//        while (true) {
////        first 获得难度值
//            powModule.getdifficulty();
////        second 开始寻找符合难度值的hash值
//            powModule.startHash();
//
//        }
//    }
//
//
//
//
//    /**
//     * 初始化lashHash
//     *
//     * @throws NoSuchAlgorithmException
//     */
//    private static void initLashHash() throws NoSuchAlgorithmException {
//        if (blocks.size()==0)
//            return;
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        Block block=blocks.getLast();
//        byte[] tem=new byte[32+4];
//        System.arraycopy(block.getLastHash(),0,tem,0,32);
//        System.arraycopy(block.getNonce(),0,tem,32,4);
//        lashHash = digest.digest(tem);
//    }
//
//    /**
//     * 初始化难度，此电脑平均1s
//     */
//    private static void initTarget() {
//        target = new byte[3];
//        target[0] = 0x00;
//        target[1] = 0x00;
//        target[2] = -1;
//    }
//
//    /**
//     * 获取每十次的平均时间
//     *
//     * @return 平均时间
//     */
//    private static int getAvgTime() {
//        double totalTime = 0;
//        Iterator it = time.iterator();
//        while (it.hasNext())
//            totalTime += (double) it.next();
//        double avgTime = totalTime / time.size();
//        System.out.println("----------");
//        System.out.println("平均耗时：" + avgTime);
//        return (int) avgTime;
//    }
//
//    /**
//     * 设置难度。
//     * 每十次更新一次难度，其余期间难度不变
//     */
//    private void getdifficulty() {
//        //get and set difficulty
//        if (time.size() == 10) {//每十次调整一次难度
//            int avgTime = getAvgTime();
//            System.out.println(time);
//            //前十个区块的平均时间
//            if (avgTime > exceptTime) {//大于10s
//                decreaseTarget();
//            }
//            if (avgTime < exceptTime) {//小于10s
//                increaseTarget();
//            }
//            time.clear();
//            updateTargetNum();
//        } else {
//            return;
//        }
//
//    }
//
//    /**
//     * 根据现在的目标值更新对应的难度值
//     */
//    private void updateTargetNum() {
//        int size=target.length;
//        int result=(size-1)*8;
//        if (target[size-1]==-1){
//            result+=4;
//        }else {
//            result+=8;
//        }
//        this.difficulty= intToOneByte(result);
//
//    }
//
//    /**
//     * 提升难度，提升一个单位难度
//     * 单位难度为4比特位（一个十六进制位）
//     */
//    private static void increaseTarget() {
//        if (target[target.length - 1] == -1) {
//            target[target.length - 1] = 0x00;
//        } else {
//            byte[] newTarget = new byte[target.length + 1];
//            System.arraycopy(target, 0, newTarget, 0, target.length);
//            newTarget[newTarget.length - 1] = -1;
//            target = newTarget;
//        }
//    }
//
//    /**
//     * 降低难度，降低一个单位难度
//     * 单位难度为4比特位（一个十六进制位）
//     */
//    private static void decreaseTarget() {
//        if (target[target.length - 1] == -1) {
//            byte newTarget[] = new byte[target.length - 1];
//            System.arraycopy(target, 0, newTarget, 0, newTarget.length);
//            target = newTarget;
//        } else {
//            target[target.length - 1] = -1;
//        }
//    }
//
//    /**
//     * 寻找符合要求的hash值
//     *
//     * @return 找到的nonce的UTF8字节码
//     * @throws NoSuchAlgorithmException 加密算法不存在
//     */
//    private void startHash() throws NoSuchAlgorithmException {
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        long start = 0, end = 0;
//        byte[] header = lashHash;//
//        byte[] source = new byte[lashHash.length + 4];
//        System.arraycopy(header, 0, source, 0, header.length);
//        start = System.currentTimeMillis();
//        for (int i = 0; true; i++) {
//            byte nonce[] = intToByte(i);
//            System.arraycopy(nonce, 0, source, header.length, 4);
//            byte[] hash = digest.digest(source);
//            if (isRight(hash)) {
//                System.out.println("----------第" + (numOfBlock++) + "区块：");
//                System.out.println("nonce is " + i);
//                lahsHashValue = lashHash;
//                hashValue = hash;
//                this.nonce=nonce;
//                System.out.println("hash value is " + hash[3]);
//                break;
//            }
//        }
//        end = System.currentTimeMillis();
//        System.out.println((end - start) / 1000.0 + "s");
//        time.add(((end - start) / 1000.0));
//
//    }
//
//    /**
//     * 判断此hash是否满足目标难度
//     *
//     * @param hash 要判断的hash值
//     * @return true:满足 false：不满足
//     */
//    private static boolean isRight(byte[] hash) {//判断是否满足条件
//        for (int i = 0; i < target.length - 1; i++) {
//            if (hash[i] != target[i])
//                return false;
//        }
//        if (target[target.length - 1] == -1) {
//            if (hash[target.length - 1] < 0 || hash[target.length - 1] > 16)//0x0f
//                return false;
//        } else {
//            if (hash[target.length - 1] != target[target.length - 1])//0x00
//                return false;
//        }
//        return true;
//    }
    //endregion
    public void start(Block block) throws NoSuchAlgorithmException {
        findNonceAndTime(block);
    }

    public static void findNonceAndTime(Block block) throws NoSuchAlgorithmException {
        byte target[]=getTarget(block.getDifficulty());
        byte[] lashHash=block.getLastHash();
        byte[] merkle=block.getMerkle();
        byte difficulty=block.getDifficulty();
        byte[] tem=new byte[block.getBlockByteNum()];
        System.arraycopy(lashHash,0,tem,0,lashHash.length);
        System.arraycopy(merkle,0,tem,lashHash.length,merkle.length);
        tem[lashHash.length+merkle.length+4]=difficulty;
        SHA256 sha256 = SHA256.getInstance();
        for (long i=0;i<0xffffffffL;i++){
            byte[]time=intToByte(getUnixTime());
            byte[]nonce=intToByte(i);
            System.arraycopy(time,0,tem,lashHash.length+merkle.length,time.length);
            System.arraycopy(nonce,0,tem,lashHash.length+merkle.length+time.length+1,nonce.length);
            if (isRight(sha256.sha256(tem),target)){
                block.setNonce(nonce);
                block.setTime(time);
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
     * @return
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
     * @param difficulty
     * @return
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

    public static void main(String[] args) {

        try {
            creatFirstBlock.start();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error on first");
        }

        while (true) {
            CreatBlock creatBlock = new CreatBlock();
            Block block = null;
            try {
                block = creatBlock.start();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("error on secone");
            }

            powModule powModule = new powModule();
            try {
                powModule.start(block);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            System.out.println(block);
            System.out.println("------");
        }
    }


}
