package com.cnpc.pms.base.extension;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperReport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Border;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

import com.cnpc.pms.base.query.QueryDefinition;
import com.cnpc.pms.base.query.model.PMSColumn;
import com.cnpc.pms.base.query.model.PMSQuery;
import com.cnpc.pms.base.report.jasper.JasperReportsException;
import com.cnpc.pms.base.util.StrUtil;

public class RipedDynamicReportHelper {
	private final static Logger log = LoggerFactory.getLogger(RipedDynamicReportHelper.class);

	private static int DEFAULT_WIDTH = 10;
	

	public static JasperReport getReport(String queryId, List<String> selected) {
		PMSQuery query = QueryDefinition.getQueryById(queryId);
		Map<String, PMSColumn> map = query.getColumnsMap();
		List<PMSColumn> selectedColumns = new ArrayList<PMSColumn>();
		for (String key : selected) {
			selectedColumns.add(map.get(key));
		}
		return doGetReport(query.getHeader(), selectedColumns);
	}

	public static JasperReport getReport(String queryId) {
		PMSQuery query = QueryDefinition.getQueryById(queryId);
		return doGetReport(query.getHeader(), query.getColumns());
	}


	private static JasperReport doGetReport(String header, List<PMSColumn> columns) {
		try {
			
			FastReportBuilder reportBuilder = new FastReportBuilder();

			int i = 0;
			Style headerStyle = new Style("headerStyle");

			headerStyle.setBorder(Border.THIN);
			headerStyle.setVerticalAlign(VerticalAlign.MIDDLE);
			headerStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			// headerStyle.setTextColor(Color.RED);
			headerStyle.setFont(Font.COMIC_SANS_MEDIUM_BOLD);
			headerStyle.setBackgroundColor(Color.LIGHT_GRAY);
			headerStyle.setTransparent(false);

			Style colNumStyle = new Style("colNumStyle");
			colNumStyle.setVerticalAlign(VerticalAlign.MIDDLE);
			colNumStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			colNumStyle.setBorder(Border.THIN);

			int pageWidth = 0;
			// int pageWidth = 210;
			// int pageHeight = 297;
			for (PMSColumn column : columns) {
				int width = getWidth(column.getWidth());
				String columnTitle = StrUtil.getI18N(column.getHeader());
				String type = column.getType();
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
				Style style = new Style("detailStyle" + i);
				// 垂直对齐
				style.setVerticalAlign(VerticalAlign.MIDDLE);
				// 横向对齐
				String align = column.getAlign();
				if ("CENTER".equals(align.toUpperCase())) {
					style.setHorizontalAlign(HorizontalAlign.CENTER);
				} else if ("LEFT".equals(align.toUpperCase())) {
					style.setHorizontalAlign(HorizontalAlign.LEFT);
				} else if ("RIGHT".equals(align.toUpperCase())) {
					style.setHorizontalAlign(HorizontalAlign.RIGHT);
				} else if ("JUSTIFY".equals(align.toUpperCase())) {
					style.setHorizontalAlign(HorizontalAlign.JUSTIFY);
				} else {
					style.setHorizontalAlign(HorizontalAlign.LEFT);
				}
				// 设置border
				style.setBorder(Border.THIN);
				int fixWidth = width * 100;
				if (i == 0) {
					reportBuilder.addColumn("序号", "colNum", Integer.class, 40, colNumStyle, headerStyle);
					pageWidth += 40;
				}
				reportBuilder.addColumn(columnTitle, column.getKey(), type.getClass(), width, style, headerStyle);
				pageWidth += width;

				i++;

			}
			// reportBuilder.setAllowDetailSplit(true);
			// reportBuilder.setTitle(StrUtil.getI18N(header));
			// reportBuilder.setPrintBackgroundOnOddRows(true);
			reportBuilder.setPrintColumnNames(true);
			reportBuilder.setIgnorePagination(true); // for Excel, we may don't
														// want pagination, just
														// a plain list
			reportBuilder.setMargins(0, 0, 0, 0);
			Style subTitleStyle = new Style("subTitleStyle");
			subTitleStyle.setHorizontalAlign(HorizontalAlign.RIGHT);
			Style titleStyle = new Style("titleStyle");
			titleStyle.setVerticalAlign(VerticalAlign.MIDDLE);
			titleStyle.setHorizontalAlign(HorizontalAlign.CENTER);
			Font font = new Font();
			font.setFontSize(20);
			titleStyle.setFont(font);
			reportBuilder.setTitle(StrUtil.getI18N(header));
			reportBuilder.setTitleStyle(titleStyle);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String todayStr = sdf.format(new Date());
			reportBuilder.setSubtitle(todayStr);
			reportBuilder.setSubtitleStyle(subTitleStyle);
			reportBuilder.setUseFullPageWidth(true);
			Page page = new Page(10000, pageWidth);
			reportBuilder.setPageSizeAndOrientation(page);
			
			
			DynamicReport dynamicReport = reportBuilder.build();
			@SuppressWarnings("rawtypes")
			JasperReport jr = DynamicJasperHelper.generateJasperReport(dynamicReport, new ClassicLayoutManager(), new HashMap());
			return jr;
		} catch (Exception e) {
			log.error("Fail to create dynamic jasperreports template for query :{}, reason", header, e);
			throw new JasperReportsException("Fail to create dynamice template for Query[ID:" + header + "], reason: " + e);
		}
	}

	private static int getWidth(String width) {
		int columnWidth = 0;
		if (StrUtil.isNotBlank(width)) {
			try {
				if (width.endsWith("%")) {
					columnWidth = Integer.valueOf(width.substring(0, width.length() - 1)) * 10;
				} else if (width.endsWith("px")) {
					columnWidth = Integer.valueOf(width.substring(0, width.length() - 2));
				} else {
					columnWidth = Integer.valueOf(width);
				}
				// columnWidth = columnWidth;
			} catch (Exception e) {
				log.warn("Fail to guess width from width: {}", width);
			}
		}
		if (columnWidth <= 0) {
			columnWidth = DEFAULT_WIDTH;
		}
		return columnWidth;
	}
}