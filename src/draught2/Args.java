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
    private Map<Character, ArgumentMarshaler> marshaler = new HashMap<>();
    private Set<Character> argFound = new HashSet<>();
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
        ArgumentMarshaler m = marshaler.get(argChar);
        if (m instanceof BooleanArgumentMarshaler)
            setBooleanArg(m);
        else if (m instanceof StringArgumentMarshaler)
            setStringArg(m);
        else if (m instanceof IntegerArgumentMarshaler)
            setIntArg(m);
        else
            return false;

        return true;
    }

    private void setStringArg(ArgumentMarshaler m) {
        currentArgument++;
        try {
            m.set(args[currentArgument]);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
            errorCode = ErrorCode.MISSING_STRING;
        }
    }

    private void setBooleanArg(ArgumentMarshaler m) {
        m.set("true");
    }

    private void setIntArg(ArgumentMarshaler m) {
        currentArgument++;
        String paramter = null;
        try {
            paramter = args[currentArgument];
            m.set(paramter);
        } catch (ArrayIndexOutOfBoundsException e) {
            valid = false;
        } catch (NumberFormatException e) {
            valid = false;
        }
    }

    public int getInt(char arg) {
        Args.ArgumentMarshaler am = marshaler.get(arg);
        return am == null ? 0 : (Integer) am.get();
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
        Args.ArgumentMarshaler am = marshaler.get(arg);
        return am != null && (Boolean) am.get();
    }

    public String getString(char arg) {
        Args.ArgumentMarshaler am = marshaler.get(arg);
        return am == null ? "" : (String) am.get();
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
            marshaler.put(elementId,new BooleanArgumentMarshaler());
        else if (isStringSchemaElementId(elementTail))
            marshaler.put(elementId,new StringArgumentMarshaler());
        else if (isIntSchemaElementId(elementTail))
            marshaler.put(elementId,new IntegerArgumentMarshaler());
    }

    private boolean isIntSchemaElementId(String elementTail){
        return elementTail.equals("#");
    }

    private boolean isStringSchemaElementId(String elementTail) {
        return elementTail.equals("*");
    }

    private boolean isBooleanSchemaElement(String elementTail) {
        return elementTail.length() == 0;
    }

    private void validateSchemaElementId(char elementId) throws ParseException {
        if (!Character.isLetter(elementId)) {
            throw new ParseException("Bad charcter:" + elementId + "in Args format:" + schema, 0);
        }
    }

    public boolean has(char arg) {
        return argFound.contains(arg);
    }

    public boolean isValid() {
        return valid;
    }


    abstract class ArgumentMarshaler {
        public abstract void set(String s);

        public abstract Object get();
    }

    class BooleanArgumentMarshaler extends ArgumentMarshaler {
        private boolean booleanValue = false;

        @Override
        public void set(String s) {
            booleanValue = true;
        }

        @Override
        public Object get() {
            return booleanValue;
        }
    }

    class StringArgumentMarshaler extends ArgumentMarshaler {
        private String stringValue = "";

        @Override
        public void set(String s) {
            stringValue = s;
        }

        @Override
        public Object get() {
            return stringValue;
        }
    }

    class IntegerArgumentMarshaler extends ArgumentMarshaler {
        private int intValue;

        @Override
        public void set(String s) {
            intValue = Integer.parseInt(s);
        }

        @Override
        public Object get() {
            return intValue;
        }
    }


}






