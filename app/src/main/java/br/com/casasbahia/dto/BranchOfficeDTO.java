package br.com.casasbahia.dto;

public record BranchOfficeDTO(
    Long id,
    String name,
    String documentNumber,
    String city,
    String state,
    String type,
    boolean active,
    String creationDateTime,
    String updateDateTime )
{
}