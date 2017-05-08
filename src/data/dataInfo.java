package data;

import java.nio.file.DirectoryStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;


/**
 * Created by EnjoyD on 2017/4/18.
 */
public class dataInfo {
    private dataInfo(){};
    public static LinkedList<Block> blocks= new LinkedList<>();
    /**
     * 验证完脚本后的纪录池
     */
    public static LinkedBlockingQueue<Record> verifyRecord1 = new LinkedBlockingQueue<>();
    /**
     * 全部验证完成后的纪录池
     */
    public static ConcurrentHashMap<String,Record> verifyRecord2=new ConcurrentHashMap<>();
    public static ArrayList<Record> unidentifedRecord=new ArrayList<>();

    public static ArrayList<Integer> timeRecord=new ArrayList<>();

    public static MessageDigest SHA256x;

    /**
     * 目标时间，期望平均多长时间产生一个区块，proof of work中为定值。
     */
    public static final int exceptTime=20;//单位：秒
    /**
     * 区块存储位置
     */
    public static final String location=".";

    public static int num=0;

    public static final String ECNAME="secp160r1";

    public static final int PORT=49999;

    public static final String ROOTIP="172.18.204.2";


    public static int getUnixTime(){
        return (int) (System.currentTimeMillis()/1000);
    }

}
