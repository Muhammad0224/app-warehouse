package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Client;
import uz.pdp.appwarehouse.domain.Supplier;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByPhoneNumberAndActiveTrue(String phoneNumber);

    boolean existsByPhoneNumberAndIdNotAndActiveTrue(String phoneNumber, Long id);

    List<Client> findAllByActiveTrue();

    Optional<Client> findByIdAndActiveTrue(Long aLong);
}
