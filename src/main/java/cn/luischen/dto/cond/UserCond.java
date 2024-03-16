package cn.luischen.dto.cond;

import cn.luischen.utils.TaleUtils;
import lombok.Data;

/**
 * 用户查找条件
 * Created by winterchen on 2018/4/30.
 */
@Data
public class UserCond {
    private String username;
    private String password;

}
