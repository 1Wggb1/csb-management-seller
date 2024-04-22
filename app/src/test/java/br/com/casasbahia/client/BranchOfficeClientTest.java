package br.com.casasbahia.client;

import static br.com.casasbahia.CommonTestData.BRANCH_OFFICE_DTO;
import static br.com.casasbahia.client.BranchOfficeClient.MAX_REQUEST_RETRY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.exception.application.SellerRestClientException;

@ExtendWith( MockitoExtension.class )
class BranchOfficeClientTest
{
    @InjectMocks
    private BranchOfficeClient subject;
    @Mock
    private RestClient restClient;

    @Test
    @DisplayName("Deve tentar 3 vezes quando a requisição falhar.")
    void shouldRetry3TimesWhenRequestFail()
    {
		when( restClient.get() ).thenThrow( new RuntimeException() );
		
        assertThrows( SellerRestClientException.class, () -> subject.findByDocumentNumber( "88999" ) );

        verify( restClient, times( MAX_REQUEST_RETRY ) ).get();
    }

    @Test
    @DisplayName( "Deve tentar 1 vez quando a requisição der não encontrado." )
    void shouldRetry1TimeWhenNotFoundBranchOffice()
    {
        final HttpClientErrorException notFound = HttpClientErrorException.create(
            HttpStatusCode.valueOf( 404 ),
            "404 Not Found", null, null, null );
        when( restClient.get() ).thenThrow( notFound );

        assertThrows( SellerRestClientException.class, () -> subject.findByDocumentNumber( "88999" ) );

        verify( restClient, times( 1 ) ).get();
    }

    @Test
    @DisplayName( "Deve tentar 1 vez quando a requisição der não encontrado." )
    void shouldRetry1TimeWhenAndReturnBranchOffice()
    {
        final RestClient.RequestHeadersUriSpec uriSpec = mock( RestClient.RequestHeadersUriSpec.class );
        when( uriSpec.uri( anyString() ) ).thenReturn( uriSpec );
        when( restClient.get() ).thenThrow( new RuntimeException() ).thenReturn( uriSpec );
        final RestClient.ResponseSpec responseSpec = mock( RestClient.ResponseSpec.class );
        when( uriSpec.retrieve() ).thenReturn( responseSpec );
        when( responseSpec.body( BranchOfficeDTO.class ) ).thenReturn( BRANCH_OFFICE_DTO );

        final BranchOfficeDTO result = subject.findByDocumentNumber( "88999" );

        verify( restClient, times( 2 ) ).get();
        assertEquals( BRANCH_OFFICE_DTO, result );
    }
}
