package br.com.casasbahia.exception;

import org.springframework.http.HttpStatus;

public abstract class SellerValidationException
    extends
        RuntimeException
{
    private final String[] messageArgs;

    public SellerValidationException(
        final String message,
        final String... messageArgs )
    {
        super( message );
        this.messageArgs = messageArgs;
    }

    public HttpStatus getStatus()
    {
        return HttpStatus.BAD_REQUEST;
    }

    public String[] getMessageArgs()
    {
        return messageArgs;
    }
}
