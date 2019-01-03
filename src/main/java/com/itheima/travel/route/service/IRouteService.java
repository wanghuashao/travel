package com.itheima.travel.route.service;

import com.itheima.travel.route.domain.Category;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.utils.PageBean;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface IRouteService {
    List<Category> findAllCategory();

    PageBean findAllRoutesByCidForPage(String cid, PageBean pageBean, String rname);

    Route showDetailRouteByRid(String rid) throws InvocationTargetException, IllegalAccessException;
}
