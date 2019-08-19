package io.renren.modules.distribution.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.bargin.entity.BarginEntity;
import io.renren.modules.distribution.dao.DistributionDao;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.groupon.entity.GrouponEntity;
import io.renren.modules.lottery.entity.LotteryEntity;
import io.renren.modules.sys.entity.ActivityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author richard
 * @email sunlightcs@gmail.com
 * @date 2018-10-26 14:49:43
 */
@Service
public interface DistributionService extends IService<Distribution> {

    List<Distribution> queryList(Map<String, Object> params);

    List<Distribution> queryListByPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params);

    int insertDistribution(Distribution distribution);

    int insertActivity (Distribution distribution);

    int insertActivity (BarginEntity barginEntity);

    int insertActivity (GatherEntity gatherEntity);

    int insertActivity (GrouponEntity grouponEntity);

    int insertActivity (LotteryEntity lotteryEntity);

    int updateActivity (Distribution distribution);

    int updateActivity (BarginEntity barginEntity);

    int updateActivity (GatherEntity gatherEntity);

    int updateActivity (GrouponEntity grouponEntity);

    int updateActivity (LotteryEntity lotteryEntity);

    int release(String id);

    int rollback(String id);

    int addWatcher(Distribution distributio);

    Distribution queryById (String id);
}

