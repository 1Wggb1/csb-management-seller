package br.com.casasbahia.service.validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Pattern;

import br.com.casasbahia.client.BranchOfficeClient;
import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.exception.validation.SellerBranchOfficeNotActiveValidationException;
import br.com.casasbahia.exception.validation.SellerDateValidationException;
import br.com.casasbahia.exception.validation.SellerInvalidContractTypeValidationException;
import br.com.casasbahia.exception.validation.SellerInvalidDateFormatValidationException;
import br.com.casasbahia.exception.validation.SellerInvalidDocumentValidationException;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.Document;
import br.com.casasbahia.util.UnmaskUtil;

public final class SellerValidator
{
    private static final Pattern DATE_FORMAT_PATTERN = Pattern.compile( "[0-9]{4}-[0-9]{2}-[0-9]{2}" );

    public static void validate(
        final BranchOfficeClient branchOfficeClient,
        final SellerRequestDTO sellerRequestDTO )
    {
        validateContractType( sellerRequestDTO );
        validateDate( sellerRequestDTO.birthDay() );
        validateBranchOffice( branchOfficeClient, sellerRequestDTO.branchOfficeDocumentNumber() );
    }

    private static void validateContractType(
        final SellerRequestDTO sellerRequestDTO )
    {
        final String possibleContractType = sellerRequestDTO.contractType();
        if( ! ContractType.exists( possibleContractType ) ) {
            throw new SellerInvalidContractTypeValidationException( Arrays.toString( ContractType.values() ) );
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
            throw new SellerInvalidDocumentValidationException();
        }
    }

    private static void validateDate(
        final String date )
    {
        if( date == null ) {
            return;
        }
        if( date.length() > 10 || ! DATE_FORMAT_PATTERN.matcher( date ).matches() ) {
            throw new SellerInvalidDateFormatValidationException();
        }
        final LocalDate parsedDate = tryParseDate( date );
        if( parsedDate.isAfter( LocalDate.now() ) || parsedDate.isEqual( LocalDate.now() ) ) {
            throw new SellerDateValidationException();
        }
    }

    private static LocalDate tryParseDate(
        final String possibleDate )
    {
        try {
            final String unmaskedDate = UnmaskUtil.unmaskDate( possibleDate );
            return LocalDate.parse( unmaskedDate, DateTimeFormatter.BASIC_ISO_DATE );
        } catch( final DateTimeException dateTimeException ) {
            throw new SellerInvalidDateFormatValidationException();
        }
    }

    private static void validateBranchOffice(
        final BranchOfficeClient branchOfficeClient,
        final String branchOfficeDocumentNumber )
    {
        final BranchOfficeDTO branchOffice = branchOfficeClient.findByDocumentNumber(
            UnmaskUtil.unmaskDocumentNumber( branchOfficeDocumentNumber ) );
        if( ! branchOffice.active() ) {
            throw new SellerBranchOfficeNotActiveValidationException( branchOfficeDocumentNumber );
        }
    }
}
