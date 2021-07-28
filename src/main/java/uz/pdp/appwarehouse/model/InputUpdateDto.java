package uz.pdp.appwarehouse.model;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class InputUpdateDto implements Serializable {
    private Long warehouseId;
    private Long supplierId;
    private Long currencyId;
    private Integer factureNumber;
}
