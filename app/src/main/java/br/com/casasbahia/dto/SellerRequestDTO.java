package br.com.casasbahia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SellerRequestDTO(
    @NotNull String name,
    String birthDate,
    @NotNull String documentNumber,
    @NotNull @Positive Long contractTypeId,
    @NotNull String branchOfficeDocumentNumber )
{
}