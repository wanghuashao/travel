package com.itheima.travel.route.dao.impl;

import com.itheima.travel.route.dao.IRouteDao;
import com.itheima.travel.route.domain.Category;
import com.itheima.travel.route.domain.Route;
import com.itheima.travel.route.domain.RouteImg;
import com.itheima.travel.utils.DRUIDUtils;
import com.itheima.travel.utils.PageBean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDaoImpl implements IRouteDao{
    private DataSource dataSource = DRUIDUtils.getDataSource();
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    /**
     * 查找所有的Category
     * @return
     */
    public List<Category> findAllCategory() {
        String sql = "select * from tab_category";
        List<Category> categories = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
        return categories;
    }

    /**
     * 根据种类为cid，标题类似rname查找所有的Route记录
     * @param cid
     * @param pageBean
     * @param rname
     * @return
     */

    public List<Route> findAllRoutesByCidForPage(String cid, PageBean pageBean,String rname) {
        StringBuilder sb = new StringBuilder("select * from tab_route where 1=1 ");
        List<Object> list = new ArrayList();
        if (cid != null && !"".equals(cid.trim()) && !"null".equals(cid)) {
            sb.append("and cid=? ");
            list.add(cid);
        }
        if (rname != null && !"".equals(rname.trim()) && !"null".equals(rname)) {
            sb.append("and rname like ? ");
            list.add("%"+rname+"%");
        }
//        System.out.println("startIndex="+pageBean.getStartIndex()+"pageSize="+pageBean.getPageSize());
        sb.append("limit ?,?");
        list.add(pageBean.getStartIndex());
        list.add(pageBean.getPageSize());
        String sql = sb.toString();
//        System.out.println(sql);
        Object[] params = list.toArray();
//        System.out.println(Arrays.toString(params));
        List<Route> routes = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params);
        return routes;

    }

    /**
     * 查找类型为cid名称为rName的总记录数
     * @param cid
     * @param rname
     * @return
     */
    public int getRouteTotalRecordByCid(String cid, String rname) {
        StringBuilder sb = new StringBuilder("select count(*) from tab_route where 1 = 1 ");
        List list = new ArrayList();
        if (cid != null && !"".equals(cid.trim()) && !"null".equals(cid)) {
            sb.append("and cid=? ") ;
            list.add(cid);
        }
        if (rname != null && !"".equals(rname.trim()) && !"null".equals(rname)) {
            sb.append("and rname like ? ");
            list.add("%"+rname+"%");
        }
        String sql = sb.toString();
        Object[] params = list.toArray();

        Integer count = jdbcTemplate.queryForObject(sql, params, int.class);
        return count;
    }


    /**
     * 查找每一条旅游信息的详细信息
     * @param rid
     * @return
     */
    @Override
    public Map<String, Object> showDetailRouteByRid(String rid) {
        try {
            String sql = "select * from tab_route r,tab_category c,tab_seller s where c.cid=r.cid and s.sid=r.sid and r.rid=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(sql, rid);
            return map;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<RouteImg> findRouteImageByRid(String rid) {
        try {
            String sql = "select * from tab_route_img where rid = ?";
            List<RouteImg> routeImgs = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
            return routeImgs;
        } catch (Exception e){
            return null;
        }

    }


    public static void main(String[] args) {
        RouteDaoImpl dao = new RouteDaoImpl();
//        PageBean pageBean = new PageBean();
//        System.out.println(dao.getRouteTotalRecordByCid(null,"春"));
//        pageBean.setTotal(175);
//        pageBean.setPageNumber(2);
//        pageBean.setPageSize(8);
//        List routeByCidOrCname = dao.findAllRoutesByCidForPage(null,pageBean,"春");
//        System.out.println(routeByCidOrCname);
        Map<String, Object> map = dao.showDetailRouteByRid("54969596");
        System.out.println(map);
//        for (Map.Entry<String,Object> entry : map.entrySet()) {
//            System.out.println(entry.getKey()+"....."+entry.getValue());
//        }
    }
}
