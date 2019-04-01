package io.renren.modules.order.controller;

import com.alibaba.fastjson.JSON;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import io.renren.modules.order.model.OrderMessage;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
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

//    @Autowired
//    BreadcrumbUtil breadcrumbUtil;
    @Autowired
    OrderService orderService;

    @Autowired
    DistributionService distributionService;
//    @Autowired
//    SysUserService sysUserService;
//    @Autowired
//    DeviceService deviceService;
//    @Autowired
//    PageUtil pageUtil;
//
//    @Autowired
//    ThreadPoolManager tpm;
//
//    @ApiIgnore
//    @RequestMapping("/getListPage")
//    @RequiresPermissions(value = {"order:query"})
//    public String list() {
//        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/getListPage");
//        return JSON.toJSONString(Breadcrumbs);
//    }
//
//    @ApiIgnore
//    @RequestMapping("/addPage")
//    @RequiresPermissions(value = {"order:query"})
//    public String addPage() {
//        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/addPage");
//        return JSON.toJSONString(Breadcrumbs);
//    }
//
//    @ApiIgnore
//    @RequestMapping("/editPage")
//    @RequiresPermissions(value = {"order:query"})
//    public String editPage() {
//        List<Breadcrumb> Breadcrumbs = breadcrumbUtil.getBreadcrumbPath("order/editPage");
//        return JSON.toJSONString(Breadcrumbs);
//    }
//
//
//    @ApiIgnore
//    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
//    @RequiresPermissions(value = {"order:query"})
//    public String queryPage(
//            @RequestParam(required = false) String user_name,
//            @RequestParam(required = false) String begindate,
//            @RequestParam(required = false) String enddate,
//            @RequestParam(required = false) String order_status,
//
//            @RequestParam int pageNumber, @RequestParam int pageSize) {
//        TablePage tp = pageUtil.getDataForPaging(orderService.queryPage(user_name, DateUtil.StrtoDate(begindate, "yyyy-MM-dd"), DateUtil.StrtoDate(enddate, "yyyy-MM-dd"), order_status, pageNumber, pageSize));
//        return JSON.toJSONString(tp);
//    }
//
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
        Distribution ds = distributionService.queryById(order.getActivityId());
        //判断是否活动还有名额
        if (ds.getTargetQuantity()!=0) {
            Map<String, Object> map = new HashMap<>();
            order.setOrderStatus("1");
            order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
            //order.setTotal_price(order.getCargo_lane().split(",").length * Constants.PRICE);
            int rs = orderService.insert(order);
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
//
//    @RequestMapping(value = "/delete", method = RequestMethod.GET)
//    @ResponseBody
//    @Transactional(rollbackFor = Exception.class)
//    public ReturnResult delete(@RequestParam String ids) {
//        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
//        Map<String, Object> map = new HashMap<>();
//        Order order = orderService.queryById(ids);
//        try {
//            orderService.delete(ids.replaceAll("\"", ""));
//            deviceService.rollback(order);
//            map.put("status", "成功");
//            return result;
//        } catch (Exception e) {
//            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
//            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
//            map.put("status", "失败");
//            return result;
//        }
//    }
//
//    @RequestMapping(value = "/getAllOrder", method = RequestMethod.GET)
//    @Transactional(isolation = Isolation.READ_COMMITTED)
//    public ReturnResult getAllOrder(@RequestParam(required = true) String device_id,
//                                    @RequestParam(required = false) String networkStatus,
//                                    @RequestParam(required = false) String temperature) {
//        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
//        Map<String, Object> map = new HashMap<>();
//        map = orderService.queryByDeviceId(device_id);
//        Date day = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//        if(map==null){
//            map = new HashMap<>();
//            map.put("order_id","");
//            map.put("cargo_lane","");
//        }
//        map.put("timeStamp", df.format(day));
//        result.setResult(map);
//        return result;
//    }
//
////    @ApiOperation(value = "获取掉货信息", response = OrderInfo.class)
////    @Transactional(rollbackFor = {Exception.class}, readOnly = false)
////    @RequestMapping(value = "/getOrderFeedBack", method = RequestMethod.POST)
////    @ResponseBody
////    public ReturnResult getOrderFeedBack(@ApiParam @RequestBody OrderInfo orderInfo) {
////        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
////        Map<String, Object> map = new HashMap<>();
////        Integer count = 0;
////        List<Cargo> CargoLst = Arrays.asList(orderInfo.getCargo_lane());
////        for (Cargo f : CargoLst) {
////            if (("true".equals(f.getStatus()))) {
////                count++;
////            }
////        }
////        if (count != CargoLst.size()) {
////            orderInfo.setOrder_status("2");
////        } else {
////            orderInfo.setOrder_status("4");
////        }
////        orderInfo.setIntegral(count * Constants.Integral);
////
////        try{
////            int rs = orderService.edit(orderInfo);
////            int rs2 = orderService.updateUserIntegral(orderInfo);
////            map.put("status", "成功");
////            result.setResult(map);
////        } catch (Exception e){
////            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
////            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
////            if (count != CargoLst.size()) {
////                map.put("status", "掉货失败");
////            } else {
////                map.put("status", "失败");
////            }
////            result.setResult(map);
////        }
////        return result;
////    }
//
//    @RequestMapping(value = "/getOrderPay", method = RequestMethod.POST)
//    @ResponseBody
//    public ReturnResult getOrderPay(@ApiParam @RequestBody OrderInfo orderInfo) {
//        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
//        Map<String, Object> map = new HashMap<>();
//        orderInfo.setOrder_status("3");
//        orderInfo.setPay_type("1");
//        Map user = sysUserService.queryById(orderInfo.getUser_id());
//        if (Integer.parseInt(user.get("integral").toString()) > orderInfo.getIntegral()) {
//            try {
//                int rs = orderService.edit(orderInfo);
//                int rs2 = orderService.minusUserIntegral(orderInfo);
//                map.put("status", "成功");
//                result.setResult(map);
//            } catch (Exception e) {
//                result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
//                result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
//                map.put("status", "失败");
//                result.setResult(map);
//            }
//
//        } else {
//            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
//            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
//            map.put("status", "积分不够");
//            result.setResult(map);
//        }
//        return result;
//    }
//
//    @RequestMapping(value = "/getOrderByUserId", method = RequestMethod.GET)
//    @ResponseBody
//    public ReturnResult getOrderByUserId(@RequestParam(required = true) String user_id) {
//        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
//        Map<String, Object> map = new HashMap<>();
//        try {
//            List<Map<String, Object>> res = orderService.getOrderByUserId(user_id);
//            map.put("data", res);
//            map.put("price",Constants.PRICE);
//            result.setResult(map);
//        } catch (Exception e) {
//            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
//            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
//            map.put("status", "失败");
//            result.setResult(map);
//        }
//        return result;
//    }
}
