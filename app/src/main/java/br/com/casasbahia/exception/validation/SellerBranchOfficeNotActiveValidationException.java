package br.com.casasbahia.exception.validation;

import br.com.casasbahia.exception.SellerValidationException;

public class SellerBranchOfficeNotActiveValidationException
    extends
        SellerValidationException
{
    private static final String MESSAGE = "cbs.branchoffice.inactive";

    public SellerBranchOfficeNotActiveValidationException(
        final String... messageArgs )
    {
        super( MESSAGE, messageArgs );
    }
}
