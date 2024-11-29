package com.shops.dreamshops.service.product;

import com.shops.dreamshops.dto.ProductDto;
import com.shops.dreamshops.model.Product;
import com.shops.dreamshops.request.product.AddProductRequest;
import com.shops.dreamshops.request.product.UpdateProductRequest;

import java.util.List;


public interface IProductService {

    Product getProductById(Long id);
    Product addProduct(AddProductRequest product);
    void deleteProduct(Long id);
    Product updateProduct(UpdateProductRequest request, Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String productName);
    List<Product> getProductsByBrandAndName(String brand, String productName);
    Long countProductsByBrandAndName(String brand, String productName);

    List<ProductDto> getConvertedProducts(List<Product> products);
    ProductDto convertToDto(Product product);
}
