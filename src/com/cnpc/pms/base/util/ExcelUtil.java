// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ExcelUtil.java

package com.cnpc.pms.base.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil
{
    public static class ExcelOutputFile
    {

        public void appendLine(List values)
            throws IOException
        {
            if(sheet == null)
                sheet = workbook.createSheet(sheetName);
            Row row = sheet.createRow(index);
            for(int i = 0; i < values.size(); i++)
            {
                Cell cell = row.createCell(i);
                cell.setCellValue((String)values.get(i));
            }

            index++;
        }

        public void close()
            throws IOException
        {
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            closed = true;
        }

        protected void finalize()
            throws IOException
        {
            if(!closed)
                close();
        }

        private Workbook workbook;
        private FileOutputStream fileOutputStream;
        private String sheetName;
        private Sheet sheet;
        private int index;
        private boolean closed;

        public ExcelOutputFile(String filePath)
            throws Exception
        {
            sheetName = "sheet1";
            index = 0;
            closed = false;
            int version = ExcelUtil.getExcelVersion(filePath.toLowerCase());
            fileOutputStream = new FileOutputStream(filePath);
            if(version == 2003)
                workbook = new HSSFWorkbook();
            else
            if(version == 2007)
                workbook = new XSSFWorkbook();
        }

        public ExcelOutputFile(String filePath, String sheetName)
            throws Exception
        {
            this(filePath);
            this.sheetName = sheetName;
        }
    }

    public static class ExcelFile
    {

        public List getSheetData(String sheetName)
        {
            return ExcelUtil.getSheetData(workbook, sheetName);
        }

        private Workbook workbook;

        public ExcelFile(String filePath)
            throws Exception
        {
            workbook = ExcelUtil.getWorkbook(filePath);
        }
    }


    public ExcelUtil()
    {
    }

    public static Workbook getWorkbook(String filePath)
        throws Exception
    {
        int version = getExcelVersion(filePath.toLowerCase());
        InputStream fis = new FileInputStream(filePath);
        Workbook workbook = null;
        if(version == 2003)
            workbook = new HSSFWorkbook(fis);
        else
        if(version == 2007)
            workbook = new XSSFWorkbook(fis);
        fis.close();
        return workbook;
    }

    public static List getSheetData(Workbook workbook, String sheetName)
    {
        Sheet sheet = null;
        if(null == sheetName)
            sheet = workbook.getSheetAt(0);
        else
            sheet = workbook.getSheet(sheetName);
        int lastRowNum = sheet.getLastRowNum();
        List result = new ArrayList(lastRowNum);
        for(int i = 0; i <= lastRowNum; i++)
        {
            Row row = sheet.getRow(i);
            Object values[];
            if(row == null)
                values = null;
            else
                values = getValues(row);
            result.add(((Object) (values)));
        }

        return result;
    }

    public static void writeData(Sheet sheet, int index, List values)
    {
        Row row = sheet.createRow(index);
        for(int i = 0; i < values.size(); i++)
        {
            Cell cell = row.createCell(i);
            cell.setCellValue((String)values.get(i));
        }

    }

    private static int getExcelVersion(String filePath)
        throws Exception
    {
        int version;
        if(filePath.endsWith(".xls"))
            version = 2003;
        else
        if(filePath.endsWith(".xlsx"))
            version = 2007;
        else
            throw new Exception("invalid excel suffix");
        return version;
    }

    private static Object[] getValues(Row row)
    {
        int cellnum = row.getLastCellNum();
        if(cellnum < 0)
            return null;
        Object values[] = new Object[cellnum];
        for(int j = 0; j < cellnum; j++)
        {
            Cell cell = row.getCell(j);
            if(cell != null)
                values[j] = getCellValue(cell);
        }

        return values;
    }

    public static Object getCellValue(Cell cell)
    {
        Object value = "";
        int cellType = cell.getCellType();
        switch(cellType)
        {
        case 1: // '\001'
            value = cell.getStringCellValue();
            break;

        case 0: // '\0'
            String s = String.valueOf(cell.getNumericCellValue());
            if(s.endsWith(".0"))
                s = s.substring(0, s.length() - 2);
            value = s;
            break;

        case 4: // '\004'
            value = Boolean.toString(cell.getBooleanCellValue());
            break;

        case 2: // '\002'
        case 3: // '\003'
        default:
            value = "";
            break;
        }
        return value;
    }

    private static final String EXCEL2003_SUFFIX = ".xls";
    private static final String EXCEL2007_SUFFIX = ".xlsx";
    private static final int EXCEL2003 = 2003;
    private static final int EXCEL2007 = 2007;

}
