package io.renren.modules.gather.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.gather.entity.PrizeEntity;
import io.renren.modules.order.model.Order;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-11 10:39:02
 */
public interface GatherService extends IService<GatherEntity> {

    PageUtils queryPage(Map<String, Object> params);

    int insertPrizeLog(PrizeEntity pz);

    int insertLikeLog(PrizeEntity pz);

    int updatePrizeLog(PrizeEntity pz);

    List<Map<String, Object>> queryLike(String id);

    List<Map<String, Object>> queryLikeLog(String id);

    List<Map<String, Object>> queryGatherByMobileAndActivityId(Order order);

    Map<String, Object> queryPrizeLog(String id);

    Map<String, Object> queryLikeTime(PrizeEntity pz);

    List<Map<String, Object>> queryList (Map<String, Object> param);

    int releasePrize(String id);
}

