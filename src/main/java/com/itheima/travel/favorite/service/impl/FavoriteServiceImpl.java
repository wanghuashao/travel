package com.itheima.travel.favorite.service.impl;

import com.itheima.travel.favorite.dao.IFavoriteDao;
import com.itheima.travel.favorite.service.IFavoriteService;
import com.itheima.travel.route.domain.Favorite;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.DRUIDUtils;
import com.itheima.travel.utils.PageBean;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteServiceImpl implements IFavoriteService{
    private IFavoriteDao dao = (IFavoriteDao) BeanFactory.getBean("IFavoriteDao");
    /**
     * 查询该rid被收藏的数目
     * @param rid
     * @return
     */
    @Override
    public int findFavoiteCount(String rid) {
       int count =  dao.findFavoiteCount(rid);
        return count;
    }

    /**
     *查询该用户是否收藏了该rid
     * @param user
     * @param rid
     * @return
     */
    @Override
    public boolean checkIsCollected(User user, String rid) {

        return dao.checkIsCollected(user,rid);

    }

    @Override
    public void collectRoute(User user, String rid) throws SQLException {
        DataSource dataSource = DRUIDUtils.getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // 事务管理器(连接对象与当前线程绑定！）
        TransactionSynchronizationManager.initSynchronization();
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            // 开启事务
            conn.setAutoCommit(false);
            dao.addFavorite(user,rid,jdbcTemplate);
            dao.modifyRouteCount(rid,jdbcTemplate,"add");
            conn.commit();
        }catch (Exception e) {
            conn.rollback();
        }finally {
            // 释放资源
            TransactionSynchronizationManager.clearSynchronization();
            // 设置提交自动
            conn.setAutoCommit(true);
        }

    }

    @Override
    public void deleteCollection(User user, String rid) throws SQLException {
        DataSource dataSource = DRUIDUtils.getDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // 事务管理器(连接对象与当前线程绑定！）
        TransactionSynchronizationManager.initSynchronization();
        Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
            // 开启事务
            conn.setAutoCommit(false);
            dao.deleteFavorite(user,rid,jdbcTemplate);
            dao.modifyRouteCount(rid,jdbcTemplate,"delete");
            conn.commit();
        }catch (Exception e) {
            conn.rollback();
        }finally {
            // 释放资源
            TransactionSynchronizationManager.clearSynchronization();
            // 设置提交自动
            conn.setAutoCommit(true);
        }
    }



    @Override
    public PageBean myFavorite(int pageNumber, User user) throws InvocationTargetException, IllegalAccessException {
        int total = dao.getMyfavoriteCount(user);
        PageBean pageBean = new PageBean(pageNumber,12,total);

        List<Map<String, Object>> maps = dao.myFavorite(pageBean, user);
        List<Favorite> favoriteList = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Route route = new Route();
            Favorite favorite = new Favorite();
            BeanUtils.populate(route,map);
            BeanUtils.populate(favorite,map);
            favorite.setRoute(route);
//            System.out.println("route"+route);
           favoriteList.add(favorite);

        }

        pageBean.setRows(favoriteList);
        return pageBean;

    }

    @Override
    public PageBean favoriterank(String pageNumber) {
        int total = dao.getRouteCount();
        PageBean<Route> pageBean = new PageBean(Integer.parseInt(pageNumber),8,total);

        List<Route> routes = dao.getRoutRankByCount(pageBean);
        pageBean.setRows(routes);
        return pageBean;
    }
}
