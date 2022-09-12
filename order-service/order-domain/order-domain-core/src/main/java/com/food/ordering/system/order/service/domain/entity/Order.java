package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.*;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueObject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueObject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueObject.TrackingId;

import java.util.List;
import java.util.UUID;

public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress delieveryAddress;
    private final Money price;
    private final List<OrderItem> orderItems;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Order(Builder builder) {
        super(builder.orderId);
        customerId = builder.customerId;
        restaurantId = builder.restaurantId;
        delieveryAddress = builder.delieveryAddress;
        price = builder.price;
        orderItems = builder.orderItems;
        trackingId = builder.trackingId;
        orderStatus = builder.orderStatus;
        failureMessages = builder.failureMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void initializeOrder(){
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }

    public void validateOrder(){
        validateInitialOrder();
        validateTotalPrice();
        validateItemsPrice();
    }

    private void validateItemsPrice() {
        Money orderItemsTotal = orderItems.stream().map(orderItem -> {
            validateItemPrice(orderItem);
            return orderItem.getSubtotal();
        }).reduce(Money.ZERO,Money::add);

        if(!price.equals(orderItemsTotal)){
            throw  new OrderDomainException("Total price: " + price.getAmount() + "is not equal to Order items total " +
                    orderItemsTotal.getAmount() + "!");
        }
    }

    public void pay(){
        if(orderStatus != OrderStatus.PENDING){
            throw new OrderDomainException("Order is not in the correct status for pay operation.");
        }
        orderStatus = OrderStatus.PAID;
    }

    public void approve(){
        if(orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order is not in the correct status for approve operation.");
        }
        orderStatus = OrderStatus.APPROVED;
    }

    public void initCancel(List<String> failureMessages){
        if(orderStatus != OrderStatus.PAID){
            throw new OrderDomainException("Order is not in the correct status for init cancel operation.");
        }
        updateFailureMessages(failureMessages);
        orderStatus = OrderStatus.CANCELLING;
    }

    public void cancel(List<String> failureMessages){
        if(!(orderStatus == OrderStatus.CANCELLING || orderStatus == OrderStatus.PENDING)){
            throw new OrderDomainException("Order is not in the correct status for cancel operation.");
        }
        updateFailureMessages(failureMessages);
        orderStatus = OrderStatus.CANCELLED;
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if(failureMessages != null && this.failureMessages != null){
            this.failureMessages.addAll(failureMessages.stream()
                    .filter(failureMessage -> !failureMessage.isEmpty())
                    .toList());
        }
        if(this.failureMessages == null){
            this.failureMessages = failureMessages;
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if(!orderItem.isPriceValid()){
            throw new OrderDomainException("Order item price: " + orderItem.getPrice().getAmount() +
                    " is not valid for product " + orderItem.getProduct().getId().getValue());
        }
    }

    private void validateTotalPrice() {
        if(price == null || !price.isGreaterThanZero()){
            throw new OrderDomainException("Total price must be greater than zero!");
        }
    }

    private void validateInitialOrder() {
        if(orderStatus != null){
            throw new OrderDomainException("Order is not in the correct state of initialization!");
        }
    }

    private void initializeOrderItems() {
        long itemId = 1;
        for(OrderItem orderItem : orderItems){
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }

    public CustomerId getCostumerId() {
        return customerId;
    }

    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    public StreetAddress getDelieveryAddress() {
        return delieveryAddress;
    }

    public Money getPrice() {
        return price;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public TrackingId getTrackingId() {
        return trackingId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public static final class Builder {
        private OrderId orderId;
        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress delieveryAddress;
        private Money price;
        private List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        private Builder() {
        }

        public Builder id(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder costumerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder restaurantId(RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Builder delieveryAddress(StreetAddress streetAddress) {
            delieveryAddress = streetAddress;
            return this;
        }

        public Builder price(Money price) {
            this.price = price;
            return this;
        }

        public Builder orderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public Builder trackingId(TrackingId trackingId) {
            this.trackingId = trackingId;
            return this;
        }

        public Builder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public Builder failureMessages(List<String> failureMessages) {
            this.failureMessages = failureMessages;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
