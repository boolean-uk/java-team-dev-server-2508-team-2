package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.DeliveryLog;
import lombok.Getter;

@Getter
public class DeliveryLogData extends Data<DeliveryLog> {
    protected DeliveryLog log;

    @Override
    public void set(DeliveryLog log) {
        this.log = log;
    }
}
