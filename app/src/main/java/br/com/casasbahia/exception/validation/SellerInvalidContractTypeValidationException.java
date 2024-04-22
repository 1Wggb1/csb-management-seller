package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

public class SellerInvalidContractTypeValidationException
    extends
        SellerValidationException
{
    private static final String MESSAGE = "csb.contacttype.invalid";

    public SellerInvalidContractTypeValidationException(
        final String... messageArgs )
    {
        super( MESSAGE, messageArgs );
    }
}