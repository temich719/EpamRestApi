package com.epam.esm.domain;

import com.epam.esm.audit.Audit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String password;
    private String role;

    @OneToMany
    @JoinTable(name = "users_orders", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "order_id")})
    private List<Order> orders = new ArrayList<>();

    @Transient
    private List<Tag> tags = new ArrayList<>();

    @Transient
    private int sum;

    @Embedded
    private Audit audit = new Audit();

    public User(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (sum != user.sum) return false;
        if (!Objects.equals(userName, user.userName)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(role, user.role)) return false;
        if (!Objects.equals(orders, user.orders)) return false;
        if (!Objects.equals(tags, user.tags)) return false;
        return Objects.equals(audit, user.audit);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + sum;
        result = 31 * result + (audit != null ? audit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", orders=" + orders +
                ", tags=" + tags +
                ", sum=" + sum +
                ", audit=" + audit +
                '}';
    }
}
