package com.calchoras;

import javax.swing.*;
import com.calchoras.controller.*;
import com.calchoras.repository.interfaces.*;
import com.calchoras.repository.*;
import com.calchoras.service.interfaces.*;
import com.calchoras.service.*;
import com.calchoras.view.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {

	public static void main(String[] args) {
		FlatLightLaf.setup();

		SwingUtilities.invokeLater(() -> {
			// Repositories
//			ICompanyRepository companyRepository = new CompanyRepository();
//			IEmployeeRepository employeeRepository = new EmployeeRepository();
//			ITimeEntryRepository timeEntryRepository = new TimeEntryRepository();
			ICompanyRepository companyRepository = new SQLiteCompanyRepository();
			IEmployeeRepository employeeRepository = new SQLiteEmployeeRepository();
			ITimeEntryRepository timeEntryRepository = new SQLiteTimeEntryRepository();

			// Services
			ICompanyService companyService = new CompanyService(companyRepository);
			IEmployeeService employeeService = new EmployeeService(employeeRepository, companyService);
			ITimeEntryService timeEntryService = new TimeEntryService(timeEntryRepository, employeeService);
			IDailyCalculationService dailyCalculationService = new DailyCalculationService();
			IReportService reportService = new ReportService(dailyCalculationService);

			// View
			MainFrame view = new MainFrame();

			// Controller
			new MainFrameController(view, companyService, employeeService, timeEntryService, dailyCalculationService, reportService);
		});
	}
}
