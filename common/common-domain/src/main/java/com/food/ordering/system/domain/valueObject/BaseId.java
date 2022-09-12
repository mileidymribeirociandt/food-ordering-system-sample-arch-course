package com.food.ordering.system.domain.valueObject;

import java.util.Objects;

public abstract class BaseId <T>{
    private final T id;

    protected BaseId(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return id.equals(baseId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
