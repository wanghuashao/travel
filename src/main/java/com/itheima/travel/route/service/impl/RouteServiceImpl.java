package com.itheima.travel.route.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.route.dao.IRouteDao;
import com.itheima.travel.route.domain.Category;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.route.domain.RouteImg;
import com.itheima.travel.route.domain.Seller;
import com.itheima.travel.route.service.IRouteService;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.JedisUtil;
import com.itheima.travel.utils.PageBean;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class RouteServiceImpl implements IRouteService{
    private IRouteDao dao = (IRouteDao) BeanFactory.getBean("IRouteDao");
    public List<Category> findAllCategory() {
        List<Category> categories = dao.findAllCategory();
        return categories;
    }

    public String findAllCategoryFast() throws JsonProcessingException {
        Jedis jedis = JedisUtil.getJedis();
        String categories = jedis.get("categories");
        if (categories != null) {
            return categories;
        } else {
            List<Category> allCategory = findAllCategory();
            categories = new ObjectMapper().writeValueAsString(allCategory);
            jedis.set("categories", categories);
            return categories;
        }

    }

    public PageBean<Route> findAllRoutesByCidForPage(String cid, PageBean pageBean,String rname) {
        int pagSize = 8;
        pageBean.setPageSize(pagSize);
        // 查询指定cid下的总记录数
        int total = dao.getRouteTotalRecordByCid(cid,rname);
        System.out.println("cid"+cid);
        System.out.println("total"+total);
        pageBean.setTotal(total);
        System.out.println("pageBean"+pageBean);

        //3.调用dao层根据cid分页查询指定分类下所有旅游线路的数据的方法
        List<Route> routes = dao.findAllRoutesByCidForPage(cid,pageBean,rname);
        System.out.println("routes:"+routes);
        pageBean.setRows(routes);
        return pageBean;
    }

    @Override
    public Route showDetailRouteByRid(String rid) throws InvocationTargetException, IllegalAccessException {
        Map<String,Object> map = dao.showDetailRouteByRid(rid); // 查询旅游信息，旅欧商家，旅游分类
        List<RouteImg> routeImgs =  dao.findRouteImageByRid(rid);
        Route route = new Route();
        Seller seller = new Seller();
        Category category = new Category();
        BeanUtils.populate(route,map);
        BeanUtils.populate(seller,map);
        BeanUtils.populate(category,map);
        // 将查询到的商家，分类，图片封装到Route中
        route.setSeller(seller);
        route.setCategory(category);
        route.setRouteImgList(routeImgs);
        return route;
    }
}
