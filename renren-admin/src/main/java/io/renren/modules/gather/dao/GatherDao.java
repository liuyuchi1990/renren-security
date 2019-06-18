package io.renren.modules.gather.dao;

import io.renren.modules.gather.entity.GatherEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.gather.entity.PrizeEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-11 10:39:02
 */
public interface GatherDao extends BaseMapper<GatherEntity> {

    int insertPrizeLog(PrizeEntity pz);

    int updatePrizeLog(PrizeEntity pz);

    int insertLikeLog(PrizeEntity pz);

    List<Map<String, Object>> queryLike(String id);

    Map<String, Object> queryPrizeLog(String id);

    Map<String, Object> queryLikeTime(PrizeEntity pz);

    int releasePrize(String id);
	
}
