package io.renren.modules.order.service;


import io.renren.modules.order.dao.OrderDao;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by richard on 2018/5/14.
 */
@Service
public class OrderService {
    @Autowired
    OrderDao orderDao;

    public int insert(Order order) {
        return orderDao.insert(order);
    }

    public int edit(OrderInfo order) {
        return orderDao.edit(order);
    }

    public int updateUserIntegral(Order order) {
        return orderDao.updateUserIntegral(order);
    }

    public int minusUserIntegral(Order order) {
        return orderDao.minusUserIntegral(order);
    }

    public int delete(String ids) {
        return orderDao.delete(ids.split(","));
    }

    public Order queryById(String order_id) {
        return orderDao.queryById(order_id);
    }

    public Map<String, Object> queryByOrderId(String order_id) {
        return orderDao.queryByOrderId(order_id);
    }

    public List<Map<String, Object>> queryByActivtyId(String activityId) {
        return orderDao.queryByActivtyId(activityId);
    }

    public List<Map<String, Object>> queryByGroupId(String groupId) {
        return orderDao.queryByGroupId(groupId);
    }

    public List<Map<String, Object>> getOrderByUserId(Order order) {
        return orderDao.getOrderByUserId(order);
    }

    public List<Map<String, Object>> getOrderByFromUserId(Order order) {
        return orderDao.getOrderByFromUserId(order);
    }

    public List<Map<String, Object>> getOrderByUserIdAndActivityType(Order order) {
        return orderDao.getOrderByUserIdAndActivityType(order);
    }

    public Map<String, Object> queryByDeviceId(String device_id) {
        return orderDao.queryByDeviceId(device_id);
    }

//    public PageInfo queryPage(String user_name, Date begindate, Date enddate, String order_status, int pageindex, int pagenum) {
//        PageHelper.startPage(pageindex, pagenum);
//        List<Order> list = orderMapper.queryPage(user_name, begindate, enddate, order_status);
//        PageInfo page = new PageInfo(list);
//        return page;
//    }

//    public Map<String, Object> queryForLane(Order order) {
//        Map<String, Object> resMap = new HashMap<>();
//        List<String> laneLst = Arrays.asList(order.getCargo_lane().split(","));
//        Map<String, Object> map = orderMapper.queryForLane(order.getDevice_id());
//        for (String lane : laneLst) {
//            if (Integer.parseInt(map.get("cargo_lane_" + lane).toString()) > 0) {
//                //resMap.put(lane, "true");
//            } else {
//                resMap.put(lane, "false");
//            }
//        }
//        return resMap;
//    }
}
