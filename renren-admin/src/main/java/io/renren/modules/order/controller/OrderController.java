package io.renren.modules.order.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import io.renren.common.config.Constants;

import io.renren.common.utils.CommonUtil;
import io.renren.common.utils.Excelutil;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Executors;

@Api(value = "Order")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Value("${export.path}")
    String exportPath;

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
                                    if ("1".equals(order.getOrderStatus())||"5".equals(order.getOrderStatus())) {//订单仍未支付或支付失败，释放货物
                                        distributionService.rollback(order.getActivityId());
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
            if (Constants.GATHER.equals(order.getOrderType())) {
                res = gatherService.queryGatherByMobileAndActivityId(order);
            } else if (Constants.Lottery.equals(order.getOrderType())) {
                res = lotteryService.queryLotteryByMobile(order);
            } else {
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

    /**
     * 导出报表
     *
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult export(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) Order order) throws Exception {
        //获取数据
        Map<String, Object> map = new HashMap<>();
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        List<Map<String, Object>> res = new ArrayList<>();
        String[] title = {"订单号", "活动名", "姓名", "金额", "电话","时间","订单状态"};
        if(Constants.GATHER.equals(order.getOrderType())){
            title[3]="赞数";
            res = gatherService.queryLike(order.getActivityId());
        }else {
            res = orderService.queryByActivtyId(order.getActivityId());
        }
        if (res.size()!=0) {
            //excel标题
            String fileName = res.get(0).get("activity_name").toString() + System.currentTimeMillis() + ".xlsx";
            File file = new File(exportPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            File excel = new File(exportPath + fileName);
            String sheetName = res.get(0).get("activity_name").toString();
            String[][] content = new String[res.size()][];
            for (int i = 0; i < res.size(); i++) {
                content[i] = new String[title.length];
                Map<String, Object> obj = res.get(i);
                content[i][0] = Constants.GATHER.equals(order.getOrderType())?obj.get("id").toString():obj.get("order_id").toString();
                content[i][1] = obj.get("activity_name").toString();
                content[i][2] = obj.get("username").toString();
                content[i][3] = Constants.GATHER.equals(order.getOrderType())?obj.get("likeNum").toString():obj.get("total_price").toString();
                content[i][4] = obj.get("mobile").toString();
                content[i][5] = obj.get("create_time").toString();
                content[i][6] = Constants.GATHER.equals(order.getOrderType())?obj.get("update_time").toString():obj.get("value").toString();
            }

            //创建HSSFWorkbook
            XSSFWorkbook wb = Excelutil.getWorkbook(sheetName, title, content);

            try (FileOutputStream fos = new FileOutputStream(excel)) {
                wb.write(fos);
            } catch (Exception e) {

            }
            map.put("fileName", fileName);
            result.setResult(map);
        } else {
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            map.put("status", "失败");
            result.setResult(map);
        }
        return  result;
    }

    /**
     * @param @param   request
     * @param @param   response
     * @param @return  设定文件
     * @param response
     * @return
     * @throws @param                       request
     * @throws UnsupportedEncodingException
     * @Title: downloadFailDetail
     * @Description:
     */
    @ApiIgnore
    @RequestMapping(value = "/deleteExcel", method = RequestMethod.GET)
    @ResponseBody
    public ReturnResult deleteExcel(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(Constants.FILE_NAME) String fileName) throws IOException {
        // 入参校验
        Preconditions.checkArgument(fileName.length() > 0, "%s 不能为空！", Constants.FILE_NAME);
        Map<String, Object> map = new HashMap<>();
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        String fileNameDecode = URLDecoder.decode(fileName, Constants.EN_CODING);
        File file = new File(exportPath, fileNameDecode);
        if (file.exists()) {
            //删除服务器文件
            if (file.exists() && file.isFile() && Files.deleteIfExists(file.toPath())) {
                map.put("data", "导出成功后删除文件");
                result.setResult(map);
            }else {
                result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
                result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
                map.put("data", "删除文件失败");
            }
        }
        return result;
    }
}
