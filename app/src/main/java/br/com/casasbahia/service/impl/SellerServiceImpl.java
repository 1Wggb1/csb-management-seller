package br.com.casasbahia.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.casasbahia.client.BranchOfficeClient;
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
    @Autowired
    private BranchOfficeClient branchOfficeClient;

    @Override
    @Transactional
    public SellerResponseDTO create(
        final SellerRequestDTO sellerRequestDTO )
    {
        LOGGER.info( "Validating seller payload..." );
        SellerValidator.validate( branchOfficeClient, sellerRequestDTO );
        LOGGER.info( "Creating seller..." );
        final PersistentSeller createdSeller = repository.save( converter.toModelCreation( sellerRequestDTO ) );
        LOGGER.info( String.format( "Seller with id = %d and enrollment = %s created successfully!",
            createdSeller.getId(), createdSeller.getEnrollment() ) );
        return converter.toDTO( createdSeller );
    }

    @Override
    @Transactional
    public void update(
        final String enrollment,
        final SellerRequestDTO sellerRequestDTO )
    {
        LOGGER.info( "Validating seller update payload..." );
        final PersistentSeller persistentSeller = findOrThrowNotFoundException( enrollment );
        SellerValidator.validate( branchOfficeClient, sellerRequestDTO );
        LOGGER.info( "Updating seller..." );
        final PersistentSeller updatedSeller = repository.save( converter.toModelUpdate( persistentSeller, sellerRequestDTO ) );
        LOGGER.info( String.format( "Seller with id = %d and enrollment = %s updated successfully!",
            updatedSeller.getId(),
            updatedSeller.getEnrollment() ) );
    }

    private PersistentSeller findOrThrowNotFoundException(
        final String enrollment )
    {
        return repository.findByEnrollment( enrollment )
            .orElseThrow( () -> new SellerNotFoundException( enrollment ) );
    }

    @Override
    public SellerPageableDTO findAll(
        final String filter,
        final Pageable pageable )
    {
        LOGGER.info( String.format( "Finding... seller by %s and %s", filter, pageable ) );
        final Page<PersistentSeller> sellerPage = repository.findAll(
            SellerSpecification.filter( converter.toFilterDTO( filter ) ), pageable );
        LOGGER.info( "Sellers found successfully!" );
        return converter.toPageableDTO( sellerPage );
    }

    @Override
    public SellerDTO findByEnrollment(
        final String enrollment )
    {
        LOGGER.info( String.format( "Finding... seller by enrollment = %s", enrollment ) );
        final PersistentSeller persistentSeller = findOrThrowNotFoundException( enrollment );
        LOGGER.info( "Seller found successfully!" );
        return converter.toSellerDTO( persistentSeller );
    }

    @Override
    @Transactional
    public void delete(
        final String enrollment )
    {
        LOGGER.info( String.format( "Deleting... seller by enrollment = %s", enrollment ) );
        final PersistentSeller persistentSeller = findOrThrowNotFoundException( enrollment );
        LOGGER.info( "Seller deleted successfully!" );
        repository.delete( persistentSeller );
    }
}
