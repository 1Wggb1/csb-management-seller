package br.com.casasbahia.exception;

public class SellerInvalidDateFormatValidationException
    extends
        SellerValidationException
{
    public SellerInvalidDateFormatValidationException(
        final String message,
        final String... messageArgs )
    {
        super( message, messageArgs );
    }
}
