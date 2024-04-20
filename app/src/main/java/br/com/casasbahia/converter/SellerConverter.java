package br.com.casasbahia.converter;

import org.springframework.stereotype.Service;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.util.EnrollmentGenerator;

@Service
public class SellerConverter
{
    public PersistentSeller toModel(
        final SellerRequestDTO sellerRequestDTO )
    {
        final ContractType contractType = ContractType.valueOf( sellerRequestDTO.contractType() );
        return new PersistentSeller(
            sellerRequestDTO.name(),
            EnrollmentGenerator.generateEnrollment( contractType ),
            sellerRequestDTO.birthDay(),
            sellerRequestDTO.documentNumber(),
            sellerRequestDTO.email(),
            contractType,
            sellerRequestDTO.branchOfficeDocumentNumber() );
    }
}
