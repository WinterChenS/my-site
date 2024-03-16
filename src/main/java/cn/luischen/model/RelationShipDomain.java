package cn.luischen.model;

import lombok.Data;

/**
 * 文章关联信息表
 * Created by winterchen on 2018/4/30.
 */
@Data
public class RelationShipDomain {

    /**
     * 文章主键编号
     */
    private Integer cid;
    /**
     * 项目编号
     */
    private Integer mid;
    
}
