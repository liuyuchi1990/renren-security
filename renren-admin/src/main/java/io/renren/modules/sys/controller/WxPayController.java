package io.renren.modules.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.renren.common.config.Constants;
import io.renren.common.utils.*;
import io.renren.modules.bargin.service.BarginService;
import io.renren.modules.order.model.Order;
import io.renren.modules.order.model.OrderEntity;
import io.renren.modules.order.model.OrderInfo;
import io.renren.modules.order.service.OrderService;
import io.renren.modules.sys.entity.PayInfo;
import io.renren.modules.sys.entity.ReturnCodeEnum;
import io.renren.modules.sys.entity.ReturnResult;
import io.renren.modules.sys.entity.SysUserEntity;
import io.renren.modules.sys.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.*;


@RestController
@RequestMapping("/api/wxpay")
public class WxPayController {
    private static Logger log = LoggerFactory.getLogger(WxPayController.class);

    @Autowired
    OrderService orderService;

    @Autowired
    BarginService barginService;

    @Autowired
    SysUserService sysUserService;

    @RequestMapping(value = "/getServer",method=RequestMethod.GET)
    public void login(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("success");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
                out.write(echostr);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
    @RequestMapping(value = "/prepay", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult prePay(@RequestParam(required = true) String user_id,
                               @RequestParam(required = true ) String orderId,
                               @RequestParam(required = true) Double total_fee,
                               HttpServletRequest request)throws Exception {

        String content = null;
        Map map = new HashMap();
        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        boolean result = true;
        String info = "";
        Map<String, Object> objectMap = orderService.queryByOrderId(orderId);
        if("3".equals(objectMap.get("order_status").toString())){
            rs.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            rs.setMsg("您已经成功支付订单，无需重复支付");
            return rs;
        }
        OrderEntity od = JSON.parseObject(JSON.toJSONString(objectMap), OrderEntity.class);
        log.error("\n======================================================");
        log.error("code: " + user_id);

        SysUserEntity user = sysUserService.queryById(user_id);
        if(total_fee==null) {
            total_fee = Double.valueOf(od.getTotalPrice());
        }
        String openId = user.getOpenId();

        //判断订单状态是否正常
        if (StringUtils.isBlank(openId)||("5".equals(od.getOrderStatus()))) {
            result = false;
            info = "订单超时";
        } else {
            openId = openId.replace("\"", "").trim();

            String clientIP = CommonUtil.getClientIp(request);

            //clientIP = "222.12.47.12";

            log.error("openId: " + openId + ", clientIP: " + clientIP);

            String randomNonceStr = RandomUtils.generateMixString(32);
            String prepayId = WxUtil.unifiedOrder(openId, clientIP, randomNonceStr, orderId,total_fee,false);

            log.error("prepayId: " + prepayId);

            if (StringUtils.isBlank(prepayId)) {
                rs.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
                rs.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
                result = false;
                info = "出错了，未获取到prepayId";
            } else {
                map.put("appId",Constants.PTAPPID);
                map.put("package", "prepay_id="+prepayId);
                map.put("nonceStr", randomNonceStr);
                map.put("timestamp",System.currentTimeMillis()/1000+"");
                map.put("paySign",WxUtil.getSecondSign(prepayId,System.currentTimeMillis()/1000+"",randomNonceStr));
            }
        }

        try {
            map.put("result", result);
            map.put("info", info);
            //content = mapper.writeValueAsString(map);
        } catch (Exception e) {
            rs.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
            rs.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
            rs.setResult(map);
            e.printStackTrace();
            return rs;
        }
        rs.setResult(map);
        return rs;
    }

    @RequestMapping(value = "/prepayApp", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult prepayApp(@RequestBody Order order,
                                  HttpServletRequest request)throws Exception {

        String content = null;
        Map map = new HashMap();
        String openId = order.getUser_id();
        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        boolean result = true;
        String info = "";
        Map<String, Object> objectMap = orderService.queryByOrderId(order.getOrderId());
        OrderEntity od = JSON.parseObject(JSON.toJSONString(objectMap), OrderEntity.class);
        log.error("\n======================================================");
        log.error("code: " + order.getUser_id());

        Double total_fee = Double.valueOf(od.getTotalPrice());

        //判断订单状态是否正常
        if (StringUtils.isBlank(openId)||("5".equals(od.getOrderStatus()))) {
            result = false;
            info = "订单超时";
        } else {
            openId = openId.replace("\"", "").trim();

            String clientIP = CommonUtil.getClientIp(request);

            //clientIP = "222.12.47.12";

            log.error("openId: " + openId + ", clientIP: " + clientIP);

            String randomNonceStr = RandomUtils.generateMixString(32);
            String prepayId = WxUtil.unifiedOrder(openId, clientIP, randomNonceStr, order.getOrderId(),total_fee,true);

            log.error("prepayId: " + prepayId);

            if (StringUtils.isBlank(prepayId)) {
                rs.setCode(ReturnCodeEnum.SYSTEM_ERROR.getCode());
                rs.setMsg(ReturnCodeEnum.SYSTEM_ERROR.getMessage());
                result = false;
                info = "出错了，未获取到小程序prepayId";
            } else {
                map.put("appId",Constants.APPID);
                map.put("package", "prepay_id="+prepayId);
                map.put("nonceStr", randomNonceStr);
                map.put("timestamp",System.currentTimeMillis()/1000+"");
                map.put("paySign",WxUtil.getSecondSignApp(prepayId,System.currentTimeMillis()/1000+"",randomNonceStr));
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
                if("1".equals(order.getOrderStatus())) {
                    rs = orderService.edit(orderInfo);
                    if(Constants.BARGIN.equals(order.getOrderType())){
                        barginService.releaseBargin(order.getActivityId());
                    }
                }
                //判断 是否更新成功
                if ((rs > 0)||("1".equals(order.getOrderStatus()))) {
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

    @RequestMapping(value = "/initwxjs", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult init(@RequestParam(required = true) String url,
                             HttpServletRequest request) throws UnsupportedEncodingException {
        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        //ServletContext servletContext = ServletContextUtil.getServletContext();
        url = java.net.URLDecoder.decode(url,"UTF-8").replace("&amp;","&");
        String accessToken = WxUtil.getWxPlatFormAccessToken();
        String jsapi_ticket = WxUtil.getAccessTicket(accessToken);
        Map<String, Object> map = new HashedMap();
        Map<String, String> ret = WxUtil.sign(jsapi_ticket, url);
        System.out.println("currurl = "+ url);

        System.out.println("signature =" + ret.get("signature"));
        map.put("data",ret);
        map.put("status", "success");
        map.put("msg", "send ok");
        rs.setResult(map);
        return rs;
    }

    @RequestMapping("/getUserInfo")
    public ReturnResult getUserInfo(@RequestParam(name="code",required=false)String code,
                         @RequestParam(name="state")String state) {

        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        Map<String, Object> ret = new HashedMap();
        System.out.println("-----------------------------收到请求，请求数据为：" + code + "-----------------------" + state);
        SysUserEntity user = new SysUserEntity();

        //通过code换取网页授权web_access_token
        if (code != null || !(code.equals(""))) {
            String CODE = code;
            String WebAccessToken = "";
            String openId = "";
            //替换字符串，获得请求URL
            String token = UserInfoUtil.getWebAccess(Constants.PTAPPID, Constants.PSERCRET, CODE);
            System.out.println("----------------------------token为：" + token);
            //通过https方式请求获得web_access_token
            JSONObject jsonObject = WxUtil.httpRequest(token, "GET", null);
            System.out.println("jsonObject------" + jsonObject);
            if (null != jsonObject) {
                try {

                    WebAccessToken = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                    System.out.println("获取access_token成功-------------------------" + WebAccessToken + "----------------" + openId);

                    //-----------------------拉取用户信息...替换字符串，获得请求URL
                    String userMessage = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                    System.out.println(" userMessage===" + userMessage);
                    //通过https方式请求获得用户信息响应
                    JSONObject userMessageJsonObject = WxUtil.httpRequest(userMessage, "GET", null);

                    System.out.println("userMessagejsonObject------" + userMessageJsonObject);

                    if (userMessageJsonObject != null) {
                        try {
                            //用户昵称
                             SysUserEntity utmp = sysUserService.queryByOpenId(userMessageJsonObject.getString("openid"));
                            //获取成果，存入数据库
                            if(utmp==null){
                                user.setUsername(userMessageJsonObject.getString("nickname"));
                                user.setNickname(userMessageJsonObject.getString("nickname"));
                                //用户性别
                                user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
                                user.setSex(Integer.parseInt(userMessageJsonObject.getString("sex")));
                                user.setStatus(0);
                                user.setProvince(userMessageJsonObject.getString("province"));
                                user.setSubscribetime(userMessageJsonObject.getString("subscribetime"));
                                user.setCity(userMessageJsonObject.getString("city"));
                                user.setHeadimgurl(userMessageJsonObject.getString("headimgurl"));
                                user.setPassword("123456");
                                //用户唯一标识
                                user.setOpenId(userMessageJsonObject.getString("openid"));
                                user.setUnionid(userMessageJsonObject.getString("unionid"));
                                sysUserService.insertUser(user);
                            }else{
                                user = utmp;
                            }

                        } catch (JSONException e) {
                            System.out.println("获取userName失败");
                        }
                    }


                } catch (JSONException e) {
                    WebAccessToken = null;// 获取code失败
                    System.out.println("获取WebAccessToken失败");
                }
            }
        }
        ret.put("user",user);
        ret.put("code",code);
        map.put("data",ret);
        map.put("status", "success");
        map.put("msg", "send ok");
        rs.setResult(map);
        return rs;
    }

    @RequestMapping("/saveUserInfo")
    public ReturnResult saveUserInfo(@RequestBody ReturnResult result) {

        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        Map<String, Object> ret = new HashedMap();
        System.out.println("-----------------------------收到请求，请求数据为：" + result.getCode() + "-----------------------" + result.getMsg());
        SysUserEntity user = new SysUserEntity();
        String openId = "";
        String session_key = "";
        String WebAccessToken = "";
        //通过code换取网页授权web_access_token
        if (result.getCode() != null || !(result.getCode().equals(""))) {
            String CODE = result.getCode();


            //替换字符串，获得请求URL
            Map<String, Object> mp = WxUtil.getSessionKeyOropenid(CODE);
            System.out.println("----------------------------sessionKeyOropenid：" + mp);
            //通过https方式请求获得web_access_token
            if (null != mp) {
                try {

                    WebAccessToken = WxUtil.getWxAppAccessToken();
                    openId = mp.get("openid").toString();
                    session_key = mp.get("session_key").toString();
                    System.out.println("获取access_token成功-------------------------" + WebAccessToken + "----------------" + openId);
                    user = JSON.parseObject(result.getMsg(),SysUserEntity.class);
                    JSONObject jsonObject = JSON.parseObject(result.getMsg());
                    if (result.getMsg() != null) {
                        try {
                            //用户昵称
                            SysUserEntity utmp = sysUserService.queryByAppOpenId(openId);
                            //获取成果，存入数据库
                            if(utmp==null&&user.getMobile()!=null){
                                user.setUsername(user.getNickname());
                                user.setNickname(user.getNickname());
                                //用户性别
                                user.setUserId(UUID.randomUUID().toString().replaceAll("-", ""));
                                user.setSex(Integer.parseInt(jsonObject.getString("gender")));
                                user.setStatus(0);
                                user.setProvince(user.getProvince());
                                user.setCity(user.getCity());
                                user.setHeadimgurl(jsonObject.getString("avatarUrl"));
                                user.setPassword("123456");
                                //用户唯一标识
                                user.setAppOpenId(openId);
                                //user.setUnionid(mp.get("unionid").toString());
                                sysUserService.insertUser(user);
                            }else{
                                user.setAppOpenId(openId);
                                user.setHeadimgurl(jsonObject.getString("avatarUrl"));
                                user.setSex(Integer.parseInt(jsonObject.getString("gender")));
                                if(utmp!=null){
                                    user = utmp;
                                }
                            }

                        } catch (JSONException e) {
                            System.out.println("获取userName失败");
                        }
                    }


                } catch (JSONException e) {
                    WebAccessToken = null;// 获取code失败
                    System.out.println("获取WebAccessToken失败");
                }
            }
        }
        ret.put("user",user);
        ret.put("WebAccessToken",WebAccessToken);
        ret.put("session_key",session_key);
        ret.put("openid",openId);
        map.put("data",ret);
        map.put("status", "success");
        map.put("msg", "send ok");
        rs.setResult(map);
        return rs;
    }

    @RequestMapping("/decryptPhone")
    public ReturnResult decryptTel(@ApiParam("微信小程序授权之后获取电话（加密字符串，json对象）") @RequestParam("encryptedData") String encryptedData,
                               @RequestParam("iv") String iv ,@RequestParam("code") String code) throws Exception {
        ReturnResult rs = new ReturnResult(ReturnCodeEnum.SUCCESS.getCode(), ReturnCodeEnum.SUCCESS.getMessage());
        Map<String, Object> map = new HashedMap();
        Map<String, Object> mp = WxUtil.getSessionKeyOropenid(code);
        String sessionKey = mp.get("session_key").toString();
        String phone = WxUtil.decryptS5(encryptedData,"UTF-8",sessionKey,iv);
        map.put("data",phone);
        map.put("status", "success");
        map.put("msg", "send ok");
        rs.setResult(map);
        return rs;
    }
}
