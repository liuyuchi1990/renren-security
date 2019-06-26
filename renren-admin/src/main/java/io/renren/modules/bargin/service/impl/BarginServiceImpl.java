package io.renren.modules.bargin.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.bargin.dao.BarginDao;
import io.renren.modules.bargin.entity.BarginEntity;
import io.renren.modules.bargin.service.BarginService;


@Service("barginService")
public class BarginServiceImpl extends ServiceImpl<BarginDao, BarginEntity> implements BarginService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<BarginEntity> page = this.selectPage(
                new Query<BarginEntity>(params).getPage(),
                new EntityWrapper<BarginEntity>()
        );

        return new PageUtils(page);
    }

}
