package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.*;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.InputProductDto;
import uz.pdp.appwarehouse.model.OutputProductDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.repository.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/output/products")
public class OutputProductController {
    @Autowired
    private OutputProductRepository outputProductRepository;

    @Autowired
    private OutputRepository outputRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/byFacture/{factureNumber}")
    public ResponseEntity<List<OutputProduct>> getByFacture(@PathVariable Integer factureNumber) {
        return new ResponseEntity<>(outputProductRepository.findAllByOutput_FactureNumber(factureNumber), HttpStatus.OK);
    }

    @GetMapping("/byOutput/{outputId}")
    public ResponseEntity<List<OutputProduct>> getByOutput(@PathVariable Long outputId) {
        return new ResponseEntity<>(outputProductRepository.findAllByOutputId(outputId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody OutputProductDto dto) {
        if (Utils.isEmpty(dto.getProductId()))
            return new ResponseEntity<>(new Result("Product shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getOutputId()))
            return new ResponseEntity<>(new Result("Output shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getAmount()))
            return new ResponseEntity<>(new Result("Amount shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPrice()))
            return new ResponseEntity<>(new Result("Price shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(dto.getProductId());
        if (!optionalProduct.isPresent())
            return new ResponseEntity<>(new Result("Product not found", false), HttpStatus.NOT_FOUND);

        Optional<Output> optionalOutput = outputRepository.findById(dto.getOutputId());
        if (!optionalOutput.isPresent())
            return new ResponseEntity<>(new Result("Output not found", false), HttpStatus.NOT_FOUND);

        outputProductRepository.save(OutputProduct.builder()
                .product(optionalProduct.get())
                .output(optionalOutput.get())
                .amount(dto.getAmount())
                .price(dto.getPrice())
                .build());

        return new ResponseEntity<>(new Result("Output/product created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody OutputProductDto dto) {
        if (Utils.isEmpty(dto.getProductId()))
            return new ResponseEntity<>(new Result("Product shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getOutputId()))
            return new ResponseEntity<>(new Result("Output shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getAmount()))
            return new ResponseEntity<>(new Result("Amount shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getPrice()))
            return new ResponseEntity<>(new Result("Price shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(dto.getProductId());
        if (!optionalProduct.isPresent())
            return new ResponseEntity<>(new Result("Product not found", false), HttpStatus.NOT_FOUND);

        Optional<Output> optionalOutput = outputRepository.findById(dto.getOutputId());
        if (!optionalOutput.isPresent())
            return new ResponseEntity<>(new Result("Output not found", false), HttpStatus.NOT_FOUND);

        Optional<OutputProduct> optionalOutputProduct = outputProductRepository.findById(id);

        if (optionalOutputProduct.isPresent()) {
            OutputProduct outputProduct = optionalOutputProduct.get();
            outputProduct.setProduct(optionalProduct.get());
            outputProduct.setOutput(optionalOutput.get());
            outputProduct.setAmount(dto.getAmount());
            outputProduct.setPrice(dto.getPrice());
            outputProductRepository.save(outputProduct);
            return new ResponseEntity<>(new Result("Output/product edited", true), HttpStatus.ACCEPTED);
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
