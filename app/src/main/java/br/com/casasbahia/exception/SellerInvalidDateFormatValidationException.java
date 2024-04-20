package br.com.casasbahia.exception;

public class SellerInvalidDateFormatValidationException
    extends
        SellerValidationException
{
    public SellerInvalidDateFormatValidationException(
        final String message )
    {
        super( message );
    }
}
