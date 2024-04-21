package br.com.casasbahia.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import br.com.casasbahia.model.ContractType;

@DataJpaTest
@ActiveProfiles( "test" )
class SellerRepositoryTest
{
    @Autowired
    private SellerRepository repository;
    @Autowired
    private TestEntityManager testEntityManager;

    @AfterEach
    void tearDown()
    {
        testEntityManager.getEntityManager()
            .createNativeQuery( "ALTER SEQUENCE ENROLLMENT_SEQ RESTART;" )
            .executeUpdate();
    }

    @ParameterizedTest
    @MethodSource( "contractTypes" )
    @DisplayName( "Deve retorna matrícula no formato XXXXXXXX-CTL (número 8 digítos mais acrônimo do tipo de contrato." )
    void shouldReturnIdentifierWithSuffixOfContractType(
        final ContractType contractType )
    {
        final String enrollment = repository.generateEnrollment( contractType );

        final Long currentSequenceValue = getCurrentEnrollmentSequenceValue();
        assertEquals( expectedEnrollmentValue( contractType, currentSequenceValue ), enrollment );
    }

    private Long getCurrentEnrollmentSequenceValue()
    {
        return (Long) testEntityManager.getEntityManager()
            .createNativeQuery( "SELECT currval('ENROLLMENT_SEQ')" )
            .getSingleResult();
    }

    private static String expectedEnrollmentValue(
        final ContractType contractType,
        final Long currentSequenceValue )
    {
        return String.format( "%08d", currentSequenceValue ) + "-" + contractType.getAcronym();
    }

    private static Stream<ContractType> contractTypes()
    {
        return Stream.of( ContractType.CLT, ContractType.PJ, ContractType.OUTSOURCING );
    }

    @Test
    @DisplayName( "Deve retornar matrícula sequencialmente." )
    void shouldReturnSequentialEnrollmentValue()
    {
        final String enrollment = repository.generateEnrollment( ContractType.CLT );
        final String enrollment2 = repository.generateEnrollment( ContractType.PJ );
        final String enrollment3 = repository.generateEnrollment( ContractType.OUTSOURCING );

        assertEquals( expectedEnrollmentValue( ContractType.CLT, 1L ), enrollment );
        assertEquals( expectedEnrollmentValue( ContractType.PJ, 2L ), enrollment2 );
        assertEquals( expectedEnrollmentValue( ContractType.OUTSOURCING, 3L ), enrollment3 );
    }

    @ParameterizedTest
    @ValueSource( longs = {
        189, 99999998
    } )
    @DisplayName( "Deve retornar matrícula no formato válido." )
    void shouldReturnEnrollmentOnValidFormat(
        final long nextValue )
    {
        testEntityManager.getEntityManager()
            .createNativeQuery( "ALTER SEQUENCE ENROLLMENT_SEQ RESTART WITH " + nextValue )
            .executeUpdate();

        final ContractType contractType = ContractType.CLT;
        final String enrollment = repository.generateEnrollment( contractType );

        assertEquals( expectedEnrollmentValue( contractType, getCurrentEnrollmentSequenceValue() ), enrollment );
    }

    @Test
    @DisplayName( "Deve lançar exceção quando sequência chegar ao limite." )
    void shouldThrownExceptionWhenSequenceOutRange()
    {
        testEntityManager.getEntityManager()
            .createNativeQuery( "ALTER SEQUENCE ENROLLMENT_SEQ RESTART WITH 99999999;" )
            .executeUpdate();

        final ContractType contractType = ContractType.CLT;
        repository.nextEnrollmentValue();
        assertThrows( Exception.class, () -> repository.generateEnrollment( contractType ) );
    }
}
