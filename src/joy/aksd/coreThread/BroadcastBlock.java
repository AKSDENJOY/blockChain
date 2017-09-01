package joy.aksd.coreThread;

import joy.aksd.data.Block;

import java.io.InputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static joy.aksd.data.dataInfo.*;
import static joy.aksd.data.protocolInfo.RECEIVEBLOCK;
import static joy.aksd.tools.toByte.intToByte;

/**
 * Created by EnjoyD on 2017/4/20.
 */
public class BroadcastBlock {
    private static ExecutorService broadCastThread= Executors.newFixedThreadPool(10);
    private Block block;
    public BroadcastBlock(Block block){
        this.block=block;
    }
    public void start(){
        broadcast();
    }

    private void broadcast() {
        System.out.println("broadcast IPlist size"+IPList.size());
        for(String ip:IPList) {
            broadCastThread.execute(new broadCastThread(ip, this.block));
        }

        System.out.println("broadcast over");
    }
}
class broadCastThread implements Runnable{
    private String ip;
    private Block block;
    broadCastThread (String ip,Block block){
        this.ip=ip;
        this.block=block;
    }
    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, PORT);
            System.out.println("broadcast to"+ip);


            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(RECEIVEBLOCK);

//            out.write(intToByte(block.getBlockDatas().length));

            out.write(block.getBlockDatas());

            byte tag[]=new byte[1];
            in.read(tag);

            if (tag[0] == 0x00) {//同步区块
                LinkedList<Block> copyBlock = new LinkedList<>();
                synchronized (blocks){
                    copyBlock.addAll(blocks);
                }
//                Collections.copy(copyBlock, blocks);
                if (copyBlock.size() >= 200) {
                    out.write(intToByte(200));
                } else {
                    out.write(intToByte(copyBlock.size()));
                }

                for (int i = 0; i < copyBlock.size() && i < 200; i++) {
                    Block temBlock = copyBlock.removeLast();
                    out.write(intToByte(temBlock.getBlockDatas().length));
                    out.write(temBlock.getBlockDatas());
                }
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(verifyRecord2);
                objectOutputStream.writeObject(identifedRecord);
                objectOutputStream.writeObject(unPackageRecord);
                objectOutputStream.writeObject(indexBlock);
                objectOutputStream.writeObject(timeRecord);
            }
            else {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(identifedRecord);
                objectOutputStream.writeObject(unPackageRecord);
            }
        }catch (Exception e){
            e.printStackTrace();
            synchronized (IPList){
                IPList.remove(ip);
            }
        }
    }
}
