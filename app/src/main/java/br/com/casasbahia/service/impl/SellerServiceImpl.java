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
        LOGGER.info( "TRACEID = {} : Validating seller payload...", traceId );
        SellerValidator.validate( sellerRequestDTO );
        LOGGER.info( "TRACEID = {} : Creating seller...", traceId );
        final PersistentSeller createdSeller = repository.save( converter.toModel( sellerRequestDTO ) );
        LOGGER.info( "TRACEID = {} : Seller with id = {} and enrollment = {} created successfully!", traceId, createdSeller.getId(),
            createdSeller.getEnrollment() );
        return converter.toDTO( createdSeller );
    }

    @Override
    public SellerPageableDTO findAll(
        final String filter,
        final Pageable pageable )
    {
        final UUID traceId = UUID.randomUUID();
        LOGGER.info( "TRACEID = {} : Finding... seller by {} and {}", traceId, filter, pageable );
        final Page<PersistentSeller> sellerPage = repository.findAll(
            SellerSpecification.filter( converter.toFilterDTO( filter ) ), pageable );
        LOGGER.info( "TRACEID = {} : Sellers found successfully!", traceId );
        return converter.toPageableDTO( sellerPage );
    }

    @Override
    public SellerDTO findByEnrollment(
        final String enrollment )
    {
        final UUID traceId = UUID.randomUUID();
        LOGGER.info( "TRACEID = {} : Finding... seller by enrollment = {} ", traceId, enrollment );
        final PersistentSeller persistentSeller = repository.findByEnrollment( enrollment )
            .orElseThrow( () -> new SellerNotFoundException( "csb.seller.not_found", enrollment ) );
        LOGGER.info( "TRACEID = {} : Seller found successfully!", traceId );
        return converter.toSellerDTO( persistentSeller );
    }
}
