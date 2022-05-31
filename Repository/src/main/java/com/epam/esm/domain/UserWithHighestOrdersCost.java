package com.epam.esm.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserWithHighestOrdersCost {

    String userName;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long user_id;
    int sum;

    @Override
    public String toString() {
        return "UserWithHighestOrdersCost{" +
                "userName='" + userName + '\'' +
                ", user_id=" + user_id +
                ", sum=" + sum +
                '}';
    }
}
