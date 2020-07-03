package ub.fet.sms.payload.request;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;


@Data
public class SignupRequest {


    @NotBlank
    @Size(max = 25)
    private long redno;

    @NotBlank
    @Size(max = 25)
    private long rollno;

    @Size(max = 4)
    private String sclass;


    @NotBlank
    @Size(max = 25)
    private String name;


    @Size(max = 25)
    private String fname;


    @Size(max = 25)
    private String mname;

    private LocalDate dob;

    private LocalDate dor;

    @Size(max = 30)
    private String address;

    @Size(max = 15)
    private String city;

    @Size(max = 15)
    private String  state;

    @Size(max = 6)
    private String pin;

    @Size(max = 15)
    private String phone;

    private Set<String> role;

}
