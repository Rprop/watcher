package com.whh.watcher.common;

import com.whh.watcher.exception.NoRetryException;

public interface Initialization {

    public void initialize() throws NoRetryException;
}