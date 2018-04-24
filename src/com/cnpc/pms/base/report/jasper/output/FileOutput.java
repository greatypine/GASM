/*    */ package com.cnpc.pms.base.report.jasper.output;
/*    */ 
/*    */ import com.cnpc.pms.base.report.jasper.ReportWrapper;
/*    */ import net.sf.jasperreports.engine.JRAbstractExporter;
/*    */ import net.sf.jasperreports.engine.JRExporterParameter;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class FileOutput
/*    */   implements IOutput
/*    */ {
/* 12 */   protected Logger log = LoggerFactory.getLogger(super.getClass());
/*    */   protected String fileName;
/*    */ 
/*    */   public FileOutput(String fileName)
/*    */   {
/* 16 */     this.fileName = fileName;
/*    */   }
/*    */ 
/*    */   public void beforeExport(ReportWrapper wrapper) {
/* 20 */     wrapper.getExporter().setParameter(JRExporterParameter.OUTPUT_FILE_NAME, this.fileName);
/* 21 */     wrapper.beforeExport(this);
/*    */   }
/*    */ 
/*    */   public void afterExport()
/*    */   {
/*    */   }
/*    */ 
/*    */   public String getFileName() {
/* 29 */     return this.fileName;
/*    */   }
/*    */ }

/* Location:           D:\资料\中信国安\国安养老\国安养老项目\Java\JavaCRM\lib\pmsbase-1.2.0-SNAPSHOT.jar
 * Qualified Name:     com.cnpc.pms.base.report.jasper.output.FileOutput
 * JD-Core Version:    0.5.3
 */