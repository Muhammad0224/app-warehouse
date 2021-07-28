package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class OutputUpdateDto implements Serializable {
    private Long warehouseId;
    private Long clientId;
    private Long currencyId;
    private Integer factureNumber;
}
