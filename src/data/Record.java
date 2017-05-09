package data;

import java.util.ArrayList;

import static tools.toByte.intToByte;

/**
 * Created by EnjoyD on 2017/4/18.
 */
public class Record {
    private byte []mac;//6字节
    private byte []orderStamp;//4字节
    private byte []time;//4
    private byte []lockScript;//32
    private byte []unLockScript;//80-100


    public byte[] getLockScript() {
        return lockScript;
    }

    public void setLockScript(byte[] lockScript) {
        this.lockScript = lockScript;
    }

    public byte[] getUnLockScript() {
        return unLockScript;
    }

    public void setUnLockScript(byte[] unLockScript) {
        this.unLockScript = unLockScript;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }

    public byte[] getOrderStamp() {
        return orderStamp;
    }

    public void setOrderStamp(byte[] orderStamp) {
        this.orderStamp = orderStamp;
    }

    public byte[] getTime() {
        return time;
    }

    public void setTime(byte[] time) {
        this.time = time;
    }

    public byte [] getBytesData(){
        int i=mac.length+orderStamp.length+time.length+lockScript.length+unLockScript.length;
        byte result[]=new byte[2+i];
        byte tem[]=intToByte(i);
        System.arraycopy(tem,2,result,0,2);
        System.arraycopy(mac,0,result,2,mac.length);
        System.arraycopy(orderStamp,0,result,2+mac.length,orderStamp.length);
        System.arraycopy(time,0,result,2+mac.length+orderStamp.length,time.length);
        System.arraycopy(lockScript,0,result,2+mac.length+orderStamp.length+time.length,lockScript.length);
        System.arraycopy(unLockScript,0,result,2+mac.length+orderStamp.length+time.length+lockScript.length,unLockScript.length);
        return result;
    }



}
