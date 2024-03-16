package cn.luischen.model;

import lombok.Data;

/**
 * 网站图片文件相关
 * Created by winterchen on 2018/4/29.
 */
@Data
public class AttAchDomain {

    /** 主键编号 */
    private Integer id;
    /** 文件名称 */
    private String fname;
    /** 文件类型 */
    private String ftype;
    /** 文件的地址 */
    private String fkey;
    /** 创建人的id */
    private Integer authorId;
    /** 创建的时间戳 */
    private Integer created;

}
