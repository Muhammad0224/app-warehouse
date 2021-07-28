package uz.pdp.appwarehouse.helpers;

import java.util.List;

public class Utils {

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmpty(Object o) {
        return o == null;
    }

    public static boolean isEmpty(Integer i) {
        return i == null || i == 0;
    }


    public static boolean isEmpty(Long i) {
        return i == null || i == 0;
    }


    public static boolean isEmpty(Double i) {
        return i == null || i == 0;
    }
}
