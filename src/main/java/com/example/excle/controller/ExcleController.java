package com.example.excle.controller;

import com.example.excle.pojo.User;
import com.example.excle.service.UserService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
public class ExcleController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/import",method = RequestMethod.POST)
    public String importUser(@RequestParam(name="file") MultipartFile file) throws Exception {
        //1.解析Excel
        //1.1.根据Excel文件创建工作簿
        Workbook wb = new XSSFWorkbook(file.getInputStream());
        //1.2.获取Sheet
        Sheet sheet = wb.getSheetAt(0);//参数：索引
        //1.3.获取Sheet中的每一行，和每一个单元格
        //2.获取用户数据列表
        List<User> list = new ArrayList<>();
        System.out.println(sheet.getLastRowNum());
        for (int rowNum = 2; rowNum<= sheet.getLastRowNum() ;rowNum ++) {
            Row row = sheet.getRow(rowNum);//根据索引获取每一个行
            Object [] values = new Object[row.getLastCellNum()];
            for(int cellNum=0;cellNum< row.getLastCellNum(); cellNum ++) {
                Cell cell = row.getCell(cellNum);
                Object value = userService.getCellValue(cell);
                values[cellNum] = value;
            }
            //在User的构造函数中，根据表格值顺序设置User对应的属性值
            User user = new User(values);
            list.add(user);
        }
        //3.调用服务层批量保存用户
        System.out.println("======="+list.toString()+"=========");
        userService.saveAll(list);
        return "SUCCESS";
    }


    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public String export(HttpServletResponse response) throws Exception {
        //1.构造数据,某个月的用户数据
        List<User> list =userService.findAll();
        System.out.println(list.toString()+"+++++++++++++++++++++++++++++++++++++++++++++++");
        //2.创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //3.构造sheet
        String[] titles = {"编号", "姓名", "密码"};
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        AtomicInteger headersAi = new AtomicInteger();
        for (String title : titles) {
            Cell cell = row.createCell(headersAi.getAndIncrement());
            cell.setCellValue(title);
        }
        AtomicInteger datasAi = new AtomicInteger(1);
        Cell cell = null;
        for (User user : list) {
            Row dataRow = sheet.createRow(datasAi.getAndIncrement());
            //编号
            cell = dataRow.createCell(0);
            cell.setCellValue(user.getId());
            //姓名
            cell = dataRow.createCell(1);
            cell.setCellValue(user.getUserName());
            //密码
            cell = dataRow.createCell(2);
            cell.setCellValue(user.getPassword());
        }
        String fileName = URLEncoder.encode("人员信息.xlsx", "UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("ISO8859-1")));
        response.setHeader("filename", fileName);
        FileOutputStream pis = new FileOutputStream("E:\\test.xlsx");
        workbook.write(pis);
        pis.close();
        return "SUCCESS";
    }

}
