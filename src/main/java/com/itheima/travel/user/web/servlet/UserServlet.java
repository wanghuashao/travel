package com.itheima.travel.user.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.utils.ResultInfo;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.user.exception.ReActiveException;
import com.itheima.travel.user.exception.RegisterErrorException;
import com.itheima.travel.user.exception.UsernameExsitException;
import com.itheima.travel.user.exception.UsernameNotNullException;
import com.itheima.travel.user.service.IUserService;
import com.itheima.travel.utils.BaseServlet;
import com.itheima.travel.utils.BeanFactory;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "UserServlet", urlPatterns = "/UserServlet")
public class UserServlet extends BaseServlet {
//    private IUserService userService = new UserServiceImpl();
    private IUserService userService = (IUserService) BeanFactory.getBean("IUserService");
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doGet(request, response);
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//
//        if ("checkUsername".equals(action)) {
//            checkUsername(request, response);
//        } else if ("checkVerifyCode".equals(action)) {
//            checkVerifyCode(request, response);
//        } else if ("register".equals(action)) {
//            register(request, response);
//        } else if ("active".equals(action)) {
//            active(request,response);
//        } else if("login".equals(action)) {
//            login(request,response);
//        } else if("getUserData".equals(action)) {
//            getUserData(request,response);
//        } else if ("logout".equals(action)) {
//            logout(request,response);
//        }
//    }



    /**
     * 检查用户名是否重复
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void checkUsername(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("username");
        ResultInfo resultInfo = null;
        try {
            boolean flag = userService.findUserInfoByUserName(userName);
            resultInfo = new ResultInfo(true);
        } catch (UsernameNotNullException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (UsernameExsitException e) {
            resultInfo = new ResultInfo(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 验证验证码
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void checkVerifyCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String verifyCode = request.getParameter("verifyCode"); // 注册页面的验证码
        String vc = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        ResultInfo resultInfo = null;
        if (vc.equalsIgnoreCase(verifyCode)) {
            resultInfo = new ResultInfo(true);
        } else {
            resultInfo = new ResultInfo(false, "验证码错误");
        }
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 用户激活账号
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        try {
            userService.active(code);
            response.sendRedirect("login.html");
        } catch (ReActiveException e) {
            response.getWriter().write("验证码已经被激活了");
        }

    }

    /**
     * 注册功能
     * @param request
     * @param response
     * @throws IOException
     */
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User();
        ResultInfo resultInfo = null;
        System.out.println(request.getParameter("username"));
        try {
            BeanUtils.populate(user, request.getParameterMap());
            int i = userService.register(user);
            System.out.println("i="+i);
            resultInfo = new ResultInfo(true);
        } catch (RegisterErrorException e) {
            resultInfo = new ResultInfo(false);
            e.printStackTrace();
        } catch (Exception e) {
            resultInfo = new ResultInfo(false);
            e.printStackTrace();
        }
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 用户登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String verifyCode = request.getParameter("check");
        ResultInfo resultInfo  = null;
        String vc = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        if(vc.equalsIgnoreCase(verifyCode)) {
            try {
                User user = userService.login(username, password);
                if(user != null) {
                    resultInfo = new ResultInfo(true);
                    request.getSession().setAttribute("user",user);
                } else {
                    resultInfo = new ResultInfo(false,"用户名或者密码错误");
                }
            } catch (Exception e) {
                resultInfo = new ResultInfo(false,"服务器错误");
            }
        } else {
            resultInfo = new ResultInfo(false,"验证码错误");
        }
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 获取session域中的用户信息
     * @param request
     * @param response
     * @throws IOException
     */
    public void getUserData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo resultInfo = null;
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            resultInfo = new ResultInfo(false);
        } else {
            resultInfo = new ResultInfo(true,user,null);
        }
        String json = new ObjectMapper().writeValueAsString(resultInfo);
        response.getWriter().write(json);
    }

    /**
     * 退出登录
     * @param request
     * @param response
     * @throws IOException
     */
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResultInfo resultInfo = null;
       request.getSession().invalidate();
       response.sendRedirect("index.html");
    }
}
