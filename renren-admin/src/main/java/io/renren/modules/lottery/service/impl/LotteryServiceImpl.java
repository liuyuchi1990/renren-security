package io.renren.modules.lottery.service.impl;

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

import io.renren.modules.lottery.dao.LotteryDao;
import io.renren.modules.lottery.entity.LotteryEntity;
import io.renren.modules.lottery.service.LotteryService;


@Service("lotteryService")
public class LotteryServiceImpl extends ServiceImpl<LotteryDao, LotteryEntity> implements LotteryService {
    @Autowired
    private LotteryDao lotteryDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<LotteryEntity> page = this.selectPage(
                new Query<LotteryEntity>(params).getPage(),
                new EntityWrapper<LotteryEntity>()
        );

        return new PageUtils(page);
    }
    public int insertLottery (LotteryEntity lottery){
        return lotteryDao.insertLottery(lottery);
    }

    public int insertLotteryLog (Order order){
        return lotteryDao.insertLotteryLog(order);
    }

    public int updateLottery (LotteryEntity lottery){
        return lotteryDao.updateLottery(lottery);
    }

    public List<Map<String,Object>> queryLotteryByMobile(Order order){
        return lotteryDao.queryLotteryByMobile(order);
    }

    public List<Map<String,Object>> queryLotteryLogById(Order order){
        return lotteryDao.queryLotteryLogById(order);
    }

    public List<Map<String,Object>> queryList(){
        return lotteryDao.queryList();
    }

    public int insertFriend(Order order){
        return lotteryDao.insertFriend(order);
    }

    public LotteryEntity queryById(Order order){
        return lotteryDao.queryById(order);
    }

    public LotteryEntity  queryLotteryLogByUserId(Order order){
        return lotteryDao.queryLotteryLogByUserId(order);
    }
}
