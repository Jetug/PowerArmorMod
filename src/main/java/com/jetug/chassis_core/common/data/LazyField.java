package com.jetug.chassis_core.common.data;

import java.util.function.Supplier;

public class LazyField<T>{
    private final Supplier<T> supplier;
    private T field = null;

    public LazyField(Supplier<T> supplier){
        this.supplier = supplier;
    }

    private T get(){
        if(field == null){
            field = supplier.get();
        }

        return field;
    }
}