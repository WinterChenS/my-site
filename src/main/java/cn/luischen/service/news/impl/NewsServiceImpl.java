package cn.luischen.service.news.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.NewsDao;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.NewsDomain;
import cn.luischen.service.news.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class NewsServiceImpl implements NewsService {

    @Autowired
    NewsDao newsDao;

    @Override
    public List<NewsDomain> getNews() {
        return newsDao.getNews();
    }

    @Override
    public void addNews(String title, String content) {
        NewsDomain newsDomain = new NewsDomain();
        newsDomain.setTitle(title);
        newsDomain.setContent(content);
        newsDomain.setCreateAt(System.currentTimeMillis() / 1000);

        newsDao.addNews(newsDomain);
        return ;
    }

    @Override
    public void updateNewsById(Integer id, String title, String content) {
        if (id == null) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        NewsDomain newsInfo = newsDao.getNewsInfoById(id);
        if (newsInfo == null) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.INVALID_PARAM);
        }

        NewsDomain newsDomain = new NewsDomain();
        newsDomain.setId(id);
        newsDomain.setTitle(title);
        newsDomain.setContent(content);
        newsDao.updateNewsById(newsDomain);
    }

    @Override
    public void deleteNewsById(Integer id) {
        if (id == null) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        }
        NewsDomain newsInfo = newsDao.getNewsInfoById(id);
        if (newsInfo == null) {
            throw BusinessException.withErrorCode(ErrorConstant.Common.INVALID_PARAM);
        }

        newsDao.deleteNewsById(id);
    }
}
