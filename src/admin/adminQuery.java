package admin;

import data.Record;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static data.dataInfo.PORT;
import static data.dataInfo.ROOTIP;
import static data.protocolInfo.ADMINQUERY;
import static tools.toInt.byteToInt;
import static tools.toString.byteToString;

/** 管理员查询
 * Created by EnjoyD on 2017/5/25.
 */
public class adminQuery {
    public void start(){
        Socket socket= null;
        try {
            socket = new Socket(ROOTIP,PORT);
            InputStream in=socket.getInputStream();
            OutputStream out=socket.getOutputStream();
            out.write(ADMINQUERY);
            try {
                while (true) {
                    byte[] tem = new byte[2];
                    int i=in.read(tem);
                    tem = new byte[byteToInt(tem)];
                    i=in.read(tem);
                    Record record=new Record(tem);
                    System.out.print(record);
                    long time=(long)byteToInt(record.getTime());
                    System.out.print("   time : "+new SimpleDateFormat("YYYY-MM-dd-EEEE HH:mm:ss").format(new Date(time*1000))+"  ");
                    System.out.println("lockScrpit: "+ byteToString(record.getLockScript()));
                }
            }catch (Exception e){
                System.out.println("receive over");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new adminQuery().start();
    }
}
