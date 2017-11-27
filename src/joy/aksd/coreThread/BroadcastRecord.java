package joy.aksd.coreThread;

import joy.aksd.data.Record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static joy.aksd.data.dataInfo.IPList;
import static joy.aksd.data.dataInfo.PORT;
import static joy.aksd.data.protocolInfo.RECIVERECORD;

/**
 * Created by EnjoyD on 2017/11/22.
 */
public class BroadcastRecord {
    private static ExecutorService RecordBroadcast= Executors.newFixedThreadPool(10);
    private Record record;
    private String fromIp;
    public BroadcastRecord(Record r,String ip){
        this.record=r;
        this.fromIp=ip;
    }
    public void start(){
        ArrayList<String> iplist=new ArrayList<>();
        synchronized (IPList){
            iplist.addAll(IPList);
        }
        for (String ip:iplist){
            if (!ip.equals(fromIp)){
                RecordBroadcast.execute(new RecordBroadcastThread(record,ip));
            }
        }
    }

}
class RecordBroadcastThread implements Runnable{
    private Record record;
    private String ip;
    RecordBroadcastThread(Record r,String ip){
        this.record=r;
        this.ip=ip;
    }
    @Override
    public void run() {
        Socket socket = null;
        OutputStream out = null;
        InputStream in = null;
        try {
            socket=new Socket(this.ip,PORT);
            out = socket.getOutputStream();
            out.write(RECIVERECORD);
            out.write(record.getMac());
            out.write(record.getOrderStamp());
            out.write(record.getTime());
            out.write(record.getLockScript());
            out.write(record.getUnLockScript());

        } catch (IOException e) {
            System.out.println("broadcast record to "+ip+" error");
        }
    }
}
