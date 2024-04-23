package br.com.casasbahia.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.exception.application.SellerRestClientException;
import io.micrometer.observation.annotation.Observed;

@Observed
@Component
public class BranchOfficeClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger( BranchOfficeClient.class );
    static final int MAX_REQUEST_RETRY = 3;
    static final int RETRY_WAIT_SECONDS = 2;

    @Autowired
    private RestTemplate restTemplate;
    @Value( "${app.branchoffice.client.url}" )
    private String clientUrl;

    public BranchOfficeDTO findByDocumentNumber(
        final String documentNumber )
    {
        return requestWithRetry( documentNumber );
    }

    private BranchOfficeDTO requestWithRetry(
        final String documentNumber )
    {
        for( int i = 1; i <= MAX_REQUEST_RETRY; i++ ) {
            try {
                final ResponseEntity<BranchOfficeDTO> responseEntity = doRequest( documentNumber );
                return responseEntity.getBody();
            } catch( final Exception exception ) {
                throwExceptionIfNotFound( documentNumber, exception );
                LOGGER.warn( "Retry {}/{} branchOffice request.", i, MAX_REQUEST_RETRY );
                retrySleep();
            }
        }
        throw new SellerRestClientException( "csb.client.error", HttpStatus.INTERNAL_SERVER_ERROR.value(), documentNumber );
    }

    private ResponseEntity<BranchOfficeDTO> doRequest(
        final String documentNumber )
    {
        return restTemplate.getForEntity( clientUrl + "/" + documentNumber, BranchOfficeDTO.class );
    }

    private static void throwExceptionIfNotFound(
        final String documentNumber,
        final Exception exception )
    {
        if( exception instanceof final HttpClientErrorException httpClientErrorException ) {
            final int statusCode = httpClientErrorException.getStatusCode().value();
            if( HttpStatus.NOT_FOUND.value() == statusCode ) {
                throw new SellerRestClientException( "csb.client.not_found", statusCode, documentNumber );
            }
        }
    }

    // visible for test
    void retrySleep()
    {
        try {
            TimeUnit.SECONDS.sleep( RETRY_WAIT_SECONDS );
        } catch( final InterruptedException interruptedException ) {
            LOGGER.error( "Retry error", interruptedException );
        }
    }
}