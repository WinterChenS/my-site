package cn.luischen.dto.cond;

/**
 * 评论的查找参数
 * Created by Donghua.Chen on 2018/4/29.
 */
public class CommentCond {
    /**
     * 状态
     */
    private String status;
    /**
     * 开始时间戳
     */
    private Integer startTime;
    /**
     * 结束时间戳
     */
    private Integer endTime;

    /**
     * 父评论编号
     */
    private Integer parent;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }
}
