package com.itheima.travel.favorite.service;

import com.itheima.travel.user.domain.User;
import com.itheima.travel.utils.PageBean;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface IFavoriteService {
    int findFavoiteCount(String rid);

    boolean checkIsCollected(User user, String rid);

    void collectRoute(User user, String rid) throws SQLException;

    void deleteCollection(User user, String rid) throws SQLException;

    PageBean myFavorite(int pageNumber, User user) throws InvocationTargetException, IllegalAccessException;

    PageBean favoriterank(String pageNumber);
}
