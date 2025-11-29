package com.calchoras.controller;

import com.calchoras.service.interfaces.*;
import com.calchoras.view.MainFrame;

public class MainFrameController {
    private final IDailyCalculationService dailyCalculationService;
    private final IEmployeeService employeeService;
    private final ICompanyService companyService;
    private final IReportService reportService;
    private final ITimeEntryService timeEntryService;

    public MainFrameController(
            MainFrame view,
            ICompanyService companyService,
            IDailyCalculationService dailyCalculationService,
            IEmployeeService employeeService,
            IReportService reportService,
            ITimeEntryService timeEntryService
    ) {
        this.dailyCalculationService = dailyCalculationService;
        this.companyService = companyService;
        this.employeeService = employeeService;
        this.reportService = reportService;
        this.timeEntryService = timeEntryService;
    }
}
