package com.tumi.haul.model.primitives;

import jakarta.persistence.Column;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
@Getter
public class PhoneNumber implements Serializable {
    @Column(name = "phoneNumber")
    private final String value;

    public PhoneNumber(final String phoneNumber) {
        if ((phoneNumber.length() != 10))
            throw new IllegalArgumentException("phone number must be 10 digits");
        if ((!phoneNumber.matches("0[0-9]{9}")))
            throw new IllegalArgumentException("phone number must start with 0 and" +
                    " be made up of 10 digits");
        this.value = phoneNumber;
    }

    public String getInternationalFormat(){
        return value.startsWith("+")? value :"+254" + value.substring(1);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "PhoneNumber{" +
                "value='" + value + '\'' +
                '}';
    }

    public static String fixPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 9)
            return "0" + phoneNumber;
        return phoneNumber;
    }
/*
    public static ATPhoneNumber convertToATType(PhoneNumber phoneNumber) {
        return new ATPhoneNumber("+254" + phoneNumber
                .getValue().substring(1));
    }*/

}
