package io.renren.modules.groupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.groupon.entity.GrouponEntity;
import io.renren.modules.order.service.OrderService;
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
    @RequiresPermissions("groupon:groupon:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = grouponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("groupon:groupon:info")
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
            grouponService.insertAllColumn(groupon);
            distributionService.insertActivity(groupon);
            String text = qrGrouponUrl.replace("id=", "id=" + groupon.getId());
            QRCodeUtils.encode(text, null, qrGrouponImgUrl, groupon.getId(), true);
        }else{
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
        ga.setQrImg(httpgrouponurl + groupon.getId() + ".jpg");
        grouponService.insertAllColumn(ga);
        distributionService.insertActivity(ga);
        String text = qrGrouponUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrGrouponImgUrl, ga.getId(), true);
        return R.ok();
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
    @RequiresPermissions("groupon:groupon:delete")
    public R delete(@RequestBody String[] ids){
        grouponService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
