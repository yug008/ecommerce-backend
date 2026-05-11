package com.example.Ecommerce_Application.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Ecommerce_Application.dto.request.ProductRequest;
import com.example.Ecommerce_Application.dto.response.ProductResponse;
import com.example.Ecommerce_Application.entity.CartItem;
import com.example.Ecommerce_Application.entity.OrderItem;
import com.example.Ecommerce_Application.entity.Product;
import com.example.Ecommerce_Application.repository.CartItemRepository;
import com.example.Ecommerce_Application.repository.CartRepository;
import com.example.Ecommerce_Application.repository.OrderItemRepository;
import com.example.Ecommerce_Application.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository repo;

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    //Add product
    public ProductResponse addProduct(ProductRequest request, MultipartFile image) throws IOException {
        String imageUrl = uploadToCloudinary(image);
        //RequestDTO -> Entity
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .stock(request.getStock())
                .price(request.getPrice())
                .category(request.getCategory())
                .imageUrl(imageUrl)                    //not from requestdto but from the image we saved using saveImage method
                .build();

        //Save Entity to DB (using repo)
        Product savedProduct = repo.save(product);

        //Entity -> ResponseDTO
        return mapToResponse(savedProduct);
    }

    //Get all products
    public List<ProductResponse> getAllProducts(){
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //Get product by ID
    public ProductResponse getProductById(Long id){
        Product product = repo.findById(id).orElseThrow(()->new RuntimeException("Product not found."));
        return mapToResponse(product);
    }

    //Search by name (search bar)
    public List<ProductResponse> searchProducts(String name){
        return repo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //update product
    public ProductResponse updateProduct(Long id, ProductRequest request, MultipartFile image) throws IOException{
        Product product = repo.findById(id).orElseThrow(()->new RuntimeException("Product not found."));
        product.setName(request.getName());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());

        Product savedProduct = repo.save(product);

        if(image!=null && !image.isEmpty()){
            product.setImageUrl(uploadToCloudinary(image));
        }

        return mapToResponse(savedProduct);
    }

    //get product by category
    public List<ProductResponse> getByCategory(String category){
        return repo.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //delete product
    public void deleteProduct(Long id){
       Product product = repo.findById(id).orElseThrow(()->new RuntimeException("Product not found."));
      List<CartItem>cartItems = cartItemRepository.findByProduct(product);
      cartItemRepository.deleteAll(cartItems);           // Delete cart items referencing this product first
      List<OrderItem>orderItems = orderItemRepository.findByProduct(product);    //Delete order items referencing this product first
      orderItemRepository.deleteAll(orderItems);

      repo.delete((product));                             // Now delete product
    }

    //save image to Cloudinary image db.
    private String uploadToCloudinary(MultipartFile image) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.asMap(
                        "folder", "ecommerce/products"
                )
        );
        return uploadResult.get("secure_url").toString();
    }

    //Method 'mapToResponse' for Entity->ResponseDTO. Create it once and use it anywhere
    private ProductResponse mapToResponse(Product product){               //we create mapToResponse method once and reuse it everytime we need to convert to ResponseDTO
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .build();
    }

}


/*
All the methods in Service layer will return ResponseDTO. Thus, mapToResponse() which returns ResponseDTO will be used in every method. Except DELETE.
 */