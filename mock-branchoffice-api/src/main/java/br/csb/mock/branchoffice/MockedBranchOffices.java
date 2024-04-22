package br.csb.mock.branchoffice;

import java.time.LocalDate;
import java.util.Map;

import br.csb.mock.branchoffice.dto.BranchOfficeDTO;

public final class MockedBranchOffices
{
    public static final Map<String,BranchOfficeDTO> BRANCHOFFICE_BY_DOCUMENTNUMBER;

    static {
        final BranchOfficeDTO branchOfficeDTO = new BranchOfficeDTO(
            1L,
            "Extra",
            "32826650000187",
            "São João",
            "PB",
            "T",
            true,
            LocalDate.now().minusDays( 10 ).toString(),
            LocalDate.now().toString() );
        final BranchOfficeDTO branchOfficeDTO2 = new BranchOfficeDTO(
            2L,
            "Ponto",
            "16772476000180",
            "São Paulo",
            "SP",
            "T",
            true,
            LocalDate.now().minusDays( 100 ).toString(),
            LocalDate.now().minusDays( 12 ).toString() );
        final BranchOfficeDTO branchOfficeDTO3 = new BranchOfficeDTO(
            3L,
            "Bartira",
            "05804725000156",
            "São Paulo ",
            "SP",
            "N",
            false,
            LocalDate.now().minusDays( 100 ).toString(),
            LocalDate.now().minusDays( 90 ).toString() );
        BRANCHOFFICE_BY_DOCUMENTNUMBER = Map.of(
            branchOfficeDTO.documentNumber(), branchOfficeDTO,
            branchOfficeDTO2.documentNumber(), branchOfficeDTO2,
            branchOfficeDTO3.documentNumber(), branchOfficeDTO3 );
    }
}
