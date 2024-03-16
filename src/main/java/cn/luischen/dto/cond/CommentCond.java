package cn.luischen.dto.cond;

import lombok.Data;

/**
 * 评论的查找参数
 * Created by winterchen on 2018/4/29.
 */
@Data
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

}
