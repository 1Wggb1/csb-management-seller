package br.com.casasbahia.exception.application;

import org.springframework.http.HttpStatus;
import br.com.casasbahia.exception.BaseSellerException;

public class SellerRestClientException
    extends
        BaseSellerException
{
    private final int statusCode;

    public SellerRestClientException(
        final String message,
        final int statusCode,
        final String... messageArgs )
    {
        super( message, messageArgs );
        this.statusCode = statusCode;
    }

    @Override
    public HttpStatus getStatus()
    {
        return HttpStatus.valueOf( statusCode );
    }
}
