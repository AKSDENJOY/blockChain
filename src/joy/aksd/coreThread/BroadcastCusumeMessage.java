package joy.aksd.coreThread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static joy.aksd.data.dataInfo.IPList;
import static joy.aksd.data.dataInfo.PORT;
import static joy.aksd.data.protocolInfo.CONSUMEMONEY;
import static joy.aksd.tools.toByte.intToByte;

public class BroadcastCusumeMessage {
    private static ExecutorService broadCastThread= Executors.newFixedThreadPool(10);
    private byte[]messageToBeSent;
    public BroadcastCusumeMessage(byte[] messageToBeSent){
        this.messageToBeSent=messageToBeSent;
    }
    public void start(){
        ArrayList<String> temList=new ArrayList<>();
        synchronized (IPList){
            temList.addAll(IPList);
        }
        for (String ip:temList){
            broadCastThread.execute(new BroadcastCusumeMessageThead(this.messageToBeSent,ip));
        }
    }

}
class BroadcastCusumeMessageThead implements Runnable{
    private byte[]message;
    private String ip;
    public BroadcastCusumeMessageThead(byte []message,String ip){
        this.message=message;
        this.ip=ip;
    }
    @Override
    public void run() {
        System.out.println("broadcast to "+ip);
        try (Socket socket=new Socket(ip,PORT)){
            OutputStream out=socket.getOutputStream();
            out.write(CONSUMEMONEY);
            out.write(intToByte(this.message.length));
            out.write(this.message);
        } catch (IOException e) {
            System.out.println("error to send consume message to "+ip);
            synchronized (IPList){
                if (IPList.contains(ip)){
                    IPList.remove(ip);
                }
            }
        }
    }
}
