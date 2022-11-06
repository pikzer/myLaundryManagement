package th.ac.ku.mylaundry.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import th.ac.ku.mylaundry.model.Order;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportWriter {


    public static void writeReport(String stDate, String enDate, ArrayList<Order> orderArrayList) throws IOException {

        String[] columns = {"ลำดับ","ไอดี","วันที่", "บริการ","ออร์เดอร์", "วันรับผ้า", "เวลารับผ้า", "วันส่งผ้า" , "เวลาส่งผ้า",
                "ที่อยู่", "ผู้รับผิดชอบ", "ผู้รับส่ง", "ชำระ", "การชำระ", "ค่ารับ", "ค่าส่ง","ราคาสุทธิ", "สถานะ"};

        File dir = new File("report") ;
        if(dir.mkdir()){

        }
        double total = 0 ;
        String fileName = "report/report-"+stDate+"-"+enDate+".xlsx" ;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("report-"+stDate+"-"+enDate+".xlsx");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 18);
        headerFont.setColor(IndexedColors.BLUE.getIndex());
        CreationHelper createHelper = workbook.getCreationHelper();
        CellStyle headerCellStyle = workbook.createCellStyle();
//        sheet.addMergedRegion(new CellRangeAddress(0,0,0,2)) ;
//        sheet.addMergedRegion(new CellRangeAddress(1,1,0,2)) ;
        headerCellStyle.setFont(headerFont);
        Row row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue(
                createHelper.createRichTextString("รายงาน-"+stDate+"-"+enDate));
        Row headerRow = sheet.createRow(1);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
        int rowNum = 2 ;
        int k = 1;
        DecimalFormat f = new DecimalFormat("#0.00");

        for (Order or:orderArrayList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(String.valueOf(k));
            k++;
            row.createCell(1).setCellValue(or.getId());
            row.createCell(2).setCellValue(or.getCreated_at());
            row.createCell(3).setCellValue(or.getService());
            row.createCell(4).setCellValue(or.getName());
            row.createCell(5).setCellValue(or.getPickDate());
            row.createCell(6).setCellValue(or.getPickTime());
            row.createCell(7).setCellValue(or.getDeliDate());
            row.createCell(8).setCellValue(or.getDeliTime());
            row.createCell(9).setCellValue(or.getAddress());
            row.createCell(10).setCellValue(or.getResponder());
            row.createCell(11).setCellValue(or.getDeliver());
            row.createCell(12).setCellValue(or.getPayStatus());
            row.createCell(13).setCellValue(or.getPayMethod());
            row.createCell(14).setCellValue(f.format(or.getPickSerCharge()));
            row.createCell(15).setCellValue(f.format(or.getDeliSerCharge()));
            row.createCell(16).setCellValue(f.format(or.getTotal()));
            total+=or.getTotal();
            row.createCell(17).setCellValue(or.getStatus());
        }

        Row row3 = sheet.createRow(rowNum+2);
        row3.createCell(16).setCellValue("จำนวนรายการ");
        row3.createCell(17).setCellValue(k-1);

        Row row2 = sheet.createRow(rowNum+3);
        row2.createCell(16).setCellValue("รายได้รวม");
        row2.createCell(17).setCellValue(f.format(total));
//        row2.createCell()

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }


        FileOutputStream fileOut = new FileOutputStream(fileName);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        Desktop.getDesktop().open(new File(fileName));
    }
}
