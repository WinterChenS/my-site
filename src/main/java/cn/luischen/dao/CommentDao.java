package cn.luischen.dao;

import cn.luischen.dto.cond.CommentCond;
import cn.luischen.model.CommentDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 评论实体类
 * Created by Donghua.Chen on 2018/4/29.
 */
@Mapper
@Component
public interface CommentDao {

    /**
     * 新增评论
     * @param commentDomain
     * @return
     */
    int addComment(CommentDomain commentDomain);

    /**
     * 删除评论
     * @param coid
     * @return
     */
    int deleteComment(@Param("coid") Integer coid);

    /**
     * 更新评论的状态
     * @param coid
     * @return
     */
    int updateCommentStatus(@Param("coid") Integer coid, @Param("status") String status);

    /**
     * 获取单条评论
     * @param coid
     * @return
     */
    CommentDomain getCommentById(@Param("coid") Integer coid);
    /**
     * 根据文章编号获取评论列表
     * @param cid
     * @return
     */
    List<CommentDomain> getCommentsByCId(@Param("cid") Integer cid);

    /**
     * 根据条件获取评论列表
     * @param commentCond
     * @return
     */
    List<CommentDomain> getCommentsByCond(CommentCond commentCond);

    /**
     * 获取文章数量
     * @return
     */
    Long getCommentsCount();
}
