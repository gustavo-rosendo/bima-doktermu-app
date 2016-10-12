package com.bima.dokterpribadimu.model;

/**
 * Created by gusta_000 on 13/10/2016.
 */

public class CallAssignedResponse {
    
    CallAssigned callAssignment; //this name MUST match the name in the JSON response from the backend

    public CallAssigned getCallAssignment() {
        return callAssignment;
    }

    public void setCallAssignment(CallAssigned callAssignment) {
        this.callAssignment = callAssignment;
    }
}
