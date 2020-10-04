package com.spring.demo.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.spring.demo.common.db.PostgreSQLEnumType;
import com.spring.demo.order.domain.OrderItem;
import com.spring.demo.order.domain.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@Entity(name = "orders")
@TypeDef(name = "order_status", typeClass = PostgreSQLEnumType.class)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Generated(GenerationTime.ALWAYS)
    private Long id;

    @Column(name = "customer_id", columnDefinition = "uuid")
    @org.hibernate.annotations.Type(type="org.hibernate.type.PostgresUUIDType")
    @Generated(GenerationTime.ALWAYS)
    private UUID customerId;

    @NotNull
    @Column(name = "order_date")
    private ZonedDateTime orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "order_status")
    private OrderStatus orderStatus;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = OrderItemEntity.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonManagedReference
    private List<OrderItem> orderItems;
}
