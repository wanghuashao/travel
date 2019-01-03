package com.itheima.travel.route.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itheima.travel.route.domain.Category;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.route.service.IRouteService;
import com.itheima.travel.utils.BaseServlet;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@WebServlet(urlPatterns = "/RouteServlet")
public class RouteServlet extends BaseServlet {
//    private IRouteService service = new RouteServiceImpl();
    private IRouteService service = (IRouteService) BeanFactory.getBean("IRouteService");
    public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Category> categories = service.findAllCategory();
        String json = new ObjectMapper().writeValueAsString(categories);
//        String json = service.findAllCategoryFast();  // 使用jedis，开发用，速度更快，现在则没有必要
        response.getWriter().write(json);
    }

    public void findAllRoutesByCidForPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cid = request.getParameter("cid");
//        System.out.println("cid="+cid);
        Integer pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
//        System.out.println("pageNumber="+pageNumber);
        String rname = request.getParameter("rname");
//        System.out.println("rname="+rname);
        PageBean pageBean = new PageBean<>();
        pageBean.setPageNumber(pageNumber);
        pageBean = service.findAllRoutesByCidForPage(cid,pageBean,rname);

        List<Route> rows = pageBean.getRows();

//        System.out.println(rows);
        String json = new ObjectMapper().writeValueAsString(pageBean);
        response.getWriter().write(json);
    }

    public void showDetailRouteByRid(HttpServletRequest request,HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        String rid = request.getParameter("rid");
//        System.out.println(rid);
        Route route = service.showDetailRouteByRid(rid);
//        System.out.println(route);
        String json = new Gson().toJson(route);
        response.getWriter().write(json);
    }


}

