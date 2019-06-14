package io.renren.modules.gather.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.distribution.dao.DistributionDao;
import io.renren.modules.gather.dao.GatherDao;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.gather.entity.PrizeEntity;
import io.renren.modules.gather.service.GatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("gatherService")
public class GatherServiceImpl extends ServiceImpl<GatherDao, GatherEntity> implements GatherService {

    @Autowired
    private GatherService gatherService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<GatherEntity> page = this.selectPage(
                new Query<GatherEntity>(params).getPage(),
                new EntityWrapper<GatherEntity>()
        );

        return new PageUtils(page);
    }

    public int insertPrizeLog(PrizeEntity pz){
        return gatherService.insertPrizeLog(pz);
    }

    public int updatePrizeLog(PrizeEntity pz){
        return gatherService.updatePrizeLog(pz);
    }

    public     int insertLikeLog(PrizeEntity pz){
        return gatherService.insertLikeLog(pz);
    }

    public List<Map<String, Object>> queryLike(String id){
        return gatherService.queryLike(id);
    }
    public List<Map<String, Object>> queryPrizeLog(String id){
        return gatherService.queryPrizeLog(id);
    }
}
