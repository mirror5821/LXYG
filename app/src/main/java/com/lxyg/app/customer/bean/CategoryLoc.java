package com.lxyg.app.customer.bean;

import java.util.ArrayList;
import java.util.List;

import dev.mirror.library.utils.JsonUtils;

/**
 * Created by 王沛栋 on 2015/12/19.
 */
public class CategoryLoc {
    public static List<Category> getCategory(){
        List<Category> list = new ArrayList<>();
        int [] ids = {1,8,7,6,2,5,4,3};
        String [] names = {"休闲零食","饮料饮品","酒类","粮油调味","冲调饮品","洗化纸品","文体五金","家居家电"};
        String [] imgs = {"http://lxyg8.b0.upaiyun.com/icon/c_xxsp.png","http://lxyg8.b0.upaiyun.com/icon/c_ylyp.png","http://lxyg8.b0.upaiyun.com/icon/c_j.png",
                "http://lxyg8.b0.upaiyun.com/icon/c_lytw.png","http://lxyg8.b0.upaiyun.com/icon/c_ctyp.png",
                "http://lxyg8.b0.upaiyun.com/icon/c_xhzp.png","http://lxyg8.b0.upaiyun.com/icon/c_wjdq.png","http://lxyg8.b0.upaiyun.com/icon/c_jyjd.png"};
        for (int i =0;i<ids.length;i++){
            Category c = new Category();
            c.setId(ids[i]);
            c.setName(names[i]);
            c.setImg(imgs[i]);
            list.add(c);
        }
        return list;
    }
}
