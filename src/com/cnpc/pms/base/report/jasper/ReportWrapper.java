/*    */ package com.cnpc.pms.base.report.jasper;
/*    */ 
/*    */ import com.cnpc.pms.base.report.jasper.output.IOutput;
/*    */ import java.io.File;
/*    */ import net.sf.jasperreports.engine.JRAbstractExporter;
/*    */ import net.sf.jasperreports.engine.export.JExcelApiExporter;
/*    */ import net.sf.jasperreports.engine.export.JRCsvExporter;
/*    */ import net.sf.jasperreports.engine.export.JRPdfExporter;
/*    */ import net.sf.jasperreports.engine.export.JRRtfExporter;
/*    */ import net.sf.jasperreports.engine.export.JRTextExporter;
/*    */ import net.sf.jasperreports.engine.export.JRTextExporterParameter;
/*    */ import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
/*    */ 
/*    */ public class ReportWrapper
/*    */ {
/*    */   private String suffix;
/*    */   private String contentType;
/*    */   protected JRAbstractExporter exporter;
/*    */ 
/*    */   public ReportWrapper(String suffix, String contentType, JRAbstractExporter exporter)
/*    */   {
/* 25 */     this.suffix = suffix;
/* 26 */     this.contentType = contentType;
/* 27 */     this.exporter = exporter;
/*    */   }
/*    */ 
/*    */   public String getContentType() {
/* 31 */     return this.contentType;
/*    */   }
/*    */ 
/*    */   public String getSuffix() {
/* 35 */     return this.suffix;
/*    */   }
/*    */ 
/*    */   public JRAbstractExporter getExporter() {
/* 39 */     return this.exporter;
/*    */   }
/*    */ 
/*    */   public void beforeExport(IOutput ouput)
/*    */   {
/*    */   }
/*    */ 
/*    */   public static ReportWrapper getReportWrapper(int type) {
/* 47 */     ReportWrapper reportWrapper = null;
/* 48 */     switch (type)
/*    */     {
/*    */     case 4:
/* 50 */       reportWrapper = new ReportWrapper("xls", "application/excel", new JExcelApiExporter())
/*    */       {
/*    */         public void beforeExport(IOutput output) {
/* 53 */           String fileName = output.getFileName();
/* 54 */           this.exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[] { fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.lastIndexOf(".")) });
/*    */         }
/*    */       };
/* 60 */       break;
/*    */     case 7:
/* 62 */       reportWrapper = new ReportWrapper("txt", "application/txt", new JRTextExporter())
/*    */       {
/*    */         public static final int PAGE_WIDTH = 640;
/*    */         public static final int PAGE_HEIGHT = 524;
/*    */         public static final int CHARACTER_WIDTH = 10;
/*    */         public static final int CHARACTER_HEIGHT = 12;
/*    */ 
/*    */         public void beforeExport(IOutput output) {
/* 70 */           this.exporter.setParameter(JRTextExporterParameter.PAGE_WIDTH, Integer.valueOf(640));
/* 71 */           this.exporter.setParameter(JRTextExporterParameter.PAGE_HEIGHT, Integer.valueOf(524));
/* 72 */           this.exporter.setParameter(JRTextExporterParameter.CHARACTER_WIDTH, new Float(10.0F));
/* 73 */           this.exporter.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, new Float(12.0F));
/*    */         }
/*    */ 
/*    */       };
/* 77 */       break;
/*    */     case 6:
/* 79 */       reportWrapper = new ReportWrapper("csv", "application/csv", new JRCsvExporter());
/* 80 */       break;
/*    */     case 1:
/* 82 */       reportWrapper = new ReportWrapper("pdf", "application/pdf", new JRPdfExporter());
/* 83 */       break;
/*    */     case 3:
/* 85 */       reportWrapper = new ReportWrapper("doc", "application/rtf", new JRRtfExporter());
/*    */     case 2:
/*    */     case 5:
/*    */     }
/*    */ 
/* 90 */     return reportWrapper;
/*    */   }
/*    */ }

/* Location:           D:\资料\中信国安\国安养老\国安养老项目\Java\JavaCRM\lib\pmsbase-1.2.0-SNAPSHOT.jar
 * Qualified Name:     com.cnpc.pms.base.report.jasper.ReportWrapper
 * JD-Core Version:    0.5.3
 */