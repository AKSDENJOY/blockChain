package sockets;

import data.Record;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;

import static data.dataInfo.PORT;
import static data.dataInfo.verifyRecord1;
import static tools.protocol.dealRecord;
import static tools.protocol.dealRegistRecord;
import static tools.toByte.intToByte;

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
                case 0x00:
                    //admin
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
                    dealRegistRecord(record);
                    break;
                case 0x01:
                    //收到record
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
                case 02:
                    sendResult(out);
                    this.socket.close();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
