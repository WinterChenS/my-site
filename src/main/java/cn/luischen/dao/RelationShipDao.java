package cn.luischen.dao;

import cn.luischen.model.RelationShipDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 中间表
 * Created by Donghua.Chen on 2018/4/30.
 */
@Mapper
@Component
public interface RelationShipDao {

    /**
     * 添加
     * @param relationShip
     * @return
     */
    int addRelationShip(RelationShipDomain relationShip);

    /**
     * 根据文章编号和meta编号删除关联
     * @param cid
     * @param mid
     * @return
     */
    int deleteRelationShipById(@Param("cid") Integer cid, @Param("mid") Integer mid);

    /**
     * 根据文章编号删除关联
     * @param cid
     * @return
     */
    int deleteRelationShipByCid(@Param("cid") Integer cid);

    /**
     * 根据meta编号删除关联
     * @param mid
     * @return
     */
    int deleteRelationShipByMid(@Param("mid") Integer mid);

    /**
     * 更新
     * @param relationShip
     * @return
     */
    int updateRelationShip(RelationShipDomain relationShip);

    /**
     * 根据文章主键获取关联
     * @param cid
     * @return
     */
    List<RelationShipDomain> getRelationShipByCid(@Param("cid") Integer cid);

    /**
     * 根据meta编号获取关联
     * @param mid
     * @return
     */
    List<RelationShipDomain> getRelationShipByMid(@Param("mid") Integer mid);

    /**
     * 获取数量
     * @param cid
     * @param mid
     * @return
     */
    Long getCountById(@Param("cid") Integer cid, @Param("mid") Integer mid);
}
