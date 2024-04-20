package br.com.casasbahia.model;

import java.util.Arrays;

public enum ContractType
{
    OUTSOURCING( "OUT", Document.CPF ),
    PJ( "PJ", Document.CNPJ ),
    CLT( "CLT", Document.CPF );

    private final String acronym;
    private final Document requiredDocument;

    ContractType(
        final String acronym,
        final Document requiredDocument )
    {
        this.acronym = acronym;
        this.requiredDocument = requiredDocument;
    }

    public String getAcronym()
    {
        return acronym;
    }

    public Document getRequiredDocument()
    {
        return requiredDocument;
    }

    public static boolean exists(
        final String contractType )
    {
        return Arrays.stream( ContractType.values() )
            .anyMatch( enumValue -> enumValue.name().equalsIgnoreCase( contractType ) );
    }
}