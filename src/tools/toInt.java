package tools;

import java.nio.ByteBuffer;

/**
 * Created by EnjoyD on 2017/4/19.
 */
public class toInt {
    public static int byteToInt(byte[]res){
        return res[3] & 0xff |
                (res[2] & 0xff) << 8 |
                (res[1] & 0xff) << 16 |
                (res[0] & 0xff) << 24;
    }
}
