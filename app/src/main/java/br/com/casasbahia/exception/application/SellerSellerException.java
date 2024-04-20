package br.com.casasbahia.exception.application;

import org.springframework.http.HttpStatus;
import br.com.casasbahia.exception.BaseSellerException;

public class SellerSellerException
    extends
        BaseSellerException
{
    public SellerSellerException(
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
