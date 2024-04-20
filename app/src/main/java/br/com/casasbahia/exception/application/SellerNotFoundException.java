package br.com.casasbahia.exception.application;

import org.springframework.http.HttpStatus;
import br.com.casasbahia.exception.BaseSellerException;

public class SellerNotFoundException
    extends
        BaseSellerException
{
    public SellerNotFoundException(
        final String message,
        final String... messageArgs )
    {
        super( message, messageArgs );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.NOT_FOUND;
    }
}