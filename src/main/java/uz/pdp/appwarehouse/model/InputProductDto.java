package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class InputProductDto implements Serializable {
    private Long productId;
    private Long inputId;
    private Double amount;
    private Double price;
    private Date expireDate;
}
