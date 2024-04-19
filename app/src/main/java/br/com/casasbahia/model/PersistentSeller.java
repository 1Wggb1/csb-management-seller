package br.com.casasbahia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table( name = "seller" )
public class PersistentSeller
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull
    @Column( nullable = false )
    private String name;

    @Column
    private String birthDate;

    @NotNull
    @Column( nullable = false )
    private String documentNumber;

    @Column( nullable = false )
    private String email;

    @Column
    @Enumerated( EnumType.STRING )
    private ContractType contractType;

    @NotNull
    @Column( nullable = false )
    private String branchOfficeDocumentNumber;

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(
        final String name )
    {
        this.name = name;
    }

    public String getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(
        final String birthDate )
    {
        this.birthDate = birthDate;
    }

    public String getDocumentNumber()
    {
        return documentNumber;
    }

    public void setDocumentNumber(
        final String documentNumber )
    {
        this.documentNumber = documentNumber;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(
        final String email )
    {
        this.email = email;
    }

    public ContractType getContractType()
    {
        return contractType;
    }

    public void setContractType(
        final ContractType contractType )
    {
        this.contractType = contractType;
    }

    public String getBranchOfficeDocumentNumber()
    {
        return branchOfficeDocumentNumber;
    }

    public void setBranchOfficeDocumentNumber(
        final String branchOfficeDocumentNumber )
    {
        this.branchOfficeDocumentNumber = branchOfficeDocumentNumber;
    }
}
