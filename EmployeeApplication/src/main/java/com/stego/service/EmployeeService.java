package com.stego.service;

import com.stego.models.Employee;
import com.stego.models.EmployeeInformation;
import com.stego.models.PersonalInformation;
import com.stego.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;
    ProcessBuilder builder;

    @Autowired
    public EmployeeService(EmployeeRepository empRepository){
        this.employeeRepository = empRepository;
        builder = new ProcessBuilder();
    }

    public Employee getFullEmployeeDetails(String employeeId, boolean managerFlag) throws IOException, InterruptedException {
        Optional<EmployeeInformation> entity = employeeRepository.findById(employeeId);
        if(managerFlag){
            System.out.println("I'm a manager");
            triggerStego(entity.get().getPhoto(), true);
            PersonalInformation personalInfo = getPersonalInfo();
            return mapEmployee(entity.get(), personalInfo);
        }
        System.out.println("Not a manager");
        return mapEmployee(entity.get(), null);
    }

    private Employee mapEmployee(EmployeeInformation employeeInformation, PersonalInformation personalInfo) {
        return Employee.builder()
                .employeeId(employeeInformation.getEmployeeId())
                .employeeImage(employeeInformation.getPhoto())
                .firstName(employeeInformation.getFirstName())
                .lastName(employeeInformation.getLastName())
                .jobTitle(employeeInformation.getJobTitle())
                .phoneNumber(employeeInformation.getPhoneNumber())
                .personalInformation(personalInfo)
                .build();
    }

    private PersonalInformation getPersonalInfo() {
        List<String> info = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Stego\\Message.txt"))) {
            String line = br.readLine();
            info = Arrays.asList(line.split(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PersonalInformation.builder()
                .homeAddress(info.get(0))
                .salary(info.get(1))
                .employmentStatus(info.get(2))
                .build();
    }


    private void triggerStego(String image, boolean isHidden) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        if(isHidden){
            pb.command("C:\\Users\\Stego\\Main.exe","-i", image, "-e", "-q", "80");
        }
        else {
            pb.command("C:\\Users\\Stego\\Main.exe", "-i", image, "-h", "Message.txt", "-q", "80");
        }
        pb.directory(new File("C:\\Users\\Stego\\"));
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        System.out.println("Waiting for hiding to finish...");
        p.waitFor();
        System.out.println("Hiding finished");
    }


    public void createEmployee(Employee employee) throws IOException, InterruptedException {
        //Places employee image in directory
        String imageName = moveEmployeeImage(employee);

        //Create text file for message
        createMessageTxt(employee);

        //Run stego to hide message
        triggerStego(imageName, false);

        EmployeeInformation employeeInformation = EmployeeInformation.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .jobTitle(employee.getJobTitle())
                .phoneNumber(employee.getPhoneNumber())
                .photo("C:\\Users\\Stego\\" + employee.getEmployeeId() + "_hidden.jpg")
                .build();

        employeeRepository.save(employeeInformation);
    }


    private String moveEmployeeImage(Employee employee) throws IOException {
        String imageName = employee.getEmployeeId() + ".jpg";
        Files.move
                (Paths.get(employee.getEmployeeImage()),
                        Paths.get("C:\\Users\\Stego\\" + imageName));
        return imageName;
    }

    private void createMessageTxt(Employee employee) throws IOException {
        File message = new File("C:\\Users\\Stego\\Message.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(message));
        writer.write(employee.getPersonalInformation().getHomeAddress()
                + ";" + employee.getPersonalInformation().getSalary()
                + ";" + employee.getPersonalInformation().getEmploymentStatus());
        writer.close();
    }

}
