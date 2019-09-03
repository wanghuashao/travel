package com.itheima.travel.route.domain;

import com.itheima.travel.user.domain.User;

import java.io.Serializable;
import java.util.Objects;

/**
 * 收藏实体类
 */
public class Favorite implements Serializable {
    private Route route;//旅游线路对象

    private String date;//收藏时间

    private User user;//所属用户

    /**
     * 无参构造方法
     */
    public Favorite() {
    }

    /**
     * 有参构造方法
     * @param route
     * @param date
     * @param user
     */
    public Favorite(Route route, String date, User user) {
            this.route = route;
            this.date = date;
            this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Favorite favorite = (Favorite) o;
        return Objects.equals(route, favorite.route) &&
                Objects.equals(date, favorite.date) &&
                Objects.equals(user, favorite.user);
    }

    @Override
    public int hashCode() {

        return Objects.hash(route, date, user);
    }
}
