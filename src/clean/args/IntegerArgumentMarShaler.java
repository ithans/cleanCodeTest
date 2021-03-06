package clean.args;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static clean.args.ErrorCode.INVALID_INTEGER;
import static clean.args.ErrorCode.MISSING_INTEGER;

public class IntegerArgumentMarShaler implements ArgumentMarshaler {
    private int intValue = 0;

    public static int getValue(ArgumentMarshaler argumentMarshaler) {
        if (argumentMarshaler != null && argumentMarshaler instanceof IntegerArgumentMarShaler)
        return ((IntegerArgumentMarShaler)argumentMarshaler).intValue;
        else {
            return 0;
        }
    }

    @Override
    public void set(Iterator<String> currentArgument) throws ArgsException {
        String parameter = null;
        try{
            parameter =currentArgument.next();
            intValue = Integer.parseInt(parameter);
        }catch (NoSuchElementException e){
            throw new ArgsException(MISSING_INTEGER);
        } catch (NumberFormatException e){
            throw new ArgsException(INVALID_INTEGER,parameter);
        }
    }
}
