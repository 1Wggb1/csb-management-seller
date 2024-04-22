package br.com.casasbahia.service.validator;

import static br.com.casasbahia.CommonTestData.BRANCH_OFFICE_DTO;
import static br.com.casasbahia.CommonTestData.INACTIVE_BRANCH_OFFICE_DTO;
import static br.com.casasbahia.CommonTestData.VALID_BIRTHDATE;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ;
import static br.com.casasbahia.CommonTestData.VALID_CPF;
import static br.com.casasbahia.CommonTestData.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.casasbahia.client.BranchOfficeClient;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.exception.validation.SellerBranchOfficeNotActiveValidationException;
import br.com.casasbahia.exception.validation.SellerDateValidationException;
import br.com.casasbahia.exception.validation.SellerInvalidContractTypeValidationException;
import br.com.casasbahia.exception.validation.SellerInvalidDateFormatValidationException;
import br.com.casasbahia.exception.validation.SellerInvalidDocumentValidationException;
import br.com.casasbahia.model.ContractType;

@ExtendWith( MockitoExtension.class )
class SellerValidatorTest
{
    @Mock
    private BranchOfficeClient branchOfficeClient;

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
            () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
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
            () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
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
            () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
    }

    private static Stream<String> outsourcingAndClt()
    {
        return Stream.of( ContractType.OUTSOURCING.name(), ContractType.CLT.name() );
    }

    @Test
    @DisplayName( "Não deve lançar exceção quando o tipo de contrato é pj e documento é cnpj." )
    void shouldNotThrownExceptionWhenContractTypeIsPjAndDocumentIsCnpj()
    {
        mockBranchOffice();

        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CNPJ,
            ContractType.PJ.name(),
            VALID_CNPJ );
        assertDoesNotThrow( () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
    }

    @ParameterizedTest
    @MethodSource( "outsourcingAndClt" )
    @DisplayName( "Não deve lançar exceção quando o tipo de contrato é outsourcing ou clt e documento é cpf." )
    void shouldNotThrownExceptionWhenContractTypeIsOutsourcingOrCltAndDocumentIsCpf(
        final String contractType )
    {
        mockBranchOffice();
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CPF,
            contractType,
            VALID_CNPJ );
        assertDoesNotThrow( () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
    }

    private void mockBranchOffice(){
        when( branchOfficeClient.findByDocumentNumber( anyString() ) )
                .thenReturn( BRANCH_OFFICE_DTO );
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
            () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
    }

    private static Stream<String> invalidDatesOrFormat()
    {
        return Stream.of( "oi92", "17/7889/552", "17/08/2003", "17-08-2003", "2003-0820", "17082003" );
    }

    @Test
    @DisplayName("Deve lançar exceção quando filial não encontrada.")
    void shouldThrownExceptionWhenBranchOfficeInactive()
    {
        when( branchOfficeClient.findByDocumentNumber( anyString() ) )
                .thenReturn( INACTIVE_BRANCH_OFFICE_DTO );
        
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "Will", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CNPJ,
            ContractType.PJ.name(),
            VALID_CNPJ );
        assertThrows( SellerBranchOfficeNotActiveValidationException.class,
            () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
    }

    @ParameterizedTest
    @MethodSource( "dateGreaterThanNowOeEquals" )
    @DisplayName( "Deve lançar exceção quando data de nascimento futura ou agora." )
    void shouldThrownExceptionWhenDateEqualOrAfterNow(
        final String birthDay )
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "Will", VALID_EMAIL,
            birthDay,
            VALID_CNPJ,
            ContractType.PJ.name(),
            VALID_CNPJ );
        assertThrows( SellerDateValidationException.class,
            () -> SellerValidator.validate( branchOfficeClient, sellerRequestDTO ) );
    }

    private static Stream<String> dateGreaterThanNowOeEquals()
    {
        return Stream.of( "9999-12-10", LocalDate.now().format( DateTimeFormatter.ISO_DATE ) );
    }
}
