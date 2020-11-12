package cn.luischen.dao;

import cn.luischen.model.NewsDomain;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NewsDao {

    public List<NewsDomain> getNews();

    /**
     * 添加新闻
     * @param newsDomain
     * @return
     */
    public int addNews(NewsDomain newsDomain);

    /**
     * 通过ID修改新闻
     * @param newsDomain
     * @return
     */
    public int updateNewsById(NewsDomain newsDomain);

    /**
     * 通过ID删除新闻
     * @param id
     * @return
     */
    public int deleteNewsById(Integer id);

    /**
     * 通过ID查询新闻
     * @param id
     * @return
     */
    public NewsDomain getNewsInfoById(Integer id);
}
