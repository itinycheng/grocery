package com.tiny.grocery.jdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tiny on 2017/8/28.
 */
public class DigestTest {
    public static void main(String args[]) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        sha.update("eee".getBytes());
        System.out.println(sha.digest());
    }
}
