package com.benzolamps.dict.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

/**
 * 数组工具类
 * @author Benzolamps
 * @version 2.1.1
 * @datetime 2018-9-12 17:14:25
 */
@SuppressWarnings("unchecked")
@UtilityClass
public class DictArray {
    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(Object[] array, Object obj) {
        if (array == null || array.length <= 0) return false;
        return Arrays.stream(array).anyMatch(item -> Objects.deepEquals(obj, item));
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(byte[] array, byte obj) {
        if (array == null || array.length <= 0) return false;
        for (byte item : array) {
            if (obj == item) return true;
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(short[] array, short obj) {
        if (array == null || array.length <= 0) return false;
        for (short item : array) {
            if (obj == item) return true;
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(int[] array, int obj) {
        if (array == null || array.length <= 0) return false;
        return Arrays.stream(array).anyMatch(item -> obj == item);
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(long[] array, long obj) {
        if (array == null || array.length <= 0) return false;
        return Arrays.stream(array).anyMatch(item -> obj == item);
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(float[] array, float obj) {
        if (array == null || array.length <= 0) return false;
        for (float item : array) {
            if (obj == item) return true;
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(double[] array, double obj) {
        if (array == null || array.length <= 0) return false;
        for (double item : array) {
            if (obj == item) return true;
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(char[] array, char obj) {
        if (array == null || array.length <= 0) return false;
        for (char item : array) {
            if (obj == item) return true;
        }
        return false;
    }

    /**
     * 判断数组中是否包含某个元素
     * @param array 数组
     * @param obj 元素
     * @return 判断结果
     */
    public boolean contains(boolean[] array, boolean obj) {
        if (array == null || array.length <= 0) return false;
        for (boolean item : array) {
            if (obj == item) return true;
        }
        return false;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public <T> T[] concat(T[] array1, T[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        T[] resultArray = (T[]) Array.newInstance(array1.getClass().getComponentType(), array1.length + array2.length);
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public byte[] concat(byte[] array1, byte[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        byte[] resultArray = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public short[] concat(short[] array1, short[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        short[] resultArray = new short[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public int[] concat(int[] array1, int[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        int[] resultArray = new int[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public long[] concat(long[] array1, long[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        long[] resultArray = new long[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public float[] concat(float[] array1, float[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        float[] resultArray = new float[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public double[] concat(double[] array1, double[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        double[] resultArray = new double[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public char[] concat(char[] array1, char[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        char[] resultArray = new char[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 两个数组相加
     * @param array1 数组1
     * @param array2 数组2
     * @return 相加的结果
     */
    public boolean[] concat(boolean[] array1, boolean[] array2) {
        if (array1 == null) return array2;
        if (array2 == null) return array1;
        boolean[] resultArray = new boolean[array1.length + array2.length];
        System.arraycopy(array1, 0, resultArray, 0, array1.length);
        System.arraycopy(array2, 0, resultArray, array1.length, array2.length);
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @param <T> 数组类型
     * @return 新数组
     */
    public <T> T[] add(T[] array, T item) {
        if (array == null) return null;
        T[] resultArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public byte[] add(byte[] array, byte item) {
        if (array == null) return new byte[] {item};
        byte[] resultArray = new byte[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public short[] add(short[] array, short item) {
        if (array == null) return new short[] {item};
        short[] resultArray = new short[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public int[] add(int[] array, int item) {
        if (array == null) return new int[] {item};
        int[] resultArray = new int[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public long[] add(long[] array, long item) {
        if (array == null) return new long[] {item};
        long[] resultArray = new long[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public float[] add(float[] array, float item) {
        if (array == null) return new float[] {item};
        float[] resultArray = new float[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public double[] add(double[] array, double item) {
        if (array == null) return new double[] {item};
        double[] resultArray = new double[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public char[] add(char[] array, char item) {
        if (array == null) return new char[] {item};
        char[] resultArray = new char[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }

    /**
     * 向数组中添加一个元素, 获得一个新的数组
     * @param array 数组
     * @param item 元素
     * @return 新数组
     */
    public boolean[] add(boolean[] array, boolean item) {
        if (array == null) return new boolean[] {item};
        boolean[] resultArray = new boolean[array.length + 1];
        System.arraycopy(array, 0, resultArray, 0, array.length);
        resultArray[array.length] = item;
        return resultArray;
    }
}
