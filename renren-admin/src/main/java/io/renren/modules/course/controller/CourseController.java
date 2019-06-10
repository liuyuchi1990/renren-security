package io.renren.modules.course.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.course.entity.CourseEntity;
import io.renren.modules.course.service.CourseService;
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
 * @date 2019-05-16 16:44:37
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("sys:course:info")
    public R info(@PathVariable("id") String id){
        CourseEntity course = courseService.selectById(id);
        return R.ok().put("course", course);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public R save(@RequestBody CourseEntity course){
        if ("".equals(course.getId())||course.getId()==null) {
            course.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            courseService.insertAllColumn(course);
        }else{
            ValidatorUtils.validateEntity(course);
            courseService.updateAllColumnById(course);//全部更新
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CourseEntity course){
        ValidatorUtils.validateEntity(course);
        courseService.updateAllColumnById(course);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody String[] ids){
        courseService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

}
