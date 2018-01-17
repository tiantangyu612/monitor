package monitor.core.collector.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bjlizhitao on 2018/1/17.
 * 只有一个 key 的采集项
 */
public abstract class OneRowCollectorItem extends AbstractCollectorItem {

    /**
     * 收集采集项数据
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> collectData() {
        List<Map<String, Object>> collectData = new ArrayList<Map<String, Object>>(1);

        try {
            Map<String, Object> itemData = collectItemData();

            if (null != itemData) {
                collectData.add(itemData);
            }
        } catch (Throwable e) {
            //NOP
        }

        return collectData;
    }

    /**
     * 收集一条采集项数据
     *
     * @return
     */
    protected abstract Map<String, Object> collectItemData();
}
