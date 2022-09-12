package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.valueObject.CustomerId;

/**
 * empty, just to cerify if there is and customer
 */
public class Customer extends AggregateRoot<CustomerId> {
    public Customer(CustomerId customerId) {
        super(customerId);
    }
}
