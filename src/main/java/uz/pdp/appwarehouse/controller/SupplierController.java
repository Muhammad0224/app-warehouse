package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Currency;
import uz.pdp.appwarehouse.domain.Supplier;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.CurrencyDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.model.SupplierDto;
import uz.pdp.appwarehouse.repository.CurrencyRepository;
import uz.pdp.appwarehouse.repository.SupplierRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public ResponseEntity<List<Supplier>> get() {
        return new ResponseEntity<>(supplierRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> get(@PathVariable Long id) {
        Optional<Supplier> optionalSupplier = supplierRepository.findByIdAndActiveTrue(id);
        return optionalSupplier.map(supplier -> new ResponseEntity<>(supplier, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody SupplierDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("Phone number shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        if (supplierRepository.existsByPhoneNumberAndActiveTrue(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("This supplier already exist", false), HttpStatus.CONFLICT);

        supplierRepository.save(Supplier.childBuilder().name(dto.getName()).phoneNumber(dto.getPhoneNumber()).build());
        return new ResponseEntity<>(new Result("Supplier created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody SupplierDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPhoneNumber()))
            return new ResponseEntity<>(new Result("Phone number shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (supplierRepository.existsByPhoneNumberAndIdNotAndActiveTrue(dto.getPhoneNumber(), id))
            return new ResponseEntity<>(new Result("This supplier already exist", false), HttpStatus.CONFLICT);

        Optional<Supplier> optionalSupplier = supplierRepository.findByIdAndActiveTrue(id);

        if (optionalSupplier.isPresent()) {
            Supplier supplier = optionalSupplier.get();
            supplier.setName(dto.getName());
            supplier.setPhoneNumber(dto.getPhoneNumber());
            supplierRepository.save(supplier);
            return new ResponseEntity<>(new Result("Supplier edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        Optional<Supplier> optionalSupplier = supplierRepository.findByIdAndActiveTrue(id);
        if (optionalSupplier.isPresent()){
            Supplier supplier = optionalSupplier.get();
            supplier.setActive(false);
            supplierRepository.save(supplier);
            return new ResponseEntity<>(true,HttpStatus.OK);
        }
        return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
    }

}
