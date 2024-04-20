package br.com.casasbahia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.casasbahia.converter.SellerConverter;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;
import br.com.casasbahia.service.validator.SellerValidatorService;

@Service
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
    public SellerResponseDTO create(
        final SellerRequestDTO sellerRequestDTO )
    {
        LOGGER.info( "Validating seller payload..." );
        SellerValidatorService.validate( sellerRequestDTO );
        LOGGER.info( "Creating seller..." );
        final PersistentSeller createdSeller = repository.save( converter.toModel( sellerRequestDTO ) );
        final String enrollment = createdSeller.getEnrollment();
        LOGGER.info( "Seller with enrollment = {} created sucessfully!", enrollment );
        return new SellerResponseDTO( enrollment );
    }
}
