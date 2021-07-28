package uz.pdp.appwarehouse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appwarehouse.domain.Category;
import uz.pdp.appwarehouse.domain.Currency;
import uz.pdp.appwarehouse.helpers.Utils;
import uz.pdp.appwarehouse.model.CategoryDto;
import uz.pdp.appwarehouse.model.CurrencyDto;
import uz.pdp.appwarehouse.model.Result;
import uz.pdp.appwarehouse.repository.CategoryRepository;
import uz.pdp.appwarehouse.repository.CurrencyRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/byParent/{parent_id}")
    public ResponseEntity<List<Category>> getByParentId(@PathVariable Long parent_id) {
        if (Utils.isEmpty(parent_id))
            parent_id = null;
        return new ResponseEntity<>(categoryRepository.findAllByParentIdAndActiveTrue(parent_id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> get(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndActiveTrue(id);
        return optionalCategory.map(category -> new ResponseEntity<>(category, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Result> add(@RequestBody CategoryDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (dto.getParent_id() == 0)
            dto.setParent_id(null);
        if (categoryRepository.existsByNameAndParentIdAndActiveTrue(dto.getName(), dto.getParent_id()))
            return new ResponseEntity<>(new Result("This category already exist", false), HttpStatus.CONFLICT);

        Category parent = null;
        if (!Utils.isEmpty(dto.getParent_id())) {
            parent = get(dto.getParent_id()).getBody();
        }

        categoryRepository.save(Category.childBuilder().name(dto.getName()).parent(parent).build());
        return new ResponseEntity<>(new Result("Category created", true), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> edit(@PathVariable Long id, @RequestBody CategoryDto dto) {
        if (Utils.isEmpty(dto.getName()))
            return new ResponseEntity<>(new Result("Name shouldn't be empty", false), HttpStatus.BAD_REQUEST);
        if (categoryRepository.existsByNameAndParentIdAndIdNotAndActiveTrue(dto.getName(), dto.getParent_id(), id))
            return new ResponseEntity<>(new Result("This currency already exist", false), HttpStatus.CONFLICT);

        Category parent = null;
        if (!Utils.isEmpty(dto.getParent_id())) {
            parent = get(dto.getParent_id()).getBody();
        }

        Optional<Category> optionalCategory = categoryRepository.findByIdAndActiveTrue(id);

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(dto.getName());
            category.setParent(parent);
            categoryRepository.save(category);
            return new ResponseEntity<>(new Result("Category edited", true), HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(new Result("Not found", false), HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            List<Category> childCategory = getByParentId(category.getId()).getBody();
            if (Utils.isEmpty(childCategory)) {
                childCategory.forEach(category1 -> {
                    category1.setActive(false);
                    categoryRepository.save(category1);
                });
            }
            category.setActive(false);
            categoryRepository.save(category);
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
}