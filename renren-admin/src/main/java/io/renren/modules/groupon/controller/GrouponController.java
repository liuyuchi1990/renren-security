package io.renren.modules.groupon.controller;

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
@RequestMapping("groupon/groupon")
public class GrouponController {
    @Autowired
    private GrouponService grouponService;

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

        return R.ok().put("groupon", groupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("groupon:groupon:save")
    public R save(@RequestBody GrouponEntity groupon){
        grouponService.insert(groupon);

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
