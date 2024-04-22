package br.csb.mock.branchoffice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.ResponseEntity;
import br.csb.mock.branchoffice.MockedBranchOffices;
import br.csb.mock.branchoffice.dto.BranchOfficeDTO;

class MockBranchOfficeControllerTest
{
    private final MockBranchOfficeController subject = new MockBranchOfficeController();

    @Test
    @DisplayName( "Deve retornar 404 quando filial não encontrada por documento." )
    void shouldReturn404WhenBranchOfficeNotFoundByDocumentNumber()
    {
        final ResponseEntity<BranchOfficeDTO> result = subject.findByDocumentNumber( "78965" );

        assertEquals( 404, result.getStatusCode().value() );
    }

    @ParameterizedTest
    @MethodSource( "validBranchOfficesDocumentNumber" )
    @DisplayName( "Deve retornar 200 quando filial encontrada por documento." )
    void shouldReturn200WhenBranchOfficeFoundByDocumentNumber(
        final String documentNumber )
    {
        final ResponseEntity<BranchOfficeDTO> result = subject.findByDocumentNumber( documentNumber );

        assertEquals( 200, result.getStatusCode().value() );
        assertEquals( MockedBranchOffices.BRANCHOFFICE_BY_DOCUMENTNUMBER.get( documentNumber ), result.getBody() );
    }

    private static Stream<String> validBranchOfficesDocumentNumber()
    {
        final Set<String> keySet = MockedBranchOffices.BRANCHOFFICE_BY_DOCUMENTNUMBER.keySet();
        return keySet.stream();
    }
}