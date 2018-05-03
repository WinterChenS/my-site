package cn.luischen.dto;

/**
 * 后台统计对象
 * Created by Donghua.Chen on 2018/4/30.
 */
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

    public Long getArticles() {
        return articles;
    }

    public void setArticles(Long articles) {
        this.articles = articles;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLinks() {
        return links;
    }

    public void setLinks(Long links) {
        this.links = links;
    }

    public Long getAttachs() {
        return attachs;
    }

    public void setAttachs(Long attachs) {
        this.attachs = attachs;
    }
}
