package com.epam.esm.domain;

import com.epam.esm.audit.Audit;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String date;
    private String cost;

    @Transient
    private User user;

    @ManyToMany
    @JoinTable(name = "orders_certificates", joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "certificate_id")})
    private List<GiftCertificate> giftCertificates = new ArrayList<>();

    @Embedded
    private Audit audit = new Audit();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (id != order.id) return false;
        if (!Objects.equals(date, order.date)) return false;
        if (!Objects.equals(cost, order.cost)) return false;
        if (!Objects.equals(user, order.user)) return false;
        if (!Objects.equals(giftCertificates, order.giftCertificates))
            return false;
        return Objects.equals(audit, order.audit);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (giftCertificates != null ? giftCertificates.hashCode() : 0);
        result = 31 * result + (audit != null ? audit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", cost='" + cost + '\'' +
                ", user=" + user +
                ", giftCertificates=" + giftCertificates +
                ", audit=" + audit +
                '}';
    }
}
