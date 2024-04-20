package br.com.casasbahia.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import br.com.casasbahia.dto.SellerFilterDTO;
import br.com.casasbahia.model.PersistentSeller;
import br.com.casasbahia.util.UnmaskUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public final class SellerSpecification
{
    public static Specification<PersistentSeller> filter(
        final SellerFilterDTO filterDTO )
    {
        if( filterDTO == null ) {
            return null;
        }
        return doFilter( filterDTO );
    }

    private static Specification<PersistentSeller> doFilter(
        final SellerFilterDTO filterDTO )
    {
        return (
            root,
            query,
            builder ) -> {
            final List<Predicate> predicates = new ArrayList<>();
            final String name = filterDTO.name();
            if( ! isNullOrEmpty( name ) ) {
                predicates.add( likeIgnoreCasePredicate( root, builder, "name", name ) );
            }
            final String contractType = filterDTO.contractType();
            if( ! isNullOrEmpty( contractType ) ) {
                predicates.add( likeIgnoreCasePredicate( root, builder, "contractType", contractType ) );
            }
            final String branchOfficeDocumentNumber = filterDTO.branchOfficeDocumentNumber();
            if( ! isNullOrEmpty( branchOfficeDocumentNumber ) ) {
                predicates.add( likeIgnoreCasePredicate( root, builder, "branchOfficeDocumentNumber",
                    UnmaskUtil.unmaskDocumentNumber( branchOfficeDocumentNumber ) ) );
            }
            return builder.and( predicates.toArray( new Predicate[ 0 ] ) );
        };
    }

    private static boolean isNullOrEmpty(
        final String value )
    {
        return value == null || value.isBlank();
    }

    private static Predicate likeIgnoreCasePredicate(
        final Root<PersistentSeller> root,
        final CriteriaBuilder builder,
        final String field,
        final String value )
    {
        return builder.like( builder.upper( root.get( field ) ), "%" + value.toUpperCase() + "%" );
    }
}
