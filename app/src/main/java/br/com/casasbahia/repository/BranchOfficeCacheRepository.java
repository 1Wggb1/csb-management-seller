package br.com.casasbahia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.casasbahia.model.PersistentBranchOffice;
import br.com.casasbahia.util.UnmaskUtil;

@Repository
public interface BranchOfficeCacheRepository
    extends
        JpaRepository<PersistentBranchOffice,Long>
{
    default Optional<PersistentBranchOffice> findByDocument(
        final String documentNumber )
    {
        return findByDocumentNumber( UnmaskUtil.unmaskDocumentNumber( documentNumber ) );
    }

    Optional<PersistentBranchOffice> findByDocumentNumber(
        String documentNumber );
}