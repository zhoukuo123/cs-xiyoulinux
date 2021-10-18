package com.xiyoulinux.service;

import com.github.pagehelper.PageInfo;
import com.xiyoulinux.pojo.PagedGridResult;

import java.util.List;

/**
 * @author CoderZk
 */
public class BaseService {
    /**
     * 分页信息封装, 以提供给前端使用
     *
     * @param list 每页的记录数据data
     * @param page 当前页数
     * @return PagedGridResult
     */
    public PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
}
