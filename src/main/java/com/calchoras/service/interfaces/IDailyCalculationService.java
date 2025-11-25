package com.calchoras.service.interfaces;

import com.calchoras.model.DailyCalculationResult;
import com.calchoras.model.Employee;
import com.calchoras.model.TimeEntry;

public interface IDailyCalculationService {

    /*
     * Calcula o balanço de horas diário de um funcionário.
     * @param timeEntry objeto TimeEntry com batidas de ponto de um dia
     * @param employee O funcionário para o qual o cálculo será feito.
     * @return Um objeto DailyCalculationResult com os totais e os detalhes para o dia.
     */
    DailyCalculationResult calculate(TimeEntry timeEntry, Employee employee);
}