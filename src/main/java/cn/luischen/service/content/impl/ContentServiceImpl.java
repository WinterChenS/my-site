package cn.luischen.service.content.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dao.CommentDao;
import cn.luischen.dao.ContentDao;
import cn.luischen.dao.RelationShipDao;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.dto.cond.MetaCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.CommentDomain;
import cn.luischen.model.ContentDomain;
import cn.luischen.model.RelationShipDomain;
import cn.luischen.service.content.ContentService;
import cn.luischen.service.meta.MetaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Donghua.Chen on 2018/4/29.
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MetaService metaService;

    @Autowired
    private RelationShipDao relationShipDao;


    @Transactional
    @Override
    @CacheEvict(value={"atricleCache","atricleCaches"},allEntries=true,beforeInvocation=true)
    public void addArticle(ContentDomain contentDomain) {
        if (null == contentDomain)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        if (StringUtils.isBlank(contentDomain.getTitle()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_CAN_NOT_EMPTY);
        if (contentDomain.getTitle().length() > WebConst.MAX_TITLE_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.TITLE_IS_TOO_LONG);
        if (StringUtils.isBlank(contentDomain.getContent()))
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_CAN_NOT_EMPTY);
        if (contentDomain.getContent().length() > WebConst.MAX_TEXT_COUNT)
            throw BusinessException.withErrorCode(ErrorConstant.Article.CONTENT_IS_TOO_LONG);

        //标签和分类
        String tags = contentDomain.getTags();
        String categories = contentDomain.getCategories();

        contentDao.addArticle(contentDomain);

        int cid = contentDomain.getCid();
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());
    }

    @Override
    @Transactional
    @CacheEvict(value={"atricleCache","atricleCaches"},allEntries=true,beforeInvocation=true)
    public void deleteArticleById(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        contentDao.deleteArticleById(cid);
        //同时也要删除该文章下的所有评论
        List<CommentDomain> comments = commentDao.getCommentsByCId(cid);
        if (null != comments && comments.size() > 0){
            comments.forEach(comment ->{
                commentDao.deleteComment(comment.getCoid());
            });
        }
        //删除标签和分类关联
        List<RelationShipDomain> relationShips = relationShipDao.getRelationShipByCid(cid);
        if (null != relationShips && relationShips.size() > 0){
            relationShipDao.deleteRelationShipByCid(cid);
        }

    }

    @Override
    @Transactional
    @CacheEvict(value={"atricleCache","atricleCaches"},allEntries=true,beforeInvocation=true)
    public void updateArticleById(ContentDomain contentDomain) {
        //标签和分类
        String tags = contentDomain.getTags();
        String categories = contentDomain.getCategories();

        contentDao.updateArticleById(contentDomain);
        int cid = contentDomain.getCid();
        relationShipDao.deleteRelationShipByCid(cid);
        metaService.addMetas(cid, tags, Types.TAG.getType());
        metaService.addMetas(cid, categories, Types.CATEGORY.getType());

    }

    @Override
    @Transactional
    @CacheEvict(value={"atricleCache","atricleCaches"},allEntries=true,beforeInvocation=true)
    public void updateCategory(String ordinal, String newCatefory) {
        ContentCond cond = new ContentCond();
        cond.setCategory(ordinal);
        List<ContentDomain> atricles = contentDao.getArticlesByCond(cond);
        atricles.forEach(atricle -> {
            atricle.setCategories(atricle.getCategories().replace(ordinal, newCatefory));
            contentDao.updateArticleById(atricle);
        });
    }



    @Override
    @CacheEvict(value={"atricleCache","atricleCaches"},allEntries=true,beforeInvocation=true)
    public void updateContentByCid(ContentDomain content) {
        if (null != content && null != content.getCid()) {
            contentDao.updateArticleById(content);
        }
    }

    @Override
    @Cacheable(value = "atricleCache", key = "'atricleById_' + #p0")
    public ContentDomain getArticleById(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return contentDao.getArticleById(cid);
    }

    @Override
    @Cacheable(value = "atricleCaches", key = "'articlesByCond_' + #p1 + 'type_' + #p0.type")
    public PageInfo<ContentDomain> getArticlesByCond(ContentCond contentCond, int pageNum, int pageSize) {
        if (null == contentCond)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> contents = contentDao.getArticlesByCond(contentCond);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(contents);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "atricleCaches", key = "'recentlyArticle_' + #p0")
    public PageInfo<ContentDomain> getRecentlyArticle(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContentDomain> recentlyArticle = contentDao.getRecentlyArticle();
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(recentlyArticle);
        return pageInfo;
    }

    @Override
    public PageInfo<ContentDomain> searchArticle(String param, int pageNun, int pageSize) {
        PageHelper.startPage(pageNun,pageSize);
        List<ContentDomain> contentDomains = contentDao.searchArticle(param);
        PageInfo<ContentDomain> pageInfo = new PageInfo<>(contentDomains);
        return pageInfo;
    }
}
