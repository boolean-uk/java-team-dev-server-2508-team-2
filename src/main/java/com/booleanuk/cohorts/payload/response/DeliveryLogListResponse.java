package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.DeliveryLog;
import lombok.Getter;

import java.util.List;

@Getter
public class DeliveryLogListResponse extends Response {
    public void set(List<DeliveryLog> logs) {
        Data<List<DeliveryLog>> data = new DeliveryLogListData();
        data.set(logs);
        super.set(data);
    }
}
