package br.com.casasbahia.service;

import static br.com.casasbahia.CommonTestData.BRANCH_OFFICE_DTO_2;
import static br.com.casasbahia.CommonTestData.INACTIVE_BRANCH_OFFICE_DTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.casasbahia.client.BranchOfficeClient;
import br.com.casasbahia.exception.validation.SellerBranchOfficeNotActiveValidationException;
import br.com.casasbahia.model.PersistentBranchOffice;
import br.com.casasbahia.repository.BranchOfficeCacheRepository;
import br.com.casasbahia.util.UnmaskUtil;

@ExtendWith( MockitoExtension.class )
class BranchOfficeCacheServiceTest
{
    @InjectMocks
    private BranchOfficeCacheService subject;
    @Mock
    private BranchOfficeCacheRepository branchOfficeRepository;
    @Mock
    private BranchOfficeClient branchOfficeClient;

    @Test
    @DisplayName( "Deve retornar filial do repositório." )
    void shouldReturnCachedBranchOfficeWhenFoundOnRepository()
    {
        final String documentNumber = BRANCH_OFFICE_DTO_2.documentNumber();
        final PersistentBranchOffice branchOfficeCache = PersistentBranchOffice.from( BRANCH_OFFICE_DTO_2 );
        when( branchOfficeRepository.findByDocument( documentNumber ) )
            .thenReturn( Optional.of( branchOfficeCache ) );

        final PersistentBranchOffice persistentBranchOffice = subject.find( documentNumber );

        verify( branchOfficeClient, never() ).findByDocumentNumber( any() );
        assertBranchOfficeFields( persistentBranchOffice );
    }

    @Test
    @DisplayName( "Deve retornar filial e salvar no repositório." )
    void shouldReturnCallClientWhenBranchOfficeNotFoundOnRepository()
    {
        when( branchOfficeRepository.findByDocument( anyString() ) )
            .thenReturn( Optional.empty() );
        mockBranchOffice();
        final PersistentBranchOffice officeCache = PersistentBranchOffice.from(BRANCH_OFFICE_DTO_2);
        when(branchOfficeRepository.save(any())).thenReturn(officeCache);

        final PersistentBranchOffice branchOfficeCache = subject.find( BRANCH_OFFICE_DTO_2.documentNumber() );

        verify( branchOfficeRepository, times( 1 ) ).save( any() );
        assertBranchOfficeFields( branchOfficeCache );
    }

    private static void assertBranchOfficeFields(
        final PersistentBranchOffice branchOfficeCache )
    {
        assertEquals( BRANCH_OFFICE_DTO_2.id(), branchOfficeCache.getId() );
        assertEquals( BRANCH_OFFICE_DTO_2.name(), branchOfficeCache.getName() );
        assertEquals( BRANCH_OFFICE_DTO_2.city(), branchOfficeCache.getCity() );
        assertEquals( BRANCH_OFFICE_DTO_2.state(), branchOfficeCache.getState() );
        assertEquals( UnmaskUtil.unmaskDocumentNumber( BRANCH_OFFICE_DTO_2.documentNumber() ), branchOfficeCache.getDocumentNumber() );
        assertEquals( BRANCH_OFFICE_DTO_2.active(), branchOfficeCache.isActive() );
        assertEquals( BRANCH_OFFICE_DTO_2.type(), branchOfficeCache.getType() );
        assertEquals( BRANCH_OFFICE_DTO_2.creationDateTime(), branchOfficeCache.getCreationDateTime() );
        assertEquals( BRANCH_OFFICE_DTO_2.updateDateTime(), branchOfficeCache.getUpdateDateTime() );
    }

    private void mockBranchOffice() {
        when( branchOfficeClient.findByDocumentNumber( anyString() ) ).thenReturn(BRANCH_OFFICE_DTO_2);
    }

    @Test
    @DisplayName( "Deve lançar exceção quando filial está inativa." )
    void shouldThrownExceptionWhenBranchOfficeNotFoundRepositoryAndNotActive()
    {
        final String documentNumber = INACTIVE_BRANCH_OFFICE_DTO.documentNumber();
        when( branchOfficeRepository.findByDocument( documentNumber ) )
            .thenReturn( Optional.empty() );
        when( branchOfficeClient.findByDocumentNumber( documentNumber ) )
            .thenReturn( INACTIVE_BRANCH_OFFICE_DTO );

        assertThrows( SellerBranchOfficeNotActiveValidationException.class,
            () -> subject.find( INACTIVE_BRANCH_OFFICE_DTO.documentNumber() ) );
    }
}
