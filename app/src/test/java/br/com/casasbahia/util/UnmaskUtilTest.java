package br.com.casasbahia.util;

import static br.com.casasbahia.service.Constants.VALID_CNPJ;
import static br.com.casasbahia.service.Constants.VALID_CPF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UnmaskUtilTest
{
    @Test
    @DisplayName( "Deve remover a máscara do documento." )
    void shouldRemoveDocumentMask()
    {
        assertEquals( "32826650000187", UnmaskUtil.unmaskDocumentNumber( VALID_CNPJ ) );
        assertEquals( "81767428057", UnmaskUtil.unmaskDocumentNumber( VALID_CPF ) );
    }

    @Test
    @DisplayName( "Deve remover a máscara da data." )
    void shouldRemoveDateMask()
    {
        assertEquals( "19990417", UnmaskUtil.unmaskDocumentNumber( "1999-04-17" ) );
    }
}
