package io.renren.modules.gather.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.gather.service.GatherService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2019-06-11 10:39:02
 */
@RestController
@RequestMapping("/gather")
public class GatherController {
    @Autowired
    private GatherService gatherService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = gatherService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        GatherEntity gather = gatherService.selectById(id);
        return R.ok().put("gather", gather);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody GatherEntity gather){
        if ("".equals(gather.getId())||gather.getId()==null) {
            gather.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            gatherService.insertAllColumn(gather);
        }else{
            gatherService.updateAllColumnById(gather);//全部更新
        }
        return R.ok().put("gather", gather);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GatherEntity gather){
        ValidatorUtils.validateEntity(gather);
        gatherService.updateAllColumnById(gather);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids){
        gatherService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
