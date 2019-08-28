package io.renren.modules.bargin.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.bargin.entity.BarginEntity;

import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.gather.entity.PrizeEntity;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.renren.modules.bargin.service.BarginService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;


/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-25 15:35:24
 */
@RestController
@RequestMapping("/bargin")
public class BarginController {
    @Autowired
    private BarginService barginService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DistributionService distributionService;

    @Value("${qr.bargin}")
    String qrBarginUrl;
    @Value("${qr.barginImgPath}")
    String qrBarginImgUrl;
    @Value("${qr.httpbarginurl}")
    String httpbarginurl;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = barginService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/queryAll")
    //@RequiresPermissions("sys:distribution:list")
    public ReturnResult queryAll(@RequestBody BarginEntity params) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        List<Map<String, Object>> activityLst = barginService.queryList(params.getId());
        Map<String, Object> map = new HashedMap();
        map.put("data", activityLst);
        result.setResult(map);
        return result;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        BarginEntity bargin = barginService.selectById(id);
        List<Map<String, Object>> orders = orderService.queryByActivtyId(id);
        return R.ok().put("bargin", bargin).put("order", orders);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Transactional
    public R save(@RequestBody BarginEntity bargin) throws Exception {
        if ("".equals(bargin.getId()) || bargin.getId() == null) {
            bargin.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            bargin.setQrImg(httpbarginurl + bargin.getId() + ".jpg");
            bargin.setPrizeLeft(bargin.getPrizeNum());
            bargin.setCreateTime(new Date());
            barginService.insertAllColumn(bargin);
            distributionService.insertActivity(bargin);
            String text = qrBarginUrl.replace("id=", "id=" + bargin.getId());
            QRCodeUtils.encode(text, null, qrBarginImgUrl, bargin.getId(), true);
        } else {
            bargin.setUpdateTime(new Date());
            barginService.updateById(bargin);//全部更新
            distributionService.updateActivity(bargin);
        }
        return R.ok().put("bargin", bargin);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public R copy(@RequestBody BarginEntity bargin) throws Exception {
        BarginEntity ga = barginService.selectById(bargin.getId());
        ga.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        ga.setQrImg(httpbarginurl + ga.getId() + ".jpg");
        ga.setCreateTime(new Date());
        barginService.insertAllColumn(ga);
        distributionService.insertActivity(ga);
        String text = qrBarginUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrBarginImgUrl, ga.getId(), true);
        return R.ok();
    }

    @RequestMapping(value = "/bargin", method = RequestMethod.POST)
    @Transactional
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult bargin(@RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DecimalFormat dfn = new DecimalFormat("0.00");
        LocalDateTime toDate = LocalDateTime.now();
        BarginEntity ba = barginService.selectById(order.getActivityId());
        List<Map<String, Object>> mp = barginService.queryBarginLog(order.getOrderId());
        if (mp == null || (mp.size() < ba.getBarginNum())) {//是否超过砍价人数
            Map<String, Object> resMap = barginService.queryMaxTime(order);
            long hours = 0;
            Long restrictTime = Long.parseLong(ba.getRestrictTime().toString());
            LocalDateTime create_time = resMap == null ? toDate : LocalDateTime.parse(resMap.get("create_time").toString().replace(".0", ""), df);
            hours = ChronoUnit.HOURS.between(create_time, toDate);
            if (restrictTime < hours || mp.size() == 0||resMap==null) {//是否超过投票间隔时间
                Double reduct = Math.random() * (ba.getMaxReduction().subtract(ba.getMinReduction()).doubleValue()) + ba.getMinReduction().doubleValue();
                Double price_left = Double.valueOf(order.getTotal_price()) - reduct;
                if (Double.valueOf(order.getTotal_price()) == (ba.getFloorPrice().doubleValue())) {
                    result.setCode(ReturnCodeEnum.INVOKE_VENDOR_DF_ERROR.getCode());
                    result.setMsg("您已砍至低价！");
                    result.setResult(map);
                } else {
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setOrder_id(order.getOrderId());
                    if (price_left <= ba.getFloorPrice().doubleValue()) {
                        orderInfo.setTotal_price(dfn.format(ba.getFloorPrice().doubleValue()));
                    } else {
                        orderInfo.setTotal_price(dfn.format(price_left));
                    }
                    orderService.edit(orderInfo);//modify price
                    orderInfo.setUser_id(order.getUser_id());
                    order.setTotal_price(dfn.format(reduct));
                    barginService.insertBarginLog(order);
                    map.put("data", order);
                    result.setResult(map);
                }
            } else {
                result.setCode(ReturnCodeEnum.INVOKE_VENDOR_DF_ERROR.getCode());
                result.setMsg("请您休息" + (restrictTime - hours) + "小时后再次砍价");
                result.setResult(map);
            }
        } else {
            result.setCode(ReturnCodeEnum.INVOKE_VENDOR_DF_ERROR.getCode());
            result.setMsg("超出砍价人数");
            result.setResult(map);
        }

        return result;
    }

    @RequestMapping(value = "/queryBarginLog", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryBarginLog(@RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        List<Map<String, Object>> mp = barginService.queryBarginLog(order.getOrderId());
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BarginEntity bargin) {
        ValidatorUtils.validateEntity(bargin);
        barginService.updateAllColumnById(bargin);//全部更新

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids) {
        barginService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
