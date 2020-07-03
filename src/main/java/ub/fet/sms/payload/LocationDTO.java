package ub.fet.sms.payload;

import lombok.Data;

import java.util.List;

@Data
public class LocationDTO {
    private String current_loctaion;
    private List<String > previous_location;
    private long userid;
}
