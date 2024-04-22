package br.com.casasbahia;

import java.time.LocalDate;

import br.com.casasbahia.dto.BranchOfficeDTO;

public interface CommonTestData
{
    String VALID_CNPJ = "32.826.650/0001-87";
    String VALID_CNPJ_UNMASKED = "32826650000187";
    String VALID_CPF = "817.674.280-57";
    String VALID_CPF_UNMASKED = "81767428057";
    String VALID_BIRTHDATE = "1999-04-17";
    String VALID_EMAIL = "som@protomail.rus";

    BranchOfficeDTO BRANCH_OFFICE_DTO = new BranchOfficeDTO(
        3L,
        "Bartira",
        "05804725000156",
        "São Paulo ",
        "SP",
        "N",
        true,
        LocalDate.now().minusDays( 100 ).toString(),
        LocalDate.now().minusDays( 90 ).toString() );

    BranchOfficeDTO INACTIVE_BRANCH_OFFICE_DTO = new BranchOfficeDTO(
        3L,
        "Bartira",
        "05804725000156",
        "São Paulo ",
        "SP",
        "N",
        false,
        LocalDate.now().minusDays( 100 ).toString(),
        LocalDate.now().minusDays( 90 ).toString() );
}
