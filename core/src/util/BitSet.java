package util;

/**
 * Tightly backed bit (boolean) array
 */
public class BitSet {
    int[] words;

    public BitSet(int length) {
        words = new int[1 + index(length - 1)];
    }

    public boolean get(int bitIndex) {
        int index = index(bitIndex);
        if (bitIndex < 0 || index >= words.length) return false;
        return (words[index] & 1 << pos(bitIndex)) != 0;
    }

    public void set(int bitIndex, boolean val) {
        int index = index(bitIndex);
        if (bitIndex < 0 || index >= words.length) return;
        if (val) words[index] |= 1 << pos(bitIndex);
        else words[index] &= 0 << pos(bitIndex);
    }

    private int index(int bitIndex) {
        return bitIndex >> 5;
    }

    private int pos(int bitIndex) {
        return bitIndex & 0b11111;
    }
}
