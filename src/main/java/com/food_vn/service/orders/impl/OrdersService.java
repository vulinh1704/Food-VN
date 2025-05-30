package com.food_vn.service.orders.impl;

import com.food_vn.lib.base_serivce.BaseService;
import com.food_vn.model.conpons.Coupon;
import com.food_vn.model.order_details.OrderDetail;
import com.food_vn.model.orders.OrderResponse;
import com.food_vn.model.orders.Orders;
import com.food_vn.model.orders.OrdersDTO;
import com.food_vn.model.products.Product;
import com.food_vn.repository.order_details.OrderDetailsRepository;
import com.food_vn.repository.orders.OrdersRepository;
import com.food_vn.repository.products.ProductRepository;
import com.food_vn.service.orders.IOrdersService;
import com.food_vn.service.revenue.IRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrdersService extends BaseService implements IOrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private IRevenueService revenueService;

    public Orders updateStatus(Orders orders) {
        Orders oldOrders = ordersRepository.findById(orders.getId()).orElse(null);
        assert oldOrders != null;
        int oldStatus = oldOrders.getStatus();
        oldOrders.setStatus(orders.getStatus());
        if(orders.getCancellationReason() != null && !orders.getCancellationReason().isEmpty()) {
            oldOrders.setCancellationReason(orders.getCancellationReason());
        }
        Orders newOrder = ordersRepository.save(oldOrders);
        if (oldStatus != 4 && orders.getStatus() == 4) {
            _handleRevenueWhenStatusToSuccess(newOrder);
        }
        if (orders.getStatus() == 0) {
            List<OrderDetail> list = orderDetailsRepository.findByOrdersId(orders.getId());
            for (OrderDetail detail : list) {
                Product product = productRepository.findById(detail.getProduct().getId()).orElseThrow(() -> new RuntimeException("Product not found"));
                product.setQuantity(product.getQuantity() + detail.getQuantity());
                productRepository.save(product);
            }
        }
        return newOrder;
    }

    public Orders getCard(Long userId) {
        return ordersRepository.findByUserIdAndStatus(userId, 1);
    }

    public List<OrderResponse> getListOrdersByUserId(Long userId) {
        List<Orders> orders = ordersRepository.findAllByUserIdAndStatus(userId, 2);
        return getOrderResponses(orders);
    }

    public Orders buy(OrdersDTO ordersDTO) {
        Orders order = ordersRepository.findById(ordersDTO.getId()).get();
        order.setDate(new Date());
        order.setAddress(ordersDTO.getAddress());
        order.setStatus(2);

        double total = 0.0;
        Date now = new Date();
        List<OrderDetail> orderDetails = Arrays.asList(ordersDTO.getOrderDetails());
        for (OrderDetail detail : orderDetails) {
            Product product = productRepository.findById(detail.getProduct().getId()).orElseThrow(() -> new RuntimeException("Product not found"));
            List<Coupon> validCoupons = Optional.ofNullable(product.getCoupons())
                    .orElse(Collections.emptySet())
                    .stream()
                    .filter(c ->
                            (c.getFromDate() == null || !now.before(c.getFromDate())) &&
                                    (c.getToDate() == null || !now.after(c.getToDate()))
                    )
                    .sorted(Comparator.comparing(
                            Coupon::getFromDate,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    ))
                    .toList();

            double finalPrice = product.getPrice();
            for (Coupon coupon : validCoupons) {
                double discountValue = coupon.getDiscount().doubleValue();
                if ("percent".equalsIgnoreCase(coupon.getType())) {
                    finalPrice -= (finalPrice * discountValue / 100.0);
                } else if ("fixed".equalsIgnoreCase(coupon.getType())) {
                    finalPrice -= discountValue;
                }

                finalPrice = Math.max(finalPrice, 0.0);
            }
            finalPrice = Math.max(finalPrice, 0.0);
            OrderDetail od = orderDetailsRepository.findById(detail.getId()).get();
            od.setOrders(order);
            od.setProduct(product);
            od.setPrice(finalPrice);
            od.setQuantity(detail.getQuantity());
            od.setCoupons(detail.getCoupons());
            orderDetailsRepository.save(od);
            total += finalPrice * od.getQuantity();
            product.setQuantity(product.getQuantity() - detail.getQuantity());
            productRepository.save(product);
        }

        order.setTotal(total);
        Orders output = ordersRepository.save(order);
        _createNewOrder(order);
        return output;
    }

    public Page<OrderResponse> getListUserInvoices(LocalDateTime startDate, LocalDateTime endDate, Integer status, Pageable pageable, Long userId) {
        Page<Orders> ordersPage = ordersRepository.findByDateRangeStatusAndUserId(startDate, endDate, status, userId, pageable);
        List<OrderResponse> orderResponses = getOrderResponses(ordersPage.getContent());
        return new PageImpl<>(
                orderResponses,
                pageable,
                ordersPage.getTotalElements()
        );
    }

    public Page<OrderResponse> getList(LocalDateTime startDate, LocalDateTime endDate, Integer status, Pageable pageable) {
        Page<Orders> ordersPage = ordersRepository.findByDateRangeAndStatus(startDate, endDate, status, pageable);
        List<OrderResponse> orderResponses = getOrderResponses(ordersPage.getContent());
        return new PageImpl<>(
                orderResponses,
                pageable,
                ordersPage.getTotalElements()
        );
    }

    private List<OrderResponse> getOrderResponses(List<Orders> orders) {
        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Orders order : orders) {
            List<OrderDetail> ods = orderDetailsRepository.findByOrdersId(order.getId());
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(order.getId());
            orderResponse.setStatus(order.getStatus());
            orderResponse.setOrderDetails((ods));
            orderResponse.setDate(order.getDate());
            orderResponse.setTotal(order.getTotal());
            orderResponse.setUser(order.getUser());
            orderResponse.setAddress(order.getAddress());
            orderResponse.setCancellationReason(order.getCancellationReason());
            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }

    private void _createNewOrder(Orders oldOrders) {
        Orders order = new Orders();
        order.setUser(oldOrders.getUser());
        order.setStatus(1);
        order.setTotal(0.0);
        ordersRepository.save(order);
    }

    private void _handleRevenueWhenStatusToSuccess(Orders order) {
        revenueService.saveRevenueForDate(order.getDate(), order.getTotal());
    }
}
