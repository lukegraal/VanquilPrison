package com.vanquil.prison.tools.command.argument;

public interface ArgumentTranslator<T> {
    T translate(String value) throws ArgumentTranslationException;
}
