package io.renren.modules.bargin.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import io.renren.common.utils.QRCodeUtils;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.bargin.entity.BarginEntity;

import io.renren.modules.gather.entity.PrizeEntity;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderInfo;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/bargin")
public class BarginController {
    @Autowired
    private BarginService barginService;

    @Autowired
    private OrderService orderService;

    @Value("${qr.bargin}")
    String qrBarginUrl;
    @Value("${qr.barginImgPath}")
    String qrBarginImgUrl;
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
            String text = qrBarginUrl.replace("id=", "id=" + bargin.getId());
            QRCodeUtils.encode(text, null, qrBarginImgUrl, bargin.getId(), true);
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
        String text = qrBarginUrl.replace("id=", "id=" + ga.getId());
        QRCodeUtils.encode(text, null, qrBarginImgUrl, ga.getId(), true);
        return R.ok();
    }

    @RequestMapping(value = "/bargin", method = RequestMethod.POST)
    @Transactional
    //@RequiresPermissions("sys:distribution:delete")
    public ReturnResult bargin(@RequestBody BarginEntity bargin) throws ParseException {
        ReturnResult result = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        BarginEntity ba = barginService.selectById(bargin.getId());
        Double reduct = Math.random()*(ba.getMaxReduction().subtract(ba.getMinReduction()).doubleValue())+ba.getMinReduction().doubleValue();
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrder_id(bargin.getGift());
        orderInfo.setTotal_price(String.valueOf(reduct));
        orderService.edit(orderInfo);
        return result;
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
