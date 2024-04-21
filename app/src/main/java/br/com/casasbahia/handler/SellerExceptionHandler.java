package br.com.casasbahia.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import br.com.casasbahia.dto.ErrorDTO;
import br.com.casasbahia.exception.BaseSellerException;

@ControllerAdvice
public class SellerExceptionHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger( SellerExceptionHandler.class );

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler( BaseSellerException.class )
    public ResponseEntity<ErrorDTO> handleSellerValidationException(
        final BaseSellerException baseSellerException )
    {
        LOGGER.error( "Error", baseSellerException );
        final String message = messageSource.getMessage( baseSellerException.getMessage(),
            baseSellerException.getMessageArgs(), Locale.getDefault() );
        return createErrorResponse( baseSellerException.getStatus(), List.of( message ) );
    }

    private static ResponseEntity<ErrorDTO> createErrorResponse(
        final HttpStatus status,
        final List<String> errorMessages )
    {
        final ErrorDTO errorDTO = new ErrorDTO( status.toString(),
            errorMessages, LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) );
        return ResponseEntity.status( status ).body( errorDTO );
    }

    @ExceptionHandler( MethodArgumentNotValidException.class )
    public ResponseEntity<ErrorDTO> handleMethodArgumentInvalid(
        final MethodArgumentNotValidException argumentNotValidException )
    {
        LOGGER.error( "Argument invalid error", argumentNotValidException );
        final List<FieldError> fieldErrors = argumentNotValidException.getFieldErrors();
        final List<String> errorMessages = new ArrayList<>();
        for( final FieldError fieldError : fieldErrors ) {
            errorMessages.add( messageSource.getMessage( fieldError.getDefaultMessage(), new Object[] {
                fieldError.getField()
            }, Locale.getDefault() ) );
        }
        return createErrorResponse( HttpStatus.BAD_REQUEST, errorMessages );
    }

    @ExceptionHandler( NoResourceFoundException.class )
    public ResponseEntity<ErrorDTO> handleNoResourceException(
        final NoResourceFoundException noResourceFoundException )
    {
        LOGGER.error( "Not found path", noResourceFoundException );
        final String message = messageSource.getMessage( "csb.no.resource.found", null, Locale.getDefault() );
        return createErrorResponse( HttpStatus.NOT_FOUND, List.of( message ) );
    }

    @ExceptionHandler( HttpMessageNotReadableException.class )
    public ResponseEntity<ErrorDTO> handleInternalErrorException(
        final HttpMessageNotReadableException messageNotReadableException )
    {
        LOGGER.error( "Not readable", messageNotReadableException );
        final String message = messageSource.getMessage( "csb.no.readable.error", null, Locale.getDefault() );
        return createErrorResponse( HttpStatus.BAD_REQUEST, List.of( message ) );
    }

    @ExceptionHandler( Exception.class )
    public ResponseEntity<ErrorDTO> handleInternalErrorException(
        final Exception exception )
    {
        LOGGER.error( "Internal error", exception );
        final String message = messageSource.getMessage( "csb.internal.error", null, Locale.getDefault() );
        return createErrorResponse( HttpStatus.INTERNAL_SERVER_ERROR, List.of( message ) );
    }
}