package br.com.casasbahia.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.casasbahia.converter.SellerConverter;
import br.com.casasbahia.dto.SellerDTO;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.exception.application.SellerNotFoundException;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;
import br.com.casasbahia.repository.specification.SellerSpecification;
import br.com.casasbahia.service.SellerService;
import br.com.casasbahia.service.validator.SellerValidator;

@Service
@Transactional( readOnly = true )
public class SellerServiceImpl
    implements
        SellerService
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SellerServiceImpl.class );

    @Autowired
    private SellerRepository repository;
    @Autowired
    private SellerConverter converter;

    @Override
    @Transactional
    public SellerResponseDTO create(
        final SellerRequestDTO sellerRequestDTO )
    {
        final UUID traceId = UUID.randomUUID();
        logInfo( traceId, "Validating seller payload..." );
        SellerValidator.validate( sellerRequestDTO );
        logInfo( traceId, "Creating seller..." );
        final PersistentSeller createdSeller = repository.save( converter.toModelCreation( sellerRequestDTO ) );
        logInfo( traceId, String.format( "Seller with id = %d and enrollment = %s created successfully!",
            createdSeller.getId(), createdSeller.getEnrollment() ) );
        return converter.toDTO( createdSeller );
    }

    private static void logInfo(
        final UUID traceId,
        final String message )
    {
        LOGGER.info( "TRACEID = {} : " + message, traceId );
    }

    @Override
    @Transactional
    public void update(
        final String enrollment,
        final SellerRequestDTO sellerRequestDTO )
    {
        final UUID traceId = UUID.randomUUID();
        logInfo( traceId, "Validating seller update payload..." );
        final PersistentSeller persistentSeller = findOrThrowNotFoundException( enrollment );
        SellerValidator.validate( sellerRequestDTO );
        logInfo( traceId, "Updating seller..." );
        final PersistentSeller updatedSeller = repository.save( converter.toModelUpdate( persistentSeller, sellerRequestDTO ) );
        logInfo( traceId, String.format( "Seller with id = %d and enrollment = %s updated successfully!",
            updatedSeller.getId(),
            updatedSeller.getEnrollment() ) );
    }

    private PersistentSeller findOrThrowNotFoundException(
        final String enrollment )
    {
        return repository.findByEnrollment( enrollment )
            .orElseThrow( () -> new SellerNotFoundException( "csb.seller.not_found", enrollment ) );
    }

    @Override
    public SellerPageableDTO findAll(
        final String filter,
        final Pageable pageable )
    {
        final UUID traceId = UUID.randomUUID();
        logInfo( traceId, String.format( "Finding... seller by %s and %s", filter, pageable ) );
        final Page<PersistentSeller> sellerPage = repository.findAll(
            SellerSpecification.filter( converter.toFilterDTO( filter ) ), pageable );
        logInfo( traceId, "Sellers found successfully!" );
        return converter.toPageableDTO( sellerPage );
    }

    @Override
    public SellerDTO findByEnrollment(
        final String enrollment )
    {
        final UUID traceId = UUID.randomUUID();
        logInfo( traceId, String.format( "Finding... seller by enrollment = %s", enrollment ) );
        final PersistentSeller persistentSeller = findOrThrowNotFoundException( enrollment );
        logInfo( traceId, "Seller found successfully!" );
        return converter.toSellerDTO( persistentSeller );
    }

    @Override
    @Transactional
    public void delete(
        final String enrollment )
    {
        final UUID traceId = UUID.randomUUID();
        logInfo( traceId, String.format( "Deleting... seller by enrollment = %s", enrollment ) );
        final PersistentSeller persistentSeller = findOrThrowNotFoundException( enrollment );
        logInfo( traceId, "Seller deleted successfully!" );
        repository.delete( persistentSeller );
    }
}
