package hdo.com.andzq.utils;

/**
 * description 排序工具类
 * author 陈锐
 * version 1.0
 * created 2017/3/25
 */

public class SortUtils {
    /**
     * 二分排序
     */
    public static String[] bubbleSort(String[] StringArr) {
        String temp = "";
        int size = StringArr.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (Long.parseLong(StringArr[j]) > Long.parseLong(StringArr[j + 1]))  //交换两数位置
                {
                    temp = StringArr[j];
                    StringArr[j] = StringArr[j + 1];
                    StringArr[j + 1] = temp;
                }
            }
        }
        return StringArr;
    }

}
