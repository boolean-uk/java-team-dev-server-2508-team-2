package com.booleanuk.cohorts.payload.response;

import com.booleanuk.cohorts.models.DeliveryLog;
import lombok.Getter;

import java.util.List;

@Getter
public class DeliveryLogListData extends Data<List<DeliveryLog>> {
    protected List<DeliveryLog> logs;

    @Override
    public void set(List<DeliveryLog> logs) {
        this.logs = logs;
    }
}
