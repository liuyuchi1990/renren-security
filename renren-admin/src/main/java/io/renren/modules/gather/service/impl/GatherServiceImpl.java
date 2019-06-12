package io.renren.modules.gather.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;
import io.renren.modules.gather.dao.GatherDao;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.gather.service.GatherService;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service("gatherService")
public class GatherServiceImpl extends ServiceImpl<GatherDao, GatherEntity> implements GatherService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<GatherEntity> page = this.selectPage(
                new Query<GatherEntity>(params).getPage(),
                new EntityWrapper<GatherEntity>()
        );

        return new PageUtils(page);
    }

}
