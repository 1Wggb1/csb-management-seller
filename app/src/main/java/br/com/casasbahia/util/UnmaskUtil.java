package br.com.casasbahia.util;

public final class UnmaskUtil
{
    public static String unmaskDate(
        final String date )
    {
        if( date == null ) {
            return null;
        }
        return date.replaceAll( "-", "" );
    }

    public static String unmaskDocumentNumber(
        final String document )
    {
        if( document == null ) {
            return null;
        }
        return document.replaceAll( "[/.-]", "" );
    }
}
