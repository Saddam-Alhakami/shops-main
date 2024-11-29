package com.shops.dreamshops.service.product;

import com.shops.dreamshops.dto.ImageDto;
import com.shops.dreamshops.dto.ProductDto;
import com.shops.dreamshops.exception.ProductNotFoundException;
import com.shops.dreamshops.model.Category;
import com.shops.dreamshops.model.Image;
import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.repository.CategoryRepository;
import com.shops.dreamshops.repository.ImageRepository;
import com.shops.dreamshops.repository.ProductRepository;
import com.shops.dreamshops.request.product.AddProductRequest;
import com.shops.dreamshops.request.product.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() ->
                new ProductNotFoundException("product not found"));
    }

    @Override
    public Product addProduct(AddProductRequest request) {

        // Check if product with the same name already exists
        List<Product> existingProduct = productRepository.findByName(request.getName());

        // If the product already exists, throw an exception or return a specific response
        if (!existingProduct.isEmpty()) {
            // Optionally throw an exception or return an error message
            throw new IllegalArgumentException("Product already exists with the same name.");
        }

        // If the category doesn't exist, create a new one
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        // Set the category in the request
        request.setCategory(category);

        // Save the product to the repository
        return productRepository.save(createProduct(request, category));
    }


    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> {
                    throw new ProductNotFoundException("product not found");
                });

    }


    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("product not found"));

    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setName(category.getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {

        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String productName) {

        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String productName) {
        return productRepository.findByBrandAndName(brand, productName);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String productName) {
        return productRepository.countByBrandAndName(brand, productName);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        List<ProductDto>productDTOS=products.stream().map(this::convertToDto).toList();
        return productDTOS;
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDTO = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDTOS = images.stream().map(image -> modelMapper
                        .map(image, ImageDto.class))
                .toList();
        productDTO.setImages(imageDTOS);
        return productDTO;
    }

}