package com.infinitycart.controller;


import com.infinitycart.model.Cart;
import com.infinitycart.model.CartItem;
import com.infinitycart.model.Product;
import com.infinitycart.model.User;
import com.infinitycart.request.AddItemRequest;
import com.infinitycart.response.ApiResponse;
import com.infinitycart.service.CartItemService;
import com.infinitycart.service.CartService;
import com.infinitycart.service.ProductService;
import com.infinitycart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req, @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);

        Product product = productService.findProductById(req.getProductId());

        CartItem cartItem = cartService.addCartItem(user, product, req.getSize(), req.getQuantity());
        ApiResponse res = new ApiResponse();
        res.setMessage("Item Added to Cart");
        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
      User user = userService.findUserByJwtToken(jwt);
      cartItemService.removeCartItem(user.getId(), cartItemId);

      ApiResponse res = new ApiResponse();
      res.setMessage("Item removed from cart");
      return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
       User user = userService.findUserByJwtToken(jwt);

       CartItem updateCartItem = null;

       if(cartItem.getQuantity() > 0){
           updateCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
       }

       ApiResponse res = new ApiResponse();
       res.setMessage("Item updated in cart");
       return new ResponseEntity<ApiResponse>(res, HttpStatus.ACCEPTED);

    }



    
}
