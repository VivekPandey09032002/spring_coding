package com.vivek.java_testing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "my_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class User {
    @Id
    private String email;
    private String userName;
    private String description;
    @OneToMany(mappedBy = "user")
    private List<Attendance> attendanceList;
}
