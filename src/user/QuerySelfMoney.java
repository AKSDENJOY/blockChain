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
    public static int process() {
//        Socket socket=null;
        byte[]receive;
        try (Socket socket=new Socket(ROOTIP,PORT)){
            socket.getOutputStream().write(SELF_MONEY_QUERY);
            socket.getOutputStream().write(moneyAddress);
            receive=new byte[4];//用来获取一个int数字
            socket.getInputStream().read(receive);
            socket.close();
        } catch (IOException e) {
            System.out.println("error in network please check the net connection");
            System.exit(1);
            return -1;
        }
        System.out.println("finish query");
        int money=byteToInt(receive);
        if (money<0)
            System.out.println("your account is not exist");

        else
            System.out.println("your account money is "+money);
        return money;
    }

    public static void main(String[] args) {
        process();
    }



}
