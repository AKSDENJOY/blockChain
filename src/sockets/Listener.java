package sockets;

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
import static data.protocolInfo.*;
import static tools.protocol.dealRecord;
import static tools.protocol.dealRegistRecord;
import static tools.toByte.intToByte;
import static tools.toInt.byteToInt;
import static tools.toString.byteToString;

/** 监听线程
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
                continue;
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
                    System.out.println("register");
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
                    System.out.println("received record");
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
                    System.out.println("query self informaiton");
                    startSearchIndividual(in,out);
                    this.socket.close();
                    break;
                case RECEIVEBLOCK:
                    System.out.println("receive block");
                    break;
                case ADMINQUERY:
                    System.out.println("admin query");
                    startAdminQuery(in,out);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            try {
                this.socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }finally {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startAdminQuery(DataInputStream in, DataOutputStream out) throws IOException{
        ArrayList<Record> recordToBeSent=new ArrayList<>();
        synchronized (identifedRecord){//从后向前遍历保证顺序
            int size=identifedRecord.size();
            for (int i=0;i<size;i++){
                recordToBeSent.add(identifedRecord.get(size-1-i));
            }
        }
        for (Record record:recordToBeSent){
            out.write(record.getBytesData());
        }
        recordToBeSent.clear();

        //已打包但为建块
        synchronized (unPackageRecord){//从后向前遍历保证顺序
            int size=unPackageRecord.size();
            for (int i=0;i<size;i++){
                recordToBeSent.add(unPackageRecord.get(size-1-i));
            }
        }
        for (Record record:recordToBeSent){
            out.write(record.getBytesData());
        }
        recordToBeSent.clear();

        ArrayList<Block> cacheBlocks=new ArrayList<>();
        //再查缓存块
        synchronized (blocks){//从后向前遍历保证顺序，由于blocks是单向链表，此处以后优化
            cacheBlocks.addAll(blocks);
        }
        int BlockSize=cacheBlocks.size();
        for (int i=0;i<BlockSize;i++){
            int recordCount=byteToInt(cacheBlocks.get(BlockSize-1-i).getRecordCount());
            if (recordCount==0)// block contian no record
                continue;
            ArrayList<Record> cacheRecord=new ArrayList<>();
            byte []blockData=cacheBlocks.get(BlockSize-1-i).getData();
            int x =0;
            for (int j=0;j<recordCount;j++){//找到区块中的lockscript区块
                byte tem[]=new byte[2];
                System.arraycopy(blockData,x,tem,0,2);
                x+=2;
                tem=new byte[byteToInt(tem)];
                System.arraycopy(blockData,x,tem,0,tem.length);
                x+=tem.length;
                Record record=new Record(tem);
                cacheRecord.add(record);
            }
            //倒序发送
            for (int j=0;j<cacheRecord.size();j++){
                out.write(cacheRecord.get(cacheRecord.size()-1-j).getBytesData());
            }
            cacheRecord.clear();
        }
        //再查硬盘 不查了 太耗性能

    }

    private void startSearchIndividual(DataInputStream in, DataOutputStream out) throws IOException {
        byte lockScrpit[]=new byte[32];
        in.read(lockScrpit);
        int count=10;//默认查询10条记录
//        ArrayDeque<Record> recordToBeSent=new ArrayDeque<>();
        ArrayList<Record> recordToBeSent=new ArrayList<>();
        //已验证但未打包
        synchronized (identifedRecord){//从后向前遍历保证顺序
            int size=identifedRecord.size();
            for (int i=0;i<size;i++){
                if (count==0)
                    break;
                if (Arrays.equals(identifedRecord.get(size-1-i).getLockScript(),lockScrpit)){
                    recordToBeSent.add(identifedRecord.get(size-1-i));
                    count--;
                }
            }
        }
        for (Record record:recordToBeSent){
            out.write(record.getBytesData());
        }
        recordToBeSent.clear();
        if (count==0){//不够默认数，继续查询
            return;
        }
        //已打包但为建块
        synchronized (unPackageRecord){//从后向前遍历保证顺序
            int size=unPackageRecord.size();
            for (int i=0;i<size;i++){
                if (count==0){
                    break;
                }
                if (Arrays.equals(unPackageRecord.get(size-1-i).getLockScript(),lockScrpit)){
                    recordToBeSent.add(unPackageRecord.get(size-1-i));
                    count--;
                }
            }
        }
        for (Record record:recordToBeSent){
            out.write(record.getBytesData());
        }
        recordToBeSent.clear();
        if (count==0){//不够默认数，继续查询
            return;
        }
        ArrayList<Block> cacheBlocks=new ArrayList<>();
        //再查缓存块
        synchronized (blocks){//从后向前遍历保证顺序，由于blocks是单向链表，此处以后优化
            for (Block block:blocks){
                cacheBlocks.add(block);
            }
        }
        int BlockSize=cacheBlocks.size();
        i:for (int i=0;i<BlockSize;i++){
            int recordCount=byteToInt(cacheBlocks.get(BlockSize-1-i).getRecordCount());
            if (recordCount==0)// block contian no record
                continue;
            ArrayList<Record> cacheRecord=new ArrayList<>();
            byte []blockData=cacheBlocks.get(BlockSize-1-i).getData();
            int x =0;
            for (int j=0;j<recordCount;j++){//找到区块中的lockscript区块
                byte tem[]=new byte[2];
                System.arraycopy(blockData,x,tem,0,2);
                x+=2;
                tem=new byte[byteToInt(tem)];
                System.arraycopy(blockData,x,tem,0,tem.length);
                x+=tem.length;
                Record record=new Record(tem);
                if (Arrays.equals(record.getLockScript(),lockScrpit))
                    cacheRecord.add(record);
            }
            //倒序发送
            for (int j=0;j<cacheRecord.size();j++){
                if (count==0)
                    break i;
                out.write(cacheRecord.get(cacheRecord.size()-1-j).getBytesData());
                count--;
            }
            cacheRecord.clear();
        }
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
