package com.itheima.travel.utils;


import java.io.Serializable;
import java.util.List;

/**
 * 分页的工具类
 *
 * @author Never Say Never
 * @version V1.0
 * @date 2018-09-17
 */
public class PageBean<T> implements Serializable {

    /**
     * 本身这个类中至少要有2个属性(分页查询的数据和总页码数)
     * 为了让工具类功能更强大，将分页需要的所有数据，全部弄成这个类的属性
     */

    // 分页查询的结果
    private List<T> rows;

    // 总页码数
    private int totalPage;

    // 要查看页码
    private int pageNumber;

    // 每页显示大小
    private int pageSize;

    // 总记录数
    private int total;

    // 起始索引
    private int startIndex;

//////////////下面的属性是新添加的的///////////////////
    //上一页
    private int prePage;

    //下一页
    private int nextPage;

    //分页显示的页数(开始:默认显示1)
    private int start =1 ;

    //分页显示的页数(结束：默认显示10)
    private int end = 10;

    public PageBean() {

    }

    // 构造方法中需要提供pageNumber pageSize total 来处理分页显示的其它数据
    public PageBean(int pageNumber, int pageSize, int total) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.total = total;

        /////////计算起始索引///////////
        // 要查看的页码-1乘以每页显示的条数
        this.startIndex = (pageNumber - 1) * pageSize;

        /////////计算总页码数///////////
        // 如果总记录数与每页显示的条数能够整除就直接相除，否则相除后+1页
        this.totalPage = (total % pageSize == 0) ? (total / pageSize) : (total / pageSize + 1);

        /////////计算上一页///////////
        //如果当前页为第一页，上一页就是第一页(页面不让点击即可)
        if (pageNumber == 1) {
            this.prePage = pageNumber;
        } else {
            // 否则当前页-1
            this.prePage = pageNumber-1;
        }

        /////////计算下一页///////////
        //如果当前页为最后一页，下一页为最后一页
        if (this.pageNumber == this.totalPage) {
            this.nextPage = totalPage;
        } else {
            //否则就是当前页+1
            this.nextPage = pageNumber+1;
        }

        /////////计算显示的页数///////
        if(totalPage<=10){
            // 总页码数都小于10页了，那么end就是总页码数了(start还从默认的1)
            this.end = totalPage;
        }else{
            // 总页码数大于10了，那么根据当前页来判断start和end了(百度：前五后四)
            this.start = pageNumber-5;
            this.end = pageNumber+4;
            // 如果用户点击的当前页低于6页，那么start和end得值需要重新计算【前10页可能会出现的问题】
            if(this.pageNumber<=5){
                this.start = 1;
                this.end = 10;
            }
            // 如果用户点击的当前页超过了总页码还小4页的页码数，那么又得重新计算start和end
            if(this.pageNumber>this.totalPage-4){
                // 开始页数为总页码数-9
                this.start = this.totalPage-9;
                // 结束页数为总页码数
                this.end = this.totalPage;
            }
        }

    }

    public int getPrePage() {
        return prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }









    public int getStartIndex() {
        return startIndex = (this.getPageNumber() - 1) * this.getPageSize();
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalPage() {
        return totalPage = (this.getTotal() % this.getPageSize() == 0) ? (this.getTotal() / this.getPageSize()) : (this.getTotal() / this.getPageSize() + 1);
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "rows=" + rows +
                ", totalPage=" + totalPage +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                ", total=" + total +
                ", startIndex=" + startIndex +
                '}';
    }
}
