package admin;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

import static joy.aksd.data.dataInfo.PORT;
import static joy.aksd.data.dataInfo.ROOTIP;
import static joy.aksd.tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2018/1/2.
 */
public class queryAllbankInfo {
    public static void main(String[] args) {
        Socket socket= null;
        try {
            socket=new Socket(ROOTIP,PORT);
            InputStream in=socket.getInputStream();

            byte tem[]=new byte[4];
            in.read(tem);
            int count=byteToInt(tem);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
