import data.Block;
import data.Record;
import firstBlock.creatFirstBlock;
import sockets.Listener;
import sockets.verifyThread;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import static data.dataInfo.*;
import static tools.toInt.byteToInt;
import static tools.toString.byteToString;

/** 主程序
 * Created by EnjoyD on 2017/4/20.
 */
public class Main2 {
    static {
        try {
            SHA256x= MessageDigest.getInstance("SHA-256");
            System.out.println("init success");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("init fail");
        }



    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * 核心流程：
     * first 得到上一区块的信息
     * second 根据上一块信息开始找符合条件的本次nonce与时间
     * third 找到后添加入主链
     * fouth 广播找到的区块
     * fifth 将存储的块写入硬盘
     * @param args 无
     */
    public static void main(String[] args) {
        //初始化
//        File file=new File(location);
//        file.delete();
        try {
            recoverFromDisk();
        } catch (IOException e) {
            System.out.println("error in recover");
            return;
        }
        //监听线程启动
        new Listener().start();
        //验证线程启动
        new verifyThread().start();

        try {
            creatFirstBlock.start();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error on first");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //核心线程启动
        coreWork.execute(new coreProcess());

        //region interupt test
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("shut down the coreProcess,10's restart");
//        interuptCoreThread();
//
//        try {
//            TimeUnit.SECONDS.sleep(5);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        interuptReset();
//        coreWork.execute(new coreProcess());
        //endregion
    }
    public static void recoverFromDisk() throws IOException {
        DataInputStream in=new DataInputStream(new FileInputStream(location));
        long index=0;
        byte tem[];
        while (true){
            //读取区块
            tem=new byte[2];
            in.read(tem);
            int byteCount=byteToInt(tem);
            if (byteCount==0)
                break;
            //建立索引
            indexBlock.add(index);
            tem=new byte[byteCount];
            in.read(tem);
            index+=(2+tem.length);
            //复原区块
            Block block=new Block(tem);
            //添加区块缓存
            if (blocks.size()>cacheBlockCount)//缓存最近cacheBlockCount个区块
                blocks.remove(0);
            blocks.addLast(block);
            //添加time
            if (timeRecord.size()==adjustCount)
                timeRecord.clear();
            timeRecord.add(byteToInt(block.getTime()));
            //读取纪录
            byte blockData[]=block.getData();
            int x=0;
            for (int i=0;i<byteToInt(block.getRecordCount());i++){
                tem=new byte[2];
                System.arraycopy(blockData,x,tem,0,2);
                x+=2;
                tem=new byte[byteToInt(tem)];
                System.arraycopy(blockData,x,tem,0,tem.length);
                x+=tem.length;
                Record record=new Record(tem);
                //添加未使用纪录
                verifyRecord2.put(byteToString(record.getLockScript()),record);
            }
        }
        in.close();
        num=byteToInt(blocks.getLast().getBlockNumber())+1;
        System.out.println("end");
    }
//
}
