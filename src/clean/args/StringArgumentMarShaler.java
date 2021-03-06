package clean.args;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StringArgumentMarShaler implements ArgumentMarshaler {
    private String StringValue ="";
    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        try{
            this.StringValue = currentArgument.next();
        } catch(NoSuchElementException e) {
            throw new ArgsException(ErrorCode.MISSING_STRING);
        }
    }

    public static String getValue(ArgumentMarshaler am) {
        if (am != null && am instanceof StringArgumentMarShaler){
            return ((StringArgumentMarShaler) am).StringValue;
        }else{
            return "";
        }
    }
}
