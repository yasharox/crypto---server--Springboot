package com.yash.trading.controller;

import com.yash.trading.domain.OrderType;
import com.yash.trading.model.Coin;
import com.yash.trading.model.Order;
import com.yash.trading.model.User;
import com.yash.trading.request.CreateOrderRequest;
import com.yash.trading.service.CoinService;
import com.yash.trading.service.OrderService;
import com.yash.trading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userSerivce;

    @Autowired
    private CoinService coinService;

//    @Autowired
//    private WalletTransactionService walletTransactionService;



    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CreateOrderRequest req

    ) throws Exception {

        if (!authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid Authorization header");
        }

        String jwt = authHeader.substring(7).trim();

        User user = userSerivce.findUserProfileByJwt(jwt);
        Coin coin =coinService.findById(req.getCoinId());


        Order order = orderService.processOrder(
                coin,
                req.getQuantity(),
                req.getOrderType(),
                user);

        return ResponseEntity.ok(order);

    }

    @GetMapping("/id/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwtToken,
            @PathVariable Long orderId
    ) throws Exception {


        User user = userSerivce.findUserProfileByJwt(jwtToken);

        Order order = orderService.getOrderById(orderId);
        if (order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        } else {
            throw new Exception("You don't have access");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    ) throws Exception {

        if ( jwt.startsWith("Bearer ")){
            jwt =jwt.substring(7).trim();
        }


        Long userId = userSerivce.findUserProfileByJwt(jwt).getId();

        List<Order> userOrders = orderService.getAllOrdersOfUser(userId,order_type,asset_symbol);
        return ResponseEntity.ok(userOrders);
    }


}
