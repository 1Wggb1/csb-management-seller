package br.com.casasbahia.exception.application;

import org.springframework.http.HttpStatus;
import br.com.casasbahia.exception.BaseSellerException;

public class SellerNotFoundException
    extends
        BaseSellerException
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