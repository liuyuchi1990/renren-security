package io.renren.common.utils;

import com.swetake.util.Qrcode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liwenlong on 2018/5/15.
 */
public class QRCodeUtils {

    public static Boolean createQRCode(String url, String imgPath, String logoPath) {
        try {
            Qrcode qrcodeHandler = new Qrcode();
            qrcodeHandler.setQrcodeErrorCorrect('M');// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeEncodeMode('B');// N代表数字,A代表字符a-Z,B代表其他字符
            qrcodeHandler.setQrcodeVersion(9);// 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大
            byte[] contentBytes = url.getBytes("utf-8");// 设置编码格式为UTF-8
            BufferedImage bufImg = new BufferedImage(168, 168, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.white);         // 设置背景色为白色
            gs.clearRect(0, 0, 168, 168);
            gs.setColor(Color.BLACK);              // 设定图像颜色 为黑色
            int pixoff = 2;                        // 设置偏移量 不设置可能导致解析出错
            // 输出内容 > 二维码
            if (contentBytes.length > 0 && contentBytes.length < 150) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                return false;
            }
            Image img = ImageIO.read(new File(logoPath));  // 实例化一个Image对象。
            gs.drawImage(img, 60, 60, 45, 45, null);       // 60,60是距离gs两个边的距离，45,45是中间logo的大小
            gs.dispose();
            bufImg.flush();
            File imgFile = new File(imgPath);
            ImageIO.write(bufImg, "png", imgFile);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Boolean createQRCode(String url, String imgPath) {
        try {
            //计算二维码图片的高宽比
            // API文档规定计算图片宽高的方式 ，v是本次测试的版本号
            int v = 6;
            int width = 67 + 12 * (v - 1);
            int height = 67 + 12 * (v - 1);

            Qrcode x = new Qrcode();
            /**
             * 纠错等级分为
             * level L : 最大 7% 的错误能够被纠正；
             * level M : 最大 15% 的错误能够被纠正；
             * level Q : 最大 25% 的错误能够被纠正；
             * level H : 最大 30% 的错误能够被纠正；
             */
            x.setQrcodeErrorCorrect('L');
            x.setQrcodeEncodeMode('B');//注意版本信息 N代表数字 、A代表 a-z,A-Z、B代表 其他)
            x.setQrcodeVersion(6);//版本号  1-40

            byte[] d = url.getBytes("utf-8");//汉字转格式需要抛出异常

            //缓冲区
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

            //绘图
            Graphics2D gs = bufferedImage.createGraphics();

            gs.setBackground(Color.WHITE);
            gs.setColor(Color.BLACK);
            gs.clearRect(0, 0, width, height);

            //偏移量
            int pixoff = 2;


            /**
             * 容易踩坑的地方
             * 1.注意for循环里面的i，j的顺序，
             *   s[j][i]二维数组的j，i的顺序要与这个方法中的 gs.fillRect(j*3+pixoff,i*3+pixoff, 3, 3);
             *   顺序匹配，否则会出现解析图片是一串数字
             * 2.注意此判断if (d.length > 0 && d.length < 120)
             *   是否会引起字符串长度大于120导致生成代码不执行，二维码空白
             *   根据自己的字符串大小来设置此配置
             */
            if (d.length > 0 && d.length < 120) {
                boolean[][] s = x.calQrcode(d);

                for (int i = 0; i < s.length; i++) {
                    for (int j = 0; j < s.length; j++) {
                        if (s[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            }
            gs.dispose();
            bufferedImage.flush();
            //设置图片格式，与输出的路径
            ImageIO.write(bufferedImage, "png", new File(imgPath));
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static Map getminiqrQr(String accessToken, String path, String urlindex) {
        RestTemplate rest = new RestTemplate();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=" + accessToken;
            Map<String, Object> param = new HashMap<>();
            //param.put("scene", "1");
            param.put("path", urlindex);
            param.put("width", 300);
            //param.put("auto_color", false);
            Map<String, Object> line_color = new HashMap<>();
            line_color.put("r", 0);
            line_color.put("g", 0);
            line_color.put("b", 0);
            //param.put("line_color", line_color);
            System.out.println("调用生成微信URL接口传参:" + param);
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            HttpEntity requestEntity = new HttpEntity(param, headers);
            ResponseEntity<byte[]> entity = rest.exchange(url, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
            System.out.println("调用小程序生成微信永久小程序码URL接口返回结果:" + entity.getBody());
            byte[] result = entity.getBody();
            inputStream = new ByteArrayInputStream(result);

            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            System.out.println("调用小程序生成微信永久小程序码URL接口异常");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }


    public static void main(String[] args) {
//        String imgPath = "C:/apache-tomcat-8.5.31/webapps/imgs/qr/abc.png";// 最后生成的图片地址
//        String logo = "pages/index/index";// 加入的logo照片
//        String acc = "9_xnAK6rJlwGfhkovOhn70km9CF_94T1P83dOucrIPA5FztqYLNbuJdSSEoNjLJQlVJDWbSaWEjPsz_RzRRUusEPDLWeHk0E-S_36yz5uqWU7GNC_vdwte3Jwv8ULheqFWQBK7kEUTzwEEYE7cCWFcAAAMGP";
//
//        QRCodeUtils.getminiqrQr(acc, imgPath, logo);
    }

}
