package monitor.view.domain.vo;

import java.util.List;

/**
 * 分页信息
 *
 * @param <T>
 */
public class Pager<T> {
    /**
     * 分页结果集
     */
    private List<T> dataList = null;
    /**
     * 记录总数
     */
    private int totalCount = 0;
    /**
     * 每页显示记录数
     */
    private int pageSize = 0;
    /**
     * 当前页数
     */
    private int currentPage = 1;
    /**
     * 总页数
     */
    private int pageCount = 1;

    private int offset = 0;

    /* 初始化分页组件 */
    public Pager(Integer pageSize, Integer totalCount, Integer currentPage) {
        if (null == pageSize) {
            pageSize = 10;
        }

        setPageSize(pageSize);

        if (null == totalCount) {
            totalCount = 0;
        }

        setTotalCount(totalCount);

        Double pageNum = Math.ceil((getTotalCount() + 0.00) / pageSize);
        if (null == currentPage || currentPage < 1 || totalCount == 0) {
            setCurrentPage(1);
        } else if (currentPage > pageNum.intValue()) {
            setCurrentPage(pageNum.intValue());
        } else {
            setCurrentPage(currentPage);
        }

        setPageCount(pageNum.intValue());

        setOffset((currentPage - 1) * pageSize);
    }

    public Pager(int totalCount, int pageSize, int currentPage, List<T> dataList) {
        this.dataList = dataList;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalCount = totalCount;
    }

    public Pager(Pager<T> pager) {
        pageSize = pager.getPageSize();
        currentPage = pager.getCurrentPage();
        totalCount = pager.getTotalCount();
        pageCount = pager.getPageCount();
    }

    public Pager() {
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    // 计算总页数，如果为0则置为1
    public int getPageCount() {
        return pageCount;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "dataList=" + dataList +
                ", totalCount=" + totalCount +
                ", pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", pageCount=" + pageCount +
                ", offset=" + offset +
                '}';
    }
}