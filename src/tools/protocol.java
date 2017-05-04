package tools;

import data.Record;

import static data.dataInfo.identifiedRecord;

/**
 * Created by EnjoyD on 2017/5/2.
 */
public class protocol {
    public static void dealRecord(Record record){
        if (verifyRecord(record)){
            identifiedRecord.add(record);
            System.out.println("handle one   "+identifiedRecord.size());
            //转发

        }
    }

    public static void dealRegistRecord(Record record){
        if (verifyRecord(record)) {
            identifiedRecord.add(record);
            //转发
        }
    }
    private static boolean verifyRecord(Record record) {

        return true;

    }
}
