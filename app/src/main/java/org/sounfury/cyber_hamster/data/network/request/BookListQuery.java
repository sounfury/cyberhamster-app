package org.sounfury.cyber_hamster.data.network.request;

import org.sounfury.cyber_hamster.data.model.Book;
import org.sounfury.cyber_hamster.data.network.page.PageQuery;

import java.util.List;

// 请求封装类
public class BookListQuery {
    private PageQuery<Book> pageQuery;
    private List<Long> ids;

    // Getter 和 Setter
    public PageQuery getPageQuery() { return pageQuery; }
    public void setPageQuery(PageQuery pageQuery) { this.pageQuery = pageQuery; }
    
    public List<Long> getIds() { return ids; }
    public void setIds(List<Long> ids) { this.ids = ids; }
}
