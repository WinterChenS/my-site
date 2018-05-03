package cn.luischen.service.attach;

import cn.luischen.dto.AttAchDto;
import cn.luischen.model.AttAchDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 附件服务层
 * Created by Donghua.Chen on 2018/4/29.
 */
public interface AttAchService {

    /**
     * 添加单个附件信息
     * @param attAchDomain
     * @return
     */
    void addAttAch(AttAchDomain attAchDomain);

    /**
     * 批量添加附件信息
     * @param list
     * @return
     */
    void batchAddAttAch(List<AttAchDomain> list);

    /**
     * 根据主键编号删除附件信息
     * @param id
     * @return
     */
    void deleteAttAch(Integer id);

    /**
     * 更新附件信息
     * @param attAchDomain
     * @return
     */
    void updateAttAch(AttAchDomain attAchDomain);

    /**
     * 根据主键获取附件信息
     * @param id
     * @return
     */
    AttAchDto getAttAchById(Integer id);

    /**
     * 获取所有的附件信息
     * @return
     */
    PageInfo<AttAchDto> getAtts(int pageNum, int pageSize);
}
