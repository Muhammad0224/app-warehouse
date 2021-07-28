package uz.pdp.appwarehouse.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Result {
    private String message;

    private Boolean status;
}
