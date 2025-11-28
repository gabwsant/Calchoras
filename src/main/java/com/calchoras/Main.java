package com.calchoras;

import com.calchoras.controller.PontoController;
import com.calchoras.service.*;
import com.calchoras.service.interfaces.*;
import com.calchoras.view.MainFrame;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ICompanyService companyService = new CompanyService();
				IEmployeeService employeeService = new EmployeeService(companyService);
				ITimeEntryService timeEntryService = new TimeEntryService();
				IDailyCalculationService dailyCalculationService = new DailyCalculationService();
				IReportService reportService = new ReportService(dailyCalculationService);

				MainFrame view = new MainFrame();

				new PontoController(view, companyService, dailyCalculationService, employeeService, reportService, timeEntryService);
			}
		});
	}
}
