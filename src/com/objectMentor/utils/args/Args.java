package com.objectMentor.utils.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Args {
    private Map<Character, ArgumentMarshaler> marshaler;

    private Set<Character> argsFound;

    private ListIterator<String> currentArgument;

    public Args(String schema, String[] args) throws ArgsException{
        marshaler = new HashMap<Character, ArgumentMarshaler>();
        argsFound = new HashSet<Character>();

        parseSchema(schema);
        parseArgumentStrings(Arrays.asList(args));
    }

    private void parseArgumentStrings(List<String> asList) {
    }

    private void parseSchema(String schema) throws ArgsException{
            for (String element : schema.split(",")){
                if (element.length()>0)
                    parseSchemaElement(element.trim());
            }
    }

    private void parseSchemaElement(String element) {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (elementTail.length()== 0){
            marshaler.put(elementId, new BooleanArgumentMarShaler());
        }else if (elementTail.equals("*")){
            marshaler.put(elementId, new StringArgumentMarShaler());
        }else if (elementTail.equals("#")){
            marshaler.put(elementId, new IntegerArgumentMarShaler());
        }else if (elementTail.equals("##")){
            marshaler.put(elementId, new DoubleArgumentMarShaler());
        }else if (elementTail.equals("[*]")){
            marshaler.put(elementId, new StringArrarArgumentMarShaler());
        }else{
            throw new ArgsException();
        }
    }


    private void validateSchemaElementId(char elementId) {
    }
}
