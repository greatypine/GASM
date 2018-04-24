package com.cnpc.pms.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 抽象Excel2007读取器，excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 * xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低
 * 内存的耗费，特别使用于大数据量的文件。
 *
 */
public class Excel2007Reader extends DefaultHandler {

    private SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;

    private int sheetIndex = -1;
    private Map<Integer,String> rowlist = new HashMap<Integer, String>();

    private int curRow = 0;
    private int curCol = 0;

    //定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
    private String preRef = null, ref = null;
    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    private String maxRef = null;

    private CellDataType nextDataType = CellDataType.SSTINDEX;
    private final DataFormatter formatter = new DataFormatter();
    private short formatIndex;
    private String formatString;

    private StylesTable stylesTable;

    //用一个enum表示单元格可能的数据类型
    enum CellDataType{
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }

    private IRowReader rowReader;

    public void setRowReader(IRowReader rowReader){
        this.rowReader = rowReader;
    }

    /**只遍历一个电子表格，其中sheetId为要遍历的sheet索引，从1开始，1-3
     */
    public void processOneSheet(String filename,int sheetId) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);

        // 根据 rId# 或 rSheet# 查找sheet
        InputStream sheet2 = r.getSheet("rId"+sheetId);
        sheetIndex++;
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     * 遍历工作簿中所有的电子表格
     */
    public void process(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        stylesTable = r.getStylesTable();
        while (sheets.hasNext()) {
            curRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst)
            throws SAXException {
        XMLReader parser = XMLReaderFactory
                .createXMLReader("com.sun.org.apache.xerces.internal.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
// c => cell
        if(name.equals("c")) {
            //前一个单元格的位置
            if(preRef == null){
                preRef = attributes.getValue("r");
            }else{
                preRef = ref;
            }
            //当前单元格的位置
            ref = attributes.getValue("r");

            this.setNextDataType(attributes);

            // Figure out if the value is an index in the SST
            String cellType = attributes.getValue("t");
            if(cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }

        }
        // Clear contents cache
        lastContents = "";
    }

    /**
     * 根据element属性设置数据类型
     * @param attributes
     */
    public void setNextDataType(Attributes attributes){

        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");
        if ("b".equals(cellType)){
            nextDataType = CellDataType.BOOL;
        }else if ("e".equals(cellType)){
            nextDataType = CellDataType.ERROR;
        }else if ("inlineStr".equals(cellType)){
            nextDataType = CellDataType.INLINESTR;
        }else if ("s".equals(cellType)){
            nextDataType = CellDataType.SSTINDEX;
        }else if ("str".equals(cellType)){
            nextDataType = CellDataType.FORMULA;
        }
        if (cellStyleStr != null){
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            if ("m/d/yy" == formatString){
                nextDataType = CellDataType.DATE;
                //full format is "yyyy-MM-dd hh:mm:ss.SSS";
                formatString = "yyyy-MM-dd";
            }
            if (formatString == null){
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }

    public void endElement(String uri, String localName, String name)
            throws SAXException {
        // Process the last contents as required.

        // Do now, as characters() may be called more than once
        if (nextIsString) {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            nextIsString = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if(name.equals("c") && "".equals(lastContents)){
            rowlist.put(curCol, "");
            curCol++;
        }else if (name.equals("v") && !"".equals(lastContents)) {
            String value = this.getDataValue(lastContents.trim(), "");
            //补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                int len = countNullCell(ref, preRef);
                for (int i = 0; i < len; i++) {
                    rowlist.put(curCol, "");
                    curCol++;
                }
            }
            rowlist.put(curCol, value);
            curCol++;
        } else {
            //如果标签名称为 row，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                String value = "";
                //默认第一行为表头，以该行单元格数目为最大数目
                if (curRow == 0) {
                    maxRef = ref;
                }
                //补全一行尾部可能缺失的单元格
                if (maxRef != null) {
                    int len = countNullCell(maxRef, ref);
                    for (int i = 0; i <= len; i++) {
                        rowlist.put(curCol, "");
                        curCol++;
                    }
                }
                rowReader.getRows(sheetIndex,curRow,rowlist);
                curRow++;
                //一行的末尾重置一些数据
                rowlist = new HashMap<Integer, String>();
                curCol = 0;
                preRef = null;
                ref = null;
            }
        }
    }

    /**
     * 根据数据类型获取数据
     * @param value
     * @param thisStr
     * @return
     */
    public String getDataValue(String value, String thisStr){
        switch (nextDataType)
        {
            //这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL:
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                thisStr = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA:
                thisStr = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                thisStr = rtsi.toString();
                rtsi = null;
                break;
            case SSTINDEX:
                String sstIndex = value.toString();
                thisStr = value.toString();
                break;
            case NUMBER:
                if (formatString != null){
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                }else{
                    thisStr = value;
                }
                thisStr = thisStr.replace("_", "").trim();
                break;
            case DATE:
                try{
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                }catch(NumberFormatException ex){
                    thisStr = value.toString();
                }
                thisStr = thisStr.replace(" ", "");
                break;
            default:
                thisStr = "";
                break;
        }
        return thisStr;
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        //得到单元格内容的值
        lastContents += new String(ch, start, length);
    }

    /**
     * 计算两个单元格之间的单元格数目(同一行)
     * @param ref
     * @param preRef
     * @return
     */
    public int countNullCell(String ref, String preRef){
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");

        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);

        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0]-letter_1[0])*26*26 + (letter[1]-letter_1[1])*26 + (letter[2]-letter_1[2]);
        return res-1;
    }

    /**
     * 字符串的填充
     * @param str
     * @param len
     * @param let
     * @param isPre
     * @return
     */
    String fillChar(String str, int len, char let, boolean isPre){
        int len_1 = str.length();
        if(len_1 <len){
            if(isPre){
                for(int i=0;i<(len-len_1);i++){
                    str = let+str;
                }
            }else{
                for(int i=0;i<(len-len_1);i++){
                    str = str+let;
                }
            }
        }
        return str;
    }
}

