package br.com.casasbahia.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.casasbahia.converter.SellerConverter;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;

@Service
public class SellerConverterImpl
    implements
        SellerConverter
{
    @Autowired
    private SellerRepository repository;

    @Override
    public PersistentSeller toModel(
        final SellerRequestDTO sellerRequestDTO )
    {
        final ContractType contractType = ContractType.valueOf( sellerRequestDTO.contractType() );
        return new PersistentSeller(
            sellerRequestDTO.name(),
            repository.generateEnrollment( contractType ),
            sellerRequestDTO.birthDay(),
            sellerRequestDTO.documentNumber(),
            sellerRequestDTO.email(),
            contractType,
            sellerRequestDTO.branchOfficeDocumentNumber() );
    }

    @Override
    public SellerResponseDTO toDTO(
        final PersistentSeller persistentSeller )
    {
        return new SellerResponseDTO( persistentSeller.getEnrollment() );
    }
}
