package br.com.casasbahia.model;

import java.util.InputMismatchException;

public enum Document
{
    CPF
    {
        @Override
        boolean doValidateDocument(
            final String document )
        {
            return isCpf( document );
        }

        private static boolean isCpf(
            final String document )
        {
            try {
                Long.parseLong( document );
            } catch( final NumberFormatException e ) {
                return false;
            }
            if( document.length() != 11 ) {
                return false;
            }

            int d1, d2;
            int digito1, digito2, resto;
            int digitoCPF;
            final String nDigResult;

            d1 = d2 = 0;
            digito1 = digito2 = resto = 0;
            for( int nCount = 1; nCount < document.length() - 1; nCount++ ) {
                digitoCPF = Integer.valueOf( document.substring( nCount - 1, nCount ) ).intValue();
                d1 = d1 + ( 11 - nCount ) * digitoCPF;
                d2 = d2 + ( 12 - nCount ) * digitoCPF;
            } ;

            resto = ( d1 % 11 );

            if( resto < 2 ) {
                digito1 = 0;
            } else {
                digito1 = 11 - resto;
            }

            d2 += 2 * digito1;
            resto = ( d2 % 11 );

            if( resto < 2 ) {
                digito2 = 0;
            } else {
                digito2 = 11 - resto;
            }

            final String nDigVerific = document.substring( document.length() - 2 );
            nDigResult = digito1 + "" + digito2;
            return nDigVerific.equals( nDigResult );
        }
    },
    CNPJ
    {
        @Override
        boolean doValidateDocument(
            final String document )
        {
            return isCnpj( document );
        }

        private boolean isCnpj(
            final String document )
        {
            try {
                Long.parseLong( document );
            } catch( final NumberFormatException e ) {
                return false;
            }

            if( document.equals( "00000000000000" ) || document.equals( "11111111111111" )
                || document.equals( "22222222222222" ) || document.equals( "33333333333333" )
                || document.equals( "44444444444444" ) || document.equals( "55555555555555" )
                || document.equals( "66666666666666" ) || document.equals( "77777777777777" )
                || document.equals( "88888888888888" ) || document.equals( "99999999999999" ) || document.length() != 14 ) {
                return false;
            }
            final char dig13;
            final char dig14;
            int sm, i, r, num, peso;
            try {
                sm = 0;
                peso = 2;
                for( i = 11; i >= 0; i-- ) {
                    num = document.charAt( i ) - 48;
                    sm = sm + ( num * peso );
                    peso = peso + 1;
                    if( peso == 10 ) {
                        peso = 2;
                    }
                }

                r = sm % 11;
                if( ( r == 0 ) || ( r == 1 ) ) {
                    dig13 = '0';
                } else {
                    dig13 = (char) ( ( 11 - r ) + 48 );
                }

                sm = 0;
                peso = 2;
                for( i = 12; i >= 0; i-- ) {
                    num = document.charAt( i ) - 48;
                    sm = sm + ( num * peso );
                    peso = peso + 1;
                    if( peso == 10 ) {
                        peso = 2;
                    }
                }
                r = sm % 11;
                if( ( r == 0 ) || ( r == 1 ) ) {
                    dig14 = '0';
                } else {
                    dig14 = (char) ( ( 11 - r ) + 48 );
                }
                if( ( dig13 == document.charAt( 12 ) ) && ( dig14 == document.charAt( 13 ) ) ) {
                    return true;
                } else {
                    return false;
                }
            } catch( final InputMismatchException error ) {
                return false;
            }
        }
    };

    private static boolean isNullOrBlank(
        final String document )
    {
        return document == null || document.isBlank();
    }

    public static String removeMask(
        final String document )
    {
        return document.replaceAll( "[/.-]", "" );
    }

    public boolean validateDocument(
        final String document )
    {
        if( isNullOrBlank( document ) ) {
            return false;
        }
        return doValidateDocument( removeMask( document ) );
    }

    abstract boolean doValidateDocument(
        final String document );
}
