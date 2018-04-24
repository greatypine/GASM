package com.cnpc.pms.base.report.jasper.output;

import com.cnpc.pms.base.report.jasper.ReportWrapper;

public abstract interface IOutput
{
  public abstract String getFileName();

  public abstract void beforeExport(ReportWrapper paramReportWrapper);

  public abstract void afterExport();
}

/* Location:           D:\资料\中信国安\国安养老\国安养老项目\Java\JavaCRM\lib\pmsbase-1.2.0-SNAPSHOT.jar
 * Qualified Name:     com.cnpc.pms.base.report.jasper.output.IOutput
 * JD-Core Version:    0.5.3
 */