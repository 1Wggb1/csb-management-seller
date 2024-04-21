package br.com.casasbahia.service;

import org.springframework.data.domain.Pageable;
import br.com.casasbahia.dto.SellerDTO;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;

public interface SellerService
{
    SellerResponseDTO create(
        SellerRequestDTO sellerRequestDTO );

    void update(
        String enrollment,
        SellerRequestDTO sellerRequestDTO );

    SellerPageableDTO findAll(
        String filter,
        Pageable pageable );

    SellerDTO findByEnrollment(
        String enrollment );

    void delete(
        String enrollment );
}