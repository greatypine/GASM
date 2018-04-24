package com.cnpc.pms.base.report.jasper;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.constants.Font;
import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.model.PMSColumn;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.report.jasper.JasperReportsException;
import com.cnpc.pms.base.util.StrUtil;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 动态根据queryxml查询所有的数据，生成报表
 */
public class DynamicReportHelper {
    private final static Logger log = LoggerFactory.getLogger(DynamicReportHelper.class);

    /**
     * 每一列的默认宽度
     */
    private static int DEFAULT_WIDTH = 10;


    /**
     * 根据queryxml的id获取所有的列，然后根据lstSelected筛选选中的列，获得报表内容
     * @param objQueryId  query.xml查询配置文件中的id标识
     * @param lstSelected 选中的列
     * @return 生成的报表内容
     */
    public static JasperReport getReport(String objQueryId, List<String> lstSelected) {
        PMSQuery objQuery = QueryDefinition.getQueryById(objQueryId);
        Map<String, PMSColumn> mColumnsMap = objQuery.getColumnsMap();
        List<PMSColumn> lstSelectedColumns = new ArrayList<PMSColumn>();
        for (String strKey : lstSelected) {
            lstSelectedColumns.add(mColumnsMap.get(strKey));
        }
        return doGetReport(objQuery.getHeader(), lstSelectedColumns);
    }

    /**
     * 根据查询的配置文件queryxml的Id标识获取xml中配置的所有的列
     * @param strQueryId 查询配置文件的id标识
     * @return 生成的报表内容
     */
    public static JasperReport getReport(String strQueryId) {
        PMSQuery objQuery = QueryDefinition.getQueryById(strQueryId);
        return doGetReport(objQuery.getHeader(), objQuery.getColumns());
    }

    /**
     * 生成报表
     * @param strHeader 报表表头
     * @param lstColumns 要展示的所有列
     * @return 生成的报表
     */
    private static JasperReport doGetReport(String strHeader, List<PMSColumn> lstColumns) {
        try {

            //初始化报表控件
            FastReportBuilder objReportBuilder = new FastReportBuilder();

            int i = 0;
            //设置表头样式
            Style objHeaderStyle = new Style("headerStyle");

            objHeaderStyle.setBorder(Border.THIN);
            objHeaderStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            objHeaderStyle.setHorizontalAlign(HorizontalAlign.CENTER);
            // headerStyle.setTextColor(Color.RED);
            objHeaderStyle.setFont(Font.COMIC_SANS_MEDIUM_BOLD);
            objHeaderStyle.setBackgroundColor(Color.LIGHT_GRAY);
            objHeaderStyle.setTransparent(false);

            //设置列样式
            Style objColNumStyle = new Style("colNumStyle");
            objColNumStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            objColNumStyle.setHorizontalAlign(HorizontalAlign.CENTER);
            objColNumStyle.setBorder(Border.THIN);
            //页面宽度
            int nPageWidth = 0;
            // int pageWidth = 210;
            // int pageHeight = 297;
            //循环所有的列
            for (PMSColumn objColumn : lstColumns) {
                //获取查询配置文件中设置的列宽度
                int nWidth = getWidth(objColumn.getWidth());
                //获取列头
                String strColumnTitle = StrUtil.getI18N(objColumn.getHeader());
                //获取列类型
                String strType = objColumn.getType();
                // use String only
                // if (clazz.equals(java.util.Date.class) ||
                // clazz.equals(java.sql.Date.class)
                // || clazz.equals(java.sql.Timestamp.class) ||
                // clazz.equals(Double.class)) {
                // reportBuilder.addColumn(columnTitle, column.getKey(),
                // String.class, width);
                // } else {
                // reportBuilder.addColumn(columnTitle, column.getKey(), clazz,
                // width);
                // }

                // 样式
                Style objStyle = new Style("detailStyle" + i);
                // 垂直对齐
                objStyle.setVerticalAlign(VerticalAlign.MIDDLE);
                // 横向对齐
                String strAlign = objColumn.getAlign();
                if ("CENTER".equals(strAlign.toUpperCase())) {
                    objStyle.setHorizontalAlign(HorizontalAlign.CENTER);
                } else if ("LEFT".equals(strAlign.toUpperCase())) {
                    objStyle.setHorizontalAlign(HorizontalAlign.LEFT);
                } else if ("RIGHT".equals(strAlign.toUpperCase())) {
                    objStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
                } else if ("JUSTIFY".equals(strAlign.toUpperCase())) {
                    objStyle.setHorizontalAlign(HorizontalAlign.JUSTIFY);
                } else {
                    objStyle.setHorizontalAlign(HorizontalAlign.LEFT);
                }
                // 设置border
                objStyle.setBorder(Border.THIN);
                if (i == 0) {
                    objReportBuilder.addColumn("序号", "colNum", Integer.class, 40, objColNumStyle, objHeaderStyle);
                    nPageWidth += 40;
                }
                //控件中添加一列
                objReportBuilder.addColumn(strColumnTitle, objColumn.getKey(), strType.getClass(), nWidth, objStyle, objHeaderStyle);
                nPageWidth += nWidth;

                i++;

            }
            // reportBuilder.setAllowDetailSplit(true);
            // reportBuilder.setTitle(StrUtil.getI18N(header));
            // reportBuilder.setPrintBackgroundOnOddRows(true);
            objReportBuilder.setPrintColumnNames(true);
            objReportBuilder.setIgnorePagination(true); // for Excel, we may don't
            // want pagination, just
            // a plain list
            objReportBuilder.setMargins(0, 0, 0, 0);
            //设置报表副标题样式
            Style objSubTitleStyle = new Style("subTitleStyle");
            objSubTitleStyle.setHorizontalAlign(HorizontalAlign.RIGHT);

            //设置报表标题样式
            Style objTitleStyle = new Style("titleStyle");
            objTitleStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            objTitleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
            Font objFont = new Font();
            objFont.setFontSize(20);
            objTitleStyle.setFont(objFont);
            objReportBuilder.setTitle(StrUtil.getI18N(strHeader));
            objReportBuilder.setTitleStyle(objTitleStyle);
            //副标题设置时间
            SimpleDateFormat objSdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String strToday = objSdf.format(new Date());
            objReportBuilder.setSubtitle(strToday);
            objReportBuilder.setSubtitleStyle(objSubTitleStyle);
            objReportBuilder.setUseFullPageWidth(true);
            Page objPage = new Page(10000, nPageWidth);
            objReportBuilder.setPageSizeAndOrientation(objPage);

            //生成报表
            DynamicReport objDynamicReport = objReportBuilder.build();
            @SuppressWarnings("rawtypes")
            JasperReport objJr = DynamicJasperHelper.generateJasperReport(objDynamicReport, new ClassicLayoutManager(), new HashMap());
            return objJr;
        } catch (Exception e) {
            log.error("Fail to create dynamic jasperreports template for query :{}, reason", strHeader, e);
            throw new JasperReportsException("Fail to create dynamice template for Query[ID:" + strHeader + "], reason: " + e);
        }
    }

    /**
     * 获取列宽度
     * @param strWidth 传入现有的字符串类型的宽度
     * @return 返回数值类型的宽度
     */
    private static int getWidth(String strWidth) {
        int nColumnWidth = 0;
        //判断传入字符串类型的宽度是否为空
        if (StrUtil.isNotBlank(strWidth)) {
            try {
                //是否以%号结束，如果以%号结束则宽度为百分比*10
                if (strWidth.endsWith("%")) {
                    nColumnWidth = Integer.valueOf(strWidth.substring(0, strWidth.length() - 1)) * 10;
                } else if (strWidth.endsWith("px")) {//是否以像素px结束
                    nColumnWidth = Integer.valueOf(strWidth.substring(0, strWidth.length() - 2));
                } else {
                    nColumnWidth = Integer.valueOf(strWidth);
                }
                // columnWidth = columnWidth;
            } catch (Exception e) {
                log.warn("Fail to guess width from width: {}", strWidth);
            }
        }
        //如果计算后的列宽小于0，设置为默认列宽
        if (nColumnWidth <= 0) {
            nColumnWidth = DEFAULT_WIDTH;
        }
        return nColumnWidth;
    }
}