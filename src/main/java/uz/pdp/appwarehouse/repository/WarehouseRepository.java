package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Currency;
import uz.pdp.appwarehouse.domain.User;
import uz.pdp.appwarehouse.domain.Warehouse;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByNameAndActiveTrue(String name);

    boolean existsByNameAndIdNotAndActiveTrue(String name, Long id);

    List<Warehouse> findAllByActiveTrue();

    Optional<Warehouse> findByIdAndActiveTrue(Long aLong);
}
