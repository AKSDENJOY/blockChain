package data;

/**
 * Created by EnjoyD on 2017/5/16.
 */
public class protocolInfo {
    private protocolInfo(){}

    public static final byte REGISTER=0x00;//新用户注册
    public static final byte RECIVERECORD=0x01;//收到记录
    public static final byte SELFQUERY=0x05;//查询个人记录
    public static final byte RECEIVEBLOCK=0x06;//收到区块

}
