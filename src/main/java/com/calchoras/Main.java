package com.calchoras;

import com.calchoras.controller.MainFrameController;
import com.calchoras.repository.CompanyRepository;
import com.calchoras.repository.EmployeeRepository;
import com.calchoras.repository.TimeEntryRepository;
import com.calchoras.service.*;
import com.calchoras.service.interfaces.*;
import com.calchoras.view.MainFrame;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			// Repositories
			CompanyRepository companyRepository = new CompanyRepository();
			EmployeeRepository employeeRepository = new EmployeeRepository();
			TimeEntryRepository timeEntryRepository = new TimeEntryRepository();

			// Services
			ICompanyService companyService = new CompanyService(companyRepository);
			IEmployeeService employeeService = new EmployeeService(employeeRepository, companyService);
			ITimeEntryService timeEntryService = new TimeEntryService(timeEntryRepository, employeeService); // <- aqui passamos o EmployeeService
			IDailyCalculationService dailyCalculationService = new DailyCalculationService();
			IReportService reportService = new ReportService(dailyCalculationService);

			// View
			MainFrame view = new MainFrame();

			// Controller
			new MainFrameController(view, companyService, dailyCalculationService, employeeService, reportService, timeEntryService);
		});
	}
}
