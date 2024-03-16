package cn.luischen.dto;

import cn.luischen.model.MetaDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签、分类列表
 * Created by winterchen on 2018/4/30.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MetaDto extends MetaDomain {

    private int count;

}
