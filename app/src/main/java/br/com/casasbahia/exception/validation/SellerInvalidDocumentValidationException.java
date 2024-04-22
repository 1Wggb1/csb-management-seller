package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

public class SellerInvalidDocumentValidationException
    extends
        SellerValidationException
{
    private static final String MESSAGE = "csb.documentnumber.invalid";

    public SellerInvalidDocumentValidationException(
        final String... messageArgs )
    {
        super( MESSAGE, messageArgs );
    }
}
