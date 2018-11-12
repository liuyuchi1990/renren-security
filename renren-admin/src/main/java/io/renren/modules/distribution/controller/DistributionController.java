package io.renren.modules.distribution.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.renren.common.utils.UploadUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;
import org.springframework.web.multipart.MultipartFile;


/**
 *
 *
 * @author richard
 * @email sunlightcs@gmail.com
 * @date 2018-10-26 14:49:43
 */
@RestController
@RequestMapping("/distribution")
public class DistributionController {
    @Autowired
    private DistributionService distributionService;

    @Value("${root.img.url}")
    String filePath;
    @Value("${root.img.url}")
    String url;
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("sys:distribution:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = distributionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id){
        Distribution distribution = distributionService.queryById(id);
        return R.ok().put("distribution", distribution);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("sys:distribution:save")
    public R save(@RequestBody Distribution distribution){
        distribution.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        distributionService.insertDistribution(distribution);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("sys:distribution:update")
    public R update(@RequestBody Distribution distribution){
        ValidatorUtils.validateEntity(distribution);
        distributionService.updateById(distribution);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("sys:distribution:delete")
    public R delete(@RequestBody String[] ids){
        distributionService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult add(@RequestParam("upfile") MultipartFile[] files) {
        String[] imgs = new String[files.length];
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());

        Map<String, Object> map = new HashedMap();
        try {
            //判断file数组不能为空并且长度大于0
            if (files != null && files.length > 0) {
                //循环获取file数组中得文件
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    //保存文件
                    String fileName = UploadUtils.saveFile(file, filePath, UUID.randomUUID().toString());
                    imgs[i] = url + fileName;
                }
            }
            map.put("status", "success");
            map.put("msg", "上传成功");
            map.put("data", imgs[0]);
            result.setResult(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "fail");
            map.put("msg", "上传失败");
            result.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            result.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            result.setResult(map);
            return result;
        }
    }

}
