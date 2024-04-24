package br.com.casasbahia.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ContractTypeTest
{
    @ParameterizedTest
    @ValueSource( strings = {
        "out", "sil", "ctl", "none"
    } )
    @DisplayName( "Deve retornar falso quando quando tipo inválido." )
    void shouldReturnFalseWhenTypeInvalid(
        final String value )
    {
        assertFalse( ContractType.exists( value ) );
    }

    @ParameterizedTest
    @ValueSource( strings = {
        "outsourcing", "clt", "pj", "OUTSOURCING", "CLT", "PJ"
    } )
    @DisplayName( "Deve retornar verdadeiro quando quando tipo válido." )
    void shouldReturnTrueWhenTypeValid(
        final String value )
    {
        assertTrue( ContractType.exists( value ) );
    }
}
