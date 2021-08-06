package com.stego.service;

import com.stego.models.Employee;
import com.stego.models.EmployeeInformation;
import com.stego.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository empRepository){
        this.employeeRepository = empRepository;
    }

    public EmployeeInformation getFullEmployeeDetails(String employeeId) {
        Optional<EmployeeInformation> entity = employeeRepository.findById(employeeId);
        return entity.get();
    }

    public EmployeeInformation createEmployee(Employee employee) {
        EmployeeInformation employeeInformation = EmployeeInformation.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .jobTitle(employee.getJobTitle())
                .phoneNumber(employee.getPhoneNumber())
                .photo(employee.getEmployeeImage())
                .build();
        return employeeRepository.save(employeeInformation);
    }

}
