package br.com.casasbahia.dto;

public record PageableDTO(
    int page,
    int size,
    int numberOfElements,
    int totalPages,
    int totalElements )
{
}
