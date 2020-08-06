package io.UserService.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public interface CommonUtils {
	
	
	/*
        Responsibility: Utility to generate Jasper Report
        Parameters: String jasperReportSource, String reportFormatType, String reportPath, List<T> repository, String reportName
        Return Type: Object (Exception or Null)
     */
	 public static <T> Object generateReport(String jasperReportSource, String reportFormatType, String reportPath, List<T> repository, String reportName){
         try {
             JasperReport jasperReport= JasperCompileManager.compileReport(jasperReportSource);
             JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(repository);
             Map<String,Object> reportParameters=new HashMap<>();
             reportParameters.put("createdBy","Infor-Car-Portal");
             JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,reportParameters,dataSource);
             switch(reportFormatType.toLowerCase()){
                 case "html":
                     File htmlReportFile=new File(reportPath);
                     if(!htmlReportFile.exists()){
                         htmlReportFile.mkdirs();
                     }
                     JasperExportManager.exportReportToHtmlFile(jasperPrint, htmlReportFile+File.separator+reportName+".html");
                     break;
                 case "pdf":
                     File pdfReportFile=new File(reportPath);
                     if(!pdfReportFile.exists()){
                         pdfReportFile.mkdirs();
                     }
                     JasperExportManager.exportReportToHtmlFile(jasperPrint,pdfReportFile+File.separator+reportName+".pdf");
                     break;
                 default:
                     break;
             }
         }catch(JRException e){
             return "Sorry! Exception:"+e.getMessage();
         }
         return null;
     }
	 
     /*
        Responsibility: Utility to change Current LocalDate to formatted String Pattern
        Parameters: String (Type of Date Pattern)
        Return Type: String
     */
	 public static UnaryOperator<String> getFormattedCurrentDateTimeString = datePattern -> LocalDateTime.now().format(DateTimeFormatter.ofPattern(datePattern));
}
