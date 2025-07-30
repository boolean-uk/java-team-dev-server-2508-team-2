package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.DeliveryLog;
import lombok.Getter;

@Getter
public class DeliveryLogResponse extends Response {
    public void set(DeliveryLog log) {
        Data<DeliveryLog> data = new DeliveryLogData();
        data.set(log);
        super.set(data);
    }
}
