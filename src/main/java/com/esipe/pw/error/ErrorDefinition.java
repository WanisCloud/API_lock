package com.esipe.pw.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

//description : description de l'erreur
public class ErrorDefinition {

    public enum ErrorTypeEnum{
        TECHNICAL("TECHNICAL");

        FUNCTIONAL("FUNCTIONAL");

        private String value;

        ErrorTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static ErrorTypeEnum fromValue(String text) {
            for (ErrorTypeEnum a : ErrorTypeEnum.values()) {
                if (String.valueOf(a.value).equals(text)) {
                    return a;
                }
            }
            return  null;
        }
    }
    @JsonProperty("errorType")
    private ErrorTypeEnum errorType = null;

    public ErrorDefinition errorType(ErrorTypeEnum errorType) {
        this.errorType = errorType;
        return this;
    }

    public ErrorTypeEnum getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorTypeEnum errorType) {
        this.errorType = errorType;
    }

    public ErrorDefinition errors(List<ErrorDefinitionErrors> errors) {
        this.errors = errors;
        return this;
    }

    public ErrorDefinition addErrorsItem(ErrorDefinitionErrors errorsItem) {
        if (this.errors == null) {
            this.errors = new ArrayList<ErrorDefinitionErrors>();
        }
        this.errors.add(errorsItem);
        return this;
    }

    public List<ErrorDefinitionErrors> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDefinitionErrors> errors) {
        this.errors = errors;
    }

    //code manquant : texte affiché au moment de l'erreur
}
