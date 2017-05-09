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
    private dataInfo(){}
    public static LinkedList<Block> blocks= new LinkedList<>();
    /**
     * 验证完脚本后的纪录池
     */
    public static LinkedBlockingQueue<Record> verifyRecord1 = new LinkedBlockingQueue<>();
    /**
     * 验证完时间与顺序戳后的未使用纪录池
     */
    public static ConcurrentHashMap<String,Record> verifyRecord2=new ConcurrentHashMap<>();
    /**
     * 验证完成后的记录池，用以创建默克尔树。
     */
    public static ArrayList<Record> identifedRecord=new ArrayList<>();
    /**
     * timeRecord  时间纪录，用以存储adjustCount个区块时间
     * adjustCount 需要多少个区块进行一次难度调整
     */
    public static ArrayList<Integer> timeRecord=new ArrayList<>();
    public static int adjustCount=10;
    /**
     * 区块能包含的最大条目数
     */
    public static int merkleTreeLimitation=64;

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
