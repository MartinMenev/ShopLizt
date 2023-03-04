package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.products.InputProductDTO;
import com.example.myshoppingapp.domain.products.OutputProductDTO;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.repository.ProductRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@Service
public class ProductService {
    private final UserService userService;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public ProductService(UserService userService, ProductRepository productRepository, ModelMapper modelMapper) {
        this.userService = userService;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;

    }

    @Modifying
    public void addProduct(InputProductDTO inputProductDTO) {
        UserEntity userEntity = userService.findByUsername(userService.getLoggedUser().getUsername());
        Product product = this.modelMapper.map(inputProductDTO, Product.class);
        product.setUserEntity(userEntity);
        product.setBoughtOn(null);
        this.productRepository.saveAndFlush(product);
        product.setPosition(product.getId());
        this.productRepository.saveAndFlush(product);

    }

    public String getAllProducts() {
        String currentUsername = userService.getLoggedUser().getUsername();
        Long currentUserId = this.userService.findByUsername(currentUsername).getId();

        return this.productRepository.findAllByUserEntityId(currentUserId)
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(product -> this.modelMapper.map(product, OutputProductDTO.class))
                .map(OutputProductDTO::toString)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public List<OutputProductDTO> getListedProducts() {
        String currentUsername =  userService.getLoggedUser().getUsername();
        Long currentUserId = this.userService.findByUsername(currentUsername).getId();

        List<OutputProductDTO> outputProductDTOS = this.productRepository.findAllByUserEntityId(currentUserId)
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .filter(product -> product.getBoughtOn() ==null)
                .map(product -> this.modelMapper.map(product, OutputProductDTO.class))
                .toList();

        return outputProductDTOS.stream().sorted((a, b) -> b.getPosition().compareTo(a.getPosition())).toList();
    }

    public void updateProduct(InputProductDTO inputProductDTO) {
        Long idToUpdate = inputProductDTO.getId();
        String newName = inputProductDTO.getName();
        this.productRepository.updateProductName(idToUpdate, newName);
    }

    @Modifying
    @Transactional
    public void deleteById(long id) {
        this.productRepository.deleteById(id);
    }

    public OutputProductDTO getProductById(Long id) {
        Product product = this.productRepository
                .getProductById(id)
                .orElseThrow(NoSuchElementException::new);
        return modelMapper.map(product, OutputProductDTO.class);
    }

    public void moveUpProduct(long position) {
        long userId = this.userService.getLoggedUserId();
        if (this.productRepository.findFirstByPositionGreaterThanAndUserEntityIdOrderByPositionAsc(position, userId) !=null) {
            long newPosition = this.productRepository.
                    findFirstByPositionGreaterThanAndUserEntityIdOrderByPositionAsc(position, userId).getPosition();
            this.productRepository.swapProductOrder(position, newPosition);
        }

    }

    public void moveDownProduct(long position) {
        long userId = this.userService.getLoggedUserId();
        if (this.productRepository.findFirstByPositionLessThanAndUserEntityIdOrderByPositionDesc(position, userId) != null) {
            long newPosition = this.productRepository.
                    findFirstByPositionLessThanAndUserEntityIdOrderByPositionDesc(position, userId).getPosition();
            this.productRepository.swapProductOrder(position, newPosition);
        }
    }

    @Transactional
    @Modifying
    public void buyProduct(long id) {
        Product product = this.productRepository.getProductById(id).orElseThrow(NoSuchElementException::new);
        product.setBoughtOn(LocalDate.now());
        UserEntity userEntity = this.userService.getLoggedUser();
        product.setBuyer(userEntity);
        this.productRepository.save(product);
    }

    public List<OutputProductDTO> showBoughtProducts() {
        Long userId = this.userService.getLoggedUserId();
        return this.productRepository.findAllByBuyerId(userId)
                .orElseThrow(NoSuchElementException::new)
                .stream()
                .map(p -> modelMapper.map(p, OutputProductDTO.class))
                .toList();
    }

    @Transactional
    @Modifying
    public void reuseProduct(long id) {
        Product product = this.productRepository
                .getProductById(id)
                .orElseThrow(NoSuchElementException::new);
         product.setBoughtOn(null)
                .setBuyer(null);
        this.productRepository.saveAndFlush(product);
    }

    public void saveProduct(Product product) {
        this.productRepository.saveAndFlush(product);
    }

    @Transactional
    @Modifying
    public void addProductToMyList(String name) {
       Product product = new Product(name);
        UserEntity userEntity = userService.getLoggedUser();
        product.setUserEntity(userEntity);
        product.setBoughtOn(null);
        this.productRepository.saveAndFlush(product);
        product.setPosition(product.getId());
        this.productRepository.saveAndFlush(product);
    }

    public Product getProductByName(String productName) {
        return this.productRepository.findByName(productName);
    }

    public Product findProductById(Long productId) {
        return this.productRepository.getProductById(productId).orElseThrow(NoSuchElementException::new);
    }
}

