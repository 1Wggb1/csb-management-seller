package br.com.casasbahia.dto;

public record SellerDTO(
    String name,
    String enrollment,
    String birthDay,
    String documentNumber,
    String email,
    String contractType,
    BranchOfficeDTO branchOffice )
{
}