package br.com.casasbahia.service.impl;

import static br.com.casasbahia.service.Constants.VALID_BIRTHDATE;
import static br.com.casasbahia.service.Constants.VALID_CNPJ;
import static br.com.casasbahia.service.Constants.VALID_CPF;
import static br.com.casasbahia.service.Constants.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.StringContains;
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
        final PersistentSeller createdSeller = sellerRepository.findByEnrollment( responseDTO.enrollment() );
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
}
