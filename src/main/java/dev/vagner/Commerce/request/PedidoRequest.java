package dev.vagner.Commerce.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PedidoRequest (

    @NotEmpty(message = "O pedido deve possuir pelo menos um item")
    List<PedidoItemRequest> items


){
}
