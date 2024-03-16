package cn.luischen.dto;

import lombok.Data;

/**
 * 后台统计对象
 * Created by winterchen on 2018/4/30.
 */
@Data
public class StatisticsDto {

    /**
     * 文章数
     */
    private Long articles;
    /**
     * 评论数
     */
    private Long comments;
    /**
     * 连接数
     */
    private Long links;
    /**
     * 附件数
     */
    private Long attachs;

}
