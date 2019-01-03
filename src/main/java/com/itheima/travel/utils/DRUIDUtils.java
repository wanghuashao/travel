package com.itheima.travel.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 德鲁伊连接池工具类
 *
 * @author Never Say Never
 * @version V1.0
 * @date 2018年8月28日
 */
public class DRUIDUtils {

    private static DataSource dataSource;

    static {
        try {
            // 获得properties文件中的数据(读的是类的根路径:直接写文件名称！以后这里可能会发生变化！)
            InputStream is = DRUIDUtils.class.getClassLoader().getResourceAsStream("druid.properties");
            // 创建一个Properties对象
            Properties prop = new Properties();
            // 加载properties文件里面的数据
            prop.load(is);
            // 获得dataSource（数据源）
            dataSource = DruidDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获得一个DataSource
     */
    public static DataSource getDataSource() {
        return dataSource;
    }



    /**
     * 获得Connection
     *
     * @throws SQLException
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 释放资源
     */
    public static void close(Connection conn,PreparedStatement pstmt) {
        try {
            if(pstmt!=null)
                pstmt.close();
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义操作数据库的工具(非常方便的执行插入、更新和删除操作)
     */
    public static int update(String sql,Object[] params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            //获得Connection对象
            conn = getConnection();
            //获得预编译对象
            pstmt = conn.prepareStatement(sql);
            //获得ParameterMetaData对象
            ParameterMetaData metaData = pstmt.getParameterMetaData();
            //获得sql语句中占位符的个数
            int count = metaData.getParameterCount();
            //遍历
            for (int i = 0; i < count; i++) {
                //挨个为sql语句的占位符赋值
                pstmt.setObject(i+1,params[i]);
            }
            //执行更新操作
            int rows = pstmt.executeUpdate();
            return rows;
        }finally {
            //使用的是DURID的close的方法(归还)
            close(conn,pstmt);
        }
    }

}
