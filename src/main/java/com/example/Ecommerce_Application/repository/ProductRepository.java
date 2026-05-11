package com.example.Ecommerce_Application.repository;

import com.example.Ecommerce_Application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategory(String category);                              //filter products by category
    List<Product> findByNameContainingIgnoreCase(String name);                  //search products by name (like a search bar)

}


/* In 'findByNameContainingIgnoreCase(String name)' :

-findBy → SELECT * FROM products WHERE...
-Name → ...name...
-Containing → ...LIKE '%value%'... (contains the word anywhere)
-IgnoreCase → ...case insensitive (uppercase/lowercase doesn't matter)

It basically acts as a search bar query.

Example:
- "javafindByNameContainingIgnoreCase("shirt")" would return:
✅ "Blue Shirt"
✅ "SHIRT XL"
✅ "shirt collection"
❌ "Shoes"

 */