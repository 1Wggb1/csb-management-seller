package br.com.casasbahia.exception;

public class SellerInvalidContractTypeValidationException
    extends
        SellerValidationException
{
    public SellerInvalidContractTypeValidationException(
        final String message,
        final String... messageArgs )
    {
        super( message, messageArgs );
    }
}
