package br.com.casasbahia.client;

import static br.com.casasbahia.CommonTestData.BRANCH_OFFICE_DTO_2;
import static br.com.casasbahia.client.BranchOfficeClient.MAX_REQUEST_RETRY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.exception.application.SellerRestClientException;

@ExtendWith( MockitoExtension.class )
class BranchOfficeClientTest
{
    @Spy
    @InjectMocks
    private BranchOfficeClient subject;
    @Mock
    private RestTemplate restTemplate;

    @Test
    @DisplayName( "Deve tentar 3 vezes quando a requisição falhar." )
    void shouldRetry3TimesWhenRequestFail()
    {
        mockRetry();
        when( restTemplate.getForEntity( anyString(), eq( BranchOfficeDTO.class ) ) ).thenThrow( new RuntimeException() );

        assertThrows( SellerRestClientException.class, () -> subject.findByDocumentNumber( "88999" ) );

        verify( restTemplate, times( MAX_REQUEST_RETRY ) ).getForEntity( anyString(), eq( BranchOfficeDTO.class ) );
    }

    private void mockRetry()
    {
        doNothing().when( subject ).retrySleep();
    }

    @Test
    @DisplayName( "Deve tentar 1 vez quando a requisição der não encontrado." )
    void shouldRetry1TimeWhenNotFoundBranchOffice()
    {
        final HttpClientErrorException notFound = HttpClientErrorException.create(
            HttpStatusCode.valueOf( 404 ),
            "404 Not Found", null, null, null );
        when( restTemplate.getForEntity( anyString(), eq( BranchOfficeDTO.class ) ) ).thenThrow( notFound );

        assertThrows( SellerRestClientException.class, () -> subject.findByDocumentNumber( "88999" ) );

        verify( restTemplate, times( 1 ) ).getForEntity( anyString(), eq( BranchOfficeDTO.class ) );
    }

    @Test
    @DisplayName( "Deve tentar 1 vez quando a requisição der não encontrado." )
    void shouldRetry1TimeWhenAndReturnBranchOffice()
    {
        mockRetry();
        when( restTemplate.getForEntity( anyString(), eq( BranchOfficeDTO.class ) ) )
            .thenThrow( new RuntimeException() ).thenReturn( ResponseEntity.ok( BRANCH_OFFICE_DTO_2 ) );

        final BranchOfficeDTO result = subject.findByDocumentNumber( "88999" );

        verify( restTemplate, times( 2 ) ).getForEntity( anyString(), eq( BranchOfficeDTO.class ) );
        assertEquals( BRANCH_OFFICE_DTO_2, result );
    }
}
