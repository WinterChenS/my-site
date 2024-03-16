package cn.luischen.model;

import cn.luischen.utils.XSSUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 文章实体
 * Created by winterchen on 2018/4/29.
 */
@Data
public class ContentDomain {

    /**
     * 文章的主键编号
     */
    private Integer cid;
    /**
     * 内容标题
     */
    private String title;

    /**
     * 标题图片
     */
    private String titlePic;
    /**
     * 内容缩略名
     */
    private String slug;
    /**
     * 内容生成时的GMT unix时间戳
     */
    private Integer created;
    /**
     * 内容更改时的GMT unix时间戳
     */
    private Integer modified;
    /**
     * 内容文字
     */
    private String content;
    /**
     * 内容所属用户id
     */
    private Integer authorId;
    /**
     * 内容类别
     */
    private String type;
    /**
     * 内容状态
     */
    private String status;
    /**
     * 标签列表
     */
    private String tags;
    /**
     * 分类列表
     */
    private String categories;
    /**
     * 点击次数
     */
    private Integer hits;
    /**
     * 内容所属评论数
     */
    private Integer commentsNum;
    /**
     * 是否允许评论
     */
    private Integer allowComment;
    /**
     * 是否允许ping
     */
    private Integer allowPing;
    /**
     * 允许出现在聚合中
     */
    private Integer allowFeed;





    public void setContent(String content) {
        //xss过滤
        if (StringUtils.isNotBlank(content)) {
            this.content = XSSUtil.stripXSS(content);
        } else {
            this.content = content;
        }
    }


}
