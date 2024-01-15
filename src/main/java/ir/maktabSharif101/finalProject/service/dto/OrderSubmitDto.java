package ir.maktabSharif101.finalProject.service.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderSubmitDto {
    Long subServiceId;
    String jobInfo;
    String date;
    String time;
    String Address;
    double price;
}
