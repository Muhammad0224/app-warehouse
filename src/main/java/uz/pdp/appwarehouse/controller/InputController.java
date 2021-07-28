package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.*;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.*;
import uz.pdp.appwarehouse.repository.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/input")
public class InputController {
    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @GetMapping
    public ResponseEntity<List<Input>> get() {
        return new ResponseEntity<>(inputRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/byWarehouse/{warehouseId}")
    public ResponseEntity<List<Input>> getByWarehouse(@PathVariable Long warehouseId) {
        return new ResponseEntity<>(inputRepository.findAllByWarehouseId(warehouseId), HttpStatus.OK);
    }

    @GetMapping("/bySupplier/{supplierId}")
    public ResponseEntity<List<Input>> getBySupplier(@PathVariable Long supplierId) {
        return new ResponseEntity<>(inputRepository.findAllBySupplierId(supplierId), HttpStatus.OK);
    }

    @GetMapping("/byCurrency/{currencyId}")
    public ResponseEntity<List<Input>> getByCurrency(@PathVariable Long currencyId) {
        return new ResponseEntity<>(inputRepository.findAllByCurrencyId(currencyId), HttpStatus.OK);
    }

    @GetMapping("/byFactureNumber/{factureNumber}")
    public ResponseEntity<Input> getByFacture(@PathVariable Integer factureNumber) {
        Optional<Input> optionalInput = inputRepository.findByFactureNumber(factureNumber);
        return optionalInput.map(input -> new ResponseEntity<>(input, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Input> get(@PathVariable Long id) {
        Optional<Input> optionalInput = inputRepository.findById(id);
        return optionalInput.map(input -> new ResponseEntity<>(input, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody InputDto dto) {
        if (Utils.isEmpty(dto.getFactureNumber()))
            return new ResponseEntity<>(new Result("Facture number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getWarehouseId()))
            return new ResponseEntity<>(new Result("Warehouse shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getCurrencyId()))
            return new ResponseEntity<>(new Result("Currency shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getSupplierId()))
            return new ResponseEntity<>(new Result("Supplier shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(dto.getWarehouseId());
        if (!optionalWarehouse.isPresent())
            return new ResponseEntity<>(new Result("Warehouse not found", false), HttpStatus.NOT_FOUND);

        Optional<Currency> optionalCurrency = currencyRepository.findByIdAndActiveTrue(dto.getCurrencyId());
        if (!optionalCurrency.isPresent())
            return new ResponseEntity<>(new Result("Currency not found", false), HttpStatus.NOT_FOUND);

        Optional<Supplier> optionalSupplier = supplierRepository.findByIdAndActiveTrue(dto.getSupplierId());
        if (!optionalSupplier.isPresent())
            return new ResponseEntity<>(new Result("Supplier not found", false), HttpStatus.NOT_FOUND);

        inputRepository.save(Input.builder()
                .warehouse(optionalWarehouse.get())
                .currency(optionalCurrency.get())
                .factureNumber(dto.getFactureNumber())
                .supplier(optionalSupplier.get())
                .code(getCode())
                .receiveDate(Timestamp.valueOf(LocalDateTime.now()))
                .build());

        return new ResponseEntity<>(new Result("Input created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody InputUpdateDto dto) {
        if (Utils.isEmpty(dto.getFactureNumber()))
            return new ResponseEntity<>(new Result("Facture number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getWarehouseId()))
            return new ResponseEntity<>(new Result("Warehouse shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getCurrencyId()))
            return new ResponseEntity<>(new Result("Currency shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getSupplierId()))
            return new ResponseEntity<>(new Result("Supplier shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByIdAndActiveTrue(dto.getWarehouseId());
        if (!optionalWarehouse.isPresent())
            return new ResponseEntity<>(new Result("Warehouse not found", false), HttpStatus.NOT_FOUND);

        Optional<Currency> optionalCurrency = currencyRepository.findByIdAndActiveTrue(dto.getCurrencyId());
        if (!optionalCurrency.isPresent())
            return new ResponseEntity<>(new Result("Currency not found", false), HttpStatus.NOT_FOUND);

        Optional<Supplier> optionalSupplier = supplierRepository.findByIdAndActiveTrue(dto.getSupplierId());
        if (!optionalSupplier.isPresent())
            return new ResponseEntity<>(new Result("Supplier not found", false), HttpStatus.NOT_FOUND);

        Optional<Input> optionalInput = inputRepository.findById(id);

        if (optionalInput.isPresent()) {
            Input input = optionalInput.get();
            input.setCurrency(optionalCurrency.get());
            input.setFactureNumber(dto.getFactureNumber());
            input.setSupplier(optionalSupplier.get());
            input.setWarehouse(optionalWarehouse.get());
            inputRepository.save(input);
            return new ResponseEntity<>(new Result("Input edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
//        Optional<Input> optionalInput = inputRepository.findById(id);
//        if (optionalInput.isPresent()) {
//            Input input = optionalInput.get();
//            input.setActive(false);
//            inputRepository.save(input);
//            return new ResponseEntity<>(true, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
//    }

    private Integer getCode() {
        Integer maxCode = inputRepository.getMaxCode();
        if (Utils.isEmpty(maxCode))
            return 1;
        return maxCode + 1;
    }

}
