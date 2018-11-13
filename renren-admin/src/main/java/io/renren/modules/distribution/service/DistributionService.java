package io.renren.modules.distribution.service;


import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.distribution.dao.DistributionDao;
import io.renren.modules.distribution.entity.Distribution;
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



    PageUtils queryPage(Map<String, Object> params);

    int insertDistribution(Distribution distribution);

    Distribution queryById (String id);
}

