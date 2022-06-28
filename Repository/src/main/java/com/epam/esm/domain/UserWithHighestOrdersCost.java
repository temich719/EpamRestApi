package com.epam.esm.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class UserWithHighestOrdersCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;
    private String userName;
    private int sum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserWithHighestOrdersCost that = (UserWithHighestOrdersCost) o;

        if (user_id != that.user_id) return false;
        if (sum != that.sum) return false;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        int result = (int) (user_id ^ (user_id >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + sum;
        return result;
    }

    @Override
    public String toString() {
        return "UserWithHighestOrdersCost{" +
                "userName='" + userName + '\'' +
                ", user_id=" + user_id +
                ", sum=" + sum +
                '}';
    }
}
