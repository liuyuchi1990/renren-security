package io.renren.modules.sys.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goku.coreui.order.model.Order;
import com.goku.coreui.order.model.OrderInfo;
import com.goku.coreui.order.service.OrderService;
import com.goku.coreui.sys.config.Constants;
import com.goku.coreui.sys.model.PayInfo;
import com.goku.coreui.sys.model.ReturnCodeEnum;
import com.goku.coreui.sys.model.ReturnResult;
import com.goku.coreui.sys.service.SysUserService;
import com.goku.coreui.sys.util.*;
import io.renren.common.config.Constants;
import io.renren.common.utils.*;
import io.renren.modules.sys.entity.PayInfo;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/wxpay")
public class WxPayController {
    private static Logger log = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    OrderService orderService;
    @Autowired
    SysUserService sysUserService;

    @RequestMapping(value = "/prepay", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult prePay(@RequestParam(required = true) String user_id,
                               @RequestParam(required = true) String orderId,
                               @RequestParam(required = true) Double total_fee,
                               HttpServletRequest request)throws Exception {

        String content = null;
        Map map = new HashMap();
        ObjectMapper mapper = new ObjectMapper();
        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        boolean result = true;
        String info = "";

        log.error("\n======================================================");
        log.error("code: " + user_id);

        Map<String,Object> mp = sysUserService.queryById(user_id);

        String openId = mp.get("open_id").toString();
        Order od = orderService.queryById(orderId);
        //判断订单状态是否正常
        if (StringUtils.isBlank(openId)||("5".equals(od.getOrder_status()))) {
            result = false;
            info = "订单超时";
        } else {
            openId = openId.replace("\"", "").trim();

            String clientIP = CommonUtil.getClientIp(request);

            //clientIP = "222.12.47.12";

            log.error("openId: " + openId + ", clientIP: " + clientIP);

            String randomNonceStr = RandomUtils.generateMixString(32);
            String prepayId = unifiedOrder(openId, clientIP, randomNonceStr, orderId,total_fee);

            log.error("prepayId: " + prepayId);

            if (StringUtils.isBlank(prepayId)) {
                rs.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
                rs.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
                result = false;
                info = "出错了，未获取到prepayId";
            } else {
                map.put("package", "prepay_id="+prepayId);
                map.put("nonceStr", randomNonceStr);
                map.put("timestamp",System.currentTimeMillis()/1000+"");
                map.put("paySign",getSecondSign(prepayId,System.currentTimeMillis()/1000+"",randomNonceStr));
            }
        }

        try {
            map.put("result", result);
            map.put("info", info);
            //content = mapper.writeValueAsString(map);
        } catch (Exception e) {
            rs.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            rs.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            rs.setResult(map);
            e.printStackTrace();
            return rs;
        }
        rs.setResult(map);
        return rs;
    }


    /**
     * 调用统一下单接口
     *
     * @param openId
     */
    private String unifiedOrder(String openId, String clientIP, String randomNonceStr, String orderId,double total_fee) {

        try {

            String url = Constants.URL_UNIFIED_ORDER;

            PayInfo payInfo = createPayInfo(openId, clientIP, randomNonceStr,orderId,total_fee);
            String md5 = getSign(payInfo);
            payInfo.setSign(md5);
            log.error("md5 value: " + md5);

            String xml = CommonUtil.payInfoToXML(payInfo);
            xml = xml.replace("__", "_").replace("<![CDATA[1]]>", "1");
            //xml = xml.replace("__", "_").replace("<![CDATA[", "").replace("]]>", "");
            log.error(xml);

            StringBuffer buffer = HttpUtil.httpsRequest(url, "POST", xml);
            log.error("unifiedOrder request return body: \n" + buffer.toString());
            Map<String, String> result = CommonUtil.parseXml(buffer.toString());


            String return_code = result.get("return_code");
            if (StringUtils.isNotBlank(return_code) && return_code.equals("SUCCESS")) {

                String return_msg = result.get("return_msg");
                if (StringUtils.isNotBlank(return_msg) && !return_msg.equals("OK")) {
                    //log.error("统一下单错误！");
                    return "";
                }

                String prepay_Id = result.get("prepay_id");
                return prepay_Id;

            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private PayInfo createPayInfo(String openId, String clientIP, String randomNonceStr,String orderId,double total_fee) {

        Date date = new Date();
        String timeStart = TimeUtils.getFormatTime(date, Constants.TIME_FORMAT);
        String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constants.TIME_EXPIRE), Constants.TIME_FORMAT);

        String randomOrderId = CommonUtil.getRandomOrderId();

        PayInfo payInfo = new PayInfo();
        payInfo.setAppid(Constants.APPID);
        payInfo.setMch_id(Constants.MCH_ID);
        payInfo.setDevice_info("WEB");
        payInfo.setNonce_str(randomNonceStr);
        payInfo.setSign_type("MD5");  //默认即为MD5
        payInfo.setBody("猩愿机");
        payInfo.setAttach("4luluteam");
        payInfo.setOut_trade_no(orderId);
        //微信价格最小单位分 转换为整数
        DecimalFormat df = new DecimalFormat("#######.##");
        total_fee = total_fee * 100;
        total_fee = Math.ceil(total_fee);
        String price = df.format(total_fee);
        payInfo.setTotal_fee(Integer.parseInt(price));
        payInfo.setSpbill_create_ip(clientIP);
        payInfo.setTime_start(timeStart);
        payInfo.setTime_expire(timeExpire);
        payInfo.setNotify_url(Constants.URL_NOTIFY);
        payInfo.setTrade_type("JSAPI");
        payInfo.setLimit_pay("no_credit");
        payInfo.setOpenid(openId);
        return payInfo;
    }

    private String getSign(PayInfo payInfo) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appid=" + payInfo.getAppid())
                .append("&attach=" + payInfo.getAttach())
                .append("&body=" + payInfo.getBody())
                .append("&device_info=" + payInfo.getDevice_info())
                .append("&limit_pay=" + payInfo.getLimit_pay())
                .append("&mch_id=" + payInfo.getMch_id())
                .append("&nonce_str=" + payInfo.getNonce_str())
                .append("&notify_url=" + payInfo.getNotify_url())
                .append("&openid=" + payInfo.getOpenid())
                .append("&out_trade_no=" + payInfo.getOut_trade_no())
                .append("&sign_type=" + payInfo.getSign_type())
                .append("&spbill_create_ip=" + payInfo.getSpbill_create_ip())
                .append("&time_expire=" + payInfo.getTime_expire())
                .append("&time_start=" + payInfo.getTime_start())
                .append("&total_fee=" + payInfo.getTotal_fee())
                .append("&trade_type=" + payInfo.getTrade_type())
                .append("&key=" + Constants.APP_KEY);

        log.error("排序后的拼接参数：" + sb.toString());

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(sb.toString().toString().getBytes("UTF-8"));
        return byteToStr(md5.digest()).toUpperCase();
    }

    private String getSecondSign(String prepay_id,String time,String noncestr) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appId=" + Constants.APPID)
                .append("&nonceStr=" + noncestr)
                .append("&package=prepay_id=" + prepay_id)
                .append("&signType=MD5&timeStamp=" + time)
                .append("&key=" + Constants.APP_KEY);
        log.error("排序后的拼接参数：" + sb.toString());

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(sb.toString().toString().getBytes("UTF-8"));
        return byteToStr(md5.digest()).toUpperCase();
    }

    @RequestMapping(value = "/payCallback", method = RequestMethod.POST)
    @Transactional(rollbackFor = {Exception.class}, readOnly = false)
    public void payCallback(HttpServletRequest request, HttpServletResponse response) {
        log.info("微信回调接口方法 start");
        log.info("微信回调接口 操作逻辑 start");
        String inputLine = "";
        String notityXml = "";
        try {
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            //关闭流
            request.getReader().close();
            log.info("微信回调内容信息：" + notityXml);
            //解析成Map
            Map<String, String> map = WxUtil.doXMLParse(notityXml);
            //判断 支付是否成功
            if ("SUCCESS".equals(map.get("result_code"))) {
                log.info("微信回调返回是否支付成功：是");
                //获得 返回的商户订单号
                String outTradeNo = map.get("out_trade_no");
                log.info("微信回调返回商户订单号：" + outTradeNo);
                //访问DB
                OrderInfo orderInfo = new OrderInfo();
                Order order = orderService.queryById(outTradeNo);
                //修改支付状态
                orderInfo.setOrder_status("3");
                orderInfo.setPay_type("0");
                orderInfo.setOrder_id(outTradeNo);


                int rs = 1;
                if("1".equals(order.getOrder_status())) {
                    rs = orderService.edit(orderInfo);
                }
                //判断 是否更新成功
                if ((rs > 0)||("1".equals(order.getOrder_status()))) {
                    log.info("微信回调  订单号：" + outTradeNo + ",修改状态成功");
                    //封装 返回值
                    StringBuffer buffer = new StringBuffer();
                    buffer.append("<xml>");
                    buffer.append("<return_code>SUCCESS</return_code>");
                    buffer.append("<return_msg>OK</return_msg>");
                    buffer.append("<xml>");

                    //给微信服务器返回 成功标示 否则会一直询问 咱们服务器 是否回调成功
                    PrintWriter writer = response.getWriter();
                    //返回
                    writer.print(buffer.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += this.byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }
    private String byteToHexStr(byte bytes) {
        char[] Digit = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(bytes >>> 4) & 0X0F];
        tempArr[1] = Digit[bytes & 0X0F];

        String s = new String(tempArr);
        return s;
    }
}
