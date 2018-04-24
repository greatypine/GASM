/*    */ package com.cnpc.pms.base.report.jasper.output;
/*    */ 
/*    */ import com.cnpc.pms.base.report.jasper.ReportWrapper;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.URLEncoder;
/*    */ import javax.servlet.http.HttpServletResponse;
/*    */ import net.sf.jasperreports.engine.JRAbstractExporter;
/*    */ import net.sf.jasperreports.engine.JRExporterParameter;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class HttpOutput extends FileOutput
/*    */ {
/*    */   private HttpServletResponse response;
/*    */   private OutputStream ouputStream;
/*    */ 
/*    */   public HttpOutput(HttpServletResponse response, String fileName)
/*    */   {
/* 19 */     super(fileName);
/* 20 */     this.response = response;
/*    */   }
/*    */ 
/*    */   public void beforeExport(ReportWrapper wrapper) {
/* 24 */     wrapper.beforeExport(this);
/* 25 */     this.response.setContentType(wrapper.getContentType());
/*    */     try {
/* 27 */       this.response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(this.fileName, "UTF-8") + "\"");
/*    */ 
/* 29 */       this.ouputStream = this.response.getOutputStream();
/* 30 */       wrapper.getExporter().setParameter(JRExporterParameter.OUTPUT_STREAM, this.ouputStream);
/*    */     } catch (IOException e) {
/* 32 */       this.log.error("Fail to open output stream", e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void afterExport(ReportWrapper report)
/*    */   {
/* 38 */     if (this.ouputStream == null) return;
/*    */     try {
/* 40 */       this.ouputStream.close();
/*    */     } catch (IOException e) {
/* 42 */       this.log.error("Fail to close output stream", e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           D:\资料\中信国安\国安养老\国安养老项目\Java\JavaCRM\lib\pmsbase-1.2.0-SNAPSHOT.jar
 * Qualified Name:     com.cnpc.pms.base.report.jasper.output.HttpOutput
 * JD-Core Version:    0.5.3
 */