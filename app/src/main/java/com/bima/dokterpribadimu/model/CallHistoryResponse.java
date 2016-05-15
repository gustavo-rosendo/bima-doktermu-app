package com.bima.dokterpribadimu.model;

import java.util.List;

/**
 * Created by gusta_000 on 16/5/2016.
 */
public class CallHistoryResponse {

    private List<BimaCall> booking;

    public List<BimaCall> getCalls() {
        return booking;
    }

    public void setCalls(List<BimaCall> booking) {
        this.booking = booking;
    }
}
