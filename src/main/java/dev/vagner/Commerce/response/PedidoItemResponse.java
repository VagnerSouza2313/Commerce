package dev.vagner.Commerce.response;

import java.math.BigDecimal;


public record PedidoItemResponse(

        Long id,

        Long productId,

        String nameProduct,

        Integer quantity,

        BigDecimal initPrice,

        BigDecimal subTotal

) {
}
