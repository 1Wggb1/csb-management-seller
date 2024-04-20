package br.com.casasbahia.util;

import br.com.casasbahia.model.ContractType;

public final class EnrollmentGenerator
{
    private static final String ENROLLMENT_FORMAT = "%08d";

    public static String generateEnrollment(
        final ContractType contractType )
    {
        return String.format( ENROLLMENT_FORMAT, 10 ) + "-" + contractType.getAcronym();
    }
}