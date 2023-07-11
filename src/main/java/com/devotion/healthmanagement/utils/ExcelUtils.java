package com.devotion.healthmanagement.utils;

import com.devotion.healthmanagement.entity.dto.UserIllness;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ExcelUtils {
    public List<UserIllness> toList(File file,Integer id) throws IOException,  org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        XSSFWorkbook wb = null;
        try {
            List<UserIllness> userIllnessList = new ArrayList<>();
            wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(0);//获取第一个sheet
            int lastRowNum = sheet.getLastRowNum(); //获取表格内容的最后一行的行数
            log.info("文件共" + lastRowNum + "行");
            for (int i = 1; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);//获取每一行
                XSSFCell cell0 = row.getCell(0);
                cell0.setCellType(CellType.STRING);

                Integer ill_id = Integer.parseInt(cell0.getStringCellValue());
                Date updateTime = row.getCell(1).getDateCellValue();
                String illName = row.getCell(2).getStringCellValue();
                String illRx = row.getCell(3).getStringCellValue();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                UserIllness userIllness = new UserIllness();
                userIllness.setId(id);
                userIllness.setIllId(ill_id);
                userIllness.setUpdateTime(sdf.format(updateTime));
                userIllness.setIllName(illName);
                userIllness.setIllRx(illRx);

                System.out.println(userIllness);
                userIllnessList.add(userIllness);
            }
            return userIllnessList;
        } finally {
            wb.close();
        }
    }
}
