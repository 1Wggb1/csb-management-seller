package br.com.casasbahia.exception;

public class SellerInvalidContractTypeValidationException
    extends
        SellerValidationException
{
    public SellerInvalidContractTypeValidationException(
        final String message )
    {
        super( message );
    }
}
