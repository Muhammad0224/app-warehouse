package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.Measurement;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    boolean existsByNameAndActiveTrue(String name);

    boolean existsByNameAndIdNotAndActiveTrue(String name, Long id);

    List<Measurement> findAllByActiveTrue();

    Optional<Measurement> findByIdAndActiveTrue(Long aLong);
}
