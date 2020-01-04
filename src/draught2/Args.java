package draught2;


import java.text.ParseException;
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
    private Map<Character, Boolean> booleanArgs = new HashMap<>();
    private Map<Character, String> stringArgs = new HashMap<>();
    private Set<Character> argFound = new HashSet<>();
    private int numOfArgument;
    private int currentArgument;
    private char errorArguement = '\0';
    private ErrorCode errorCode = ErrorCode.OK;

    enum ErrorCode {
        OK, MISSING_STRING
    }

    public Args(String schema, String[] args) throws ParseException {
        this.schema = schema;
        this.args = args;
        valid = parse();
    }

    private boolean parse() throws ParseException {
        if (schema.length() == 0 && args.length == 0) {
            return true;
        }
        parseSchema();
        parseArguments();
        return valid;
    }

    private boolean parseArguments() {
        for (currentArgument = 0; currentArgument < args.length; currentArgument++) {
            String arg = args[currentArgument];
            parseArgument(arg);
        }
        return true;
    }

    private void parseArgument(String arg) {
        if (arg.startsWith("-")) {
            parseElements(arg);
        }
    }

    private void parseElements(String arg) {
        for (int i = 1; i < arg.length(); i++) {
            parseElement(arg.charAt(i));
        }
    }

    private void parseElement(char argChar) {
        if (setArgument(argChar)) {
            argFound.add(argChar);
        } else {
            unexpectedArguments.add(argChar);
            valid = false;
        }
    }

    private boolean setArgument(char argChar) {
        boolean set = true;
        if (isBoolean(argChar))
            setBooleanArg(argChar, true);
        else if (isString(argChar))
            setStringArg(argChar, "");
        else {
            set = false;
        }
        return set;
    }

    private void setStringArg(char argChar, String s) {
        currentArgument++;
        try {
            stringArgs.put(argChar, args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorArguement = argChar;
            errorCode = ErrorCode.MISSING_STRING;
        }
    }

    private boolean isString(char argChar) {
        return stringArgs.containsKey(argChar);
    }

    private void setBooleanArg(char argChar, boolean value) {
        booleanArgs.put(argChar, value);
    }

    private boolean isBoolean(char argChar) {
        return booleanArgs.containsKey(argChar);
    }

    public int cardinality() {
        return argFound.size();
    }

    public String usage() {
        if (schema.length() > 0) {
            return "-[" + schema + "]";
        }
        return "";
    }

    public String errorMessage() throws Exception {
        if (unexpectedArguments.size() > 0) {
            return unexpectedArgumentMessage();
        } else
            switch (errorCode) {
                case MISSING_STRING:
                    return String.format("Could not find string paramter for -%c.", errorArguement);
                case OK:
                    throw new Exception("TILT: should not get here.");
            }
        return "";
    }

    private String unexpectedArgumentMessage() {
        StringBuffer message = new StringBuffer("Argument(s) -");
        for (char c : unexpectedArguments) {
            message.append(c);
        }
        message.append(" unexpected.");
        return message.toString();
    }

    public boolean getBoolean(char arg) {
        return falseIfNull(booleanArgs.get(arg));
    }

    private boolean falseIfNull(Boolean b) {
        return b == null ? false : b;
    }

    private String getString(char arg){
        return blankIfNull(stringArgs.get(arg));
    }

    private String blankIfNull(String s) {
        return s== null?"":s;
    }

    private boolean parseSchema() throws ParseException {
        for (String element : schema.split(",")) {
            if (element.length() > 0) {
                String trimedElement = element.trim();
                parseSchemaElement(trimedElement);
            }
        }
        return true;
    }

    private void parseSchemaElement(String element) throws ParseException {
        char elementId = element.charAt(0);
        String elementTail = element.substring(1);
        validateSchemaElementId(elementId);
        if (isBooleanSchemaElement(elementTail))
            parseBooleanSchemaElement(elementId);
        else if (isStringSchemaElementId(elementTail))
            parseStringSchemaElement(elementId);
    }

    private boolean isStringSchemaElementId(String elementTail) {
        return elementTail.equals("*");
    }

    private void parseStringSchemaElement(char elementId) {
        stringArgs.put(elementId, "");
    }

    private boolean isBooleanSchemaElement(String elementTail) {
        return elementTail.length() == 0;
    }

    private void validateSchemaElementId(char elementId) throws ParseException {
        if (!Character.isLetter(elementId)) {
            throw new ParseException("Bad charcter:" + elementId + "in Args format:" + schema, 0);
        }
    }

    private void parseBooleanSchemaElement(char element) {
        booleanArgs.put(element, false);
    }

    public boolean has(char arg){
        return argFound.contains(arg);
    }

    public boolean isValid(){
        return valid;
    }
}
