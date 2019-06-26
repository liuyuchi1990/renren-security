package io.renren.modules.bargin.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.bargin.entity.BarginEntity;
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
    @RequestMapping("/save")
    public R save(@RequestBody BarginEntity bargin){
        barginService.insert(bargin);

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
