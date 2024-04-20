package br.com.casasbahia.exception;

public class SellerInvalidDocumentValidationException
    extends
        SellerValidationException
{
    public SellerInvalidDocumentValidationException(
        final String message,
        final String... messageArgs )
    {
        super( message, messageArgs );
    }
}
