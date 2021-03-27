package com.techlogix.pacabs_driver.enumirations;

public enum ErrorDescription {

    URL_ACCESS(new ErrorDesc(-10000001, "Unable to access remote serve.")),
    SERVER_RESPONDING(new ErrorDesc(-10000002, "Server is not responding.")),
    NO_INTERNET(new ErrorDesc(-10000003, "Internet connectivity problem.")),
    GENERAL_ERROR(new ErrorDesc(-10000004, "Please try again later.")),
    INVALID_RESPONCE(new ErrorDesc(-10000005, "Invalid response. Please try again!"));

    public ErrorDesc ed;
    ErrorDescription(ErrorDesc ed) {
        this.ed = ed;
    }

    public static class ErrorDesc {

        public int code;
        public String desc;

        public ErrorDesc(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}


