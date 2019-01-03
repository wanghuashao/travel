package com.itheima.travel.favorite.dao.impl;

import com.itheima.travel.favorite.dao.IFavoriteDao;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.utils.DRUIDUtils;
import com.itheima.travel.utils.PageBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class FavoriteDaoImpl implements IFavoriteDao {
    private DataSource dataSource = DRUIDUtils.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    /**
     * 查询该rid下的总收藏数
     * @param rid
     * @return
     */
    @Override
    public int findFavoiteCount(String rid) {
        String sql = "select count from tab_route where rid = ?";
//        System.out.println("rid=" + rid);
        try {
            Integer count = jdbcTemplate.queryForObject(sql, int.class, rid);
            return count;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 查询该user是否收藏了该rid
     *
     * @param user
     * @param rid
     * @return
     */
    @Override
    public boolean checkIsCollected(User user, String rid) {
        String sql = "select count(*) from tab_favorite where uid = ? and rid =?";
        Object[] params = {user.getUid(), rid};
        Integer count = jdbcTemplate.queryForObject(sql, int.class, params);
        return count > 0;
    }

    /**
     * 添加该用户喜欢的rid
     *
     * @param user
     * @param rid
     * @param template
     */
    @Override
    public void addFavorite(User user, String rid, JdbcTemplate template) {
        String sql = "insert into tab_favorite values(?,NOW(),?)";
        // 执行更新操作
        template.update(sql, rid, user.getUid());
    }

    /**
     * 修改route的count
     *
     * @param rid
     * @param template
     * @param addOrDelete
     */
    @Override
    public void modifyRouteCount(String rid, JdbcTemplate template, String addOrDelete) {
        String sql = "";
        if ("add".equals(addOrDelete)) {
            sql = "update tab_route set count=count+1 where rid=?";
        } else {
            sql = "update tab_route set count=count-1 where rid=?";
        }

        // 执行更新操作
        template.update(sql, rid);
    }

    /**
     * 取消该用户收藏的rid
     *
     * @param user
     * @param rid
     * @param template
     */
    @Override
    public void deleteFavorite(User user, String rid, JdbcTemplate template) {
        String sql = "delete  FROM tab_favorite where rid = ? and uid=?";
        // 执行更新操作
        template.update(sql, rid, user.getUid());
    }

    @Override
    public int getMyfavoriteCount(User user) {
        String sql = "select count(*) from tab_favorite where uid=?";
        Integer count = jdbcTemplate.queryForObject(sql, int.class, user.getUid());
        return count;
    }

    @Override
    public List<Map<String,Object>>  myFavorite(PageBean<Route> pageBean, User user) {
        String sql = "select * from tab_favorite f,tab_route r where r.rid=f.rid and f.uid=? limit ?,?";
        Object[] params = {user.getUid(),pageBean.getStartIndex(),pageBean.getPageSize()};
        // 4.执行查询操作
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, params);
        return mapList;
    }

    @Override
    public int getRouteCount() {
        String sql = "select count(*) from tab_route ";
        Integer count = jdbcTemplate.queryForObject(sql, int.class);
        return count;
    }

    @Override
    public List<Route> getRoutRankByCount(PageBean<Route> pageBean) {
        String sql = "select * FROM tab_route ORDER BY count desc limit ?,?";
        Object[] params = {pageBean.getStartIndex(),pageBean.getPageSize()};
        try {
            List<Route> routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params);
            return routes;
        } catch (Exception e) {
            return null;
        }
    }


    public static void main(String[] args) {
        FavoriteDaoImpl dao = new FavoriteDaoImpl();
//        int c = dao.findFavoiteCount("2");
        User user = new User();
        user.setUid(1);
        int c = dao.getMyfavoriteCount(user);
        System.out.println(c);
    }
}
