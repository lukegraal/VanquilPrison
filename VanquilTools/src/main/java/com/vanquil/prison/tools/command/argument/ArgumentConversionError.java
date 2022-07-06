package com.vanquil.prison.tools.command.argument;

public class ArgumentConversionError extends RuntimeException {
    public enum Reason {
        FAILED_TO_PARSE, MISSING_VALUE, INTERNAL_ERROR;
    }

    private final Reason reason;

    public ArgumentConversionError(Reason reason) {
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}
