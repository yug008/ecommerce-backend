package com.example.Ecommerce_Application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne                                                        //each user has one cart only
    @JoinColumn(name = "user_id",nullable = false)                   //a separate column "user_id" for each cart
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)            //each cart has multiple cart items
    private List<CartItem> items = new ArrayList<>();                                         //here after linking both entities there is no separate linked column as we have created it in cartItem class as we need the linked column which mentions the cart of items in the cartItem table

    public double getTotalPrice(){                                        //its just like other fields in this class
        return items.stream()
                .mapToDouble(item->item.getProduct().getPrice()*item.getQuantity())
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}


/*  cascade = CascadeType.ALL — if cart is deleted, all its items are deleted too
    orphanRemoval = true — if item is removed from the list, it's deleted from DB

 */