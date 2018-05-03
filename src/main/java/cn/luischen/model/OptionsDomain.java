package cn.luischen.model;

/**
 * 网站配置项
 * Created by Donghua.Chen on 2018/4/28.
 */
public class OptionsDomain {

    /** 名称 */
    private String name;
    /** 内容 */
    private String value;
    /** 备注 */
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
