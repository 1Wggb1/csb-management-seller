package br.com.casasbahia.service;

import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;

public interface SellerService
{
    SellerResponseDTO create(
        SellerRequestDTO sellerRequestDTO );
}