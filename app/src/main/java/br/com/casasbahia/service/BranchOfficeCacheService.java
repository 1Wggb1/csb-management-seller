package br.com.casasbahia.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.casasbahia.client.BranchOfficeClient;
import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.exception.validation.SellerBranchOfficeNotActiveValidationException;
import br.com.casasbahia.model.PersistentBranchOffice;
import br.com.casasbahia.repository.BranchOfficeCacheRepository;
import io.micrometer.observation.annotation.Observed;

@Observed
@Service
public class BranchOfficeCacheService
{
    @Autowired
    private BranchOfficeCacheRepository branchOfficeRepository;
    @Autowired
    private BranchOfficeClient branchOfficeClient;

    public PersistentBranchOffice find(
        final String branchOfficeDocumentNumber )
    {
        final Optional<PersistentBranchOffice> cachedBranchOffice = branchOfficeRepository.findByDocument(
            branchOfficeDocumentNumber );
        return cachedBranchOffice.orElseGet( () -> saveOnCache( branchOfficeDocumentNumber ) );
    }

    private PersistentBranchOffice saveOnCache(
        final String documentNumber )
    {
        final BranchOfficeDTO branchOfficeDTO = branchOfficeClient.findByDocumentNumber( documentNumber );
        if( ! branchOfficeDTO.active() ) {
            throw new SellerBranchOfficeNotActiveValidationException( documentNumber );
        }
        return branchOfficeRepository.save( PersistentBranchOffice.from( branchOfficeDTO ) );
    }
}
