package br.com.casasbahia.exception.application;

import org.springframework.http.HttpStatus;
import br.com.casasbahia.exception.BaseSellerException;

public class SellerApplicationException
    extends
        BaseSellerException
{
    public SellerApplicationException(
        final String message,
        final String... messageArgs )
    {
        super( message, messageArgs );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.UNPROCESSABLE_ENTITY;
    }
}
