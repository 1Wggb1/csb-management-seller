package br.com.casasbahia.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseSellerException
    extends
        RuntimeException
{
    private final String[] messageArgs;

    public BaseSellerException(
        final String message,
        final String... messageArgs )
    {
        super( message );
        this.messageArgs = messageArgs;
    }

    public abstract HttpStatus getStatus();

    public String[] getMessageArgs()
    {
        return messageArgs;
    }
}
