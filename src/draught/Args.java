package draught;

import clean.args.ArgsException;
import jdk.nashorn.internal.runtime.ParserException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Args {
    private String schema;
    private String[] args;
    private boolean valid = true;
    private Set<Character> unexpectedArguments = new TreeSet<>();
    private Map<Character,Boolean> booleanArgs = new HashMap<>();
    private Map<Character,String> stringArgs = new HashMap<>();
    private Map<Character,Integer> intArgs = new HashMap<>();
    private Set<Character> argFound = new HashSet<>();
    private int currentArgument;
    private char errorArgument = '\0';
    private String errorParameter = "TILT";
    private ErrorCode errorCode = ErrorCode.OK;

    private enum ErrorCode {
        OK,MISSING_STRING,MISS_INTEGER,INVALID_INTEGER,UNEXPECTED_ARGUMENT
    }

    public Args(String schema,String[] args){
        this.schema = schema;
        this.args = args;
        valid = parse();
    }

    private boolean parse() throws ArgsException {
        if (schema.length() == 0 && args.length == 0){
            return true;
        }
        parseSchema();
        try{
            parseArguments();
        }catch (ArgsException e){
        }
        return valid;
    }

    private boolean parseArguments() throws ArgsException{
        for (currentArgument =0; currentArgument<args.length;currentArgument++){
            String arg =args[currentArgument];
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) throws ArgsException {
        if (arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg)throws ArgsException {
        for (int i=0; i<arg.length();i++)
            parseElement(arg.charAt(i));
    }

    private boolean parseSchema() throws ArgsException{
        for (String element : schema.split(",")){
            if (element.length() > 0){
                String trimmedElement = element.trim();
                parseSchemaElement(trimmedElement);
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ArgsException{
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (isBooleanSchemaElement(elementTail))
            parseBooleanSchemaElement(elementId);
        else if (isStringSchemaElement(elementTail))
            parseStringSchemaElement(elementId);
        else if (isIntegerSchemaElement(elementTail))
            parseIntegerSchemaElement(elementId);
        else
            throw new ParserException(String.format("Argument: %c has invalid format: %s.",elementId,elementTail),0);
    }

    private boolean isIntegerSchemaElement(String elementTail) {
        return elementTail.equals("#");
    }

    private boolean isStringSchemaElement(String elementTail) {
        return elementTail.equals("*");
    }

    private boolean isBooleanSchemaElement(String elementTail) {
        return elementTail.length()==0;
    }

    private void validateSchemaElementId(char elementId)throws ArgsException {
        if (!Character.isLetter(elementId)){
            throw new ParserException("Bad charcter:" + elementId + "in Args format:" + schema, 0);
        }
    }

    private void parseBooleanSchemaElement(char elementId) {
        booleanArgs.put(elementId,false);
    }

    private void parseStringSchemaElement(char elementId) {
        stringArgs.put(elementId,"");
    }

    private void parseIntegerSchemaElement(char elementId) {
        intArgs.put(elementId,0);
    }
}
