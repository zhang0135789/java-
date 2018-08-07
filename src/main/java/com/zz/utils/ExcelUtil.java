package com.zz.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;


/**
 * @Author: zz
 * @Description: ExcelUtils
 * @Date: 2018年7月27日19:56:07
 * @Modified By
 */
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * generate excel
     * @param inputText
     */
    public static HSSFWorkbook map2excel(Map<Integer, List> inputText  ,String title) {
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet = wb.createSheet(title);
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue(title);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        int rownum = 1;
        Iterator it = inputText.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            List<String> list = (List<String>) entry.getValue();
            HSSFRow row2 = sheet.createRow(rownum);
            for(int i = 0 ; i < list.size() ; i++) {
                row2.createCell(i).setCellValue(list.get(i));
            }
            rownum++;
        }
        return wb;

    }

    /**
     *  read excel
     * @param wb
     */
    public static Map<String,List> excel2map(Workbook wb , int sheetIndex) {
        Map<String,List> sheetMap = new HashMap<>(); //sheet
        List<String> rowList = null; //rows


        for(int i = 0; i < sheetIndex ; i++) {
            Sheet sheet = wb.getSheetAt(i);
            int rownum = 0;
            for(Row row : sheet) {
                rowList = new ArrayList();
                int rows = row.getPhysicalNumberOfCells();



                for(int j =0 ; j<rows ;j++) {
                    row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    String str = row.getCell(j).getStringCellValue();
                    rowList.add(str);
                }
                sheetMap.put("sheet"+i+"row"+rownum++ ,rowList);
            }

        }

        return sheetMap;
    }

    /**
     * generate excle file
     * @param is
     * @param filename
     * @return
     */
    public static Workbook inputStream2Excel(FileInputStream is ,String filename) throws Exception {
        Workbook wb = null;

        if(filename==null){
            return null;
        }
        String extString = filename.substring(filename.lastIndexOf("."));
        if(".xls".equals(extString)){
            return wb = new HSSFWorkbook(is);
        }else if(".xlsx".equals(extString)){
            return wb = new XSSFWorkbook(is);
        }else{
            return wb = null;
        }

    }





    public static void main(String arg[]) {
        File fs = new File("C:\\Users\\Administrator\\Desktop\\list_1R.xlsx");
        try {
            InputStream is = new FileInputStream(fs);
            Workbook wb = new XSSFWorkbook(is);


            int index = wb.getNumberOfSheets();
            System.out.println(index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
