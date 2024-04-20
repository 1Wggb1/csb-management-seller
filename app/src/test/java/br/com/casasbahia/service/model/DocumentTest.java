package br.com.casasbahia.service.model;

import static br.com.casasbahia.service.Constants.VALID_CNPJ;
import static br.com.casasbahia.service.Constants.VALID_CPF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import br.com.casasbahia.model.Document;

public class DocumentTest
{
    @ParameterizedTest
    @MethodSource( "nullBlankAndEmpty" )
    @DisplayName( "Deve retornar falso quando documento não é válido (nulo ou vazio)." )
    void shouldReturnFalseWhenDocumentIsNullOrEmptyOrBlank(
        final String value )
    {
        assertFalse( Document.CNPJ.validateDocument( value ) );
        assertFalse( Document.CPF.validateDocument( value ) );
    }

    private static Stream<String> nullBlankAndEmpty()
    {
        return Stream.of( null, " ".repeat( 10 ), "" );
    }

    @Test
    @DisplayName( "Deve retornar falso quando documento não tem tamanho válido." )
    void shouldReturnFalseWhenDocumentHasInvalidSize()
    {
        assertFalse( Document.CPF.validateDocument( "2873" ) );
        assertFalse( Document.CPF.validateDocument( "7".repeat( 12 ) ) );

        assertFalse( Document.CNPJ.validateDocument( "2873" ) );
        assertFalse( Document.CNPJ.validateDocument( "7".repeat( 15 ) ) );
    }

    @Test
    @DisplayName( "Deve retornar falso quando documento não é válido." )
    void shouldReturnFalseWhenDocumentIsInvalid()
    {
        assertFalse( Document.CNPJ.validateDocument( "92.735.738/0001-15".replace( "0", "1" ) ) );
        assertFalse( Document.CPF.validateDocument( "761.495.950-72".replace( "2", "9" ) ) );
    }

    @Test
    @DisplayName( "Deve retornar verdadeiro quando documento é válido." )
    void shouldReturnTrueWhenDocumentIsValid()
    {
        assertTrue( Document.CNPJ.validateDocument( VALID_CNPJ ) );
        assertTrue( Document.CPF.validateDocument( VALID_CPF ) );
    }

    @Test
    @DisplayName( "Deve remover a máscara do documento." )
    void shouldRemoveDocumentMask()
    {
        assertEquals( Document.removeMask( VALID_CNPJ ), "32826650000187" );
        assertEquals( Document.removeMask( VALID_CPF ), "81767428057" );
    }
}
