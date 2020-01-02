package com.objectMentor.utils.args;

import java.util.Iterator;

public class BooleanArgumentMarShaler implements ArgumentMarshaler{
    private boolean booleanValue =false;

    public static boolean getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof  BooleanArgumentMarShaler)
            return ((BooleanArgumentMarShaler)am).booleanValue;
        else return false;
    }

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        booleanValue = true;
    }
}
