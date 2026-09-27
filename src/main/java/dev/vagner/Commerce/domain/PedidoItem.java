package dev.vagner.Commerce.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name = "order_item")
public class PedidoItem {

    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private float unitPrice;
    private float subTotal;

    @ManyToOne
    private Pedido pedido;


}
