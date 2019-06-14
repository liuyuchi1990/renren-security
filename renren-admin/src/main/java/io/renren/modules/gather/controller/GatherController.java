package io.renren.modules.gather.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.gather.entity.GatherEntity;
import io.renren.modules.gather.entity.PrizeEntity;
import io.renren.modules.gather.service.GatherService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${qr.gather}")
    String qrGatherUrl;
    @Value("${qr.gatherImgPath}")
    String qrGatherImgUrl;
    @Value("${qr.httpgatherurl}")
    String httpgatherurl;
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
    public R save(@RequestBody GatherEntity gather) throws Exception {
        if ("".equals(gather.getId())||gather.getId()==null) {
            gather.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            gather.setQrImg(httpgatherurl + gather.getId() + ".jpg");
            gatherService.insertAllColumn(gather);
            String text = qrGatherUrl.replace("id=", "id=" + gather.getId());
            QRCodeUtils.encode(text, null, qrGatherImgUrl, gather.getId(), true);
        }else{
            gatherService.updateAllColumnById(gather);//全部更新
        }
        return R.ok().put("gather", gather);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public R copy(@RequestBody GatherEntity gather) throws Exception {
        GatherEntity ga = gatherService.selectById(gather.getId());
        ga.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        ga.setQrImg(httpgatherurl + gather.getId() + ".jpg");
        gatherService.insertAllColumn(ga);
        String text = qrGatherUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrGatherImgUrl, ga.getId(), true);
        return R.ok();
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

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult like(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        int mp = gatherService.updatePrizeLog(pz);
        int mp2 = gatherService.insertLikeLog(pz);
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/makeLike", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult makeLike(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        pz.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        int mp = gatherService.insertPrizeLog(pz);
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", pz);
        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/queryLike", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryLike(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        pz.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        List<Map<String, Object>> mp = gatherService.queryLike(pz.getActivityId());
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }

    @RequestMapping(value = "/queryPrizeLog", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult queryPrizeLog(@RequestBody PrizeEntity pz) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        pz.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        List<Map<String, Object>> mp = gatherService.queryPrizeLog(pz.getId());
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
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
