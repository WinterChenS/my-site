package cn.luischen.model;

import lombok.Data;

/**
 * 网站配置项
 * Created by winterchen on 2018/4/28.
 */
@Data
public class OptionsDomain {

    /** 名称 */
    private String name;
    /** 内容 */
    private String value;
    /** 备注 */
    private String description;

}
