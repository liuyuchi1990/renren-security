package io.renren.modules.distribution.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.*;

import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.distribution.entity.Distribution;
import io.renren.modules.distribution.service.DistributionService;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author richard
 * @email sunlightcs@gmail.com
 * @date 2018-10-26 14:49:43
 */
@RestController
@RequestMapping("/distribution")
public class DistributionController {
    @Autowired
    private DistributionService distributionService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OrderService orderService;

    @Value("${root.img.url}")
    String filePath;
    @Value("${root.img.path}")
    String url;
    @Value("${qr.distribution}")
    String qrDistributionUrl;
    @Value("${qr.distributionImgPath}")
    String qrDistributionImgUrl;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("sys:distribution:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = distributionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        Distribution distribution = distributionService.queryById(id);
        List<SysUserEntity> users = new ArrayList<>();
        if ((!"".equals(distribution.getWatcher())) && (distribution.getWatcher() != null)) {
            String[] idList = distribution.getWatcher().split(",");
            users = sysUserService.queryForUsers(idList);
        }
        List<Map<String, Object>> orders = orderService.queryByActivtyId(id);
        return R.ok().put("distribution", distribution).put("user", users).put("order", orders);
    }

    /**
     * 信息
     */
    @RequestMapping("/info")
    public R infoAuth(@PathVariable("id") String id) {
        Distribution distribution = distributionService.queryById(id);
        return R.ok().put("distribution", distribution);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public R save(@RequestBody Distribution distribution) throws Exception {
        if("".equals(distribution.getId())){
            distribution.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            distributionService.insertDistribution(distribution);
            String text = qrDistributionUrl.replace("id=","id="+distribution.getId());
            QRCodeUtils.encode(text,null, qrDistributionImgUrl, distribution.getId(), true );
        }else{
            ValidatorUtils.validateEntity(distribution);
            distributionService.updateById(distribution);//全部更新
        }
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:save")
    @ResponseBody
    public R copy(@RequestBody Distribution distribution) throws Exception {
        Distribution ds = distributionService.queryById(distribution.getId());
        ds.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        distributionService.insertDistribution(ds);
        String text = qrDistributionUrl.replace("id=","id="+ds.getId());
        QRCodeUtils.encode(text,null, qrDistributionImgUrl, ds.getId(), true );
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:update")
    @ResponseBody
    public R update(@RequestBody Distribution distribution) {
        ValidatorUtils.validateEntity(distribution);
        distributionService.updateById(distribution);//全部更新
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("sys:distribution:delete")
    public R delete(@RequestBody String[] ids) {
        distributionService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }


    @RequestMapping("/send")
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult send() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, DocumentException, IOException {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        Map<String, String> mp = MD5.sendRedPack();
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
    }


    @RequestMapping(value = "/addWatcher", method = RequestMethod.POST)
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult addWatcher(@RequestBody Distribution distribution) {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        int mp = distributionService.addWatcher(distribution);
        map.put("status", "success");
        map.put("msg", "send ok");
        map.put("data", mp);
        result.setResult(map);
        return result;
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
