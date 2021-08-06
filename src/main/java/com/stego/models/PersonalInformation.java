package com.stego.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonalInformation {
    private String homeAddress;
    private String salary;
    private String employmentStatus;
}
