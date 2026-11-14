package dev.vagner.Commerce.response;

import dev.vagner.Commerce.enums.PedidoStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse (

       Long id,

       PedidoStatus pedidoStatus,

       BigDecimal totalAmount,

       List<PedidoItemResponse> items,

       LocalDateTime createdAt,

       LocalDateTime UpdateAt

){}
