package dev.vagner.Commerce.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PedidoItemRequest (

    @NotNull(message = "O produto é obrigatório")
    Long productId,

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    Integer quantity

){
}

