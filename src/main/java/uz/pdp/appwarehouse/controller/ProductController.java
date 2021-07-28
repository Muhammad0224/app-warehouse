package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.*;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.CurrencyDto;
import uz.pdp.appwarehouse.model.ProductDto;
import uz.pdp.appwarehouse.model.ProductUpdateDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.repository.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AttachmentController attachmentController;

    @GetMapping
    public ResponseEntity<List<Product>> get() {
        return new ResponseEntity<>(productRepository.findAllByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/byCategory/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(productRepository.findAllByCategoryIdAndActiveTrue(categoryId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
        return optionalProduct.map(product -> new ResponseEntity<>(product, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@ModelAttribute ProductDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getCategoryId()))
            return new ResponseEntity<>(new Result("Category shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getMeasurementId()))
            return new ResponseEntity<>(new Result("Measurement shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Measurement> optionalMeasurement = measurementRepository.findByIdAndActiveTrue(dto.getMeasurementId());
        if (!optionalMeasurement.isPresent())
            return new ResponseEntity<>(new Result("Measurement not found", false), HttpStatus.NOT_FOUND);

        Optional<Category> optionalCategory = categoryRepository.findByIdAndActiveTrue(dto.getCategoryId());
        if (!optionalCategory.isPresent())
            return new ResponseEntity<>(new Result("Category not found", false), HttpStatus.NOT_FOUND);

        if (productRepository.existsByNameAndCategoryIdAndActiveTrue(dto.getName(), dto.getCategoryId()))
            return new ResponseEntity<>(new Result("This product already exist", false), HttpStatus.CONFLICT);

        productRepository.save(Product.childBuilder()
                .name(dto.getName())
                .attachment(attachmentController.add(dto.getFile()))
                .category(optionalCategory.get())
                .code(getCode())
                .measurement(optionalMeasurement.get())
                .build());

        return new ResponseEntity<>(new Result("Product created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @ModelAttribute ProductUpdateDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getCategoryId()))
            return new ResponseEntity<>(new Result("Category shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (Utils.isEmpty(dto.getMeasurementId()))
            return new ResponseEntity<>(new Result("Measurement shouldn't be empty", false), HttpStatus.BAD_REQUEST);

        Optional<Measurement> optionalMeasurement = measurementRepository.findByIdAndActiveTrue(dto.getMeasurementId());
        if (!optionalMeasurement.isPresent())
            return new ResponseEntity<>(new Result("Measurement not found", false), HttpStatus.NOT_FOUND);

        Optional<Category> optionalCategory = categoryRepository.findByIdAndActiveTrue(dto.getMeasurementId());
        if (!optionalCategory.isPresent())
            return new ResponseEntity<>(new Result("Category not found", false), HttpStatus.NOT_FOUND);

        if (productRepository.existsByNameAndCategoryIdAndActiveTrueAndIdNot(dto.getName(), dto.getCategoryId(), id))
            return new ResponseEntity<>(new Result("This product already exist", false), HttpStatus.CONFLICT);

        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);

        if (optionalProduct.isPresent()) {
            Attachment attachment = attachmentController.add(dto.getFile());
            Product product = optionalProduct.get();
            product.setName(dto.getName());
            product.setAttachment(attachment);
            product.setCategory(optionalCategory.get());
            product.setMeasurement(optionalMeasurement.get());
            productRepository.save(product);
            return new ResponseEntity<>(new Result("Product edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setActive(false);
            productRepository.save(product);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }

    private Integer getCode() {
        Integer maxCode = productRepository.getMaxCode();
        if (Utils.isEmpty(maxCode))
            return 1;
        return maxCode + 1;
    }

}
