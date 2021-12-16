package com.stockrock.analysis.utils;

import com.stockrock.analysis.constants.CommandConstants;
import org.apache.commons.lang3.EnumUtils;

import java.util.*;

public class ArrayUtils {


    public static boolean hasString(String[] array, String element){
        return Arrays.asList(array).contains(element);
    }

    public static boolean hasInt(Integer[] array, int element){
        return Arrays.asList(array).contains(element);
    }

    public static boolean EnumHasString(Class enumClass, String search){
        return hasString(stringArrayFromEnum(CommandConstants.Commands.class), search);
    }

    public static String[] stringArrayFromEnum(Class enumClass){
        Object[] objects = EnumUtils.getEnumList(enumClass).toArray();
        String[] array = new String[objects.length];
        for (int i = 0; i < objects.length; i++){
            array[i] = objects[i].toString();
        }
        return array;
    }

    public static String[] mergeArray(String[] arrayA, String[] arrayB) {
        String[] mergedArray = new String[arrayA.length + arrayB.length];
        int i=0, j=0, k=0;
        while (i < arrayA.length) {
            mergedArray[k] = arrayA[i];
            k++;
            i++;
        }

        while (j < arrayB.length) {
            mergedArray[k] = arrayB[j];
            k++;
            j++;
        }

        Set<String> setWithNoDuplicates = new HashSet<>(Arrays.asList(mergedArray));
        Iterator<String> it = setWithNoDuplicates.iterator();
        String[] mergedArrayWithNoDuplicates = new String[setWithNoDuplicates.size()];
        int n = 0;
        while (it.hasNext()) {
            mergedArrayWithNoDuplicates[n] = it.next();
            n++;
        }

        Arrays.sort(mergedArrayWithNoDuplicates);

        return mergedArrayWithNoDuplicates;
    }

    public static String[] removeElement(String[] array, String element){
        List<String> list = new ArrayList<String>(Arrays.asList(array));
        list.remove(element);
        return list.toArray(new String[0]);
    }

    public static String[] removeElements(String[] array, String[] elements){
        List<String> list = new ArrayList<>(Arrays.asList(array));
        for (String element : elements) {
            list.remove(element);
        }
        return list.toArray(new String[0]);
    }

    public static int[] stringToIntegerArray(String[] array){
        int[] x = new int[array.length];
        for (int i = 0; i < array.length; i++){
            x[i] = Integer.parseInt(array[i]);
        }
        return x;
    }

}
