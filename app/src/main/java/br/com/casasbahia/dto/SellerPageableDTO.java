package br.com.casasbahia.dto;

import java.util.List;

public record SellerPageableDTO(
    PageableDTO pageable,
    List<SellerDTO> elements )
{
}