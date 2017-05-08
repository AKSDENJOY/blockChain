package data;

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


}
