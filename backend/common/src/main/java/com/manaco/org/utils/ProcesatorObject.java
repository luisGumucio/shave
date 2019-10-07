package com.manaco.org.utils;

import com.manaco.org.model.TransactionOption;
import com.manaco.org.model.Process;

import java.io.InputStream;
import java.util.Map;

public interface ProcesatorObject {

    void execute(Map<String, String> map, TransactionOption option, Process processActive);

    void execute(InputStream file, TransactionOption option, Process processActive);
}
