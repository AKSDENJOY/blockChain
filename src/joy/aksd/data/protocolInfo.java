package joy.aksd.data;

/** 协议设置
 * Created by EnjoyD on 2017/5/16.
 */
public class protocolInfo {
    private protocolInfo(){}
    //监听线程
    public static final byte REGISTER=0x00;//新用户注册
    public static final byte RECIVERECORD=0x01;//收到记录
    public static final byte QUERYORDERSTAMPANDTIME =0x03;//请求顺序戳
    public static final byte SELFQUERY=0x05;//查询个人记录
    public static final byte RECEIVEBLOCK=0x06;//收到区块
    public static final byte ADMINQUERY=0x07;//管理员查询
    public static final byte DOWNLOADBLOCK=0x08;//下载区块
    public static final byte GETIPLIST=0x09;//获取IPlist
    public static final byte GETBANKINFO=0x0a;//admin查询bank所有记录
    public static final byte SELF_MONEY_QUERY=0x0b;//self查询个人代币
    public static final byte CONSUMEMONEY=0x0c;//self消费个人代币


    public static final byte LINKTEST =0x0f;//连接测试


    //下载区块文件时使用
//    public static final byte HAVEBLOCKFILE=0x10;//区块文件存在
//    public static final byte NOBLICKFILE=0x11;//区块文件不存在

}
