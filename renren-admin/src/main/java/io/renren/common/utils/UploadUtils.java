package io.renren.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class UploadUtils {
    /**
     * 保存图片
     * @param file
     * @param path
     * @return
     */
    public static String saveFile(MultipartFile file, String path, String name) {
        // 判断文件是否为空
        if (!file.isEmpty()) {
            try {
                File filepath = new File(path);
                if (!filepath.exists())
                    filepath.mkdirs();
                // 文件保存路径

                String fileName = file.getOriginalFilename();
                int index = fileName.lastIndexOf(".");
                fileName = name + fileName.substring(index);
                String savePath = path + File.separator + fileName;
                // 转存文件
                file.transferTo(new File(savePath));
                return fileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
