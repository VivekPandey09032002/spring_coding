package com.learn.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
public class Address {
    @Id private long addressId;
    @NonNull private String addressLine;
    @OneToOne
    private Student student;

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", addressLine='" + addressLine + '\'' +
                '}';
    }
}
