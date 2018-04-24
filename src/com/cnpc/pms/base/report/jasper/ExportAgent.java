/*     */ package com.cnpc.pms.base.report.jasper;
/*     */ 
/*     */  
import com.cnpc.pms.base.report.jasper.output.FileOutput;
/*     */ import com.cnpc.pms.base.report.jasper.output.HttpOutput;
/*     */ import com.cnpc.pms.base.report.jasper.output.IOutput;
/*     */ import com.cnpc.pms.base.util.PropertiesUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import net.sf.jasperreports.engine.JRAbstractExporter;
/*     */ import net.sf.jasperreports.engine.JRException;
/*     */ import net.sf.jasperreports.engine.JRExporterParameter;
/*     */ import net.sf.jasperreports.engine.JasperFillManager;
/*     */ import net.sf.jasperreports.engine.JasperPrint;
/*     */ import net.sf.jasperreports.engine.JasperReport;
/*     */ import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ExportAgent
/*     */ {
/*  27 */   private Logger log = LoggerFactory.getLogger(super.getClass());
/*     */   private JasperReport report;
/*     */   private ReportWrapper reportWrapper;
/*     */   private IOutput output;
/*     */   private List<?> data;
/*  32 */   private Map<String, Object> param = new HashMap();
/*     */ 
/*     */   public ExportAgent(int type) {
/*  35 */     this.reportWrapper = ReportWrapper.getReportWrapper(type);
/*     */   }
/*     */ 
/*     */   public ExportAgent(ReportWrapper reportWrapper) {
/*  39 */     this.reportWrapper = reportWrapper;
/*     */   }
/*     */ 
/*     */   public void setTemplate(String template) {
/*     */     try {
/*  44 */       this.report = JasperReportsHelper.getReport(template);
/*     */     } catch (JasperReportsException e) {
/*  46 */       this.log.error("Fail to get JasperReport from template: {}", template, e);
/*  47 */       setErrorReport(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setDynamicReport(String queryId)
/*     */   {
/*     */     try
/*     */     {
/*  59 */       this.report = DynamicReportHelper.getReport(queryId);
/*     */     } catch (JasperReportsException e) {
/*  61 */       this.log.error("Fail to get Dyanmic JasperReport from query: {}", queryId, e);
/*  62 */       setErrorReport(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDynamicReport(String queryId, List<String> selected) {
/*     */     try {
/*  68 */       this.report = DynamicReportHelper.getReport(queryId, selected);
/*     */     } catch (JasperReportsException e) {
/*  70 */       this.log.error("Fail to get Dyanmic JasperReport from query: {}", queryId, e);
/*  71 */       setErrorReport(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setReport(JasperReport report) {
/*  76 */     this.report = report;
/*     */   }
/*     */ 
/*     */   private void setErrorReport(String message) {
/*  80 */     this.param.put("error", message);
/*  81 */     this.report = JasperReportsHelper.ERROR_REPORT;
/*     */   }
/*     */ 
/*     */   public void setData(List<?> data) {
/*  85 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public void setOutput(String fileName) {
/*  89 */     String file = PropertiesUtil.getValue("file.root") + getFileName(fileName);
/*  90 */     this.output = new FileOutput(file);
/*     */   }
/*     */ 
/*     */   public void setOutput(HttpServletResponse response, String fileName) {
/*  94 */     this.output = new HttpOutput(response, getFileName(fileName));
/*     */   }
/*     */ 
/*     */   private String getFileName(String fileName) {
/*  98 */     return fileName + "." + this.reportWrapper.getSuffix();
/*     */   }
/*     */ 
/*     */   public void addParams(Map<String, Object> param) {
/* 102 */     this.param.putAll(param);
/*     */   }
/*     */ 
/*     */   public void export() throws JRException {
/* 106 */     if ((this.report == null) || (this.reportWrapper == null) || (this.output == null)) {
/* 107 */       this.log.error("Please initialize correctly before export. Report: {}, Wrapper: {}, Output: {} ", new Object[] { this.report, this.reportWrapper, this.output });
/*     */     }
/*     */ 
/* 111 */     this.output.beforeExport(this.reportWrapper);
/*     */ 
/* 113 */     JRAbstractExporter exporter = this.reportWrapper.getExporter();
/*     */     JasperPrint jp;
/* 116 */     if (this.data != null)
/* 117 */       jp = JasperFillManager.fillReport(this.report, this.param, new JRBeanCollectionDataSource(this.data));
/*     */     else {
/* 119 */       jp = JasperFillManager.fillReport(this.report, this.param);
/*     */     }
/* 121 */     exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
/*     */ 
/* 123 */     exporter.exportReport();
/*     */ 
/* 125 */     this.output.afterExport();
/*     */   }
/*     */ }

/* Location:           D:\资料\中信国安\国安养老\国安养老项目\Java\JavaCRM\lib\pmsbase-1.2.0-SNAPSHOT.jar
 * Qualified Name:     com.cnpc.pms.base.report.jasper.ExportAgent
 * JD-Core Version:    0.5.3
 */