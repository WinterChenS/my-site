package cn.luischen.dto;

import cn.luischen.model.ContentDomain;

import java.util.List;

/**
 * 文章归档类
 * Created by Donghua.Chen on 2018/4/30.
 */
public class ArchiveDto {

    private String date;
    private String count;
    private List<ContentDomain> articles;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<ContentDomain> getArticles() {
        return articles;
    }

    public void setArticles(List<ContentDomain> articles) {
        this.articles = articles;
    }
}
