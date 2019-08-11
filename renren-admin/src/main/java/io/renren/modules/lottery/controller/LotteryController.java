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
    /**
     * 列表
     */
    @ApiIgnore
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lotteryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        LotteryEntity lottery = lotteryService.selectById(id);

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
            String text = qrLotteryUrl.replace("id=", "id=" + lottery.getId());
            QRCodeUtils.encode(text, null, qrLotteryImgUrl, lottery.getId(), true);
        }else{
            lotteryService.updateLottery(lottery);//全部更新
        }
        return R.ok().put("lottery", lottery);
    }

    @RequestMapping(value = "/lottery", method = RequestMethod.POST)
    @Transactional
    public ReturnResult lottery(@RequestBody Order order) throws InvocationTargetException {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        LotteryEntity lottery = lotteryService.queryLotteryLogByUserId(order);
        Map<String, Object> map = new HashedMap();
        if(lottery.getRollNum() < lottery.getMaxTime()||(lottery.getFriend()>0&&lottery.getRollNum()<(lottery.getMaxTime()+lottery.getInterval())) ){
            List<Map> disList = JSONArray.parseObject(lottery.getPrizeRule(),List.class);
            //LotteryUtil.lottery()
            List<Gift> giftList = LotteryUtil.convertMapListToBeanList(disList,Gift.class);
            Gift gift = LotteryUtil.lottery(giftList);
            //lotteryService.in
            map.put("data",gift);
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
    public R delete(@RequestBody String[] ids){
        lotteryService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
