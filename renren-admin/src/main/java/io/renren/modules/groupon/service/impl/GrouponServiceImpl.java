package io.renren.modules.groupon.service.impl;

import io.renren.modules.bargin.dao.BarginDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.groupon.dao.GrouponDao;
import io.renren.modules.groupon.entity.GrouponEntity;
import io.renren.modules.groupon.service.GrouponService;


@Service("grouponService")
public class GrouponServiceImpl extends ServiceImpl<GrouponDao, GrouponEntity> implements GrouponService {
    @Autowired
    private GrouponDao grouponDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<GrouponEntity> page = this.selectPage(
                new Query<GrouponEntity>(params).getPage(),
                new EntityWrapper<GrouponEntity>()
        );

        return new PageUtils(page);
    }
    public List<Map<String, Object>> queryList (String id){
        return grouponDao.queryList(id);
    };
}
