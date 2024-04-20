package br.com.casasbahia.dto;

public record PageableDTO(
    long page,
    long size,
    long numberOfElements,
    long totalPages,
    long totalElements )
{
}
