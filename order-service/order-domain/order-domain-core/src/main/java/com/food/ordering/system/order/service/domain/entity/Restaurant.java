package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.RestaurantId;

import java.util.List;
import java.util.Map;

public class Restaurant extends AggregateRoot<RestaurantId> {
    private final Map<String, Product> products;
    private boolean active;

    private Restaurant(Builder builder) {
        super(builder.restaurantId);
        products = builder.products;
        active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public boolean isActive() {
        return active;
    }

    public static final class Builder {
        private RestaurantId restaurantId;
        private Map<String, Product> products;
        private boolean active;

        private Builder() {
        }

        public Builder id(RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Builder products(Map<String, Product> products) {
            this.products = products;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}
