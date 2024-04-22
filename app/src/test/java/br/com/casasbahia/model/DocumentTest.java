package br.com.casasbahia.model;

import static br.com.casasbahia.CommonTestData.VALID_CNPJ;
import static br.com.casasbahia.CommonTestData.VALID_CNPJ_UNMASKED;
import static br.com.casasbahia.CommonTestData.VALID_CPF;
import static br.com.casasbahia.CommonTestData.VALID_CPF_UNMASKED;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DocumentTest
{
    @ParameterizedTest
    @MethodSource( "nullBlankAndEmpty" )
    @DisplayName( "Deve retornar falso quando documento não é válido (nulo ou vazio)." )
    void shouldReturnFalseWhenDocumentIsNullOrEmptyOrBlank(
        final String value )
    {
        assertFalse( Document.CNPJ.isValidDocument( value ) );
        assertFalse( Document.CPF.isValidDocument( value ) );
    }

    private static Stream<String> nullBlankAndEmpty()
    {
        return Stream.of( null, " ".repeat( 10 ), "" );
    }

    @Test
    @DisplayName( "Deve retornar falso quando documento não tem tamanho válido." )
    void shouldReturnFalseWhenDocumentHasInvalidSize()
    {
        assertFalse( Document.CPF.isValidDocument( "2873" ) );
        assertFalse( Document.CPF.isValidDocument( "7".repeat( 12 ) ) );

        assertFalse( Document.CNPJ.isValidDocument( "2873" ) );
        assertFalse( Document.CNPJ.isValidDocument( "7".repeat( 15 ) ) );
    }

    @Test
    @DisplayName( "Deve retornar falso quando documento não é válido." )
    void shouldReturnFalseWhenDocumentIsInvalid()
    {
        assertFalse( Document.CNPJ.isValidDocument( "92.735.738/0001-15".replace( "0", "1" ) ) );
        assertFalse( Document.CPF.isValidDocument( "761.495.950-72".replace( "2", "9" ) ) );
    }

    @Test
    @DisplayName( "Deve retornar verdadeiro quando documento é válido (com máscara)." )
    void shouldReturnTrueWhenDocumentIsValid()
    {
        assertTrue( Document.CNPJ.isValidDocument( VALID_CNPJ ) );
        assertTrue( Document.CPF.isValidDocument( VALID_CPF ) );
    }

    @Test
    @DisplayName( "Deve retornar verdadeiro quando documento é válido (sem máscara)." )
    void shouldReturnTrueWhenDocumentValidAndUnMasked()
    {
        assertTrue( Document.CNPJ.isValidDocument( VALID_CNPJ_UNMASKED ) );
        assertTrue( Document.CPF.isValidDocument( VALID_CPF_UNMASKED ) );
    }

    @Test
    @DisplayName( "Deve retornar verdadeiro quando documento é válido (sem máscara)." )
    void shouldReturnFalseWhenDocumentValidButInvalidMask()
    {
        assertFalse( Document.CNPJ.isValidDocument( "328266500001-87" ) );
        assertFalse( Document.CPF.isValidDocument( "817.674.28057" ) );
    }
}
