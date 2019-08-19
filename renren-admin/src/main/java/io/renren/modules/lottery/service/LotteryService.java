package io.renren.modules.lottery.service;

import com.baomidou.mybatisplus.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.lottery.entity.LotteryEntity;
import io.renren.modules.order.model.Order;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-25 17:32:00
 */
public interface LotteryService extends IService<LotteryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    int insertLottery (LotteryEntity lottery);

    int insertLotteryLog (Order order);

    List<Map<String,Object>> queryLotteryByMobile(Order order);

    List<Map<String,Object>> queryLotteryLogById(Order order);

    List<Map<String,Object>> queryList();

    int updateLottery (LotteryEntity lottery);

    int insertFriend(Order order);

    LotteryEntity queryById(Order order);

    LotteryEntity queryLotteryLogByUserId(Order order);
}

