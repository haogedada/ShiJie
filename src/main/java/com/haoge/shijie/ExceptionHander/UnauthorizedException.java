package com.haoge.shijie.ExceptionHander;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);

    }
    public UnauthorizedException() {
        super();
    }
}

