package io.renren.modules.bargin.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.bargin.entity.BarginEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.renren.modules.bargin.service.BarginService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-25 15:35:24
 */
@RestController
@RequestMapping("bargin/bargin")
public class BarginController {
    @Autowired
    private BarginService barginService;

    @Value("${qr.bargin}")
    String qrGatherUrl;
    @Value("${qr.barginImgPath}")
    String qrGatherImgUrl;
    @Value("${qr.httpbarginurl}")
    String httpbarginurl;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = barginService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        BarginEntity bargin = barginService.selectById(id);

        return R.ok().put("bargin", bargin);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody BarginEntity bargin) throws Exception {
        if ("".equals(bargin.getId())||bargin.getId()==null) {
            bargin.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            bargin.setQrImg(httpbarginurl + bargin.getId() + ".jpg");
            bargin.setPrizeLeft(bargin.getPrizeNum());
            barginService.insertAllColumn(bargin);
            String text = qrGatherUrl.replace("id=", "id=" + bargin.getId());
            QRCodeUtils.encode(text, null, qrGatherImgUrl, bargin.getId(), true);
        }else{
            barginService.updateById(bargin);//全部更新
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
        ga.setQrImg(httpbarginurl + bargin.getId() + ".jpg");
        barginService.insertAllColumn(ga);
        String text = qrGatherUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrGatherImgUrl, ga.getId(), true);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BarginEntity bargin){
        ValidatorUtils.validateEntity(bargin);
        barginService.updateAllColumnById(bargin);//全部更新
        
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids){
        barginService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
