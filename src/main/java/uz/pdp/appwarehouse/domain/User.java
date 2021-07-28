package uz.pdp.appwarehouse.domain;

import lombok.*;
import uz.pdp.appwarehouse.domain.modelEntity.AbsEntity;

import javax.persistence.*;
import java.util.Set;

import static com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS.optional;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String lastName;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private Integer code;

    @Column
    private String password;

    @Column(columnDefinition = "boolean default true")
    private Boolean active;

    @ManyToMany
    private Set<Warehouse> warehouses;
}
