package com.objectMentor.utils.args;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.objectMentor.utils.args.ErrorCode.MISSING_STRING;

public class StringArgumentMarShaler implements ArgumentMarshaler {
    private String StringValue ="";
    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        try{
            this.StringValue = currentArgument.next();
        } catch(NoSuchElementException e) {
            throw new ArgsException(MISSING_STRING);
        }
    }

    public static String getValue(ArgumentMarshaler argumentMarshaler) {
        if (argumentMarshaler != null && argumentMarshaler instanceof StringArgumentMarShaler){
            return ((StringArgumentMarShaler) argumentMarshaler).StringValue;
        }else{
            return "";
        }
    }
}
