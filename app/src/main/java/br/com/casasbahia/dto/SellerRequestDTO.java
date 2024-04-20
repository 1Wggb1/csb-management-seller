package br.com.casasbahia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SellerRequestDTO(
    @NotNull String name,
    @NotNull @Email String email,
    String birthDay,
    @NotNull String documentNumber,
    @NotNull String contractType,
    @NotNull String branchOfficeDocumentNumber )
{
}