package clean.args;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

public class Args {
    private Map<Character, ArgumentMarshaler> marshalers;

    private Set<Character> argsFound;

    private ListIterator<String> currentArgument;

    public Args(String schema, String[] args) throws ArgsException {
        marshalers = new HashMap<Character, ArgumentMarshaler>();
        argsFound = new HashSet<Character>();

        parseSchema(schema);
        parseArgumentStrings(Arrays.asList(args));
    }

    private void parseSchema(String schema) throws ArgsException {
        for (String element : schema.split(",")) {
            if (element.length() > 0)
                parseSchemaElement(element.trim());
        }
    }

    private void parseSchemaElement(String element) throws ArgsException {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (elementTail.length() == 0) {
            marshalers.put(elementId, new BooleanArgumentMarShaler());
        } else if (elementTail.equals("*")) {
            marshalers.put(elementId, new StringArgumentMarShaler());
        } else if (elementTail.equals("#")) {
            marshalers.put(elementId, new IntegerArgumentMarShaler());
        }
        else {
            throw new ArgsException(ErrorCode.INVALID_ARGUMENT_FORMAT, elementId, elementTail);
        }
    }

    private void validateSchemaElementId(char elementId) throws ArgsException {
        if (!Character.isLetter(elementId))
            throw new ArgsException(ErrorCode.INVALID_ARGUMENT_NAME, elementId, null);
    }

    private void parseArgumentStrings(List<String> argsList) throws ArgsException {
        for (currentArgument = argsList.listIterator(); currentArgument.hasNext(); ) {
            String argString = currentArgument.next();
            if (argString.startsWith("-")) {
                parseArgumentCharacters(argString.substring(1));
            } else {
                currentArgument.previous();
                break;
            }
        }
    }

    private void parseArgumentCharacters(String argChars) throws ArgsException {
        for (int i = 0; i < argChars.length(); i++) {
            parseArgumentCharacter(argChars.charAt(i));
        }
    }

    private void parseArgumentCharacter(char argchar) throws ArgsException {
        ArgumentMarshaler m = marshalers.get(argchar);
        if (m == null) {
            throw new ArgsException(ErrorCode.UNEXPECTED_ARGUMENT, argchar, null);
        }else {
            argsFound.add(argchar);
            try{
                m.set(currentArgument);
            } catch (ArgsException e){
                e.setErrorArgumentId(argchar);
                throw e;
            }
        }
    }

    public boolean has(char arg){
        return argsFound.contains(arg);
    }

    public int nextArgument(){
        return currentArgument.nextIndex();
    }

    public boolean getBoolean(char arg){
        return BooleanArgumentMarShaler.getValue(marshalers.get(arg));
    }

    public String getString(char arg){
        return StringArgumentMarShaler.getValue(marshalers.get(arg));
    }

    public int getInt(char arg){
        return IntegerArgumentMarShaler.getValue(marshalers.get(arg));
    }
}
