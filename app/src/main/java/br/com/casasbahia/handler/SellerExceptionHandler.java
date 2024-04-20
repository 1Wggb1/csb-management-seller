package br.com.casasbahia.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import br.com.casasbahia.dto.ErrorDTO;
import br.com.casasbahia.exception.SellerValidationException;

@ControllerAdvice
public class SellerExceptionHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SellerExceptionHandler.class );

    @ExceptionHandler( SellerValidationException.class )
    public ResponseEntity<ErrorDTO> handleSellerValidationException(
        final SellerValidationException sellerValidationException )
    {
        LOGGER.error( "Validation error", sellerValidationException );
        return ResponseEntity.badRequest().body( createErrorResponse( sellerValidationException.getMessage() ) );
    }

    private static ErrorDTO createErrorResponse(
        final String errorMessage )
    {
        return new ErrorDTO( HttpStatus.BAD_REQUEST.toString(),
            List.of( errorMessage ),
            LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyy/MM/dd HH:mm:ss" ) ) );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<ErrorDTO> handleInternalErrorException(
        final Exception exception )
    {
        LOGGER.error( "Internal error", exception );
        return ResponseEntity.internalServerError().body( createErrorResponse( "Internal error. Contact support." ) );
    }
}