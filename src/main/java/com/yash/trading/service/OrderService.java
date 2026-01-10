package com.yash.trading.service;

import com.yash.trading.domain.OrderType;
import com.yash.trading.model.Coin;
import com.yash.trading.model.Order;
import com.yash.trading.model.OrderItem;
import com.yash.trading.model.User;

import java.util.List;

public interface OrderService {

    Order createOrder (User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById( Long orderId) throws Exception;

    List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol);

    Order processOrder (Coin coin, double quantity, OrderType orderType, User user) throws Exception;


}
