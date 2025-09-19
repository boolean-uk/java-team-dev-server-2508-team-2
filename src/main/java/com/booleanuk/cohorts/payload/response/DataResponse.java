package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.DeliveryLog;

import java.util.List;

public class DataResponse<T> extends Response{
    public void set(T item) {
        Data<T> data = new AnyData<T>();
        data.set(item);
        super.set(data);
    }

    public void set(List<T> list) {
        Data<List<T>> data = new AnyData<>();
        data.set(list);
        super.set(data);
    }
}
