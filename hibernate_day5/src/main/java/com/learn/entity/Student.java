package com.learn.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@Cacheable


@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "hibernate_student")
public class Student {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id private long id;
    @NonNull private String name;
    @OneToOne(mappedBy = "student",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Address address;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
