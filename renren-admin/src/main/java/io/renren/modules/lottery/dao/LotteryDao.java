package io.renren.modules.lottery.dao;

import io.renren.modules.lottery.entity.LotteryEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import io.renren.modules.order.model.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-25 17:32:00
 */
public interface LotteryDao extends BaseMapper<LotteryEntity> {
	int insertLottery (LotteryEntity lottery);

	int insertFriend(Order order);

	int insertLotteryLog(Order order);

	LotteryEntity queryById(Order order);

	List<Map<String,Object>> queryLotteryByMobile(Order order);

	List<Map<String,Object>> queryLotteryLogById(Order order);

	List<Map<String,Object>> queryList();

	int updateLottery (LotteryEntity lottery);

	LotteryEntity queryLotteryLogByUserId(Order order);
}
