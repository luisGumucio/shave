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
import com.manaco.org.repositories.ItemRepository;
import com.manaco.org.repositories.TransactionRepository;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.manaco.org.model.TransactionOption.*;

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

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private int total;

//    @Async("threadPoolTaskExecutor")
    public synchronized CompletableFuture<Void> readFile(InputStream file, Process processActive, String option) {
        total = 0;
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
            case PRODUCTO:
                initialExecute(file, procesatorMoving, TransactionOption.PRODUCTO, processActive);
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
                        total++;
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
        TransactionOption transactionOption = TransactionOption.valueOf(option);
        TransactionOption current = null;
        switch (transactionOption) {
            case SALDO_INITIAL_REPUESTOS:
                current = REPUESTOS;
                break;
            case SALDO_INITIAL_PRIMA:
                current = PRIMA;
                break;
            case SALDO_INITIAL_PRODUCTO:
                current = PRODUCTO;
                break;
        }
        Process process = processService.findByNumberProcessAndIsActive(Integer.valueOf(number),
                true);

        if (process == null) {
            process = new Process();
            process.setNumberProcess(Integer.valueOf(number));
            process.getTransactionOptions().add(current);
        }

        return processService.createProcess(process);
    }

    public FileUpload saveFile(String option, String name, int total) {
        FileUpload fileUpload = new FileUpload();
        TransactionOption transactionOption = TransactionOption.valueOf(option);
        fileUpload.setName(name);
        fileUpload.setOption(transactionOption);
        fileUpload.setTotalRows(total);
        return filesRepository.save(fileUpload);
    }


    public Page<FileUpload> getFiles(PageRequest of) {
        return filesRepository.findAll(of);
    }

    public int getTotal() {
        return total;
    }


    public void ajuste(InputStream file, Process processActive, String option) {
        OPCPackage pkg = null;
        try {
            ExcelWorkSheetRowCallbackHandler sheetRowCallbackHandler =
                    new ExcelWorkSheetRowCallbackHandler((rowNum, map) -> {
                        LOGGER.info("rowNum=" + rowNum + ", map=" + map);
                        total++;
                        TransactionOption transactionOption = TransactionOption.valueOf(option);
                        ajusteTransaction(map, transactionOption, processActive);
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

    private void ajusteTransaction(Map<String, String> map, TransactionOption option, Process processActive) {
        Transaction transaction = new Transaction();
        Item item = itemRepository.findById(map.get("ITEM")).get();
        BigDecimal quantity = new BigDecimal(map.get("CANTIDAD").replace(",", ""))
                .setScale(6, BigDecimal.ROUND_DOWN);
        BigDecimal diferencial = new BigDecimal(map.get("DIFERENTE").replace(",", ""))
                .setScale(6, BigDecimal.ROUND_DOWN);
        BigDecimal x = null;
        if(quantity.compareTo(item.getQuantity()) >= 0) {
            if(item.getQuantity().compareTo(BigDecimal.ZERO) < 0) {
                x = diferencial.add(item.getQuantity().negate());
            }
            x = diferencial.add(item.getQuantity());
            transaction.setEntry(diferencial);
            transaction.setBalance(x);
            item.setQuantity(x);
        } else {
            x = item.getQuantity().subtract(diferencial);
            transaction.setEgress(diferencial);
            transaction.setBalance(x);
            item.setQuantity(x);

        }

        item.setTotal(item.getPrice().multiply(item.getQuantity()));

        transaction.setType(TransactionType.AJUSTE);
        transaction.setPriceActual(item.getPrice());
        transaction.setPriceNeto(BigDecimal.ZERO);
        transaction.setTransactionDate(item.getLastUpdate());
        transaction.setItem(item);
        transaction.setTotalNormal(item.getTotal());
        transaction.setTotalUpdate(item.getTotal());
        transaction.setIncrement(BigDecimal.ZERO);
        transaction.setProcessId(processActive.getId());
//        transaction.setUfv(ufvRepository.findByCreationDate(item.getLastUpdate()));
//        transaction.setDetail(buildDetail(map, option));
        transaction.setIdentifier(option);
        itemRepository.save(item);
        transactionRepository.save(transaction);
    }

}
