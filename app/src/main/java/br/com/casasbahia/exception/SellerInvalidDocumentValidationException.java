package br.com.casasbahia.exception;

public class SellerInvalidDocumentValidationException
    extends
        SellerValidationException
{
    public SellerInvalidDocumentValidationException(
        final String message )
    {
        super( message );
    }
}
