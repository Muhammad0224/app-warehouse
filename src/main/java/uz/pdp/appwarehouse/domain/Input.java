package uz.pdp.appwarehouse.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "inputs")
public class Input {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "timestamp default now()")
    private Timestamp receiveDate;

    @ManyToOne(optional = false)
    private Warehouse warehouse;

    @ManyToOne(optional = false)
    private Supplier supplier;

    @ManyToOne(optional = false)
    private Currency currency;

    @Column(nullable = false)
    private Integer factureNumber;

    private Integer code;
}
