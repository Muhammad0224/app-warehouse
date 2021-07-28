package uz.pdp.appwarehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.appwarehouse.domain.InputProduct;
import uz.pdp.appwarehouse.domain.OutputProduct;

import java.util.List;

@Repository
public interface OutputProductRepository extends JpaRepository<OutputProduct, Long> {
    List<OutputProduct> findAllByOutputId(Long output_id);

    List<OutputProduct> findAllByOutput_FactureNumber(Integer output_factureNumber);
}
