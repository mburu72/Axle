package com.tumi.haul.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/*
public class ReportService {
    private final ResourceLoader resourceLoader;

    public ReportService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    public void generateReport(String inputPath, String outputPath){
try{
    JRBeanCollectionDataSource jrds = new JRBeanCollectionDataSource();
    JasperReport report = JasperCompileManager.compileReport(resourceLoader.getResource(inputPath).getInputStream());
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("generatedBy", "Edward");
    JasperPrint print = JasperFillManager.fillReport(report, parameters,jrds);
    JasperExportManager.exportReportToPdf(print);
} catch (JRException | IOException e) {
    throw new RuntimeException(e);
}
    }
}
*/