package com.jd.watcher.common;

import com.jd.watcher.exception.NoRetryException;

public interface Initialization {

    public void initialize() throws NoRetryException;
}