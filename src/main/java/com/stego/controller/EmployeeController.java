package com.stego.controller;

import com.fasterxml.jackson.databind.node.TextNode;
import com.stego.models.Employee;
import com.stego.models.ManagerFlag;
import com.stego.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService empService){
        this.employeeService = empService;
    }

    @CrossOrigin
    @GetMapping(value = "/employees/{employeeId}")
    public ResponseEntity<Employee> getProduct(@PathVariable("employeeId") String employeeId, @RequestParam String flag) throws IOException, InterruptedException {
        return new ResponseEntity<>(getEmployee(employeeId,Boolean.parseBoolean(flag)), HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping(value = "/employees")
    public HttpStatus createEmployee(@RequestBody Employee employee){
        try{
            employeeService.createEmployee(employee);
            return HttpStatus.CREATED;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private Employee getEmployee(String empId, boolean managerFlag) throws IOException, InterruptedException {
        return employeeService.getFullEmployeeDetails(empId, managerFlag);
    }
}
