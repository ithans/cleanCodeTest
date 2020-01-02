package com.objectMentor.utils.args;

import static com.objectMentor.utils.args.ErrorCode.Ok;

public class ArgsException extends Exception {
    private ErrorCode errorCode = Ok;
    private char errorArgumentId ='\0';
    private String errorParameter = null;

    public ArgsException(ErrorCode errorCode, char errorElementId, String errorParameter) {
        this.errorCode = errorCode;
        this.errorArgumentId = errorElementId;
        this.errorParameter = errorParameter;
    }
    public ArgsException(){

    }
    public ArgsException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

    public ArgsException(ErrorCode errorCode, String errorParameter) {
        this.errorCode = errorCode;
        this.errorParameter = errorParameter;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public char getErrorArgumentId() {
        return errorArgumentId;
    }

    public String getErrorParameter() {
        return errorParameter;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorArgumentId(char errorArgumentId) {
        this.errorArgumentId = errorArgumentId;
    }

    public void setErrorParameter(String errorParameter) {
        this.errorParameter = errorParameter;
    }

    public String errorMessage(){
        switch (errorCode) {
            case Ok:
                return "TITLE: Should not get here.";
            case UNEXPECTED_ARGUMENT:
                return String.format("Argument -%c unexpected.",errorArgumentId);
            case MISSING_STRING:
                return String.format("Could not find string parameter for -%c.",errorArgumentId);
            case INVALID_INTEGER:
                return String.format("Argument -%c expects an integer but was '%s'.",errorArgumentId,errorParameter);
            case INVALID_ARGUMENT_FORMAT:
                return String.format("'%s' is not a valid argument format.", errorParameter);
        }
        return "";
    }
}
