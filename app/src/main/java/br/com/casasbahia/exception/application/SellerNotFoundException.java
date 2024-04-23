package br.com.casasbahia.exception.application;

import org.springframework.http.HttpStatus;
import br.com.casasbahia.exception.SellerValidationException;

public class SellerNotFoundException
    extends
        SellerValidationException
{
    private static final String MESSAGE = "csb.seller.not_found";

    public SellerNotFoundException(
        final String... messageArgs )
    {
        super( MESSAGE, messageArgs );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.NOT_FOUND;
    }
}