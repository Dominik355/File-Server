package org.dbilik.fileServer.utils;

import java.lang.reflect.Array;
import java.util.*;

public final class ArrayUtils {

    public static <T> T[] add2BeginningOfArray(T[] elements, T element) {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    public static <T> T[] remove(T[] array, T... valuesToBeRemoved) {
        if (isEmpty(array) || isEmpty(valuesToBeRemoved)) {
            return clone(array);
        }
        List<T> list = List.of(valuesToBeRemoved);
        final BitSet toRemove = new BitSet(array.length);
        for (int i = 0; i < array.length; i++) {
            if (list.contains(array[i])) {
                toRemove.set(i);
            }
        }

        return (T[]) removeAll(array, toRemove);
    }

    public static boolean isEmpty(Object[] array) {
        return getLength(array) == 0;
    }

    public static int getLength(final Object[] array) {
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    public static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }

    static Object[] removeAll(final Object array, final BitSet bitset) {
        if (array == null) {
            return null;
        }

        int arrayLength = Array.getLength(array);
        final Object result = Array.newInstance(array.getClass().getComponentType(), arrayLength - bitset.cardinality());
        int srcIndex = 0;
        int destIndex = 0;
        int diff;
        int nextbitIndex;
        while((nextbitIndex = bitset.nextSetBit(srcIndex)) != -1) {
            diff = nextbitIndex - srcIndex;
            if (diff > 0) {
                System.arraycopy(array, srcIndex, result, destIndex, diff);
            }
            destIndex += diff;
            srcIndex = bitset.nextClearBit(nextbitIndex);
        }

        diff = arrayLength - srcIndex; // loop goes until last Bit. So everything after was not coppied
        if (diff > 0) {
            System.arraycopy(array, srcIndex, result, destIndex, diff);
        }
        return (Object[]) result;
    }

    public <T> boolean contains(T[] arr, T obj) {
        for(T object : arr) {
            if (object.equals(obj)) {
                return true;
            }
        }
        return false;
    }

}
