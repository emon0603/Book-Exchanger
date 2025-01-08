package com.emon.bookexchanger.util;


import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptDecrypt {
    public static String Key = ".Ba2nG.la#Des0hi";

    public static String EncryptMethod(String text, String pass) throws Exception{

        //String pass = "!WB'OC@PkE@m#O&N";

        byte[] textbyte = text.getBytes("UTF-8");
        byte[] passbyte = pass.getBytes("UTF-8");

        SecretKeySpec spec = new SecretKeySpec(passbyte, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        byte[] encrypted = cipher.doFinal(textbyte);

        String encoded = Base64.encodeToString(encrypted, Base64.DEFAULT);

        return encoded ;



    }


    public static String DecryptMethod(String text, String pass) throws Exception{

        byte[] textbyte = Base64.decode(text, Base64.DEFAULT);
        byte[] passbyte = pass.getBytes("UTF-8");

        SecretKeySpec spec = new SecretKeySpec(passbyte, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);
        byte[] decrypted = cipher.doFinal(textbyte);

        String decoded = new String(decrypted);

        return decoded;

    }


}