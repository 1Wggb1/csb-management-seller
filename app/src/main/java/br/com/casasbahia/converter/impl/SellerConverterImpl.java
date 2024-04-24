package br.com.casasbahia.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.casasbahia.converter.SellerConverter;
import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.dto.PageableDTO;
import br.com.casasbahia.dto.SellerDTO;
import br.com.casasbahia.dto.SellerFilterDTO;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.dto.SellerUpdateRequestDTO;
import br.com.casasbahia.exception.application.SellerGenericApplicationException;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentBranchOffice;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;
import io.micrometer.observation.annotation.Observed;

@Observed
@Service
public class SellerConverterImpl
    implements
        SellerConverter
{
    @Autowired
    private SellerRepository repository;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public PersistentSeller toModelCreation(
        final PersistentBranchOffice branchOfficeCache,
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
            branchOfficeCache );
    }

    @Override
    public PersistentSeller toModelUpdate(
        final PersistentBranchOffice branchOfficeCache,
        final PersistentSeller persistentSeller,
        final SellerUpdateRequestDTO sellerRequestDTO )
    {
        persistentSeller.setName( sellerRequestDTO.name() );
        persistentSeller.setEmail( sellerRequestDTO.email() );
        persistentSeller.setBirthDay( sellerRequestDTO.birthDay() );
        persistentSeller.setDocumentNumber( sellerRequestDTO.documentNumber() );
        persistentSeller.setBranchOffice( branchOfficeCache );
        return persistentSeller;
    }

    @Override
    public SellerResponseDTO toDTO(
        final PersistentSeller persistentSeller )
    {
        return new SellerResponseDTO( persistentSeller.getEnrollment() );
    }

    @Override
    public SellerPageableDTO toPageableDTO(
        final Page<PersistentSeller> sellerPage )
    {
        final PageableDTO pageableDTO = new PageableDTO(
            sellerPage.getNumber(),
            sellerPage.getSize(),
            sellerPage.getNumberOfElements(),
            sellerPage.getTotalPages(),
            (int) sellerPage.getTotalElements() );
        return new SellerPageableDTO( pageableDTO, toSellerDTO( sellerPage.getContent() ) );
    }

    @Override
    public SellerDTO toSellerDTO(
        final PersistentSeller persistentSeller )
    {
        return new SellerDTO( persistentSeller.getName(),
            persistentSeller.getEnrollment(),
            persistentSeller.getBirthDay(),
            persistentSeller.getDocumentNumber(),
            persistentSeller.getEmail(),
            persistentSeller.getContractType().name(),
            toBranchOfficeDTO( persistentSeller ) );
    }

    private static BranchOfficeDTO toBranchOfficeDTO(
        final PersistentSeller persistentSeller )
    {
        final PersistentBranchOffice branchOffice = persistentSeller.getBranchOffice();
        return new BranchOfficeDTO(
            branchOffice.getId(),
            branchOffice.getName(), branchOffice.getDocumentNumber(),
            branchOffice.getCity(), branchOffice.getState(),
            branchOffice.getType(), branchOffice.isActive(),
            null, null );
    }

    @Override
    public SellerFilterDTO toFilterDTO(
        final String filter )
    {
        if( filter == null || filter.isBlank() ) {
            return null;
        }
        try {
            return objectMapper.readValue( filter, SellerFilterDTO.class );
        } catch( final JsonProcessingException e ) {
            throw new SellerGenericApplicationException( "csb.filter.conversion.error" );
        }
    }
}
