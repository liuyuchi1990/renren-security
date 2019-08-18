package io.renren.modules.order.controller;

import com.alibaba.fastjson.JSON;
import io.renren.common.config.Constants;

import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.gather.service.GatherService;
import io.renren.modules.lottery.service.LotteryService;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import io.renren.modules.order.model.OrderMessage;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Api(value = "Order")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    DistributionService distributionService;

    @Autowired
    GatherService gatherService;

    @Autowired
    LotteryService lotteryService;
    /**
     * 订单生成
     *
     * @param order
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ReturnResult save(@ApiParam @RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        final DelayQueue<OrderMessage> delayQueue = new DelayQueue<OrderMessage>();
        long time = System.currentTimeMillis();
        SysUserEntity user = new SysUserEntity();
        Distribution ds = distributionService.queryById(order.getActivityId());
        //判断是否活动还有名额
        if (ds.getTargetQuantity() != 0) {
            Map<String, Object> map = new HashMap<>();
            order.setOrderStatus("1");
            order.setOrderType(Constants.DISTRIBUTION);
            order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
            //order.setTotal_price(order.getCargo_lane().split(",").length * Constants.PRICE);
            user.setUserId(order.getUser_id());
            user.setMobile(order.getMobile());
            user.setUsername(order.getUser_name());
            int rs = orderService.insert(order);
            sysUserService.updateUser(user);
            int rs2 = distributionService.release(order.getActivityId());
            if (rs > 0 && rs2 > 0) {
                map.put("status", "成功");
                map.put("orderId", order.getOrderId());
                result.setResult(map);
                delayQueue.add(new OrderMessage(order.getOrderId(), time, "60秒后执行"));
                /**启动一个线程，处理延迟订单消息**/
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        OrderMessage message = null;
                        while (!delayQueue.isEmpty()) {
                            try {
                                message = delayQueue.take();
                                if (message != null) {
                                    Order order = orderService.queryById(message.getOrderId());
                                    OrderInfo orderInfo = new OrderInfo();
                                    orderInfo.setOrder_id(order.getOrderId());
                                    if ("1".equals(order.getOrderStatus())) {//订单仍未支付，释放货物
                                        distributionService.rollback(order.getOrderId());
                                        orderInfo.setOrder_status("5");
                                        orderService.edit(orderInfo);
                                    } else {

                                    }
                                }
                                System.out.println(new Date() + "  处理延迟消息:  " + JSON.toJSONString(message));
                            } catch (Exception e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                });
            } else {
                result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
                result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
                map.put("status", "失败");
                result.setResult(map);
            }
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            Map<String, Object> map = new HashMap<>();
            map.put("description", "活动无名额");
            result.setResult(map);
        }

        return result;
    }

    /**
     * 订单生成
     *
     * @param order
     * @return
     */
    @RequestMapping(value = "/saveOrder", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public ReturnResult saveOrder(@ApiParam @RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        SysUserEntity user = new SysUserEntity();
        Map<String, Object> map = new HashMap<>();
        order.setOrderStatus("1");
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        if (Constants.GROUPON.equals(order.getOrderType()) && ((order.getGroupId() == null) || ("".equals(order.getGroupId())))) {
            order.setGroupId(UUID.randomUUID().toString().replaceAll("-", ""));
        }
        user.setUserId(order.getUser_id());
        user.setMobile(order.getMobile());
        user.setUsername(order.getUser_name());
        int rs = orderService.insert(order);
        sysUserService.updateUser(user);
        if (rs > 0) {
            map.put("status", "成功");
            map.put("order", order);
            result.setResult(map);
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderByFromUserId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderByFromUserId(@ApiParam @RequestBody(required = false) Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> res = orderService.getOrderByFromUserId(order);
            map.put("data", res);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderByUserIdAndActivityId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderByUserIdAndActivityId(@RequestBody(required = false) Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> res = orderService.getOrderByUserId(order);
            map.put("data", res);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderByActivityId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderByActivityId(@RequestBody(required = false) Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> res = orderService.queryByActivtyId(order.getActivityId());
            map.put("data", res);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderByGroupId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderByGroupId(@RequestBody(required = false) Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try {
            List<Map<String, Object>> res = orderService.queryByGroupId(order.getGroupId());
            map.put("data", res);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderByOrderId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderByOrderId(@RequestBody(required = false) Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        try {
            Map<String, Object> od = orderService.queryByOrderId(order.getOrderId());
            map.put("data", od);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/getOrderByUserIdAndActivityType", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult getOrderByUserIdAndActivityType(@RequestBody(required = false) Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> res = new ArrayList<>();
        try {
            if(Constants.GATHER.equals(order.getOrderType())){
                 res = gatherService.queryGatherByMobileAndActivityId(order);
            }else if(Constants.Lottery.equals(order.getOrderType())){
                 res = lotteryService.queryLotteryByMobile(order);
            }else{
                res = orderService.getOrderByUserIdAndActivityType(order);
            }
            map.put("data", res);
            result.setResult(map);
        } catch (Exception e) {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return result;
    }
}
