package br.com.casasbahia;

import java.time.LocalDate;

import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;

public interface CommonTestData
{
    String VALID_CNPJ = "32.826.650/0001-87";
    String VALID_CNPJ_2 = "42.768.931/0001-84";
    String VALID_CNPJ_UNMASKED = "32826650000187";
    String VALID_CPF = "817.674.280-57";
    String VALID_CPF_2 = "378.288.320-95";
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

    PersistentSeller PERSISTENT_SELLER_CLT = new PersistentSeller(
        "name",
        "00000001-CLT",
        "19990417",
        VALID_CPF,
        VALID_EMAIL,
        ContractType.CLT,
        VALID_CNPJ );
}
