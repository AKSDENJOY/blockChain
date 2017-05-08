package sockets;

import data.Record;

import static data.dataInfo.verifyRecord1;
import static data.dataInfo.verifyRecord2;
import static tools.toInt.byteToInt;
import static tools.toString.byteToString;

/**
 * Created by EnjoyD on 2017/5/4.
 */
public class verifyThread extends Thread{
    @Override
    public void run() {
        Record record= null;
        try {
            record = verifyRecord1.take();
        } catch (InterruptedException e) {
            return;
        }
        if (verifyStamp(record)&&verifyTime(record)){
            String key=byteToString(record.getLockScript());
            verifyRecord2.put(key,record);
        }
    }

    private boolean verifyTime(Record record) {
        String key=byteToString(record.getLockScript());
        if (byteToInt(verifyRecord2.get(key).getTime())<byteToInt(record.getTime()))
            return true;
        return false;
    }
    private boolean verifyStamp(Record record) {
        String key=byteToString(record.getLockScript());
        if (byteToInt(verifyRecord2.get(key).getOrderStamp())+1==byteToInt(record.getOrderStamp()))
            return true;
        return false;
    }
}
