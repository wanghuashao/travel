package com.itheima.travel.route.dao;

import com.itheima.travel.route.domain.Category;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.route.domain.RouteImg;
import com.itheima.travel.utils.PageBean;

import java.util.List;
import java.util.Map;

public interface IRouteDao {
    List<Category> findAllCategory();

    int getRouteTotalRecordByCid(String cid, String rname);


    List<Route> findAllRoutesByCidForPage(String cid, PageBean pageBean, String rname);

    Map<String,Object> showDetailRouteByRid(String rid);

    List<RouteImg> findRouteImageByRid(String rid);
}
