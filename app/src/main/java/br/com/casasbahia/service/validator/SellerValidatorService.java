package br.com.casasbahia.service.validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.exception.SellerInvalidContractTypeValidationException;
import br.com.casasbahia.exception.SellerInvalidDateFormatValidationException;
import br.com.casasbahia.exception.SellerInvalidDocumentValidationException;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.Document;

public final class SellerValidatorService
{
    public static void validate(
        final SellerRequestDTO sellerRequestDTO )
    {
        validateContractType( sellerRequestDTO );
        validateDate( sellerRequestDTO.birthDay() );
    }

    private static void validateContractType(
        final SellerRequestDTO sellerRequestDTO )
    {
        final String possibleContractType = sellerRequestDTO.contractType();
        if( ! ContractType.exists( possibleContractType ) ) {
            throw new SellerInvalidContractTypeValidationException( "Invalid contract type. Valid values = " + Arrays.toString( ContractType
                .values() ) );
        }
        final ContractType contractType = ContractType.valueOf( possibleContractType.toUpperCase() );
        final Document document = contractType.getRequiredDocument();
        validateDocument( document, sellerRequestDTO );
    }

    private static void validateDocument(
        final Document document,
        final SellerRequestDTO sellerRequestDTO )
    {
        final boolean isValidDocument = document.validateDocument( sellerRequestDTO.documentNumber() );
        if( ! isValidDocument ) {
            throw new SellerInvalidDocumentValidationException( "Invalid document." );
        }
    }

    private static void validateDate(
        final String date )
    {
        if( date == null ) {
            return;
        }
        if( date.length() > 10 ) {
            throw new SellerInvalidDateFormatValidationException( "Invalid date format." );
        }
        tryParseDate( date );
    }

    private static void tryParseDate(
        final String possibleDate )
    {
        try {
            final String unmaskedDate = possibleDate.replaceAll( "/", "" );
            LocalDate.parse( unmaskedDate, DateTimeFormatter.BASIC_ISO_DATE );
        } catch( final DateTimeException dateTimeException ) {
            throw new SellerInvalidDateFormatValidationException( "Invalid date." );
        }
    }
}
