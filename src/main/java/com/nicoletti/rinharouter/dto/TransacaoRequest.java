package com.nicoletti.rinharouter.dto;

public record TransacaoRequest(
        int valor,
        String tipo,
        String descricao
) {}