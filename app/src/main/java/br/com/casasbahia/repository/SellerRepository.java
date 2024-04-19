package br.com.casasbahia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.casasbahia.model.PersistentSeller;

@Repository
public interface SellerRepository
    extends
        JpaRepository<PersistentSeller,Long>
{
}
