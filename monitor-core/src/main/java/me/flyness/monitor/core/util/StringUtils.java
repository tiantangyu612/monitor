package me.flyness.monitor.core.util;

/**
 * Created by lizhitao on 2018/1/8.
 * String 工具类
 */
public class StringUtils {
    private StringUtils() {
    }

    /**
     * 检查字符串是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }

        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查字符串是否不为空
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !StringUtils.isBlank(cs);
    }
}
