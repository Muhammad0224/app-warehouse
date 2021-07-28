package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Currency;
import uz.pdp.appwarehouse.domain.Supplier;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    boolean existsByPhoneNumberAndActiveTrue(String phoneNumber);

    boolean existsByPhoneNumberAndIdNotAndActiveTrue(String phoneNumber, Long id);

    List<Supplier> findAllByActiveTrue();

    Optional<Supplier> findByIdAndActiveTrue(Long aLong);
}
