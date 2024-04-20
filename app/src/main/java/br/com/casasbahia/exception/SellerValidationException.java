package br.com.casasbahia.exception;

import org.springframework.http.HttpStatus;

public abstract class SellerValidationException
    extends
        BaseSellerException
{
    public SellerValidationException(
        final String message,
        final String... messageArgs )
    {
        super( message, messageArgs );
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.BAD_REQUEST;
    }
}
