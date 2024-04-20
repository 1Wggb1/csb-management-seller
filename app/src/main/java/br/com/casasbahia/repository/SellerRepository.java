package br.com.casasbahia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.casasbahia.model.ContractType;
import br.com.casasbahia.model.PersistentSeller;

@Repository
public interface SellerRepository
    extends
        JpaRepository<PersistentSeller,Long>
{
    String ENROLLMENT_FORMAT = "%08d";

    default String generateEnrollment(
        final ContractType contractType )
    {
        return String.format( ENROLLMENT_FORMAT, nextEnrollmentValue() ) + "-" + contractType.getAcronym();
    }

    @Query( value = "SELECT nextval('ENROLLMENT_SEQ')", nativeQuery = true )
    Long nextEnrollmentValue();

    PersistentSeller findByEnrollment(
        String enrollment );
}
