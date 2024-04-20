package br.com.casasbahia.dto;

import java.util.List;

public record ErrorDTO(
    String status,
    List<String> errorMessages,
    String dateTime )
{
}