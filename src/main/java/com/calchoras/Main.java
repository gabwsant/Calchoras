package com.calchoras;

import com.calchoras.controller.MainFrameController;
import com.calchoras.repository.CompanyRepository;
import com.calchoras.repository.EmployeeRepository;
import com.calchoras.repository.TimeEntryRepository;
import com.calchoras.repository.interfaces.ICompanyRepository;
import com.calchoras.repository.interfaces.IEmployeeRepository;
import com.calchoras.repository.interfaces.ITimeEntryRepository;
import com.calchoras.service.*;
import com.calchoras.service.interfaces.*;
import com.calchoras.view.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		FlatLightLaf.setup();

		SwingUtilities.invokeLater(() -> {
			// Repositories
			ICompanyRepository companyRepository = new CompanyRepository();
			IEmployeeRepository employeeRepository = new EmployeeRepository();
			ITimeEntryRepository timeEntryRepository = new TimeEntryRepository();

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
