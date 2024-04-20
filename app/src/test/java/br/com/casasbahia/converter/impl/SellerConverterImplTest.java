package br.com.casasbahia.converter.impl;

import static br.com.casasbahia.service.Constants.VALID_BIRTHDATE;
import static br.com.casasbahia.service.Constants.VALID_CNPJ;
import static br.com.casasbahia.service.Constants.VALID_CPF;
import static br.com.casasbahia.service.Constants.VALID_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.casasbahia.dto.SellerRequestDTO;
import br.com.casasbahia.dto.SellerResponseDTO;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.repository.SellerRepository;

@ExtendWith( MockitoExtension.class )
class SellerConverterImplTest
{
    @InjectMocks
    private SellerConverterImpl subject;
    @Mock
    private SellerRepository sellerRepository;

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

        final PersistentSeller persistentSeller = subject.toModel( sellerRequestDTO );
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
        final PersistentSeller persistentSeller = new PersistentSeller(
            "name",
            "00000001-CLT",
            "19990417",
            "78596589657",
            VALID_EMAIL,
            ContractType.CLT,
            "14555896574563" );
        final SellerResponseDTO responseDTO = subject.toDTO( persistentSeller );
        assertEquals( persistentSeller.getEnrollment(), responseDTO.enrollment() );
    }
}