package cn.luischen.service.news;

import cn.luischen.model.NewsDomain;

import java.util.List;

public interface NewsService {

    List<NewsDomain> getNews();

    void addNews(String title, String content);

    void updateNewsById(Integer id, String title, String content);

    void deleteNewsById(Integer id);
}
