package io.renren.modules.order.dao;

import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by richard on 2018/5/14.
 */
public interface OrderDao {
    int insert(Order order);

    int edit(OrderInfo order);

    int delete(String[] ids);

    Order queryById(@Param("order_id") String order_id);

    Map<String, Object> queryByOrderId(String order_id);

    int updateUserIntegral(Order order);

    int minusUserIntegral(Order order);

    List<Map<String, Object>> getOrderByUserId(Order order);

    List<Map<String, Object>> getOrderByFromUserId(Order order);

    List<Map<String, Object>> queryByActivtyId(String activityId);

    List<Map<String, Object>> getOrderByUserIdAndActivityType(Order order);

    List<Map<String, Object>> queryByGroupId(String groupId);

    Map<String, Object> queryByDeviceId(String device_id);

    Map<String, Object> queryForLane(String device_id);

    List<Order> queryPage(@Param("user_name") String user_name, @Param("begindate") Date begindate, @Param("enddate") Date enddate, @Param("order_status") String order_status);
}
