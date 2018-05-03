package cn.luischen.service.meta;

import cn.luischen.dto.MetaDto;
import cn.luischen.dto.cond.MetaCond;
import cn.luischen.model.MetaDomain;

import java.util.List;

/**
 * 项目服务层
 * Created by Donghua.Chen on 2018/4/29.
 */
public interface MetaService {
    /**
     * 添加项目
     * @param meta
     * @return
     */
    void addMeta(MetaDomain meta);

    /**
     * 添加
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);



    /**
     * 批量添加
     * @param cid
     * @param names
     * @param type
     */
    void addMetas(Integer cid, String names, String type);



    /**
     * 添加或者更新
     * @param cid
     * @param name
     * @param type
     */
    void saveOrUpdate(Integer cid, String name, String type);

    /**
     * 删除项目
     * @param mid
     * @return
     */
    void deleteMetaById(Integer mid);

    /**
     * 更新项目
     * @param meta
     * @return
     */
    void updateMeta(MetaDomain meta);

    /**
     * 根据编号获取项目
     * @param mid
     * @return
     */
    MetaDomain getMetaById(Integer mid);

    /**
     * 获取所有的项目
     * @param metaCond 查询条件
     * @return
     */
    List<MetaDomain> getMetas(MetaCond metaCond);

    /**
     * 根据类型查询项目列表，带项目下面的文章数
     * @param type
     * @param orderby
     * @param limit
     * @return
     */
    List<MetaDto> getMetaList(String type, String orderby, int limit);
}
