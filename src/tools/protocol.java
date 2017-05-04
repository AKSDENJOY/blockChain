package tools;

import data.Record;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.util.ECUtil;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECPoint;

import static data.dataInfo.ECNAME;
import static data.dataInfo.verifyRecord1;
import static data.dataInfo.verifyRecord2;
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
        byte [] unLockScrpit=record.getUnLockScript();
        byte [] LockScript=record.getLockScript();
        byte [] x=new byte[20];
        byte [] y=new byte[20];
        byte [] verifingSign=new byte[LockScript.length-40];
        byte [] mac=record.getMac();
        byte [] orderStamp=record.getOrderStamp();
        byte [] time=record.getTime();
        System.arraycopy(mac,0,unLockScrpit,0,6);
        System.arraycopy(orderStamp,0,unLockScrpit,6,4);
        System.arraycopy(time,0,unLockScrpit,10,4);
        byte []sha= SHA256.getInstance().sha256(unLockScrpit);

        System.arraycopy(LockScript,0,x,0,20);
        System.arraycopy(LockScript,20,y,0,20);
        System.arraycopy(LockScript,40,verifingSign,0,verifingSign.length);
        boolean result=false;
        try {
            ECPublicKeyImpl publicKey = new ECPublicKeyImpl(new ECPoint(new BigInteger(1, x), new BigInteger(1, y)), ECUtil.getECParameterSpec(null, ECNAME));
            Signature s = Signature.getInstance("SHA1withECDSA", "SunEC");
            s.initVerify(publicKey);
            s.update(sha);
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
