package br.com.casasbahia.model;

import br.com.casasbahia.util.UnmaskUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
@Table( name = "seller" )
public class PersistentSeller
{
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull
    @Column( name = "name", nullable = false )
    private String name;

    @NotNull
    @Column( name = "enrollment", nullable = false, unique = true )
    private String enrollment;

    @Column( name = "birthday" )
    private String birthDay;

    @NotNull
    @Column( name = "document_number", nullable = false )
    private String documentNumber;

    @Email
    @Column( name = "email", nullable = false )
    private String email;

    @Column( name = "contract_type" )
    @Enumerated( EnumType.STRING )
    private ContractType contractType;

    @NotNull
    @Column( name = "branch_office_document_number", nullable = false )
    private String branchOfficeDocumentNumber;

    protected PersistentSeller()
    {
    }

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

    public String getBirthDay()
    {
        return birthDay;
    }

    public void setBirthDay(
        final String birthDay )
    {
        if( birthDay != null ) {
            this.birthDay = UnmaskUtil.unmaskDate( birthDay );
        }
    }

    public String getDocumentNumber()
    {
        return documentNumber;
    }

    public void setDocumentNumber(
        final String documentNumber )
    {
        this.documentNumber = UnmaskUtil.unmaskDocumentNumber( documentNumber );
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
        this.branchOfficeDocumentNumber = UnmaskUtil.unmaskDocumentNumber( branchOfficeDocumentNumber );
    }
}
