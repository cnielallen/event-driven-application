package com.cnielallen.eventdriven.event;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {
    @JsonProperty("Create")
    CREATE,
    @JsonProperty("Update")
    UPDATE;

    private  EventType(){
        
    }
}
