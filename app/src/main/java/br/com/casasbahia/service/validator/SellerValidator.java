package br.com.casasbahia.service.validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.exception.SellerInvalidContractTypeValidationException;
import br.com.casasbahia.exception.SellerInvalidDateFormatValidationException;
import br.com.casasbahia.exception.SellerInvalidDocumentValidationException;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.Document;
import br.com.casasbahia.util.UnmaskUtil;

public final class SellerValidator
{
    private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile( "[0-9]{4}-[0-9]{2}-[0-9]{2}" );

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
            throw new SellerInvalidContractTypeValidationException( "csb.contacttype.invalid",
                Arrays.toString( ContractType.values() ) );
        }
        final ContractType contractType = ContractType.valueOf( possibleContractType.toUpperCase() );
        final Document document = contractType.getRequiredDocument();
        validateDocument( document, sellerRequestDTO );
    }

    private static void validateDocument(
        final Document document,
        final SellerRequestDTO sellerRequestDTO )
    {
        final boolean isValidDocument = document.isValidDocument( sellerRequestDTO.documentNumber() );
        if( ! isValidDocument ) {
            throw new SellerInvalidDocumentValidationException( "csb.documentnumber.invalid" );
        }
    }

    private static void validateDate(
        final String date )
    {
        if( date == null ) {
            return;
        }
        if( date.length() > 10 || ! DATE_FORMAT_PATTERN.matcher( date ).matches() ) {
            throw new SellerInvalidDateFormatValidationException( "csb.date.invalid.format" );
        }
        tryParseDate( date );
    }

    private static void tryParseDate(
        final String possibleDate )
    {
        try {
            final String unmaskedDate = UnmaskUtil.unmaskDate( possibleDate );
            LocalDate.parse( unmaskedDate, DateTimeFormatter.BASIC_ISO_DATE );
        } catch( final DateTimeException dateTimeException ) {
            throw new SellerInvalidDateFormatValidationException( "csb.date.invalid.format" );
        }
    }
}
