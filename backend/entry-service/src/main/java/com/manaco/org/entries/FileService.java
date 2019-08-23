package com.manaco.org.entries;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manaco.org.entries.excel.ExcelReader;
import com.manaco.org.entries.excel.ExcelSheetCallback;
import com.manaco.org.entries.excel.ExcelWorkSheetRowCallbackHandler;
import com.manaco.org.entries.processator.ProcesatorInitial;
import com.manaco.org.entries.processator.ProcesatorMoving;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.repositories.FilesRepository;
import com.manaco.org.utils.ProcesatorObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Service
public class FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileService.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ProcessService processService;

    @Autowired
    ProcesatorInitial processatorInitial;

    @Autowired
    FilesRepository filesRepository;

    @Autowired
    private ProcesatorMoving procesatorMoving;


    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> readFile(InputStream file, Process processActive, String option) {
        TransactionOption transactionOption = TransactionOption.valueOf(option);
        switch (transactionOption) {
            case SALDO_INITIAL_PRIMA:
                initialExecute(file, processatorInitial, TransactionOption.PRIMA, processActive);
                break;
            case SALDO_INITIAL_REPUESTOS:
                initialExecute(file, processatorInitial, TransactionOption.REPUESTOS, processActive);
                break;
            case SALDO_INITIAL_PRODUCTO:
                initialExecute(file, processatorInitial, TransactionOption.PRODUCTO, processActive);
                break;
            case PRIMA:
                initialExecute(file, procesatorMoving, TransactionOption.PRIMA, processActive);
                break;
            case REPUESTOS:
                initialExecute(file, procesatorMoving, TransactionOption.REPUESTOS, processActive);
                break;

        }
        return null;
    }

    public void initialExecute(InputStream file, ProcesatorObject procesatorObject, TransactionOption option, Process processActive) {
        OPCPackage pkg = null;
        try {
            ExcelWorkSheetRowCallbackHandler sheetRowCallbackHandler =
                    new ExcelWorkSheetRowCallbackHandler((rowNum, map) -> {
                        LOGGER.info("rowNum=" + rowNum + ", map=" + map);
                        procesatorObject.execute(map, option, processActive);
                    });
            pkg = OPCPackage.open(file);
            ExcelSheetCallback sheetCallback = new ExcelSheetCallback() {
                private int sheetNumber = 0;

                @Override
                public void startSheet(int sheetNum, String sheetName) {
                    this.sheetNumber = sheetNum;
                    System.out.println("Started processing sheet number=" + sheetNumber
                            + " and Sheet Name is '" + sheetName + "'");
                }

                @Override
                public void endSheet() {
                    System.out.println("Processing completed for sheet number=" + sheetNumber);
                }
            };
            ExcelReader excelReader = new ExcelReader(pkg, sheetRowCallbackHandler, sheetCallback);
            excelReader.process();

        } catch (RuntimeException are) {
            LOGGER.error(are.getMessage(), are.getCause());
        } catch (InvalidFormatException ife) {
            LOGGER.error(ife.getMessage(), ife.getCause());
        } catch (IOException ioe) {
            LOGGER.error(ioe.getMessage(), ioe.getCause());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(file);
            try {
                if (null != pkg) {
                    pkg.close();
                }
            } catch (IOException e) {
                // just ignore IO exception
            }
        }
    }

    public Process saveProcess(String option, String number) {
        Process process = processService.findByNumberProcessAndIsActive(Integer.valueOf(number), true);
        TransactionOption transactionOption = TransactionOption.valueOf(option);
        if (process != null) {
            process.addTransaction(transactionOption);
        } else {
            process = new Process();
            process.setNumberProcess(Integer.valueOf(number));
            process.addTransaction(transactionOption);
        }

        return processService.createProcess(process);
    }

    public FileUpload saveFile(String option, String name) {
        FileUpload fileUpload = new FileUpload();
        TransactionOption transactionOption = TransactionOption.valueOf(option);
        fileUpload.setName(name);
        fileUpload.setOption(transactionOption);
        return filesRepository.save(fileUpload);
    }


    public Page<FileUpload> getFiles(PageRequest of) {
        return filesRepository.findAll(of);
    }
}
