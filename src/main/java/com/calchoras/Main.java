package com.calchoras;

import com.calchoras.controller.MainFrameController;
import com.calchoras.repository.CompanyRepository;
import com.calchoras.repository.EmployeeRepository;
import com.calchoras.service.*;
import com.calchoras.service.interfaces.*;
import com.calchoras.view.MainFrame;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CompanyRepository companyRepository = new CompanyRepository();
				EmployeeRepository employeeRepository = new EmployeeRepository();
				ICompanyService companyService = new CompanyService(companyRepository);
				IEmployeeService employeeService = new EmployeeService(employeeRepository, companyService);
				ITimeEntryService timeEntryService = new TimeEntryService();
				IDailyCalculationService dailyCalculationService = new DailyCalculationService();
				IReportService reportService = new ReportService(dailyCalculationService);

				MainFrame view = new MainFrame();

				new MainFrameController(view, companyService, dailyCalculationService, employeeService, reportService, timeEntryService);
			}
		});
	}
}
