package com.itheima.travel.favorite.web.servlet;

import com.google.gson.Gson;
import com.itheima.travel.favorite.service.IFavoriteService;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.utils.BaseServlet;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.PageBean;
import com.itheima.travel.utils.ResultInfo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/FavoriteServlet")
public class FavoriteServlet extends BaseServlet {
    private IFavoriteService service = (IFavoriteService) BeanFactory.getBean("IFavoriteService");

    /**
     *检查该rid是否被user收藏
     * @param request
     * @param response
     * @throws IOException
     */
    public void checkIsCollected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo resultInfo = null;
        String rid = request.getParameter("rid");
        if (rid != null && !"".equals(rid.trim())&& !"null".equals(rid)) {
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                boolean flag = service.checkIsCollected(user, rid);
//                System.out.println("用户是否收藏" + flag);
                resultInfo = new ResultInfo(true,flag);
            } else {
                resultInfo = new ResultInfo(false, false, "登录超时");
            }
        } else {
            resultInfo = new ResultInfo(false,false,"内部错误");
        }
        String json = new Gson().toJson(resultInfo);

        response.getWriter().write(json);
    }

    /**
     * 用户收藏
     * @param request
     * @param response
     */
    public void collectRoute(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo resultInfo = new ResultInfo();
        if (user != null) {
            if (rid != null && !"".equals(rid.trim())&& !"null".equals(rid)) {
                service.collectRoute(user,rid);
                resultInfo = new ResultInfo(true,true);
            }
        } else {
            resultInfo = new ResultInfo(false,false,"用户未登录");
        }
        String json = new Gson().toJson(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 取消收藏
     * @param request
     * @param response
     */
    public void deleteCollection(HttpServletRequest request,HttpServletResponse response) throws SQLException, IOException {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo resultInfo = new ResultInfo();
        if (user != null) {
            if (rid != null && !"".equals(rid.trim()) && !"null".equals(rid)) {
                service.deleteCollection(user,rid);
                resultInfo = new ResultInfo(true,true);
            }
        }else {
            resultInfo = new ResultInfo(false,false,"登录超时");
        }
        String json = new Gson().toJson(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 我的收藏界面
     * @param request
     * @param response
     * @throws IOException
     */
    public void myFavorite(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String pageNumber = request.getParameter("pageNumebr");
        User user = (User) request.getSession().getAttribute("user");
        ResultInfo resultInfo = new ResultInfo();
        if (user != null) {
           try {

               PageBean pageBean = service.myFavorite(Integer.parseInt(pageNumber), user);
               resultInfo = new ResultInfo(true,pageBean);
           } catch (Exception e) {
               resultInfo = new ResultInfo(false,"出错了，请联系管理员");
           }
        } else {
            resultInfo = new ResultInfo(false,"登录超时了");
        }
        String json = new Gson().toJson(resultInfo);
        response.getWriter().write(json);
    }

    public void favoriterank(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String pageNumber = request.getParameter("pageNumber");
        ResultInfo resultInfo = null;
        try {
            PageBean pageBean = service.favoriterank(pageNumber);
            System.out.println("startindex："+pageBean.getStartIndex());
            resultInfo = new ResultInfo(true,pageBean);
        } catch (Exception e) {
            resultInfo = new ResultInfo(false,"系统错误");
        }
        String json = new Gson().toJson(resultInfo);
        // System.out.println(json);

        response.getWriter().write(json);


    }
}