package cn.luischen.service.content;

import cn.luischen.dto.cond.ContentCond;
import cn.luischen.model.ContentDomain;
import com.github.pagehelper.PageInfo;

/**
 * 文章服务层
 * Created by Donghua.Chen on 2018/4/29.
 */
public interface ContentService {

    /**
     * 添加文章
     * @param contentDomain
     * @return
     */
    void addArticle(ContentDomain contentDomain);

    /**
     * 根据编号删除文章
     * @param cid
     * @return
     */
    void deleteArticleById(Integer cid);

    /**
     * 更新文章
     * @param contentDomain
     * @return
     */
    void updateArticleById(ContentDomain contentDomain);

    /**
     * 更新分类
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal, String newCatefory);



    /**
     * 添加文章点击量
     * @param content
     */
    void updateContentByCid(ContentDomain content);

    /**
     * 根据编号获取文章
     * @param cid
     * @return
     */
    ContentDomain getArticleById(Integer cid);

    /**
     * 根据条件获取文章列表
     * @param contentCond
     * @return
     */
    PageInfo<ContentDomain> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize);

    /**
     * 获取最近的文章（只包含id和title）
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<ContentDomain> getRecentlyArticle(int pageNum, int pageSize);

    /**
     * 搜索文章
     * @param param
     * @param pageNun
     * @param pageSize
     * @return
     */
    PageInfo<ContentDomain> searchArticle(String param, int pageNun, int pageSize);
}
