package com.amanapp.crypto;

/**
 * Created by USER on 12/28/2016.
 */
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

public class Enc_Dec_String {



//Here data is Secretauth
    public static byte[] encryptString(byte[] data,byte[] iv) {
        try {
            //IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
          //  byte[] iv = getIvBytes();
           Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
           // SecretKeySpec k = new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
            c.init(Cipher.ENCRYPT_MODE, SecretKey.get(),new IvParameterSpec(iv) );
             byte[] Encrybt= c.doFinal(data);

            return Encrybt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] decryptString(byte[] data, byte[] iv) {
        try {



          //  byte[] ivBytes = new byte[16];


           // IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
           // SecretKeySpec k = new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
            c.init(Cipher.DECRYPT_MODE, SecretKey.get(),new IvParameterSpec(iv) );
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static byte[] getIvBytes() throws NoSuchAlgorithmException {
        byte[] ivBytes = new byte[16];
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.nextBytes(ivBytes);
        return ivBytes;
    }

    public static byte[] AppendByte(byte[]src,byte[]des) {
        byte []final_secauth=new byte[src.length+des.length];

        System.arraycopy(src,0,final_secauth,0,src.length);
        System.arraycopy(des,0,final_secauth,src.length,des.length);
        

        return final_secauth;
    }



    ///////////////////////////////////////

    public static byte[] GetIv(byte[]a){
       byte[]iv = new byte[16];
        System.arraycopy(a,0,iv,0,16);
        
        return  iv;
    }
//////////////////////////////////////////////////////////

    public static byte[] GetSecauth(byte[]a){
        byte[]secauth = new byte[a.length-16];
        System.arraycopy(a,17,secauth,0,a.length-16);

        return  secauth;
    }
}
