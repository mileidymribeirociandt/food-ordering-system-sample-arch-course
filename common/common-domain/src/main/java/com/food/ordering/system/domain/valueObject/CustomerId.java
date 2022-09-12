package com.food.ordering.system.domain.valueObject;

import java.util.UUID;

public class CustomerId extends BaseId<UUID>{
    public CustomerId(UUID id) {
        super(id);
    }
}
