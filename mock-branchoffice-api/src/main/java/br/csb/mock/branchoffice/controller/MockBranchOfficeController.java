package br.csb.mock.branchoffice.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.csb.mock.branchoffice.MockedBranchOffices;
import br.csb.mock.branchoffice.dto.BranchOfficeDTO;

@RestController
@RequestMapping( "/v1/mock-branch-office" )
public class MockBranchOfficeController
{
    private static final Logger LOGGER = LoggerFactory.getLogger( MockBranchOfficeController.class );

    @GetMapping( "/{documentNumber}" )
    public ResponseEntity<BranchOfficeDTO> findByDocumentNumber(
        @PathVariable( "documentNumber" ) final String documentNumber )
    {
        LOGGER.info( "Finding... by documentNumber = {}", documentNumber );
        final BranchOfficeDTO branchOfficeDTO = MockedBranchOffices.BRANCHOFFICE_BY_DOCUMENTNUMBER.get( documentNumber );
        LOGGER.info( "Returning... result of documentNumber = {}", documentNumber );
        return Objects.isNull( branchOfficeDTO ) ? ResponseEntity.notFound().build() : ResponseEntity.ok(
            branchOfficeDTO );
    }
}
