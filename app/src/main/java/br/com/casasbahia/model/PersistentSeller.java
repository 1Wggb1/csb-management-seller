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

    @NotNull
    @Column( nullable = false, unique = true )
    private String enrollment;

    @Column
    private String birthDay;

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

    public PersistentSeller(
        final String name,
        final String enrollment,
        final String birthDay,
        final String documentNumber,
        final String email,
        final ContractType contractType,
        final String branchOfficeDocumentNumber )
    {
        this.name = name;
        this.enrollment = enrollment;
        setBirthDay( birthDay );
        setDocumentNumber( documentNumber );
        this.email = email;
        this.contractType = contractType;
        setBranchOfficeDocumentNumber( branchOfficeDocumentNumber );
    }

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

    public String getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment(
        final String enrollment )
    {
        this.enrollment = enrollment;
    }

    public String getBirthDay()
    {
        return birthDay;
    }

    public void setBirthDay(
        final String birthDay )
    {
        if( birthDay != null ) {
            this.birthDay = birthDay.replaceAll( "/", "" );
        }
    }

    public String getDocumentNumber()
    {
        return documentNumber;
    }

    public void setDocumentNumber(
        final String documentNumber )
    {
        this.documentNumber = Document.removeMask( documentNumber );
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
        this.branchOfficeDocumentNumber = Document.removeMask( branchOfficeDocumentNumber );
    }
}
