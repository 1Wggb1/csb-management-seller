package br.com.casasbahia.converter;

import java.util.List;

import org.springframework.data.domain.Page;
import br.com.casasbahia.dto.SellerDTO;
import br.com.casasbahia.dto.SellerFilterDTO;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.PersistentSeller;

public interface SellerConverter
{
    PersistentSeller toModelCreation(
        SellerRequestDTO sellerRequestDTO );

    PersistentSeller toModelUpdate(
        PersistentSeller persistentSeller,
        SellerRequestDTO sellerRequestDTO );

    SellerResponseDTO toDTO(
        PersistentSeller persistentSeller );

    SellerPageableDTO toPageableDTO(
        Page<PersistentSeller> persistentSeller );

    SellerFilterDTO toFilterDTO(
        String filter );

    SellerDTO toSellerDTO(
        PersistentSeller persistentSeller );

    default List<SellerDTO> toSellerDTO(
        final List<PersistentSeller> persistentSellers )
    {
        return persistentSellers.stream()
            .map( this::toSellerDTO )
            .toList();
    }
}