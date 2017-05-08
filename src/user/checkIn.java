package user;

import CreatRecord.CreatRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static data.dataInfo.PORT;
import static data.dataInfo.ROOTIP;

/**
 * Created by EnjoyD on 2017/5/8.
 */
public class checkIn {
    public static void main(String[] args) {
        //检查连接
        Socket socket= null;
        try {
            socket = new Socket(ROOTIP,PORT);
            socket.getOutputStream().write(0x0f);
            socket.close();
        } catch (IOException e) {
            System.out.println("error in network please check the net connection");
            return;
        }
        new CreatRecord().start();

    }
}
