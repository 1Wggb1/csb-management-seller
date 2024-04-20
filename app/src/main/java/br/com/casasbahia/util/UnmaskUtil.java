package br.com.casasbahia.util;

public final class UnmaskUtil
{
    public static String unmaskDate(
        final String date )
    {
        return date.replaceAll( "-", "" );
    }

    public static String unmaskDocumentNumber(
        final String document )
    {
        return document.replaceAll( "[/.-]", "" );
    }
}
