package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.*;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.*;
import uz.pdp.appwarehouse.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/input/products")
public class InputProductController {
    @Autowired
    private InputProductRepository inputProductRepository;

    @Autowired
    private InputRepository inputRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/byFacture/{factureNumber}")
    public ResponseEntity<List<InputProduct>> getByFacture(@PathVariable Integer factureNumber) {
        return new ResponseEntity<>(inputProductRepository.findAllByInput_FactureNumber(factureNumber), HttpStatus.OK);
    }

    @GetMapping("/byInput/{inputId}")
    public ResponseEntity<List<InputProduct>> getByInput(@PathVariable Long inputId) {
        return new ResponseEntity<>(inputProductRepository.findAllByInputId(inputId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody InputProductDto dto) {
        if (Utils.isEmpty(dto.getProductId()))
            return new ResponseEntity<>(new Result("Product shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getInputId()))
            return new ResponseEntity<>(new Result("Input shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getAmount()))
            return new ResponseEntity<>(new Result("Amount shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPrice()))
            return new ResponseEntity<>(new Result("Price shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(dto.getProductId());
        if (!optionalProduct.isPresent())
            return new ResponseEntity<>(new Result("Product not found", false), HttpStatus.NOT_FOUND);

        Optional<Input> optionalInput = inputRepository.findById(dto.getInputId());
        if (!optionalInput.isPresent())
            return new ResponseEntity<>(new Result("Input not found", false), HttpStatus.NOT_FOUND);

        inputProductRepository.save(InputProduct.builder()
                .product(optionalProduct.get())
                .input(optionalInput.get())
                .amount(dto.getAmount())
                .price(dto.getPrice())
                .expireDate(dto.getExpireDate())
                .build());

        return new ResponseEntity<>(new Result("Input/product created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody InputProductDto dto) {
        if (Utils.isEmpty(dto.getProductId()))
            return new ResponseEntity<>(new Result("Product shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getInputId()))
            return new ResponseEntity<>(new Result("Input shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getAmount()))
            return new ResponseEntity<>(new Result("Amount shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPrice()))
            return new ResponseEntity<>(new Result("Price shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(dto.getProductId());
        if (!optionalProduct.isPresent())
            return new ResponseEntity<>(new Result("Product not found", false), HttpStatus.NOT_FOUND);

        Optional<Input> optionalInput = inputRepository.findById(dto.getInputId());
        if (!optionalInput.isPresent())
            return new ResponseEntity<>(new Result("Input not found", false), HttpStatus.NOT_FOUND);

        Optional<InputProduct> optionalInputProduct = inputProductRepository.findById(id);

        if (optionalInputProduct.isPresent()) {
            InputProduct inputProduct = optionalInputProduct.get();
            inputProduct.setProduct(optionalProduct.get());
            inputProduct.setInput(optionalInput.get());
            inputProduct.setAmount(dto.getAmount());
            inputProduct.setPrice(dto.getPrice());
            inputProduct.setExpireDate(dto.getExpireDate());
            inputProductRepository.save(inputProduct);
            return new ResponseEntity<>(new Result("Input/product edited", true), HttpStatus.ACCEPTED);
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
}
