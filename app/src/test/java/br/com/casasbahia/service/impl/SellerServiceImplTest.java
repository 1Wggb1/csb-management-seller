package br.com.casasbahia.service.impl;

import static br.com.casasbahia.service.Constants.VALID_BIRTHDATE;
import static br.com.casasbahia.service.Constants.VALID_CNPJ;
import static br.com.casasbahia.service.Constants.VALID_CNPJ_UNMASKED;
import static br.com.casasbahia.service.Constants.VALID_CPF;
import static br.com.casasbahia.service.Constants.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.casasbahia.dto.SellerFilterDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;

@ExtendWith( SpringExtension.class )
@SpringBootTest
@ActiveProfiles( "test" )
@AutoConfigureMockMvc
class SellerServiceImplTest
{
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mvc;
    @Autowired
    private SellerRepository sellerRepository;

    @AfterEach
    void tearDown()
    {
        sellerRepository.deleteAll();
    }

    @Test
    @DisplayName( "Deve criar vendedor com sucesso." )
    void shouldCreateSellerSuccessfully()
        throws Exception
    {
        final ContractType contractType = ContractType.CLT;
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "Will", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CPF,
            contractType.name(),
            VALID_CNPJ );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/sellers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( writeAsJson( sellerRequestDTO ) );

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
        assertEquals( sellerRequestDTO.name(), createdSeller.getName() );
        assertEquals( sellerRequestDTO.email(), createdSeller.getEmail() );
        assertEquals( sellerRequestDTO.documentNumber().replaceAll( "[.-]", "" ),
            createdSeller.getDocumentNumber() );
        assertEquals( sellerRequestDTO.branchOfficeDocumentNumber().replaceAll( "[./-]", "" ),
            createdSeller.getBranchOfficeDocumentNumber() );
        assertEquals( sellerRequestDTO.birthDay().replaceAll( "-", "" ),
            createdSeller.getBirthDay() );
        assertEquals( sellerRequestDTO.contractType(), createdSeller.getContractType().name() );
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
    @DisplayName( "Deve retornar mensagem de erro quando campos do payload estão inválidos." )
    void shouldReturnErrorMessageWhenInvalidPayload()
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
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages.length()", Is.is( 5 ) ) );
        assertTrue( sellerRepository.findAll().isEmpty() );
    }

    @Test
    @DisplayName( "Deve retornar mensagem de erro quando payload não está no formato correto." )
    void shouldReturnErrorMessageWhenInvalidPayloadFormat()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post( "/v1/sellers" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( "{sasp: }" );

        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isInternalServerError() );
    }

    @Test
    @DisplayName( "Deve retornar 404 quando vendedor não encontrado pela matrícula." )
    void shouldReturnErrorMessageWhenSellerNotFound()
        throws Exception
    {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers/ssss-333" );

        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isNotFound() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.errorMessages", IsNull.notNullValue() ) );
    }

    @Test
    @DisplayName( "Deve retornar 200 quando verdedor encontrado pela matrícula e validar os campos." )
    void shouldReturnSellerWhenFoundByEnrollment()
        throws Exception
    {
        final PersistentSeller seller = new PersistentSeller(
            "Will",
            "00000099-CLT",
            "19990417",
            VALID_CPF,
            VALID_EMAIL,
            ContractType.OUTSOURCING,
            VALID_CNPJ_UNMASKED );
        final PersistentSeller saved = createSeller( seller );

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers/" + seller.getEnrollment() );

        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isOk() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.name", Is.is( saved.getName() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.email", Is.is( saved.getEmail() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.documentNumber", Is.is( saved.getDocumentNumber() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.birthDay", Is.is( saved.getBirthDay() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.enrollment", Is.is( saved.getEnrollment() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.branchOfficeDocumentNumber", Is.is( saved.getBranchOfficeDocumentNumber() ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.contractType", Is.is( saved.getContractType().name() ) ) );
    }

    private PersistentSeller createSeller(
        final PersistentSeller persistentSeller )
    {
        return sellerRepository.save( persistentSeller );
    }

    @Test
    @DisplayName( "Deve retornar 200 e retornar pageable com vendedores." )
    void shouldReturnSellersByPageable()
        throws Exception
    {
        final PersistentSeller seller = new PersistentSeller(
            "Will",
            "00000091-CLT",
            "19990417",
            VALID_CPF,
            VALID_EMAIL,
            ContractType.OUTSOURCING,
            VALID_CNPJ_UNMASKED );
        createSeller( seller );

        final int page = 0;
        final int size = 2;
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers" )
            .queryParam( "page", "" + page )
            .queryParam( "size", "" + size );

        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isOk() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.page", Is.is( page ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.size", Is.is( size ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.totalPages", Is.is( 1 ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.totalElements", Is.is( 1 ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.numberOfElements", Is.is( 1 ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.elements.length()", Is.is( 1 ) ) );
    }

    @Test
    @DisplayName( "Deve retornar 200 e retornar pageable com vendedores por filtro." )
    void shouldReturnSellersByPageableAndContractTypeFilter()
        throws Exception
    {
        final PersistentSeller seller = new PersistentSeller(
            "Will",
            "00000091-CLT",
            "19990417",
            VALID_CPF,
            VALID_EMAIL,
            ContractType.OUTSOURCING,
            VALID_CNPJ_UNMASKED );
        createSeller( seller );
        final PersistentSeller seller2 = new PersistentSeller(
            "Garbo",
            "00000191-CLT",
            "20050417",
            VALID_CPF,
            VALID_EMAIL,
            ContractType.OUTSOURCING,
            VALID_CNPJ_UNMASKED );
        createSeller( seller2 );

        final SellerFilterDTO filterDTO = new SellerFilterDTO( null,
            ContractType.OUTSOURCING.name(), null );

        final int page = 0;
        final int size = 10;
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get( "/v1/sellers" )
            .queryParam( "page", "" + page )
            .queryParam( "size", "" + size )
            .queryParam( "filter", writeAsJson( filterDTO ) );

        final ResultActions result = mvc.perform( request );

        result
            .andExpect( MockMvcResultMatchers.status().isOk() )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.page", Is.is( page ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.size", Is.is( size ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.totalPages", Is.is( 1 ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.totalElements", Is.is( 2 ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.pageable.numberOfElements", Is.is( 2 ) ) )
            .andExpect( MockMvcResultMatchers.jsonPath( "$.elements.length()", Is.is( 2 ) ) );
    }
}
