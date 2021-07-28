package uz.pdp.appwarehouse.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InputProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Product product;

    private Double amount;

    private Double price;

    private Date expireDate;

    @ManyToOne(optional = false)
    private Input input;
}
