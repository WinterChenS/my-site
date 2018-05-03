package cn.luischen.service.relation.impl;

import cn.luischen.dao.RelationShipDao;
import cn.luischen.service.relation.RelationShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Donghua.Chen on 2018/4/30.
 */
@Service
public class RelationShipServiceImpl implements RelationShipService {

    @Autowired
    private RelationShipDao relationShipDao;


}
