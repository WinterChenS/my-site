package cn.luischen.model;

import java.io.Serializable;

/**
 * 日志类
 * Created by Donghua.Chen on 2018/4/29.
 */
public class LogDomain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */
    private Integer id;

    /**
     * 产生的动作
     */
    private String action;

    /**
     * 产生的数据
     */
    private String data;

    /**
     * 发生人id
     */
    private Integer authorId;

    /**
     * 日志产生的ip
     */
    private String ip;

    /**
     * 日志创建时间
     */
    private Integer created;





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
