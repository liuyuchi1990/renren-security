package io.renren.modules.distribution.service.impl;

import io.renren.modules.distribution.dao.DistributionDao;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;


@Service("distributionService")
public class DistributionServiceImpl extends ServiceImpl<DistributionDao, Distribution> implements DistributionService,Serializable {
    @Autowired
    private DistributionDao distributionDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<Distribution> page = this.selectPage(
                new Query<Distribution>(params).getPage(),
                new EntityWrapper<Distribution>()
        );

        return new PageUtils(page);
    }


    public List<Distribution> queryList(Map<String, Object> params) {
        return distributionDao.queryList(params);
    }

    public Distribution queryById(String id) {
        return distributionDao.queryById(id);
    }


    public int insertDistribution(Distribution distribution) {
        return distributionDao.insertDistribution(distribution);
    }

}
