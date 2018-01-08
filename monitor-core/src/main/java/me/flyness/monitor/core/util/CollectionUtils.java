package me.flyness.monitor.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by lizhitao on 2018/1/8.
 * 集合工具类
 */
public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * 判断集合是否为空集合
     *
     * @param collection
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断集合是否不为空集合
     *
     * @param collection
     * @return
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !CollectionUtils.isEmpty(collection);
    }

    /**
     * 判断 Map 是否为空
     *
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断 Map 是否不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !CollectionUtils.isEmpty(map);
    }
}
