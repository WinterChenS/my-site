package cn.luischen.model;

/**
 * 文章关联信息表
 * Created by Donghua.Chen on 2018/4/30.
 */
public class RelationShipDomain {

    /**
     * 文章主键编号
     */
    private Integer cid;
    /**
     * 项目编号
     */
    private Integer mid;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}
