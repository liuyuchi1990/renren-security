package io.renren.modules.bargin.dao;

import io.renren.modules.bargin.entity.BarginEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.order.model.Order;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-25 15:35:24
 */
public interface BarginDao extends BaseMapper<BarginEntity> {

    int insertBarginLog(Order order);

    List<Map<String, Object>> queryBarginLog (String id);

    List<Map<String, Object>> queryList (String id);

    Map<String, Object> queryMaxTime(Order order);
	
}
