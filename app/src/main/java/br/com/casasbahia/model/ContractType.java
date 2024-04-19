package br.com.casasbahia.model;

public enum ContractType
{
    OUTSOURCING( "OUT", DocumentType.CPF ),
    PJ( "PJ", DocumentType.CNPJ ),
    CLT( "CLT", DocumentType.CPF );

    private final String acronym;
    private final DocumentType documentType;

    ContractType(
        final String acronym,
        final DocumentType documentType )
    {
        this.acronym = acronym;
        this.documentType = documentType;
    }

    public String getAcronym()
    {
        return acronym;
    }

    public DocumentType getDocumentType()
    {
        return documentType;
    }

    public boolean validateDocumentType()
    {
        return true;
    }
}