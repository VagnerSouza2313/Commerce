package dev.vagner.Commerce.mapper;

import dev.vagner.Commerce.domain.Pedido;
import dev.vagner.Commerce.domain.PedidoItem;
import dev.vagner.Commerce.request.PedidoRequest;
import dev.vagner.Commerce.response.PedidoItemResponse;
import dev.vagner.Commerce.response.PedidoResponse;
import org.hibernate.query.Order;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.stream;

public class PedidoMapper {

    public static Pedido toEntity(PedidoRequest pedidoRequest) {
        Pedido pedido = new Pedido();

        List<PedidoItem> items = pedidoRequest.items()
                .stream()
                .map(itemRequest -> {
                    PedidoItem item = new PedidoItem();
                    item.setProductId(itemRequest.productId());
                    item.setQuantity(itemRequest.quantity());
                    return item;
                })
                .toList();

        pedido.setItems(items);
        return pedido;
    }

    public static PedidoResponse toResponse(Pedido pedido){

        List<PedidoItemResponse> items = pedido.getItems()
                .stream()
                .map(PedidoMapper::toResponse)
                .toList();

        return  new PedidoResponse(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getTotalAmount(),
                items,
                pedido.getCreatedAt(),
                pedido.getUpdateAt()
        );
    }

    private static PedidoItemResponse toResponse(PedidoItem pedidoItem) {
        return new PedidoItemResponse(
                pedidoItem.getId(),
                pedidoItem.getProductId(),
                pedidoItem.getProductName(),
                pedidoItem.getQuantity(),
                BigDecimal.valueOf(pedidoItem.getUnitPrice()),
                BigDecimal.valueOf(pedidoItem.getSubTotal())
        );
    }

}
