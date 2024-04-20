package br.com.casasbahia.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.casasbahia.converter.SellerConverter;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;
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
        LOGGER.info( "Validating seller payload..." );
        SellerValidator.validate( sellerRequestDTO );
        LOGGER.info( "Creating seller..." );
        final PersistentSeller createdSeller = repository.save( converter.toModel( sellerRequestDTO ) );
        LOGGER.info( "Seller with enrollment = {} created successfully!", createdSeller.getEnrollment() );
        return converter.toDTO( createdSeller );
    }
}
