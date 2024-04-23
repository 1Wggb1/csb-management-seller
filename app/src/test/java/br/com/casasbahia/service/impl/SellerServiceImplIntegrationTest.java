package br.com.casasbahia.service.impl;

import static br.com.casasbahia.CommonTestData.BRANCH_OFFICE_DTO;
import static br.com.casasbahia.CommonTestData.VALID_BIRTHDATE;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ_2;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ_UNMASKED;
import static br.com.casasbahia.CommonTestData.VALID_CPF;
import static br.com.casasbahia.CommonTestData.VALID_CPF_2;
import static br.com.casasbahia.CommonTestData.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.hamcrest.Matcher;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.casasbahia.client.BranchOfficeClient;
import br.com.casasbahia.dto.PageableDTO;
import br.com.casasbahia.dto.SellerFilterDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.dto.SellerUpdateRequestDTO;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;
import br.com.casasbahia.util.UnmaskUtil;

@ExtendWith( SpringExtension.class )
@SpringBootTest
@ActiveProfiles( "test" )
@AutoConfigureMockMvc
class SellerServiceImplIntegrationTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final SellerRequestDTO SELLER_REQUEST_DTO = new SellerRequestDTO( "Will", VALID_EMAIL,
        VALID_BIRTHDATE,
        VALID_CPF,
        ContractType.CLT.name(),
        VALID_CNPJ );

    @Autowired
    private MockMvc mvc;
    @Autowired
    private SellerRepository sellerRepository;
    @MockBean
    private BranchOfficeClient branchOfficeClient;

    @BeforeEach
    void setUp()
    {
        when( branchOfficeClient.findByDocumentNumber( anyString() ) )
            .thenReturn( BRANCH_OFFICE_DTO );
    }

    @AfterEach
    void tearDown()
    {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName( "Deve retornar 201 quando criar vendedor com sucesso." )
    void shouldReturn201WhenCreateSellerSuccessfully()
        throws Exception
    {
        final ContractType contractType = ContractType.valueOf( SELLER_REQUEST_DTO.contractType() );
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/sellers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( SELLER_REQUEST_DTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isCreated() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.enrollment", IsNull.notNullValue() ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.enrollment",
                StringContains.containsString( "-" + contractType.getAcronym() ) ) );
        final String response = result.andReturn().getResponse().getContentAsString();
        final SellerResponseDTO responseDTO = readAsObject( response, SellerResponseDTO.class );
        final PersistentSeller createdSeller = sellerRepository.findByEnrollment( responseDTO.enrollment() )
            .orElseThrow();
        assertNotNull( createdSeller.getId() );
        assertEquals( SELLER_REQUEST_DTO.name(), createdSeller.getName() );
        assertEquals( SELLER_REQUEST_DTO.email(), createdSeller.getEmail() );
        assertEquals( SELLER_REQUEST_DTO.documentNumber().replaceAll( "[.-]", "" ),
            createdSeller.getDocumentNumber() );
        assertEquals( SELLER_REQUEST_DTO.branchOfficeDocumentNumber().replaceAll( "[./-]", "" ),
            createdSeller.getBranchOfficeDocumentNumber() );
        assertEquals( SELLER_REQUEST_DTO.birthDay().replaceAll( "-", "" ),
            createdSeller.getBirthDay() );
        assertEquals( SELLER_REQUEST_DTO.contractType(), createdSeller.getContractType().name() );
    }

    private static String writeAsJson(
        final Object object )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.writeValueAsString( object );
    }

    private static <T> T readAsObject(
        final String json,
        final Class<T> clazz )
        throws JsonProcessingException
    {
        return OBJECT_MAPPER.readValue( json, clazz );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando campos do payload estão inválidos." )
    void shouldReturn400ErrorMessageWhenInvalidPayload()
        throws Exception
    {
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "", null,
            null,
            "",
            "",
            null );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/sellers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( sellerRequestDTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isBadRequest() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages.length()", IsEqual.equalTo( 5 ) ) );
        assertTrue( sellerRepository.findAll().isEmpty() );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando payload não está no formato correto." )
    void shouldReturnErrorMessageWhenInvalidPayloadFormat()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/sellers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( "{sasp: }" );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isBadRequest() );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando vendedor não encontrado pela matrícula." )
    void shouldReturnErrorMessageWhenSellerNotFound()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers/ssss-333" );

        final ResultActions result = mvc.perform( request );

        validateNotFoundSellerResponse( result );
    }

    private static void validateNotFoundSellerResponse(
        final ResultActions result )
        throws Exception
    {
        result.andExpect( MockMvcResultMatchers.status().isNotFound() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages", IsNull.notNullValue() ) );
    }

    @Test
    @DisplayName( "Deve retornar 200 quando verdedor encontrado pela matrícula e validar os campos." )
    void shouldReturnSellerWhenFoundByEnrollment()
        throws Exception
    {
        final PersistentSeller saved = createSeller( "Will",
            "00000099-OUT", ContractType.OUTSOURCING, VALID_CNPJ_UNMASKED );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers/" + saved.getEnrollment() );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isOk() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.name", IsEqual.equalTo( saved.getName() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.email", IsEqual.equalTo( saved.getEmail() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.documentNumber", IsEqual.equalTo( saved.getDocumentNumber() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.birthDay", IsEqual.equalTo( saved.getBirthDay() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.enrollment", IsEqual.equalTo( saved.getEnrollment() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.branchOfficeDocumentNumber", IsEqual.equalTo( saved
                .getBranchOfficeDocumentNumber() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.contractType", IsEqual.equalTo( saved.getContractType().name() ) ) );
    }

    @Test
    @DisplayName( "Deve retornar 200 e retornar pageable com vendedores." )
    void shouldReturnSellersByPageable()
        throws Exception
    {
        create3DefaultsSellers();

        final int page = 0;
        final int size = 2;
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers" )
            .queryParam( "page", "" + page )
            .queryParam( "size", "" + size );
        final ResultActions result = mvc.perform( request );

        final PageableDTO expectedPageable = new PageableDTO( page, size, 2, 2, 3 );
        validatePageableResult( result, expectedPageable );
    }

    private void create3DefaultsSellers()
    {
        createSeller( "Joan", "00000097-OUT",
            ContractType.OUTSOURCING, VALID_CNPJ_UNMASKED );
        createSeller( "Mari", "00000898-OUT",
            ContractType.OUTSOURCING, VALID_CNPJ );
        createSeller( "Garbo", "00000191-CLT",
            ContractType.CLT, VALID_CNPJ );
    }

    private PersistentSeller createSeller(
        final String name,
        final String enrollment,
        final ContractType contractType,
        final String branchOfficeDocumentNumber )
    {
        final PersistentSeller seller = new PersistentSeller(
            name,
            enrollment,
            "20050417",
            VALID_CPF,
            VALID_EMAIL,
            contractType,
            branchOfficeDocumentNumber );
        return sellerRepository.save( seller );
    }

    private static void validatePageableResult(
        final ResultActions result,
        final PageableDTO expectedResult )
        throws Exception
    {
        final String pageableFields = "$.pageable.";
        final int numberOfElements = expectedResult.numberOfElements();
        result.andExpect( MockMvcResultMatchers.status().isOk() )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "page", isEqual( expectedResult.page() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "size", isEqual( expectedResult.size() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "numberOfElements", Is.is( numberOfElements ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "totalPages", isEqual( expectedResult.totalPages() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( pageableFields + "totalElements",
                isEqual( expectedResult.totalElements() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.elements.length()", isEqual( numberOfElements ) ) );
    }

    private static Matcher<Integer> isEqual(
        final int value )
    {
        return IsEqual.equalTo( value );
    }

    @Test
    @DisplayName( "Deve retornar 200 e retornar pageable com vendedores por filtro (contractType)." )
    void shouldReturnSellersByPageableAndContractTypeFilter()
        throws Exception
    {
        create3DefaultsSellers();
        final SellerFilterDTO filterDTO = new SellerFilterDTO( null,
            ContractType.OUTSOURCING.name(), null );

        final int page = 0;
        final int size = 10;
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers" )
            .queryParam( "page", "" + page )
            .queryParam( "size", "" + size )
            .queryParam( "filter", writeAsJson( filterDTO ) );
        final ResultActions result = mvc.perform( request );

        final PageableDTO expectedPageable = new PageableDTO( page, size, 2, 1, 2 );
        validatePageableResult( result, expectedPageable );
        validateResultFilter( expectedPageable.numberOfElements(), "contractType",
            result, IsEqual.equalTo( filterDTO.contractType() ) );
    }

    private static void validateResultFilter(
        final int numberOfElements,
        final String fieldName,
        final ResultActions result,
        final Matcher<String> fieldMatcher )
        throws Exception
    {
        final String elementPrefix = "$.elements";
        for( int i = 0; i < numberOfElements; i++ ) {
            final ResultMatcher resultMatcher = MockMvcResultMatchers.jsonPath(
                elementPrefix + "[" + i + "]." + fieldName, fieldMatcher );
            result.andExpect( resultMatcher );
        }
    }

    @Test
    @DisplayName( "Deve retornar 200 e retornar pageable com vendedores por filtro (name e contractType)." )
    void shouldReturnSellersByPageableAndNameAndContractTypeFilter()
        throws Exception
    {
        create3DefaultsSellers();
        final SellerFilterDTO filterDTO = new SellerFilterDTO( "J",
            ContractType.OUTSOURCING.name(), null );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers" )
            .queryParam( "filter", writeAsJson( filterDTO ) );
        final ResultActions result = mvc.perform( request );

        final PageableDTO expectedPageable = new PageableDTO( 0, 10, 1, 1, 1 );
        validatePageableResult( result, expectedPageable );
        validateResultFilter( expectedPageable.numberOfElements(), "contractType",
            result, IsEqual.equalTo( filterDTO.contractType() ) );
        validateResultFilter( expectedPageable.numberOfElements(), "name", result,
            StringContains.containsStringIgnoringCase( filterDTO.name() ) );
    }

    @Test
    @DisplayName( "Deve retornar 200 e retornar pageable com vendedores por filtro (name e branchOfficeDocumentNumber)." )
    void shouldReturnSellersByPageableAndNameAndBranchDocumentNumberFilter()
        throws Exception
    {
        create3DefaultsSellers();
        final SellerFilterDTO filterDTO = new SellerFilterDTO( "A",
            null, VALID_CNPJ );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers" )
            .queryParam( "filter", writeAsJson( filterDTO ) )
            .queryParam( "page", "" + 0 )
            .queryParam( "size", "" + 1 );
        final ResultActions result = mvc.perform( request );

        final PageableDTO expectedPageable = new PageableDTO( 0, 1, 1, 3, 3 );
        validatePageableResult( result, expectedPageable );
        validateResultFilter( expectedPageable.numberOfElements(), "branchOfficeDocumentNumber",
            result, IsEqual.equalTo( UnmaskUtil.unmaskDocumentNumber( filterDTO.branchOfficeDocumentNumber() ) ) );
        validateResultFilter( expectedPageable.numberOfElements(), "name", result,
            StringContains.containsStringIgnoringCase( filterDTO.name() ) );
    }

    @Test
    @DisplayName( "Deve retornar 204 quando vendedor deletado com sucesso." )
    void shouldReturn204WhenSellerDeletedSuccessfully()
        throws Exception
    {
        final String enrollment = "00000005-CLT";
        createSeller( "Mariana", enrollment, ContractType.CLT, VALID_CNPJ );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete( "/v1/sellers/{enrollment}", enrollment );
        final ResultActions result = mvc.perform( request );

        result.andExpect( MockMvcResultMatchers.status().isNoContent() );
        assertTrue( sellerRepository.findByEnrollment( enrollment ).isEmpty() );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando vendedor não encontrado para deleção." )
    void shouldReturn404WhenSellerNotFoundOnDelete()
        throws Exception
    {
        final String enrollment = "00000005-CLT";

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete( "/v1/sellers/{enrollment}", enrollment );
        final ResultActions result = mvc.perform( request );

        assertTrue( sellerRepository.findByEnrollment( enrollment ).isEmpty() );
        validateNotFoundSellerResponse( result );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando vendedor não encontrado na atualização." )
    void shouldReturn404WhenSellerNotFoundOUpdate()
        throws Exception
    {
        final String enrollment = "00000005-CLT";
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( "/v1/sellers/{enrollment}", enrollment )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( SELLER_REQUEST_DTO ) );

        final ResultActions result = mvc.perform( request );

        assertTrue( sellerRepository.findByEnrollment( enrollment ).isEmpty() );
        validateNotFoundSellerResponse( result );
    }

    @Test
    @DisplayName( "Deve retornar 204 quando vendedor atualizado com sucesso." )
    void shouldReturn204WhenSellerUpdatedSuccessfully()
        throws Exception
    {
        final String enrollment = "10000001-OUT";
        createSeller( "Will",
            enrollment,
            ContractType.OUTSOURCING,
            VALID_CNPJ );
        final SellerUpdateRequestDTO sellerRequestDTO = new SellerUpdateRequestDTO( "Will Garbo",
            "ru@ru.gov",
            "2000-08-12",
            VALID_CPF_2,
            VALID_CNPJ );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( "/v1/sellers/{enrollment}", enrollment )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( sellerRequestDTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isNoContent() );
        final PersistentSeller createdSeller = sellerRepository.findByEnrollment( enrollment )
            .orElseThrow();
        assertNotNull( createdSeller.getId() );
        assertEquals( sellerRequestDTO.name(), createdSeller.getName() );
        assertEquals( sellerRequestDTO.email(), createdSeller.getEmail() );
        assertEquals( sellerRequestDTO.documentNumber().replaceAll( "[.-]", "" ),
            createdSeller.getDocumentNumber() );
        assertEquals( sellerRequestDTO.branchOfficeDocumentNumber().replaceAll( "[./-]", "" ),
            createdSeller.getBranchOfficeDocumentNumber() );
        assertEquals( sellerRequestDTO.birthDay().replaceAll( "-", "" ),
            createdSeller.getBirthDay() );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando campos do payload estão inválidos na atualização." )
    void shouldReturn400ErrorMessageWhenInvalidPayloadOnUpdate()
        throws Exception
    {
        final SellerUpdateRequestDTO sellerRequestDTO = new SellerUpdateRequestDTO( "", null,
            null,
            "",
            "" );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( "/v1/sellers/{enrollment}", "10000001-OUT" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( sellerRequestDTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isBadRequest() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages.length()", IsEqual.equalTo( 4 ) ) );
    }

    @Test
    @DisplayName( "Deve retornar 400 e mensagem de erro quando documento modificado na atualização para cpf -> cnpj." )
    void shouldReturn400ErrorMessageWhenDocumentChangedOfCpfToCnpjOnUpdate()
        throws Exception
    {
        final String enrollment = "10000001-OUT";
        createSeller( "Will",
            enrollment,
            ContractType.OUTSOURCING,
            VALID_CNPJ );
        final SellerUpdateRequestDTO sellerRequestDTO = new SellerUpdateRequestDTO( "Will Garbo",
            "ru@ru.gov",
            "2000-08-12",
            VALID_CNPJ_2,
            VALID_CNPJ );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put( "/v1/sellers/{enrollment}", "10000001-OUT" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( sellerRequestDTO ) );
        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isBadRequest() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages.length()", IsEqual.equalTo( 1 ) ) );
    }
}