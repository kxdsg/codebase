package com.argus.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingding on 18/12/11.
 */
public class PageUtil {
    private static final long serialVersionUID = 1L;
    private long nextPage;
    private long prePage;
    private long curPage;
    private long totalPage;
    private long rowCount;
    private List pageData;

    public PageUtil(String curPage, long rowCount, String pageSize, List pageData)
    {
        this.curPage = Long.parseLong(curPage);

        this.totalPage = ((rowCount + Long.parseLong(pageSize) - 1L) / Long.parseLong(pageSize));
        if (this.curPage == 1L) {
            this.prePage = 1L;
        } else {
            this.prePage = (Long.parseLong(curPage) - 1L);
        }
        this.rowCount = rowCount;
        if (this.curPage == this.totalPage) {
            this.nextPage = this.totalPage;
        } else {
            this.nextPage = (Long.parseLong(curPage) + 1L);
        }
        this.pageData = pageData;
    }

    public long getNextPage()
    {
        return this.nextPage;
    }

    public void setNextPage(long nextPage)
    {
        this.nextPage = nextPage;
    }

    public long getPrePage()
    {
        return this.prePage;
    }

    public void setPrePage(long prePage)
    {
        this.prePage = prePage;
    }

    public long getCurPage()
    {
        return this.curPage;
    }

    public void setCurPage(long curPage)
    {
        this.curPage = curPage;
    }

    public long getTotalPage()
    {
        return this.totalPage;
    }

    public void setTotalPage(long totalPage)
    {
        this.totalPage = totalPage;
    }

    public long getRowCount()
    {
        return this.rowCount;
    }

    public void setRowCount(long rowCount)
    {
        this.rowCount = rowCount;
    }

    public List getPageData()
    {
        return this.pageData;
    }

    public void setPageData(List pageData)
    {
        this.pageData = pageData;
    }

    public static <T> List<T> getPageList(int curPage, int perCount, List<T> list)
    {
        List<T> listPage = new ArrayList();
        int startIndex = (curPage - 1) * perCount;
        if (startIndex >= list.size()) {
            return listPage;
        }
        int endIndex = curPage * perCount - 1;
        if (endIndex >= list.size()) {
            endIndex = list.size() - 1;
        }
        for (int i = startIndex; i <= endIndex; i++) {
            listPage.add(list.get(i));
        }
        return listPage;
    }

    public static int getPageCount(int count, int perCount)
    {
        int pageCount = count / perCount;
        if (count % perCount > 0) {
            pageCount++;
        }
        return pageCount;
    }
}
