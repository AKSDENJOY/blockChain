import CreatBlock.CreatBlock;
import ProofOfWork.powModule;
import broadcastBlock.BroadcastBlock;
import data.Block;
import firstBlock.creatFirstBlock;
import sockets.Listener;
import sockets.verifyThread;
import writeBlock.WriteBlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static data.dataInfo.SHA256x;
import static data.dataInfo.location;

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
        try {
            creatFirstBlock.start();
            System.out.println("first block created");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("first block error ");
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
        File file=new File(location);
        file.delete();

        //监听线程启动
        new Listener().start();
        //验证线程启动
        new verifyThread().start();

        try {
            creatFirstBlock.start();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("error on first");
        }
        while (true) {
            //创建区块
            CreatBlock creatBlock = new CreatBlock();
            Block block ;
            try {
                block = creatBlock.start();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("创建区块失败");
                continue;
            }
            //pow找hash值完成区块的最后nonce值部分并加入链中
            powModule pow = new powModule();
            try {
                pow.start(block);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("pow模块失败");
                continue;
            }
            //广播区块
            BroadcastBlock broadcastBlock = new BroadcastBlock(block);
            broadcastBlock.start();
            //写链中区块入硬盘
            WriteBlock writeBlock = new WriteBlock(block);
            try {
                writeBlock.start();
            } catch (FileNotFoundException e) {
                System.out.println("写入失败");
                continue;
            }
            System.out.println(block);
        }
    }
}
