package br.com.casasbahia.converter.impl;

import static br.com.casasbahia.CommonTestData.VALID_BIRTHDATE;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ_UNMASKED;
import static br.com.casasbahia.CommonTestData.VALID_CPF;
import static br.com.casasbahia.CommonTestData.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.casasbahia.dto.PageableDTO;
import br.com.casasbahia.dto.SellerDTO;
import br.com.casasbahia.dto.SellerFilterDTO;
import br.com.casasbahia.dto.SellerPageableDTO;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.exception.application.SellerApplicationException;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;

@ExtendWith( MockitoExtension.class )
class SellerConverterImplTest
{
    private static final PersistentSeller PERSISTENT_SELLER = new PersistentSeller(
        "name",
        "00000001-CLT",
        "19990417",
        "78596589657",
        VALID_EMAIL,
        ContractType.CLT,
        "14555896574563" );

    @InjectMocks
    private SellerConverterImpl subject;
    @Mock
    private SellerRepository sellerRepository;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName( "Deve retornar persistente a partir de DTO." )
    void shouldReturnPersistFromDTO()
    {
        final ContractType contractType = ContractType.CLT;
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "name", VALID_EMAIL,
            VALID_BIRTHDATE,
            VALID_CPF,
            contractType.name(),
            VALID_CNPJ );
        doCallRealMethod().when( sellerRepository ).generateEnrollment( contractType );
        when( sellerRepository.nextEnrollmentValue() ).thenReturn( 107L );

        final PersistentSeller persistentSeller = subject.toModelCreation( sellerRequestDTO );
        assertEquals( sellerRequestDTO.name(), persistentSeller.getName() );
        assertEquals( sellerRequestDTO.birthDay().replaceAll( "-", "" ),
            persistentSeller.getBirthDay() );
        assertEquals( sellerRequestDTO.email(), persistentSeller.getEmail() );
        assertEquals( sellerRequestDTO.documentNumber().replaceAll( "[.-]", "" ),
            persistentSeller.getDocumentNumber() );
        assertEquals( sellerRequestDTO.contractType(), persistentSeller.getContractType().name() );
        assertEquals( sellerRequestDTO.branchOfficeDocumentNumber().replaceAll( "[./-]",
            "" ), persistentSeller.getBranchOfficeDocumentNumber() );
        final String enrollment = persistentSeller.getEnrollment();
        assertNotNull( enrollment );
        assertTrue( enrollment.contains( "-" + contractType.getAcronym() ) );
    }

    @Test
    @DisplayName( "Deve retornar DTO a partir de persistente." )
    void shouldReturnDTOFromPersistent()
    {
        final SellerResponseDTO responseDTO = subject.toDTO( PERSISTENT_SELLER );
        assertEquals( PERSISTENT_SELLER.getEnrollment(), responseDTO.enrollment() );
    }

    @Test
    @DisplayName( "Deve lançar exceção quando filtro tem string com json inválido." )
    void shouldThrownExceptionWhenFilterHasInvalidJsonFormat()
    {
        assertThrows( SellerApplicationException.class, () -> subject.toFilterDTO( "{s}" ) );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName( "Deve retornar nulo qunado filtro nulo ou vazio." )
    void shouldReturnNullWhenFilterNullOrEmpty(
        final String nullOrEmpty )
    {
        assertNull( subject.toFilterDTO( nullOrEmpty ) );
    }

    @Test
    @DisplayName( "Deve converter string para filtro." )
    void shouldConvertToFilterDTO()
        throws JsonProcessingException
    {
        final SellerFilterDTO filterDTO = new SellerFilterDTO( "Name", ContractType.CLT.name(), VALID_CNPJ_UNMASKED );
        final SellerFilterDTO result = subject.toFilterDTO( new ObjectMapper().writeValueAsString( filterDTO ) );

        assertEquals( filterDTO, result );
    }

    @Test
    @DisplayName( "Deve converter persistente para DTO." )
    void shouldConvertToSellerDTO()
    {
        final SellerDTO sellerDTO = subject.toSellerDTO( PERSISTENT_SELLER );

        assertEquals( PERSISTENT_SELLER.getName(), sellerDTO.name() );
        assertEquals( PERSISTENT_SELLER.getBirthDay(), sellerDTO.birthDay() );
        assertEquals( PERSISTENT_SELLER.getEmail(), sellerDTO.email() );
        assertEquals( PERSISTENT_SELLER.getEnrollment(), sellerDTO.enrollment() );
        assertEquals( PERSISTENT_SELLER.getContractType().name(), sellerDTO.contractType() );
        assertEquals( PERSISTENT_SELLER.getDocumentNumber(), sellerDTO.documentNumber() );
        assertEquals( PERSISTENT_SELLER.getBranchOfficeDocumentNumber(), sellerDTO.branchOfficeDocumentNumber() );
    }

    @Test
    @DisplayName( "Deve converter persistentes para DTO paginavel." )
    void shouldConvertToSellerPageableDTO()
    {
        final PageRequest pageRequest = PageRequest.of( 0, 4 );
        final int totalElements = 5;
        final Page<PersistentSeller> sellers = new PageImpl<>(
            List.of( PERSISTENT_SELLER, PERSISTENT_SELLER ), pageRequest, totalElements );

        final SellerPageableDTO sellerPageableDTO = subject.toPageableDTO( sellers );

        final PageableDTO pageable = sellerPageableDTO.pageable();
        assertEquals( pageRequest.getPageNumber(), pageable.page() );
        assertEquals( pageRequest.getPageSize(), pageable.size() );
        assertEquals( 2, pageable.numberOfElements() );
        assertEquals( 2, pageable.totalPages() );
        assertEquals( totalElements, pageable.totalElements() );
        assertEquals( 2, sellerPageableDTO.elements().size() );
    }

    @Test
    @DisplayName( "Deve converter atualizar modelo com DTO." )
    void shouldConvertToSellerOnUpdate()
    {
        final PersistentSeller seller = new PersistentSeller(
            "Will",
            "00000001-CLT",
            "19990417",
            "78596589657",
            VALID_EMAIL,
            ContractType.CLT,
            "14555896574563" );
        final ContractType contractType = ContractType.OUTSOURCING;
        final SellerRequestDTO sellerRequestDTO = new SellerRequestDTO( "Will Garbo",
            "email@email.org",
            "1999-04-18",
            VALID_CPF,
            contractType.name(),
            VALID_CNPJ );

        final PersistentSeller updated = subject.toModelUpdate( seller, sellerRequestDTO );

        assertEquals( seller.getId(), updated.getId() );
        assertEquals( seller.getEnrollment(), updated.getEnrollment() );
        assertEquals( sellerRequestDTO.name(), updated.getName() );
        assertEquals( sellerRequestDTO.birthDay().replaceAll( "-", "" ),
            updated.getBirthDay() );
        assertEquals( sellerRequestDTO.email(), updated.getEmail() );
        assertEquals( sellerRequestDTO.documentNumber().replaceAll( "[.-]", "" ),
            updated.getDocumentNumber() );
        assertEquals( sellerRequestDTO.contractType(), updated.getContractType().name() );
        assertEquals( sellerRequestDTO.branchOfficeDocumentNumber().replaceAll( "[./-]",
            "" ), updated.getBranchOfficeDocumentNumber() );
    }
}