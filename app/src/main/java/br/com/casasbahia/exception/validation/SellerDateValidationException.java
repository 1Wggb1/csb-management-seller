package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

public class SellerDateValidationException
    extends
        SellerValidationException
{
    private static final String MESSAGE = "csb.date.invalid.range";

    public SellerDateValidationException(
        final String... messageArgs )
    {
        super( MESSAGE, messageArgs );
    }
}
