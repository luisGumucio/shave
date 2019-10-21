package com.manaco.org.entries.file;

import com.manaco.org.dto.ProductDto;
import com.manaco.org.entries.excel.ExcelGenerator;
import com.manaco.org.model.*;
import org.apache.poi.util.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FileRead {
    private List<String> fileAdd = new ArrayList<>();
    private List<Transaction> move = new ArrayList<>();
    private List<ProductDto> move1 = new ArrayList<>();

    public static void main(String args[]) throws IOException {
        ExcelGenerator excelGenerator = new ExcelGenerator();

        Transaction transaction = new Transaction();
        List<String> fileAdd = new ArrayList<>();

        List<Transaction> move = new ArrayList<>();
        long start = new Date().getTime();
        System.out.println("BufferedReader Time Consumed => " + new Date().toString());
        String fileName = "C:\\Users\\lucho\\Documents\\manaco\\datosOficial\\productoTerminado\\movimiento.txt"; //this path is on my local
        int cont = 0;
        try (BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName))) {
            String fileLineContent;
            while ((fileLineContent = fileBufferReader.readLine()) != null) {
                if (cont == 0) {
                    cont++;
                } else {
                    System.out.println(fileLineContent);
                    cont++;
                    if(move.size() == 100) {
                        ByteArrayInputStream in = excelGenerator.downloadTransation1(move, "PRODUCTO");
                        IOUtils.toByteArray(in);
                        return;
                    }
//                    fileAdd.add(fileLineContent);
                    move.add(readContent(fileLineContent));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


//        for (String value: fileAdd) {
//           if(move.size() == 100) {
//               excelGenerator.downloadTransation1(move, "PRODUCTO");
//               return;
//           }
//            move.add(readContent(value));
//        }
        long end = new Date().getTime();

        long time = (long) (end - start);
//        executeTransaction(move);
        System.out.println(fileAdd.size());

        System.out.println("BufferedReader Time Consumed => " + time);
        System.out.println("total Time Consumed => " + move.size());
        System.out.println(cont);
    }

    public void ReadFile(String value) {
        ExcelGenerator excelGenerator = new ExcelGenerator();
//        Transaction transaction = new Transaction();
        ProductDto dto = new ProductDto();


        long start = new Date().getTime();
        System.out.println("BufferedReader Time Consumed => " + new Date().toString());
        String fileName = "C:\\Users\\lucho\\Documents\\manaco\\datosOficial\\productoTerminado\\procesar\\pag" +value+".txt"; //this path is on my local
        int cont = 0;
        try (BufferedReader fileBufferReader = new BufferedReader(new FileReader(fileName))) {
            String fileLineContent;
            while ((fileLineContent = fileBufferReader.readLine()) != null) {
                if (cont == 0) {
                    cont++;
                } else {
//                    System.out.println(fileLineContent);
                    cont++;
//                    fileAdd.add(fileLineContent);
                    move1.add(readContent1(fileLineContent));
                }
            }
//            ByteArrayInputStream in = excelGenerator.downloadTransation1(move, "PRODUCTO");
//            return IOUtils.toByteArray(in);

        } catch (IOException e) {
            e.printStackTrace();
        }


//        for (String value: fileAdd) {
//           if(move.size() == 100) {
//               excelGenerator.downloadTransation1(move, "PRODUCTO");
//               return;
//           }
//            move.add(readContent(value));
//        }
        long end = new Date().getTime();

        long time = (long) (end - start);
//        executeTransaction(move);
        System.out.println(fileAdd.size());

        System.out.println("BufferedReader Time Consumed => " + time);
        System.out.println("total Time Consumed => " + move.size());
        System.out.println(cont);
    }

    private static Transaction readContent(String content) {
        try {
            String value = "";
            int cont = 0;
            Map<String, String> info = new HashMap<>();
            Transaction transaction = new Transaction();
            Item item = new Item();
            for (int x = 0; x < content.length(); x++) {
                if (String.valueOf(content.charAt(x)).equals("|")) {
                    switch (cont) {
                        case 0:
                            if (value.equals("E")) {
                                transaction.setType(TransactionType.ENTRY);
                            } else if (value.equals("S")) {
                                transaction.setType(TransactionType.EGRESS);
                            } else if (value.equals("EC")) {
                                transaction.setType(TransactionType.ENTRY_BUY);
                            }
                            break;
                        case 1:
                            info.put("ALMACEN", value);
                            break;
                        case 2:
                            info.put("NRO_DOC", value);
                            break;
                        case 3:
                            item.setId(value);
                            break;
                        case 4:
                            item.setQuantity(new BigDecimal(value));
                            break;
                        case 5:
                            item.setPrice(new BigDecimal(value));
                            break;
                        case 6:
                            LocalDate currentDate = convertToDate(value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                            transaction.setTransactionDate(currentDate);
                            break;
                        case 7:
                            info.put("ORIGEN", value);
                            break;
                        case 8:
                            info.put("SEMANA", value);
                            break;
                        case 10:
                            info.put("TIPO_MOV", value);
                            break;
                    }
                    cont++;
                    value = "";
                } else {
                    value = value.concat(String.valueOf(content.charAt(x)));
                }
            }
            transaction.setInformation(info);
            item.setIdentifier(TransactionOption.PRODUCTO);
            transaction.setItem(item);
            if (transaction.getType() == TransactionType.ENTRY || transaction.getType() == TransactionType.ENTRY_BUY) {
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(item.getPrice());
            } else if (transaction.getType() == TransactionType.EGRESS) {
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(BigDecimal.ZERO);
            }
            transaction.setIdentifier(item.getIdentifier());
            return transaction;
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }

    }

    private static ProductDto readContent1(String content) {
        try {
            String value = "";
            int cont = 0;
            ProductDto dto = new ProductDto();
            for (int x = 0; x <= content.length(); x++) {
                if(x == content.length()){
                    dto.setTIPO_MOV(value);
                     break;
                }
                if (String.valueOf(content.charAt(x)).equals("|")) {
                    switch (cont) {
                        case 0:
                            dto.setTIPO(value);
                            break;
                        case 1:
                            dto.setALMACEN(value);
                            break;
                        case 2:
                            dto.setNRO_DOC(value);
                            break;
                        case 3:
                            dto.setARTICULO(value);
                            break;
                        case 4:
                            dto.setPARES(value);
                            break;
                        case 5:
                            dto.setPCOSTO(value);
                            break;
                        case 6:
                            LocalDate currentDate = convertToDate(value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//                            dto.setFECHA_DOC(currentDate);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            dto.setFECHA_DOC(currentDate.format(formatter));
                            break;
                        case 7:
                            dto.setORIGEN(value);
                            break;
                        case 8:
                            dto.setSEMANA(value);
                            break;
                        case 9:
                            dto.setTABLA_ORIGEN(value);
                            break;
                        case 10:
                            dto.setTIPO_MOV(value);
                            break;
                    }
                    cont++;
                    value = "";
                } else {
                    value = value.concat(String.valueOf(content.charAt(x)));
                }
        }
            return dto;
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static Date convertToDate(String receivedDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private static void executeTransaction(List<Transaction> raws) {
        if (raws.isEmpty()) {
            return;
        } else {
            removeAll(raws, raws.get(0).getItem().getId());
        }
        executeTransaction(raws);
    }

    private static void removeAll(List<Transaction> raws, String id) {
        try {
            List<Transaction> actual = raws.stream()
                    .filter(b -> Objects.equals(b.getItem().getId(), id))
                    .collect(Collectors.toList());
            Collections.sort(actual);
            System.out.println(actual.get(0).getItem().getId());
            raws.removeIf(b -> Objects.equals(b.getItem().getId(), id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<ProductDto> getTransaction() {
        return move1;
    }
}


