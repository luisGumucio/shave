package com.manaco.org.utils;

import com.manaco.org.model.Process;
import com.manaco.org.model.TransactionOption;

import java.util.Map;

public interface ProcesatorObject {

    void execute(Map<String, String> map, TransactionOption option, Process processActive);
}
