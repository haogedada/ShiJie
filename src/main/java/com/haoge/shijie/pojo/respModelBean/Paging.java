package com.haoge.shijie.pojo.respModelBean;


public class Paging {
    private Integer indexPage;
    private Integer tatolPage;
    private Object list;

    public Paging() {

    }

    public Paging(Integer indexPage, Integer tatolPage, Object list) {
        this.indexPage = indexPage;
        this.tatolPage = tatolPage;
        this.list = list;
    }

    public Integer getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(Integer indexPage) {
        this.indexPage = indexPage;
    }

    public Integer getTatolPage() {
        return tatolPage;
    }

    public void setTatolPage(Integer tatolPage) {
        this.tatolPage = tatolPage;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

}
