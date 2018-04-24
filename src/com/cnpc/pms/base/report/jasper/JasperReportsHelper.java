/*    */ package com.cnpc.pms.base.report.jasper;
/*    */ 
/*    */ import com.cnpc.pms.base.util.ConfigurationUtil;
/*    */ import com.cnpc.pms.base.util.StrUtil;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import net.sf.jasperreports.engine.JRException;
/*    */ import net.sf.jasperreports.engine.JasperCompileManager;
/*    */ import net.sf.jasperreports.engine.JasperReport;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.core.io.Resource;
/*    */ 
/*    */ public class JasperReportsHelper
/*    */ {
/* 33 */   private static final Logger log = LoggerFactory.getLogger(JasperReportsHelper.class);
/*    */   private static final String ERROR_TEMPLATE = "/report/error.jrxml";
/* 36 */   private static JasperReportsHelper instance = new JasperReportsHelper();
/*    */ 
/* 38 */   private final Map<String, JasperReport> reports = new HashMap();
/*    */ 
/* 40 */   private final Map<String, Long> cachedFiles = new ConcurrentHashMap();
/*    */ 
/* 42 */   public static JasperReport ERROR_REPORT = null;
/*    */ 
/*    */   public static JasperReport getReport(String fileName)
/*    */     throws JasperReportsException
/*    */   {
/* 58 */     if (StrUtil.isEmpty(fileName)) {
/* 59 */       return null;
/*    */     }
/* 61 */     JasperReport report = (JasperReport)instance.reports.get(fileName);
/* 62 */     if (report == null) {
/* 63 */       report = instance.compileReport(fileName);
/*    */     }
/* 65 */     return report;
/*    */   }
/*    */ 
/*    */   private synchronized JasperReport compileReport(String fileName) throws JasperReportsException {
/* 69 */     JasperReport report = (JasperReport)this.reports.get(fileName);
/* 70 */     if (report == null) {
/*    */       try {
/* 72 */         Resource resource = ConfigurationUtil.getSingleResource(fileName);
/* 73 */         if (resource != null) {
/* 74 */           log.debug("Try to compile JasperReport file: {}", fileName);
/* 75 */           report = JasperCompileManager.compileReport(resource.getInputStream());
/* 76 */           if (resource.getURL().getProtocol().equals("file")) {
/* 77 */             this.cachedFiles.put(fileName, Long.valueOf(resource.getFile().lastModified()));
/*    */           }
/* 79 */           this.reports.put(fileName, report);
/*    */         } else {
/* 81 */           throw new JasperReportsException("Fail to find template: " + fileName);
/*    */         }
/*    */       } catch (JRException e) {
/* 84 */         log.error("Fail to compile report: {}", fileName, e);
/* 85 */         throw new JasperReportsException("Fail to compile template: " + fileName);
/*    */       } catch (IOException e) {
/* 87 */         log.error("Fail to load report: {} ", fileName, e);
/* 88 */         throw new JasperReportsException("Fail to read template: " + fileName);
/*    */       }
/*    */     }
/* 91 */     return report;
/*    */   }
/*    */ 
/*    */   public static void update() {
/* 95 */     for (String fileName : instance.cachedFiles.keySet()) {
/* 96 */       Resource resource = ConfigurationUtil.getSingleResource(fileName);
/*    */       try {
/* 98 */         if (resource.getFile().lastModified() > ((Long)instance.cachedFiles.get(resource)).longValue()) {
/* 99 */           log.info("Report {} updated, we should re-compile it.", fileName);
/* 100 */           instance.compileReport(fileName);
/*    */         }
/*    */       } catch (IOException e) {
/* 103 */         log.error("Fail to load report: {} ", fileName, e);
/*    */       } catch (JasperReportsException e) {
/* 105 */         log.error("Fail to update report: {} ", fileName, e);
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 44 */     Resource resource = ConfigurationUtil.getSingleResource("/report/error.jrxml");
///* 45 */     if (resource == null) return;
///*    */     try {
///* 47 */       ERROR_REPORT = JasperCompileManager.compileReport(resource.getInputStream());
///*    */     } catch (Exception e) {
///* 49 */       log.error("Fail to compile error report: {}", "/report/error.jrxml", e);
///*    */     }
/*    */   }
/*    */ }

/* Location:           D:\资料\中信国安\国安养老\国安养老项目\Java\JavaCRM\lib\pmsbase-1.2.0-SNAPSHOT.jar
 * Qualified Name:     com.cnpc.pms.base.report.jasper.JasperReportsHelper
 * JD-Core Version:    0.5.3
 */