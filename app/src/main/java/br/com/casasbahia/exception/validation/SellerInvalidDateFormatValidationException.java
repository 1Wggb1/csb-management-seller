package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

public class SellerInvalidDateFormatValidationException
    extends
        SellerValidationException
{
    private static final String MESSAGE = "csb.date.invalid.format";

    public SellerInvalidDateFormatValidationException(
        final String... messageArgs )
    {
        super( MESSAGE, messageArgs );
    }
}
