package bitmap;

import java.util.BitSet;

/**
 * @author tiny.wang
 */
public class BitSet0 {
    public static void main(String[] args) {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(4);
        byte[] bytes = bitSet.toByteArray();
        int i = bitSet.nextSetBit(0);
        int i1 = bitSet.nextClearBit(1);
        int i3 = bitSet.nextSetBit(2);
        System.out.println(bytes);
    }

}
