package com.machangzhe.exception.runtime;

/**
 * Created by MaChangzhe on 2016/12/20.
 */
public class WrongRegexException extends RuntimeException {
    public WrongRegexException(String msg) {
        super(msg);
    }
}
