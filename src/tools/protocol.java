package tools;

import data.Record;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECPoint;
import java.util.Arrays;

import static data.dataInfo.*;
import static tools.toString.byteToString;

/**
 * Created by EnjoyD on 2017/5/2.
 */
public class protocol {
    public static void dealRecord(Record record){
        if (verifyScriptRecord(record)){
            verifyRecord1.add(record);
            System.out.println("handle one   "+ verifyRecord1.size());
            //转发

        }
    }

    public static void dealRegistRecord(Record record){
        if (verifyScriptRecord(record)) {
            String key=byteToString(record.getLockScript());
            verifyRecord2.put(key,record);
            //转发
        }
    }
    private static boolean verifyScriptRecord(Record record) {
        MessageDigest digest= null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //获得解锁脚本
        byte [] unLockScrpit=record.getUnLockScript();
        //获得锁定脚本
        byte [] LockScript=record.getLockScript();
        //验证公钥hash
        byte [] tem=new byte[40];
        System.arraycopy(unLockScrpit,0,tem,0,40);
        byte [] temHash=digest.digest(tem);
        if (!Arrays.equals(temHash,LockScript))
            return false;
        //验证签名
        tem=new byte[14];
        byte [] mac=record.getMac();
        byte [] orderStamp=record.getOrderStamp();
        byte [] time=record.getTime();
        byte [] verifingSign=new byte[unLockScrpit.length-40];
        byte [] x=new byte[20];
        byte [] y=new byte[20];
        System.arraycopy(unLockScrpit,0,x,0,20);
        System.arraycopy(unLockScrpit,20,y,0,20);
        System.arraycopy(unLockScrpit,40,verifingSign,0,verifingSign.length);
        System.arraycopy(mac,0,tem,0,6);
        System.arraycopy(orderStamp,0,tem,6,4);
        System.arraycopy(time,0,tem,10,4);
        temHash= digest.digest(tem);
        boolean result=false;
        try {
            ECPublicKeyImpl publicKey = new ECPublicKeyImpl(new ECPoint(new BigInteger(1, x), new BigInteger(1, y)), ECUtil.getECParameterSpec(null, ECNAME));
            Signature s = Signature.getInstance("SHA1withECDSA", "SunEC");
            s.initVerify(publicKey);
            s.update(temHash);
            result= s.verify(verifingSign);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return result;
    }
}
