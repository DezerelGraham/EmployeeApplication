package com.stego.controller;

import com.stego.models.Employee;
import com.stego.models.EmployeeInformation;
import com.stego.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class EmployeeController {
    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService empService){
        this.employeeService = empService;
    }
    @GetMapping(value = "/employees/{employeeId}")
    public ResponseEntity<EmployeeInformation> getProduct(@PathVariable("employeeId") String employeeId){
        return new ResponseEntity<>(getEmployee(employeeId), HttpStatus.OK);
    }

    @PutMapping(value = "/employees")
    public HttpStatus createEmployee(@RequestBody Employee employee){
        try{
            employeeService.createEmployee(employee);
            return HttpStatus.CREATED;
        }catch(Exception e){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private EmployeeInformation getEmployee(String empId) {
        return employeeService.getFullEmployeeDetails(empId);
    }
}
