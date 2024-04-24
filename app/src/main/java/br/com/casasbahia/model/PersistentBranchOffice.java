package br.com.casasbahia.model;

import br.com.casasbahia.dto.BranchOfficeDTO;
import br.com.casasbahia.util.UnmaskUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name = "branch_office" )
public class PersistentBranchOffice
{
    @Id
    private Long id;

    @Column( name = "name" )
    private String name;

    @Column( name = "document_number" )
    private String documentNumber;

    @Column( name = "city" )
    private String city;

    @Column( name = "state" )
    private String state;

    @Column( name = "type" )
    private String type;

    @Column( name = "active" )
    private boolean active;

    @Column( name = "creation_datetime" )
    private String creationDateTime;

    @Column( name = "update_datetime" )
    private String updateDateTime;

    protected PersistentBranchOffice()
    {
    }

    public PersistentBranchOffice(
        final Long id,
        final String name,
        final String documentNumber,
        final String city,
        final String state,
        final String type,
        final boolean active,
        final String creationDateTime,
        final String updateDateTime )
    {
        this.id = id;
        this.name = name;
        this.documentNumber = documentNumber;
        this.city = city;
        this.state = state;
        this.type = type;
        this.active = active;
        this.creationDateTime = creationDateTime;
        this.updateDateTime = updateDateTime;
    }

    public static PersistentBranchOffice from(
        final BranchOfficeDTO branchOfficeDTO )
    {
        return new PersistentBranchOffice(
            branchOfficeDTO.id(),
            branchOfficeDTO.name(),
            UnmaskUtil.unmaskDocumentNumber( branchOfficeDTO.documentNumber() ),
            branchOfficeDTO.city(),
            branchOfficeDTO.state(),
            branchOfficeDTO.type(),
            branchOfficeDTO.active(),
            branchOfficeDTO.creationDateTime(),
            branchOfficeDTO.updateDateTime() );
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

    public String getDocumentNumber()
    {
        return documentNumber;
    }

    public void setDocumentNumber(
        final String documentNumber )
    {
        this.documentNumber = documentNumber;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(
        final String city )
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(
        final String state )
    {
        this.state = state;
    }

    public String getType()
    {
        return type;
    }

    public void setType(
        final String type )
    {
        this.type = type;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(
        final boolean active )
    {
        this.active = active;
    }

    public String getCreationDateTime()
    {
        return creationDateTime;
    }

    public void setCreationDateTime(
        final String creationDateTime )
    {
        this.creationDateTime = creationDateTime;
    }

    public String getUpdateDateTime()
    {
        return updateDateTime;
    }

    public void setUpdateDateTime(
        final String updateDateTime )
    {
        this.updateDateTime = updateDateTime;
    }
}
