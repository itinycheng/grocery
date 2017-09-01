package com.tiny.grocery.jdk;

import org.junit.Test;

import java.util.BitSet;

/**
 * Created by tiny on 2017/9/1.
 */
public class BitSetTest {

    @Test
    public void test0(){
        BitSet set = new BitSet();
        System.out.println(set.isEmpty() + ", " + set.size());
        set.set(0);
        System.out.println(set.isEmpty()+"--"+set.size());
        set.set(2);
        System.out.println(set.isEmpty()+"--"+set.size());
        System.out.println(set.get(65));
        System.out.println(set.isEmpty()+"--"+set.size());
        set.set(64);
        System.out.println(set.isEmpty()+"--"+set.size());
        set.flip(0);
        System.out.println(set.length());
    }
}
