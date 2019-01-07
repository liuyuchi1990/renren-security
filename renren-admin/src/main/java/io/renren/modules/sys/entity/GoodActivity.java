package io.renren.modules.sys.entity;

public class GoodActivity {
    private String id;
    private String name;
    private String list_pic_url;
    private String retail_price;
    private String have_pay_num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getList_pic_url() {
        return list_pic_url;
    }

    public void setList_pic_url(String list_pic_url) {
        this.list_pic_url = list_pic_url;
    }

    public String getRetail_price() {
        return retail_price;
    }

    public void setRetail_price(String retail_price) {
        this.retail_price = retail_price;
    }

    public String getHave_pay_num() {
        return have_pay_num;
    }

    public void setHave_pay_num(String have_pay_num) {
        this.have_pay_num = have_pay_num;
    }
}
