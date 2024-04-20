package br.com.casasbahia.exception;

public class SellerValidationException
    extends
        RuntimeException
{
    public SellerValidationException(
        final String message )
    {
        super( message );
    }
}
