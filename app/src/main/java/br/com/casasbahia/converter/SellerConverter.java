package br.com.casasbahia.converter;

import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.PersistentSeller;

public interface SellerConverter
{
    PersistentSeller toModel(
        SellerRequestDTO sellerRequestDTO );

    SellerResponseDTO toDTO(
        PersistentSeller persistentSeller );
}