package com.stego.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = EmployeeInformation.TABLE_NAME)
public class EmployeeInformation {
    public static final String TABLE_NAME = "EmployeeInformation";
    @Id
    private String employeeId;

    private String lastName;
    private String firstName;
    private String phoneNumber;
    private String jobTitle;
    private String photo;
}
