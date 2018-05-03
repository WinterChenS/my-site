package cn.luischen.service.log;

import cn.luischen.model.LogDomain;
import com.github.pagehelper.PageInfo;

/**
 * 用户请求日志
 * Created by Donghua.Chen on 2018/4/29.
 */
public interface LogService {

    /**
     * 添加
     * @param action
     * @param data
     * @param ip
     * @param authorId
     */
    void addLog(String action, String data, String ip, Integer authorId);

    /**
     * 删除日志
     * @param id
     * @return
     */
    void deleteLogById(Integer id);

    /**
     * 获取日志
     * @return
     */
    PageInfo<LogDomain> getLogs(int pageNum, int pageSize);
}
