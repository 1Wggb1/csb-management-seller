package br.com.casasbahia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SellerRequestDTO(
    @NotEmpty( message = "csb.field.cannot.be.null_or_empty" ) String name,
    @NotEmpty( message = "csb.field.cannot.be.null_or_empty" ) @Email( message = "csb.email.invalid.format" ) String email,
    String birthDay,
    @NotEmpty( message = "csb.field.cannot.be.null_or_empty" ) String documentNumber,
    @NotEmpty( message = "csb.field.cannot.be.null_or_empty" ) String contractType,
    @NotEmpty( message = "csb.field.cannot.be.null_or_empty" ) String branchOfficeDocumentNumber )
{
}