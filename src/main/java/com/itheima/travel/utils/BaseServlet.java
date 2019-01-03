package com.itheima.travel.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author Never Say Never
 * @version V1.0
 * @date 2018-09-28
 */
public class BaseServlet extends HttpServlet{

    // 提供一个处理用户请求能力的方法！
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //// 使用相同参数的不同值来区分用户指定的是什么功能！(以后指向这个Servlet的时候，都需要携带一个相同的参数名<action>，不同的参数值！)
        String action = request.getParameter("action");
        // 非空判断
        if(action!=null){
            try {
                // 获得字节码对象
                Class clazz = this.getClass();
                // 获得方法对象
                Method method = clazz.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
                // 执行指定的方法
                method.invoke(this,request,response);
            }catch (Exception e){
                System.out.println("哥们，你在页面指向Servlet的时候action参数的值与Servlet的方法名称不一致！");
                e.printStackTrace();
            }
        }else{
            System.out.println("哥们，你忘记在页面指向Servlet的时候携带action参数和值了……");
        }


    }
}
