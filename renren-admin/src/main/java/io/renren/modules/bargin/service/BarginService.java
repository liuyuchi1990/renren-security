package io.renren.modules.bargin.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.bargin.entity.BarginEntity;
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
public interface BarginService extends IService<BarginEntity> {

    PageUtils queryPage(Map<String, Object> params);

    int insertBarginLog(Order order);

    int releaseBargin(String id);

    List<Map<String, Object>> queryBarginLog (String id);

    List<Map<String, Object>> queryList (String id);

    Map<String, Object> queryMaxTime(Order order);
}

