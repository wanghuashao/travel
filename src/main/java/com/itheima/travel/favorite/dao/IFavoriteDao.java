package com.itheima.travel.favorite.dao;

import com.itheima.travel.route.domain.Route;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.utils.PageBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public interface IFavoriteDao {
    int findFavoiteCount(String rid);

    boolean checkIsCollected(User user, String rid);


    void addFavorite(User user, String rid, JdbcTemplate jdbcTemplate);

    void modifyRouteCount(String rid, JdbcTemplate jdbcTemplate ,String addOrDelete);

    void deleteFavorite(User user, String rid, JdbcTemplate jdbcTemplate);

    int getMyfavoriteCount(User user);

    List<Map<String,Object>> myFavorite(PageBean<Route> pageBean, User user);

    int getRouteCount();

    List<Route> getRoutRankByCount(PageBean<Route> pageBean);
}
