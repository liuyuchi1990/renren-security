package io.renren.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import io.renren.common.config.Constants;
import io.renren.modules.sys.controller.WxPayController;
import io.renren.modules.sys.entity.PayInfo;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.DecimalFormat;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import java.util.*;

public class WxUtil {
    private static Logger log = LoggerFactory.getLogger(WxPayController.class);
    public static String getWxAppAccessToken() {
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", Constants.APPID);  //开发者设置中的appId
        requestUrlParam.put("secret", Constants.SERCRET); //开发者设置中的appSecret
        //requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "client_credential");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpUtil.sendPost("https://api.weixin.qq.com/cgi-bin/token", requestUrlParam));
        return jsonObject.get("access_token").toString();
    }

    public static String getWxPlatFormAccessToken() {
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        requestUrlParam.put("appid", Constants.PTAPPID);  //开发者设置中的appId
        requestUrlParam.put("secret", Constants.PSERCRET); //开发者设置中的appSecret
        //requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "client_credential");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpUtil.sendPost("https://api.weixin.qq.com/cgi-bin/token", requestUrlParam));
        return jsonObject.get("access_token").toString();
    }

    public static String getAccessTicket(String Accesstkoken) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + Accesstkoken + "&type=jsapi";
        JSONObject jsonObject = httpRequest(requestUrl, "GET", null);
        String ticket = "";
        // 如果请求成功
        if (null != jsonObject) {
            try {
                ticket = jsonObject.getString("ticket").toString();

            } catch (JSONException e) {

                // 获取token失败 , jsonObject.getInt("errcode"), jsonObject.getString("errmsg")
                System.out.println("获取token失败 errcode:{} errmsg:{}");
            }
        }
        return ticket;
    }

    public static Map<String, Object> getSessionKeyOropenid(String wxCode) {
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<String, Object>();
        requestUrlParam.put("appid", Constants.APPID);  //开发者设置中的appId
        requestUrlParam.put("secret", Constants.SERCRET); //开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "client_credential");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpUtil.sendPost("https://api.weixin.qq.com/sns/jscode2session", requestUrlParam));
        map.put("openid", jsonObject.get("openid"));
        map.put("session_key", jsonObject.get("session_key"));
        return map;
    }

    public static JSONObject getJsonByCode(String wxCode) {
        Map<String, String> requestUrlParam = new HashMap<String, String>();
        Map<String, Object> map = new HashMap<String, Object>();
        requestUrlParam.put("appid", Constants.APPID);  //开发者设置中的appId
        requestUrlParam.put("secret", Constants.SERCRET); //开发者设置中的appSecret
        requestUrlParam.put("js_code", wxCode); //小程序调用wx.login返回的code
        requestUrlParam.put("grant_type", "client_credential");    //默认参数

        //发送post请求读取调用微信 https://api.weixin.qq.com/sns/jscode2session 接口获取openid用户唯一标识
        JSONObject jsonObject = JSON.parseObject(HttpUtil.sendPost("https://api.weixin.qq.com/sns/jscode2session", requestUrlParam));
        return jsonObject;
    }

    /**
     * StringUtils工具类方法
     * 获取一定长度的随机字符串，范围0-9，a-z
     *
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }



    /**
     * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
     *
     * @param strxml
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> doXMLParse(String strxml) throws Exception {
        if (null == strxml || "".equals(strxml)) {
            return null;
        }

        Map<String, String> m = new HashMap<String, String>();
        InputStream in = String2Inputstream(strxml);
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        Element root = doc.getRootElement();
        List list = root.getChildren();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element e = (Element) it.next();
            String k = e.getName();
            String v = "";
            List children = e.getChildren();
            if (children.isEmpty()) {
                v = e.getTextNormalize();
            } else {
                v = getChildrenText(children);
            }

            m.put(k, v);
        }

        //关闭流
        in.close();

        return m;
    }

    private static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
     */
    @SuppressWarnings("rawtypes")
    private static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }
        return sb.toString();
    }


    /**
     * 发起https请求并获取结果
     *
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 将字节转换为十六进制字符串 * * @param mByte * @return
     */
    public static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }

    /**
     * 将字节数组转换为十六进制字符串 * * @param byteArray * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 调用统一下单接口
     *
     * @param openId
     */
    public static String unifiedOrder(String openId, String clientIP, String randomNonceStr, String orderId,double total_fee,boolean ifApp) {

        try {

            String url = Constants.URL_UNIFIED_ORDER;

            PayInfo payInfo = createPayInfo(openId, clientIP, randomNonceStr,orderId,total_fee,ifApp);
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

    public static PayInfo createPayInfo(String openId, String clientIP, String randomNonceStr,String orderId,double total_fee,boolean ifAppp) {
        Date date = new Date();
        String timeStart = TimeUtils.getFormatTime(date, Constants.TIME_FORMAT);
        String timeExpire = TimeUtils.getFormatTime(TimeUtils.addDay(date, Constants.TIME_EXPIRE), Constants.TIME_FORMAT);

        String randomOrderId = CommonUtil.getRandomOrderId();

        PayInfo payInfo = new PayInfo();
        if(ifAppp){
            payInfo.setAppid(Constants.APPID);
            payInfo.setMch_id(Constants.PMCH_ID);
        }else{
            payInfo.setAppid(Constants.PTAPPID);
            payInfo.setMch_id(Constants.PMCH_ID);
        }
        payInfo.setDevice_info("WEB");
        payInfo.setNonce_str(randomNonceStr);
        payInfo.setSign_type("MD5");  //默认即为MD5
        payInfo.setBody("小飞象");
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

    public static String getSign(PayInfo payInfo) throws Exception {
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
                .append("&key=" + Constants.PSIGN);

        log.error("排序后的拼接参数：" + sb.toString());

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(sb.toString().toString().getBytes("UTF-8"));
        return byteToStr(md5.digest()).toUpperCase();
    }

    public static String getSecondSign(String prepay_id,String time,String noncestr) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appId=" + Constants.PTAPPID)
                .append("&nonceStr=" + noncestr)
                .append("&package=prepay_id=" + prepay_id)
                .append("&signType=MD5&timeStamp=" + time)
                .append("&key=" + Constants.PSIGN);
        log.error("排序后的拼接参数：" + sb.toString());

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(sb.toString().toString().getBytes("UTF-8"));
        return byteToStr(md5.digest()).toUpperCase();
    }

    public static String getSecondSignApp(String prepay_id,String time,String noncestr) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("appId=" + Constants.APPID)
                .append("&nonceStr=" + noncestr)
                .append("&package=prepay_id=" + prepay_id)
                .append("&signType=MD5&timeStamp=" + time)
                .append("&key=" + Constants.PSIGN);
        log.error("排序后的拼接参数：" + sb.toString());

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(sb.toString().toString().getBytes("UTF-8"));
        return byteToStr(md5.digest()).toUpperCase();
    }

    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
        System.out.println(string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("appId",Constants.PTAPPID);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String decrypt(String sessionKey,String ivData, String encrypData) throws Exception {
        byte[] encData = Base64.decodeBase64(encrypData);
        byte[] iv = Base64.decodeBase64(ivData);
        byte[] key = Base64.decodeBase64(sessionKey);
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData), "UTF-8");
    }

    /**
     * 解密工具直接放进去即可
     */
    public static String decryptS5(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] raw = decoder.decodeBuffer(sKey);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(decoder.decodeBuffer(ivParameter));
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] myendicod = decoder.decodeBuffer(sSrc);
            byte[] original = cipher.doFinal(myendicod);
            String originalString = new String(original, encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
