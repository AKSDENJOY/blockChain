package user;


import java.io.IOException;
import java.net.Socket;

import static joy.aksd.data.dataInfo.PORT;
import static joy.aksd.data.dataInfo.ROOTIP;
import static joy.aksd.data.dataInfo.moneyAddress;
import static joy.aksd.data.protocolInfo.SELF_MONEY_QUERY;
import static joy.aksd.tools.toInt.byteToInt;

/**
 * Created by EnjoyD on 2018/1/9.
 */
public class QuerySelfMoney {
    public static void main(String[] args) {
        Socket socket;
        byte[]receive;
        try {
            socket = new Socket(ROOTIP,PORT);
            socket.getOutputStream().write(SELF_MONEY_QUERY);
            socket.getOutputStream().write(moneyAddress);
            receive=new byte[4];
            socket.getInputStream().read(receive);
            socket.close();
        } catch (IOException e) {
            System.out.println("error in network please check the net connection");
            return;
        }
        System.out.println("finish query");
        int money=byteToInt(receive);
        if (money<0){
            System.out.println("your account is not exist");
        }
        else
            System.out.println("your account money is "+money);
    }
}
