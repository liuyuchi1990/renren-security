package io.renren.common.config;

public class Constants {
    /**
     * 微信小程序APPID
     */
    public static final String APPID = "wx6a09ed4a9b5b3970";

    /**
     * 微信公众号APPID
     */
    public static final String PAPPID = "wxd8ea2d8f88363b1f";

    public static final String PTAPPID = "wx2517d7a8920ab213";


    public static final String SIGN = "HJJI8970OKMNHY726YTGHJI908YHNBGT";

    public static final String PSIGN = "fc1b9700e07c9e385cc0a0d101185980";

    /**
     * 微信小程序SERCRET
     */
    public static final String SERCRET = "7e7a01e5f83330ad0362b4acefe71ffc";

    /**
     * 微信公众号SERCRET
     */
    public static final String PSERCRET = "03d04cb32236f9a95d4656863310b317";

    /**
     * 微信单次货物购买积分
     */
    public static final Integer Integral = 20;


    /**
     * 微信单次货物购买价格
     */
    public static final Double PRICE = 20.00;

    //签名方式，固定值
    public static final String SIGNTYPE = "MD5";
    //交易类型，小程序支付的固定值为JSAPI
    public static final String TRADETYPE = "JSAPI";

    public static final String APP_KEY = "xyj185568014781qaz2wsx3edc4rfv5t";

    public static final String MCH_ID = "1513253941";  //商户号

    public static final String PMCH_ID = "1511521411";  //商户号

    public static final String URL_UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public static final String DOMAIN = "https://wx.sharkmeida.cn";

    public static final String URL_NOTIFY = Constants.DOMAIN + "/api/wxpay/payCallback";

    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final int TIME_EXPIRE = 2;  //单位是day

    public static final String QR_FILE_PATH = "C:/apache-tomcat-8.5.31/webapps/imgs/qr/";

    /**
     * @Fields EN_CODING : 定义编码格式
     */
    public static final String EN_CODING = "UTF-8";

    public static final String CONTENT_DISPOSITION = "Content-disposition";

    public static final String ATTACHMENT_FILENAME = "attachment; filename=\"%s\"";

    /**
     * iso-8859-1编码格式
     */
    public static final String EN_CODING_ISO = "iso-8859-1";

    /**
     * gbk编码格式
     */
    public static final String EN_CODING_GBK = "gbk";

    public static final String FILE_NAME = "fileName";

    public static final String DISTRIBUTION = "1";

    public static final String GATHER = "2";

    public static final String BARGIN = "3";

    public static final String GROUPON = "4";

    public static final String Lottery = "5";

}
