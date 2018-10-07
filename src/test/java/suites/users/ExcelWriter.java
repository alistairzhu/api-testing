package suites.users;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExcelWriter {

    private static String[] columns = {"time", "txid", "Flag", "valueOut", "Price", "Total", "quantityNow", "totalCashNow"};
    private static List<Transaction> theTransaction =  new ArrayList();



    // Initializing employees data to insert into the excel file

    public static void main(String[] args) throws IOException, InvalidFormatException {

       // theTransaction.add(new Transaction("111111", "rajeev@example.com","true", 1200000.0));

       // writeToExcel( theTransaction , "dsfsd");

    }


    public static void writeToExcel(List<Transaction> theTransaction , String  account) throws IOException, InvalidFormatException {


        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        // CreationHelper helps us create instances of various things like DataFormat,  Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Transaction");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        double quantityNow = 0;
        double totalCashNow = 0;
        double lastTrans = 0;
        for(Transaction transaction: theTransaction) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(transaction.getTime());
            row.createCell(1).setCellValue(transaction.getTxid());
            row.createCell(2).setCellValue(transaction.getBuyFlag());
            double valueOut = 0;
            if("-".equals(transaction.getBuyFlag())){
                valueOut = -transaction.getValueOut();
            }else {valueOut = transaction.getValueOut();}
            row.createCell(3).setCellValue(valueOut);
            String priceString = transaction.getPrice();
            row.createCell(4).setCellValue(transaction.getPrice());
            double priceValue = Double.parseDouble(priceString)*valueOut;
            row.createCell(5).setCellValue(priceValue);
            quantityNow = quantityNow + valueOut;
            row.createCell(6).setCellValue(quantityNow);
            totalCashNow = lastTrans - priceValue;
            row.createCell(7).setCellValue(totalCashNow);
          //  row.createCell(8).setCellValue(totalCashNow + valueOut * priceNow);
            lastTrans = totalCashNow;
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String addName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("c:\\temp\\" + account + "-" + addName + ".xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();
    }



}