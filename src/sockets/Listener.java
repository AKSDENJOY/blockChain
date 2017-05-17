package sockets;

import com.sun.jmx.remote.internal.ArrayQueue;
import data.Block;
import data.Record;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import static data.dataInfo.*;
import static data.protocolInfo.RECIVERECORD;
import static data.protocolInfo.REGISTER;
import static data.protocolInfo.SELFQUERY;
import static tools.protocol.dealRecord;
import static tools.protocol.dealRegistRecord;
import static tools.toByte.intToByte;
import static tools.toInt.byteToInt;
import static tools.toString.byteToString;

/**
 * Created by EnjoyD on 2017/5/2.
 */
public class Listener extends Thread {
    @Override
    public void run() {
        System.out.println("-----123123123123");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("start listen");
        } catch (IOException e) {
            System.out.println("servsocket error");
        }
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("receive a connection");
            } catch (IOException e) {
                e.printStackTrace();
            }
            new handleThread(socket);
        }
    }
}

class handleThread implements Runnable {
    private Socket socket;

    public handleThread(Socket socket) {
        this.socket = socket;
        new Thread(this).start();
    }

    @Override
    public void run() {
        DataInputStream in = null;
        DataOutputStream out=null;
        try {
            in = new DataInputStream(socket.getInputStream());
            out=new DataOutputStream(socket.getOutputStream());
            byte tag = in.readByte();
            Record record = null;
            byte[] receive = null;
            int i;
            byte tem[];
            switch (tag) {
                case REGISTER://新用户注册进区块链
                    //admin

                    receive =new byte[2];
                    in.read(receive);
                    receive=new byte[byteToInt(receive)];
                    in.read(receive);
                    record = new Record(receive);
//                    receive = new byte[6];
//                    in.read(receive);
//                    record.setMac(receive);
//                    receive=new byte[4];
//                    in.read(receive);
//                    record.setOrderStamp(receive);
//                    receive=new byte[4];
//                    in.read(receive);
//                    record.setTime(receive);
//                    receive=new byte[32];
//                    in.read(receive);
//                    record.setLockScript(receive);
//                    receive=new byte[100];
//                    i=in.read(receive);
//                    tem=new byte[i];
//                    System.arraycopy(receive,0,tem,0,i);
//                    record.setUnLockScript(tem);
                    dealRegistRecord(record);
                    this.socket.close();
                    break;
                case RECIVERECORD://收到纪录
                    record = new Record();
                    receive = new byte[6];
                    in.read(receive);
                    record.setMac(receive);
                    receive=new byte[4];
                    in.read(receive);
                    record.setOrderStamp(receive);
                    receive=new byte[4];
                    in.read(receive);
                    record.setTime(receive);
                    receive=new byte[32];
                    in.read(receive);
                    record.setLockScript(receive);
                    receive=new byte[100];
                    i=in.read(receive);
                    tem=new byte[i];
                    System.arraycopy(receive,0,tem,0,i);
                    record.setUnLockScript(tem);
                    dealRecord(record);
                    this.socket.close();
//                in.read(receive);
                    break;
                case 0x02:
                    sendResult(out);
                    this.socket.close();
                    break;
                case 0x03://查询顺序戳
                    sendOrderStamp(in,out);
                    this.socket.close();
                    break;
                case 0x0f://测试链接
                    break;
                case SELFQUERY://查询个人记录
                    startSearchIndividual(in,out);
                    this.socket.close();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            try {
                this.socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void startSearchIndividual(DataInputStream in, DataOutputStream out) throws IOException {
        byte lockScrpit[]=new byte[32];
        in.read(lockScrpit);
        int count=10;//默认查询10条记录
//        ArrayDeque<Record> recordToBeSent=new ArrayDeque<>();
        Stack<Record> recordToBeSent=new Stack<>();
        //已验证但未打包
        synchronized (identifedRecord){
            for (Record record:identifedRecord){
                if (count==0)
                    break;
                if (Arrays.equals(record.getLockScript(),lockScrpit)){
                    recordToBeSent.push(record);
                    count--;
                }
            }
        }
        while (recordToBeSent.size()!=0)
            out.write(recordToBeSent.pop().getBytesData());
        if (count==0){//不够默认数，继续查询
            return;
        }
        //已打包但为建块
        synchronized (unPackageRecord){
            for (Record record:unPackageRecord){
                if (count==0){
                    break;
                }
                if (Arrays.equals(record.getLockScript(),lockScrpit)){
                    recordToBeSent.push(record);
                    count--;
                }
            }
        }
        while (recordToBeSent.size()!=0)
            out.write(recordToBeSent.pop().getBytesData());
//        for (Record record:recordToBeSent){
//            out.write(record.getBytesData());
//        }
//        recordToBeSent.clear();
        if (count==0){//不够默认数，继续查询
            return;
        }
        //再查缓存块
        synchronized (blocks){
            x:for (Block block:blocks){
                int recordCount=byteToInt(block.getRecordCount());
                if (recordCount==0){
                    continue;
                }
                else {
                    byte tems[]=block.getData();
                    int x=0;
                    for (int i=0;i<recordCount;i++){
                        if (count==0){
                            break x;
                        }
                        byte temRecord[]=new byte[2];
                        System.arraycopy(tems,x,temRecord,0,2);
                        x+=2;
                        temRecord=new byte[byteToInt(temRecord)];
                        System.arraycopy(tems,x,temRecord,0,temRecord.length);
                        x+=temRecord.length;
                        Record record=new Record(temRecord);
                        recordToBeSent.push(record);
                        count--;
                    }
                }
            }
        }
        while (recordToBeSent.size()!=0)
            out.write(recordToBeSent.pop().getBytesData());
//        for (Record record:recordToBeSent){
//            out.write(record.getBytesData());
//        }
//        recordToBeSent.clear();
        if (count==0){//不够默认数，继续查询
            return;
        }
        //再查硬盘 不查了 太耗性能
    }

    private void sendOrderStamp(DataInputStream in, DataOutputStream out) throws IOException {
        byte [] receive=new byte[32];
        in.read(receive);
        String key=byteToString(receive);
        if (!verifyRecord2.containsKey(key)){
            out.write(new byte[4]);
        }
        Record record=verifyRecord2.get(key);
        out.write(record.getOrderStamp());

    }

    private static void sendResult(OutputStream out) throws IOException {
        synchronized (verifyRecord1){
            out.write(intToByte(verifyRecord1.size()));
            Iterator<Record> iterator= verifyRecord1.iterator();
            while (iterator.hasNext()){
                Record record=iterator.next();
                out.write(record.getLockScript());
            }
        }
    }
}
