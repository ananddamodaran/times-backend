package com.viginfotech.chennaitimes.backend.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;


@Entity
public class RegistrationRecord {

    @Id
    Long id;

    @Index
    private String regId;

    public RegistrationRecord() {
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }
}
