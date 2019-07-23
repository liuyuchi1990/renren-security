package io.renren.modules.bargin.service.impl;

import io.renren.modules.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    @Autowired
    private BarginDao barginDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<BarginEntity> page = this.selectPage(
                new Query<BarginEntity>(params).getPage(),
                new EntityWrapper<BarginEntity>()
        );

        return new PageUtils(page);
    }

    public int insertBarginLog(Order order) {
        return barginDao.insertBarginLog(order);
    }

    public List<Map<String, Object>> queryBarginLog(String id) {
        return barginDao.queryBarginLog(id);
    }

    public List<Map<String, Object>> queryList(String id) {
        return barginDao.queryList(id);
    }

    public Map<String, Object> queryMaxTime(Order order) {
        return barginDao.queryMaxTime(order);
    }
}
