package com.shops.dreamshops.controller;
import com.shops.dreamshops.dto.ProductDto;
import com.shops.dreamshops.exception.ProductNotFoundException;
import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.request.product.AddProductRequest;
import com.shops.dreamshops.request.product.UpdateProductRequest;
import com.shops.dreamshops.response.ApiResponse;
import com.shops.dreamshops.service.product.ProductService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {

        List<Product> products = productService.getAllProducts();
        List<ProductDto>productDTOS=productService.getConvertedProducts(products);

        return  ResponseEntity.ok(new ApiResponse("success", productDTOS));
    }

    @GetMapping("/product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            var productDTO=productService.convertToDto(product);
            if(product == null) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", NOT_FOUND));
            }
            return ResponseEntity.ok(new ApiResponse("product", productDTO));
        } catch (ProductNotFoundException  e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createProduct(@RequestBody AddProductRequest request) {

        try {
            Product product = productService.addProduct(request);
            var productDTO= productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Add product success", productDTO));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId) {

        try {
            Product product = productService.updateProduct(request, productId);
            var productDTO= productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Update product success", productDTO));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }


    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(new ApiResponse("Delete product success", productId));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    @GetMapping("/by/brand-and-name/{brand}/{name}")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@PathVariable String brand, @PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            List<ProductDto>productDTOS=productService.getConvertedProducts(products);
            if (productDTOS.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("products", productDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/category-and-brand")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            List<ProductDto>productDTOS=productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("products",productDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by/name/{productName}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable  String productName) {

        try {
            List<Product>products= productService.getProductsByName(productName);
            List<ProductDto>productDTOS=productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("products", productDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/{category}")
    public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable  String category) {

        try {
            List<Product> products=productService.getProductsByCategory(category);
            List<ProductDto>productDTOS=productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));
            }
            return ResponseEntity.ok(new ApiResponse("products", productDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand( @RequestParam String brand) {

        try {
            List<Product>products= productService.getProductsByBrand(brand);
            List<ProductDto>productDTOS=productService.getConvertedProducts(products);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Product not found", null));

            }
            return ResponseEntity.ok(new ApiResponse("products", productDTOS));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("/product/count/by-brand/and-name")
    public ResponseEntity<ApiResponse> countProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var productCount = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", productCount));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(), null));
        }
    }
}
