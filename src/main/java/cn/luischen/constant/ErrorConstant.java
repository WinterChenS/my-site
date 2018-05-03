package cn.luischen.constant;

/**
 * Created by Donghua.Chen on 2018/4/29.
 */
public interface ErrorConstant {

    interface Common {
        static final String PARAM_IS_EMPTY = "参数为空";
        static final String INVALID_PARAM = "无效的参数";
        static final String CAN_NOT_FIND_PARAM_TO_CONTIUNE = "找不到参数继续运行";
    }

    interface Article {
        static final String UPDATE_ARTICLE_FAIL = "更新文章失败";
        static final String ADD_NEW_ARTICLE_FAIL = "添加文章失败";
        static final String DELETE_ARTICLE_ERROR = "删除文章失败";
        static final String TITLE_IS_TOO_LONG = "文章标题过长";
        static final String TITLE_CAN_NOT_EMPTY = "文章标题不能为空";
        static final String CONTENT_CAN_NOT_EMPTY = "文章内容不能为空";
        static final String CONTENT_IS_TOO_LONG = "文章字数超过限制";

    }

    interface Att {
        static final String ADD_NEW_ATT_FAIL = "添加附件信息失败";
        static final String UPDATE_ATT_FAIL =  "更新附件信息失败";
        static final String DELETE_ATT_FAIL = "删除附件信息失败";
        static final String UPLOAD_FILE_FAIL = "上传附件失败";
    }

    interface Comment {
        static final String ADD_NEW_COMMENT_FAIL = "添加评论失败";
        static final String UPDATE_COMMENT_FAIL = "更新评论失败";
        static final String DELETE_COMMENT_FAIL = "删除评论失败";
        static final String COMMENT_NOT_EXIST = "评论不存在";
    }

    interface Option {
        static final String DELETE_OPTION_FAIL = "删除配置失败";
        static final String UPDATE_OPTION_FAIL = "更新配置失败";
    }

    interface Meta {
        static final String ADD_META_FAIL = "添加项目信息失败";
        static final String UPDATE_META_FAIL = "更新项目信息失败";
        static final String DELETE_META_FAIL = "删除项目信息失败";
        static final String NOT_ONE_RESULT = "获取的项目的数量不止一个";
        static final String META_IS_EXIST = "该项目已经存在";
    }

    interface Auth {
        static final String USERNAME_PASSWORD_IS_EMPTY = "用户名和密码不可为空";
        static final String USERNAME_PASSWORD_ERROR = "用户名不存在或密码错误";
        static final String NOT_LOGIN = "用户未登录";
    }



}
