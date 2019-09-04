package io.renren.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Excelutil {
    /**
     * @Author： Lee
     * @Time：2017/7/24
     * @Description：读xlsx类型的文件
     * @修改人：
     * @修改时间：
     */
    public static String readXlsx(String filePath) throws Exception {
        InputStream is = new FileInputStream(filePath);
        //List<List<String>> result;
        String result;
        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is)) {
            //result = new ArrayList<>();
            result = "";
            //循环每一页，并处理当前的循环页
            for (Sheet sheet : xssfWorkbook) {
                if (sheet == null) {
                    continue;
                }
                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);//Row表示每一行的数据
                    int minColIx = row.getFirstCellNum();
                    int maxColIx = row.getLastCellNum();
                    //List<String> rowList = new ArrayList<>();
                    String rowList = "";
                    //遍历该行，并获取每一个cell的数据
                    for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                        Cell cell = row.getCell(colIx);
                        cell.setCellType(CellType.STRING);
                        if (cell == null) {
                            continue;
                        }
                        if (colIx == minColIx) {
                            rowList = cell.getStringCellValue();
                        } else {
                            rowList = rowList + "','" + cell.toString();
                        }
                    }
                    if (!rowList.equals("','")) {
                        if (rowNum == 1) {
                            result = result + "('" + rowList + "')";
                        } else {
                            result = result + ",('" + rowList + "')";
                        }
                    }
                }
            }
        }
        return result;
    }

    public static void alterStringToCreateNewFile(String path, String oldString,
                                                  String newString) {
        try {
            File file = new File(path);
            long start = System.currentTimeMillis(); //开始时间
            File newFile;
            int sum;
            String filePath;
            long time;
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file)))) {
                newFile = new File("path");
                if (!newFile.exists()) {
                    newFile.createNewFile(); //不存在则创建
                }
                //创建对临时文件输出流，并追加
                try (BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(
                                new FileOutputStream(newFile, true)))) {
                    String string = null; //存储对目标文件读取的内容
                    sum = 0;
                    while ((string = br.readLine()) != null) {
                        //判断读取的内容是否包含原字符串
                        if (string.contains(oldString)) {
                            //替换读取内容中的原字符串为新字符串
                            string = new String(
                                    string.replace(oldString, newString));
                            sum++;
                        }
                        bw.write(string);
                        bw.newLine(); //添加换行
                    }
                    br.close(); //关闭流，对文件进行删除等操作需先关闭文件流操作
                    bw.close();
                }
            }
            filePath = file.getPath();
            file.delete(); //删除源文件
            newFile.renameTo(new File(filePath)); //将新文件更名为源文件
            time = System.currentTimeMillis() - start;
            System.out.println(sum + "个" + oldString + "替换成" + newString + "耗费时间:" + time);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static XSSFWorkbook getWorkbook(String sheetname, String[] title, String[][] content) {
        //新建文档实例
        XSSFWorkbook workbook = new XSSFWorkbook();

        //在文档中添加表单
        XSSFSheet sheet = workbook.createSheet(sheetname);

        //创建单元格格式，并设置居中
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFCellStyle style2 = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style2.setAlignment(HorizontalAlignment.CENTER);
        XSSFFont font = workbook.createFont();
        font.setColor(XSSFFont.COLOR_RED);//HSSFColor.VIOLET.index //字体颜色
        style2.setFont(font);
        XSSFRow titleRow = sheet.createRow(0);
        //填充标题
        for (int i = 0; i < title.length; i++) {
            //创建单元格
            XSSFCell cell = titleRow.createCell(i);
            //设置单元格内容
            cell.setCellValue(title[i]);
            //设置单元格样式
            cell.setCellStyle(style);
        }

        //填充内容
        for (int i = 0; i < content.length; i++) {
            //创建行
            XSSFRow row = sheet.createRow(i + 1);
            //遍历某一行
            for (int j = 0; j < content[i].length; j++) {
                //创建单元格
                XSSFCell cell = row.createCell(j);
                //设置单元格内容
                cell.setCellValue(content[i][j]);
                //设置单元格样式
                if("支付成功".equals(content[i][content[i].length - 1])){
                    cell.setCellStyle(style2);
                }else {
                    cell.setCellStyle(style);
                }
            }
        }

        //返回文档实例
        return workbook;
    }

    public static void main(String[] args) throws Exception {
        String s = readXlsx("C:\\Users\\rliu9\\Desktop\\test\\C34 Change Outreach 0318.xlsx");

        alterStringToCreateNewFile("C:\\Users\\rliu9\\Desktop\\test\\[20190219]_CHG0030541_OutreachStrategy_BPMO.sql"
                , "$temp$", s);
        alterStringToCreateNewFile("C:\\Users\\rliu9\\Desktop\\test\\[20190219]_CHG0030541_OutreachStrategy_DataTeam.sql"
                , "$temp$", s);

    }
}
