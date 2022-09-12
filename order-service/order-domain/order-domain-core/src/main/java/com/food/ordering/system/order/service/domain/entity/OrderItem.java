package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueObject.Money;
import com.food.ordering.system.domain.valueObject.OrderId;
import com.food.ordering.system.order.service.domain.valueObject.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;
    private final Product product;
    private final int quantity;
    private final Money price;
    private final Money subtotal;

    private OrderItem(OrderItemBuilder builder) {
        super(builder.orderItemId);
        product = builder.product;
        quantity = builder.quantity;
        price = builder.price;
        subtotal = builder.subtotal;
    }

    boolean isPriceValid(){
        return price.isGreaterThanZero()
                && price.equals(product.getPrice())
                && price.multiply(quantity).equals(subtotal);
    }

    void initializeOrderItem(OrderId orderId, OrderItemId orderItemId){
        this.orderId = orderId;
        this.setId(orderItemId);
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getPrice() {
        return price;
    }

    public Money getSubtotal() {
        return subtotal;
    }

    public static final class OrderItemBuilder {
        private OrderItemId orderItemId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subtotal;

        private OrderItemBuilder() {
        }

        public OrderItemBuilder id(OrderItemId orderItemId) {
            this.orderItemId = orderItemId;
            return this;
        }

        public OrderItemBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public OrderItemBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder price(Money price) {
            this.price = price;
            return this;
        }

        public OrderItemBuilder subtotal(Money subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this);
        }
    }
}
