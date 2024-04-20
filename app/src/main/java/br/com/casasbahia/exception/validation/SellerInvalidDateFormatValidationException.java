package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

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
