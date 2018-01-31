package com.example.rachel.vetapplove;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encriptacio {
    public static String md5(String clear) {
        StringBuffer hexad = new StringBuffer();

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(clear.getBytes());

            int size = b.length;
            hexad = new StringBuffer(size);
            for (int i = 0; i < size; i++) {
                int u = b[i] & 255;
                if (u < 16) {
                    hexad.append("0" + Integer.toHexString(u));
                } else {
                    hexad.append(Integer.toHexString(u));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("L'algorisme enviat al mètode getInstance és " +
                    "incorrecte");
        }
        return hexad.toString();
    }
}
