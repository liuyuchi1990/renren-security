package io.renren.modules.lottery.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import io.renren.common.utils.LotteryUtil;
import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.lottery.entity.Gift;
import io.renren.modules.order.model.Order;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import io.swagger.annotations.Api;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.lottery.entity.LotteryEntity;
import io.renren.modules.lottery.service.LotteryService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import springfox.documentation.annotations.ApiIgnore;


/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-07-25 17:32:00
 */
@RestController
@RequestMapping("/lottery")
public class LotteryController {
    @Autowired
    private LotteryService lotteryService;
    @Value("${qr.lottery}")
    String qrLotteryUrl;
    @Value("${qr.lotteryImgPath}")
    String qrLotteryImgUrl;
    @Value("${qr.httplotteryurl}")
    String httplotteryurl;

    @Autowired
    private DistributionService distributionService;
    /**
     * 列表
     */
    @ApiIgnore
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lotteryService.queryPage(params);

        return R.ok().put("page", page);
    }

    @RequestMapping("/queryAll")
    //@RequiresPermissions("sys:distribution:list")
    public ReturnResult queryAll(@RequestBody LotteryEntity params) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        List<Map<String, Object>> activityLst = lotteryService.queryList();
        Map<String, Object> map = new HashedMap();
        map.put("data", activityLst);
        result.setResult(map);
        return result;
    }


    @RequestMapping("/queryLotteryLogById")
    //@RequiresPermissions("sys:distribution:list")
    public ReturnResult queryLotteryLogById(@RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        List<Map<String, Object>> activityLst = lotteryService.queryLotteryLogById(order);
        Map<String, Object> map = new HashedMap();
        map.put("data", activityLst);
        result.setResult(map);
        return result;
    }

    /**
     * 信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public R info(@RequestBody Order order){
        LotteryEntity lottery = lotteryService.queryById(order);

        return R.ok().put("lottery", lottery);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LotteryEntity lottery) throws Exception {
        if ("".equals(lottery.getId())||lottery.getId()==null) {
            lottery.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            lottery.setQrImg(httplotteryurl + lottery.getId() + ".jpg");
            lotteryService.insertLottery(lottery);
            distributionService.insertActivity(lottery);
            String text = qrLotteryUrl.replace("id=", "id=" + lottery.getId());
            QRCodeUtils.encode(text, null, qrLotteryImgUrl, lottery.getId(), true);
        }else{
            lotteryService.updateLottery(lottery);//全部更新
            distributionService.updateActivity(lottery);
        }
        return R.ok().put("lottery", lottery);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public R copy(@RequestBody LotteryEntity bargin) throws Exception {
        LotteryEntity ga = lotteryService.selectById(bargin.getId());
        ga.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        ga.setQrImg(httplotteryurl + ga.getId() + ".jpg");
        lotteryService.insertAllColumn(ga);
        distributionService.insertActivity(ga);
        String text = qrLotteryUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrLotteryImgUrl, ga.getId(), true);
        return R.ok();
    }

    @RequestMapping(value = "/lottery", method = RequestMethod.POST)
    @Transactional
    public ReturnResult lottery(@RequestBody Order order) throws InvocationTargetException {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        LotteryEntity lottery = lotteryService.queryLotteryLogByUserId(order);
        Map<String, Object> map = new HashedMap();
        if(lottery.getRollNum() < lottery.getMaxTime()||(lottery.getFriend()>0&&lottery.getRollNum()<(lottery.getMaxTime()+lottery.getIntervals())) ){
            List<Map> disList = JSONArray.parseObject(lottery.getPrizeRule(),List.class);
            //LotteryUtil.lottery()
            List<Gift> giftList = LotteryUtil.convertGiftList(disList);
            Gift gift = LotteryUtil.lottery(giftList);
            order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
            order.setOrderType(gift.getGiftName());//插入中奖名称
            order.setOrderStatus(gift.getGitfId());//插入中奖id
            lotteryService.insertLotteryLog(order);
            //lotteryService.in
            map.put("data",gift);
            result.setResult(map);
        }else{
            map.put("data","您今日抽奖已经达到限制次数，请改日试试手气");
            result.setResult(map);
        }
        return result;
    }

    @RequestMapping(value = "/friend", method = RequestMethod.POST)
    @Transactional
    public ReturnResult friend(@RequestBody Order order){
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        lotteryService.insertFriend(order);
        map.put("data",order);
        result.setResult(map);
        return result;
    }

    /**
     * 修改
     */
    @ApiIgnore
    @RequestMapping("/update")
    public R update(@RequestBody LotteryEntity lottery){
        ValidatorUtils.validateEntity(lottery);
        lotteryService.updateAllColumnById(lottery);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @ApiIgnore
    @RequestMapping("/delete")
    @Transactional
    public R delete(@RequestBody String[] ids){
        lotteryService.deleteBatchIds(Arrays.asList(ids));
        distributionService.deleteActivity(Arrays.asList(ids));
        return R.ok();
    }

}
