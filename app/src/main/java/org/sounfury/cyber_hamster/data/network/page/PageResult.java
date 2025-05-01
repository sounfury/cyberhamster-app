package org.sounfury.cyber_hamster.data.network.page;// PageResult.java


import java.util.List;

/**
 * 通用分页返回结果
 * @param <T> 分页的数据类型
 */
public class PageResult<T> {

    // 当前页的数据列表
    private List<T> rows;

    // 总记录数
    private int totalRowCount;

    // 总页数
    private int totalPageCount;

    // Getter和Setter方法

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public int getTotalRowCount() {
        return totalRowCount;
    }

    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
