package com.jd.jmi.escort.model;

/**
 * 翻页查询基础对象
 * Created by xuyonggang on 2016/2/18.
 */
public class PageBaseQuery {

    /**
     * 一页显示数量
     */
    private Integer pageSize;

    /**
     * 开始位置
     */
    private Integer startRow;

    private Integer page;


    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 1) {
            return 20;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRow() {
        if (startRow == null) {
            startRow = (this.getPage() - 1) * getPageSize();
        }
        if (startRow < 0) {
            startRow = 0;
        }
        return startRow;
    }

    public Integer getPage() {
        if (page == null || page <= 0) {
            return 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }
}
