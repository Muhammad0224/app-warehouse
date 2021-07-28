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
@Entity(name = "outputs")
public class Output {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "timestamp default now()")
    private Timestamp soldDate;

    @ManyToOne(optional = false)
    private Warehouse warehouse;

    @ManyToOne(optional = false)
    private Client client;

    @ManyToOne(optional = false)
    private Currency currency;

    private Integer factureNumber;

    private Integer code;
}
