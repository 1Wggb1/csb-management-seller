package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

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
