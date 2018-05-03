package cn.luischen.service.comment.impl;

import cn.luischen.constant.ErrorConstant;
import cn.luischen.dao.CommentDao;
import cn.luischen.dto.cond.CommentCond;
import cn.luischen.exception.BusinessException;
import cn.luischen.model.CommentDomain;
import cn.luischen.service.comment.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 评论实现类
 * Created by Donghua.Chen on 2018/4/29.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;



    private static final Map<String,String> STATUS_MAP = new ConcurrentHashMap<>();

    /**
     * 评论状态：正常
     */
    private static final String STATUS_NORMAL = "normal";
    /**
     * 评论状态：不显示
     */
    private static final String STATUS_BLANK = "blank";

    static {
        STATUS_MAP.put("normal",STATUS_NORMAL);
        STATUS_MAP.put("blank",STATUS_BLANK);
    }

    @Override
    public void addComment(CommentDomain commentDomain) {
        if (StringUtils.isBlank(commentDomain.getAuthor()))
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);

        commentDao.addComment(commentDomain);
    }

    @Transactional
    @Override
    public void deleteComment(Integer coid) {
        if (null == coid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        // 如果删除的评论存在子评论，一并删除
        //查找当前评论是否有子评论
        CommentCond commentCond = new CommentCond();
        commentCond.setParent(coid);
        List<CommentDomain> childComments = commentDao.getCommentsByCond(commentCond);
        //删除子评论
        if (null != childComments && childComments.size() > 0){
            for (int i = 0; i < childComments.size(); i++) {
                commentDao.deleteComment(childComments.get(i).getCoid());
            }
        }
        //删除当前评论
        commentDao.deleteComment(coid);

    }

    @Override
    public void updateCommentStatus(Integer coid, String status) {
        if (null == coid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        commentDao.updateCommentStatus(coid, status);
    }

    @Override
    public CommentDomain getCommentById(Integer coid) {
        if (null == coid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);

        return commentDao.getCommentById(coid);
    }

    @Override
    public List<CommentDomain> getCommentsByCId(Integer cid) {
        if (null == cid)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        return commentDao.getCommentsByCId(cid);
    }

    @Override
    public PageInfo<CommentDomain> getCommentsByCond(CommentCond commentCond, int pageNum, int pageSize) {
        if (null == commentCond)
            throw BusinessException.withErrorCode(ErrorConstant.Common.PARAM_IS_EMPTY);
        PageHelper.startPage(pageNum, pageSize);
        List<CommentDomain> comments = commentDao.getCommentsByCond(commentCond);
        PageInfo<CommentDomain> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }
}
