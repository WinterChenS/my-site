package cn.luischen.service.site.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.constant.Types;
import cn.luischen.constant.WebConst;
import cn.luischen.dao.AttAchDao;
import cn.luischen.dao.CommentDao;
import cn.luischen.dao.ContentDao;
import cn.luischen.dao.MetaDao;
import cn.luischen.dto.ArchiveDto;
import cn.luischen.dto.MetaDto;
import cn.luischen.dto.StatisticsDto;
import cn.luischen.dto.cond.CommentCond;
import cn.luischen.dto.cond.ContentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.CommentDomain;
import cn.luischen.model.ContentDomain;
import cn.luischen.service.site.SiteService;
import cn.luischen.utils.DateKit;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站点服务
 * Created by Donghua.Chen on 2018/4/30.
 */
@Service
public class SiteServiceImpl implements SiteService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private MetaDao metaDao;

    @Autowired
    private AttAchDao attAchDao;

    @Override
    @Cacheable(value = "siteCache", key = "'comments_' + #p0")
    public List<CommentDomain> getComments(int limit) {
        LOGGER.debug("Enter recentComments method:limit={}", limit);
        if (limit < 0 || limit > 10){
            limit = 10;
        }
        PageHelper.startPage(1, limit);
        List<CommentDomain> rs = commentDao.getCommentsByCond(new CommentCond());
        LOGGER.debug("Exit recentComments method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'newArticles_' + #p0")
    public List<ContentDomain> getNewArticles(int limit) {
        LOGGER.debug("Enter recentArticles method:limit={}", limit);
        if (limit < 0 || limit > 10)
            limit = 10;
        PageHelper.startPage(1, limit);
        List<ContentDomain> rs = contentDao.getArticlesByCond(new ContentCond());
        LOGGER.debug("Exit recentArticles method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'comment_' + #p0")
    public CommentDomain getComment(Integer coid) {
        LOGGER.debug("Enter recentComment method");
        if (null == coid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        CommentDomain comment = commentDao.getCommentById(coid);
        LOGGER.debug("Exit recentComment method");
        return comment;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'statistics_'")
    public StatisticsDto getStatistics() {
        LOGGER.debug("Enter recentStatistics method");
        //文章总数
        Long artices = contentDao.getArticleCount();

        Long comments = commentDao.getCommentsCount();

        Long links = metaDao.getMetasCountByType(Types.LINK.getType());

        Long atts = attAchDao.getAttsCount();

        StatisticsDto rs = new StatisticsDto();
        rs.setArticles(artices);
        rs.setAttachs(atts);
        rs.setComments(comments);
        rs.setLinks(links);

        LOGGER.debug("Exit recentStatistics method");
        return rs;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'archivesSimple_' + #p0")
    public List<ArchiveDto> getArchivesSimple(ContentCond contentCond) {
        LOGGER.debug("Enter getArchives method");
        List<ArchiveDto> archives = contentDao.getArchive(contentCond);
        LOGGER.debug("Exit getArchives method");
        return archives;
    }

    @Override
    @Cacheable(value = "siteCache", key = "'archives_' + #p0")
    public List<ArchiveDto> getArchives(ContentCond contentCond) {
        LOGGER.debug("Enter getArchives method");
        List<ArchiveDto> archives = contentDao.getArchive(contentCond);
        parseArchives(archives, contentCond);
        LOGGER.debug("Exit getArchives method");
        return archives;
    }



    private void parseArchives(List<ArchiveDto> archives, ContentCond contentCond) {
        if (null != archives){
            archives.forEach(archive -> {
                String date = archive.getDate();
                Date sd = DateKit.dateFormat(date, "yyyy年MM月");
                int start = DateKit.getUnixTimeByDate(sd);
                int end = DateKit.getUnixTimeByDate(DateKit.dateAdd(DateKit.INTERVAL_MONTH, sd, 1)) - 1;
                ContentCond cond = new ContentCond();
                cond.setStartTime(start);
                cond.setEndTime(end);
                cond.setType(contentCond.getType());
                List<ContentDomain> contentss = contentDao.getArticlesByCond(cond);
                archive.setArticles(contentss);
            });
        }
    }

    @Override
    @Cacheable(value = "siteCache", key = "'metas_' + #p0")
    public List<MetaDto> getMetas(String type, String orderBy, int limit) {
        LOGGER.debug("Enter metas method:type={},order={},limit={}", type, orderBy, limit);
        List<MetaDto> retList=null;
        if (StringUtils.isNotBlank(type)) {
            if(StringUtils.isBlank(orderBy)){
                orderBy = "count desc, a.mid desc";
            }
            if(limit < 1 || limit > WebConst.MAX_POSTS){
                limit = 10;
            }
            Map<String, Object> paraMap = new HashMap<>();
            paraMap.put("type", type);
            paraMap.put("order", orderBy);
            paraMap.put("limit", limit);
            retList= metaDao.selectFromSql(paraMap);
        }
        LOGGER.debug("Exit metas method");
        return retList;
    }
}
