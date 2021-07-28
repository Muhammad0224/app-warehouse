package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Input;
import uz.pdp.appwarehouse.domain.Output;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutputRepository extends JpaRepository<Output, Long> {

    List<Output> findAllByWarehouseId(Long category_id);

    List<Output> findAllByClientId(Long client_id);

    List<Output> findAllByCurrencyId(Long currency_id);

    Optional<Output> findByFactureNumber(Integer factureNumber);

    @Query(nativeQuery = true, value = "select max(code) from outputs")
    Integer getMaxCode();
}
