package br.com.casasbahia.model;

import br.com.casasbahia.util.UnmaskUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @JoinColumn( name = "branch_office_id", nullable = false )
    @ManyToOne
    private PersistentBranchOffice branchOffice;

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
        final PersistentBranchOffice branchOffice )
    {
        this.name = name;
        this.enrollment = enrollment;
        setBirthDay( birthDay );
        setDocumentNumber( documentNumber );
        this.email = email;
        this.contractType = contractType;
        this.branchOffice = branchOffice;
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

    public PersistentBranchOffice getBranchOffice()
    {
        return branchOffice;
    }

    public void setBranchOffice(
        final PersistentBranchOffice branchOffice )
    {
        this.branchOffice = branchOffice;
    }
}
