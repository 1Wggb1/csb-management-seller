package br.com.casasbahia.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContractTypeTest
{
    @ParameterizedTest
    @ValueSource( strings = {
        "out", "sil", "ctl", "none"
    } )
    void shouldReturnFalseWhenTypeInvalid(
        final String value )
    {
        assertFalse( ContractType.exists( value ) );
    }

    @ParameterizedTest
    @ValueSource( strings = {
        "outsourcing", "clt", "pj", "OUTSOURCING", "CLT", "PJ"
    } )
    void shouldReturnTrueWhenTypeValid(
        final String value )
    {
        assertTrue( ContractType.exists( value ) );
    }
}
