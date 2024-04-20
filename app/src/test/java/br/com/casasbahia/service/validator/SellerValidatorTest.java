package br.com.casasbahia.service.validator;

import static br.com.casasbahia.service.Constants.VALID_BIRTHDATE;
import static br.com.casasbahia.service.Constants.VALID_CNPJ;
import static br.com.casasbahia.service.Constants.VALID_CPF;
import static br.com.casasbahia.service.Constants.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.exception.SellerInvalidContractTypeValidationException;
import br.com.casasbahia.exception.SellerInvalidDateFormatValidationException;
import br.com.casasbahia.exception.SellerInvalidDocumentValidationException;
import br.com.casasbahia.model.ContractType;

class SellerValidatorTest
{
    @Test
    @DisplayName( "Deve lançar exceção quando o tipo de contrato for inválido." )
    void shouldThrownExceptionWhenContractTypeIsInvalid()
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CPF,
            "NOT_FOUND",
            VALID_CNPJ );
        try {
            System.out.println( new ObjectMapper().writeValueAsString( sellerRequestDTO ) );
        } catch( final JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        assertThrows( SellerInvalidContractTypeValidationException.class,
            () -> SellerValidator.validate( sellerRequestDTO ) );
    }

    @Test
    @DisplayName( "Deve lançar exceção quando o tipo de contrato é pj e documento é cpf." )
    void shouldThrownExceptionWhenContractTypeIsPjAndDocumentIsCpf()
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CPF,
            ContractType.PJ.name(),
            VALID_CNPJ );
        assertThrows( SellerInvalidDocumentValidationException.class,
            () -> SellerValidator.validate( sellerRequestDTO ) );
    }

    @ParameterizedTest
    @MethodSource( "outsourcingAndClt" )
    @DisplayName( "Deve lançar exceção quando o tipo de contrato é outsoursing ou clt e documento é cnpj." )
    void shouldThrownExceptionWhenContractTypeIsOutOrCltAndDocumentIsCnpj(
        final String contractType )
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CNPJ,
            contractType,
            VALID_CNPJ );
        assertThrows( SellerInvalidDocumentValidationException.class,
            () -> SellerValidator.validate( sellerRequestDTO ) );
    }

    private static Stream<String> outsourcingAndClt()
    {
        return Stream.of( ContractType.OUTSOURCING.name(), ContractType.CLT.name() );
    }

    @Test
    @DisplayName( "Não deve lançar exceção quando o tipo de contrato é pj e documento é cnpj." )
    void shouldNotThrownExceptionWhenContractTypeIsPjAndDocumentIsCnpj()
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CNPJ,
            ContractType.PJ.name(),
            VALID_CNPJ );
        assertDoesNotThrow( () -> SellerValidator.validate( sellerRequestDTO ) );
    }

    @ParameterizedTest
    @MethodSource( "outsourcingAndClt" )
    @DisplayName( "Não deve lançar exceção quando o tipo de contrato é outsourcing ou clt e documento é cpf." )
    void shouldNotThrownExceptionWhenContractTypeIsOutsourcingOrCltAndDocumentIsCpf(
        final String contractType )
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CPF,
            contractType,
            VALID_CNPJ );
        assertDoesNotThrow( () -> SellerValidator.validate( sellerRequestDTO ) );
    }

    @ParameterizedTest
    @MethodSource( "invalidDatesOrFormat" )
    @DisplayName( "Deve lançar exceção quando data de aniversário está inválida ou com formato diferente de yyyy/MM/dd." )
    void shouldThrownExceptionWhenBirthdayDateInvalid(
        final String invalidDate )
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            invalidDate,
            VALID_CNPJ,
            ContractType.PJ.name(),
            VALID_CNPJ );
        assertThrows( SellerInvalidDateFormatValidationException.class,
            () -> SellerValidator.validate( sellerRequestDTO ) );
    }

    private static Stream<String> invalidDatesOrFormat()
    {
        return Stream.of( "oi92", "17/7889/552", "17/08/2003", "17-08-2003", "2003-0820", "17082003" );
    }
}
