package cn.luischen.dto;

import cn.luischen.model.MetaDomain;

/**
 * 标签、分类列表
 * Created by Donghua.Chen on 2018/4/30.
 */
public class MetaDto extends MetaDomain {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
