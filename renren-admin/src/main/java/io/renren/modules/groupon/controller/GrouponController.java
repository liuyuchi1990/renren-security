package io.renren.modules.groupon.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import io.renren.common.config.Constants;
import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.bargin.entity.BarginEntity;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.groupon.entity.GrouponEntity;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.groupon.entity.GrouponEntity;
import io.renren.modules.groupon.service.GrouponService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-27 17:17:35
 */
@RestController
@RequestMapping("/groupon")
public class GrouponController {
    @Autowired
    private GrouponService grouponService;

    @Autowired
    DistributionService distributionService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    private OrderService orderService;
    @Value("${qr.groupon}")
    String qrGrouponUrl;
    @Value("${qr.grouponImgPath}")
    String qrGrouponImgUrl;
    @Value("${qr.httpgrouponurl}")
    String httpgrouponurl;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("groupon:groupon:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = grouponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        GrouponEntity groupon = grouponService.selectById(id);
        List<Map<String, Object>> orders = orderService.queryByActivtyId(id);
        return R.ok().put("groupon", groupon).put("order", orders);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @Transactional
    public R save(@RequestBody GrouponEntity groupon) throws Exception {
        if ("".equals(groupon.getId())||groupon.getId()==null) {
            groupon.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            groupon.setQrImg(httpgrouponurl + groupon.getId() + ".jpg");
            groupon.setCreateTime(new Date());
            grouponService.insertAllColumn(groupon);
            distributionService.insertActivity(groupon);
            String text = qrGrouponUrl.replace("id=", "id=" + groupon.getId());
            QRCodeUtils.encode(text, null, qrGrouponImgUrl, groupon.getId(), true);
        }else{
            groupon.setUpdateTime(new Date());
            grouponService.updateById(groupon);//全部更新
            distributionService.updateActivity(groupon);

        }
        return R.ok().put("groupon", groupon);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @Transactional
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public R copy(@RequestBody GrouponEntity groupon) throws Exception {
        GrouponEntity ga = grouponService.selectById(groupon.getId());
        ga.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        ga.setQrImg(httpgrouponurl + ga.getId() + ".jpg");
        ga.setCreateTime(new Date());
        grouponService.insertAllColumn(ga);
        distributionService.insertActivity(ga);
        String text = qrGrouponUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrGrouponImgUrl, ga.getId(), true);
        return R.ok();
    }


    @RequestMapping(value = "/groupon", method = RequestMethod.POST)
    @Transactional
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult groupon(@RequestBody Order order) throws ParseException {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> mp = new HashedMap();
        SysUserEntity user = new SysUserEntity();
        GrouponEntity ge = grouponService.selectById(order.getActivityId());
        List<Map<String,Object>> groupList = orderService.queryByGroupId(order.getGroupId());
        List<Map<String,String>> disList = JSONArray.parseObject(ge.getDiscount(),List.class);
        OrderInfo orderInfo = new OrderInfo();
        for(Map<String,String> map:disList){
            if((groupList.size()+ 1) >=Integer.parseInt(map.get("num").toString()) ){
                orderInfo.setTotal_price(map.get("price").toString());//modify price
            }
        }
        orderInfo.setGroup_id(order.getGroupId());
        orderService.edit(orderInfo);
        orderInfo.setUser_id(order.getUser_id());
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setTotal_price(orderInfo.getTotal_price());
        order.setOrderType(Constants.GROUPON);
        order.setOrderStatus("1");
        orderService.insert(order);

        user.setUserId(order.getUser_id());
        user.setMobile(order.getMobile());
        user.setUsername(order.getUser_name());
        sysUserService.updateUser(user);
        mp.put("data",order);
        result.setResult(mp);
        return result;
    }

    @RequestMapping(value = "/queryGrouponId", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryGrouponId(@RequestBody Order order) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        List<Map<String, Object>> mp = orderService.queryByGroupId(order.getGroupId());
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
    @RequiresPermissions("groupon:groupon:update")
    public R update(@RequestBody GrouponEntity groupon){
        ValidatorUtils.validateEntity(groupon);
        grouponService.updateAllColumnById(groupon);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Transactional
    //@RequiresPermissions("groupon:groupon:delete")
    public R delete(@RequestBody String[] ids){
        grouponService.deleteBatchIds(Arrays.asList(ids));
        distributionService.deleteActivity(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/queryAll")
    //@RequiresPermissions("sys:distribution:list")
    public ReturnResult queryAll(@RequestBody GrouponEntity params) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> mp = new HashedMap();
        List<Map<String, Object>> activityLst = grouponService.queryList(params.getId());
        mp.put("data",activityLst);
        result.setResult(mp);
        return result;
    }

}
