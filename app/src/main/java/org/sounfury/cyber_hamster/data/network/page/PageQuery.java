package org.sounfury.cyber_hamster.data.network.page;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 分页查询参数包装类，用于封装查询条件、分页参数和排序条件
 *
 * @param <T> 查询条件类型
 */
public class PageQuery<T> {

    /**
     * 查询条件
     */
    @Nullable
    private T spec;

    /**
     * 页码索引，从0开始
     */

    private int pageIndex;

    /**
     * 每页大小
     */
    private int pageSize;

    public PageQuery(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    /**
     * 排序条件列表
     */

    @Nullable
    private List<SortOrder> sorts;



    @Nullable
    public T getSpec() {
        return spec;
    }

    public void setSpec(@Nullable T spec) {
        this.spec = spec;
    }

    @Nullable
    public List<SortOrder> getSorts() {
        return sorts;
    }

    public void setSorts(@Nullable List<SortOrder> sorts) {
        this.sorts = sorts;
    }

    // 内部排序参数类
    public static class SortOrder {
        private String property;
        private String direction;

        // Getter和Setter
        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }

}