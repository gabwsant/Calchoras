package com.calchoras.service.interfaces;

import com.calchoras.model.Employee;
import com.calchoras.model.PeriodCalculationResult;

import java.io.IOException;

public interface IReportExporterService {
    void exportToTxt(PeriodCalculationResult result, Employee employee, String companyName, String filePath) throws IOException;
}
