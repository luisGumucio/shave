package com.manaco.org.entries.processator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manaco.org.entries.Publisher;
import com.manaco.org.model.*;
import com.manaco.org.model.Process;
import com.manaco.org.utils.ProcesatorObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Component
public class ProcesatorMoving implements ProcesatorObject {

    @Autowired
    private Publisher publisher;

    @Override
    public void execute(Map<String, String> map, TransactionOption option, Process processActive) {
        Transaction transaction;

        if(processActive.getNumberProcess() == 2) {
            transaction = buildTransactionProcessTwo(map, processActive, option);
        } else {
            transaction = buildTransactionProcessOne(map, processActive, option);
        }

        publisher.sentToTransaction(transaction, option);
    }

    @Override
    public void execute(InputStream file, TransactionOption option, Process processActive) {
    }

    private Transaction buildTransactionProcessTwo(Map<String, String> map, Process processActive, TransactionOption option) {
        String aa = "[\n" +
                "        {\n" +
                "            \"item\": \"10003\",\n" +
                "            \"costo\": \"3.06471\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10004\",\n" +
                "            \"costo\": \"1.211219\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10600\",\n" +
                "            \"costo\": \"8.479527\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10604\",\n" +
                "            \"costo\": \"8.488151\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10610\",\n" +
                "            \"costo\": \"15.302742\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10613\",\n" +
                "            \"costo\": \"15.327148\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10614\",\n" +
                "            \"costo\": \"15.32368\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10615\",\n" +
                "            \"costo\": \"15.327238\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10619\",\n" +
                "            \"costo\": \"15.337333\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10621\",\n" +
                "            \"costo\": \"15.324699\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10632\",\n" +
                "            \"costo\": \"15.306214\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10636\",\n" +
                "            \"costo\": \"14.73235\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10637\",\n" +
                "            \"costo\": \"14.94429\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10651\",\n" +
                "            \"costo\": \"13.76577\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10658\",\n" +
                "            \"costo\": \"13.909174\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10662\",\n" +
                "            \"costo\": \"13.83004\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10676\",\n" +
                "            \"costo\": \"13.565336\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10680\",\n" +
                "            \"costo\": \"13.99758\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10691\",\n" +
                "            \"costo\": \"15.052294\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10694\",\n" +
                "            \"costo\": \"15.136425\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10703\",\n" +
                "            \"costo\": \"14.866853\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10705\",\n" +
                "            \"costo\": \"14.881169\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10740\",\n" +
                "            \"costo\": \"15.590761\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10779\",\n" +
                "            \"costo\": \"15.974182\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10849\",\n" +
                "            \"costo\": \"17.541028\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10850\",\n" +
                "            \"costo\": \"17.538424\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10855\",\n" +
                "            \"costo\": \"17.529698\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10858\",\n" +
                "            \"costo\": \"17.528597\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10864\",\n" +
                "            \"costo\": \"13.543085\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10868\",\n" +
                "            \"costo\": \"13.543087\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10871\",\n" +
                "            \"costo\": \"14.204652\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10875\",\n" +
                "            \"costo\": \"12.144638\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10878\",\n" +
                "            \"costo\": \"12.499311\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10888\",\n" +
                "            \"costo\": \"14.986787\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10897\",\n" +
                "            \"costo\": \"14.818675\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10906\",\n" +
                "            \"costo\": \"15.209541\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10907\",\n" +
                "            \"costo\": \"14.674741\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10933\",\n" +
                "            \"costo\": \"15.052043\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10961\",\n" +
                "            \"costo\": \"17.172147\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10965\",\n" +
                "            \"costo\": \"6.288061\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10966\",\n" +
                "            \"costo\": \"6.309908\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10969\",\n" +
                "            \"costo\": \"4.379809\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10974\",\n" +
                "            \"costo\": \"3.489554\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10975\",\n" +
                "            \"costo\": \"7.87113\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10978\",\n" +
                "            \"costo\": \"7.871806\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10983\",\n" +
                "            \"costo\": \"7.867921\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10992\",\n" +
                "            \"costo\": \"7.871854\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10993\",\n" +
                "            \"costo\": \"7.873994\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"10998\",\n" +
                "            \"costo\": \"7.871257\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11013\",\n" +
                "            \"costo\": \"4.242273\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11016\",\n" +
                "            \"costo\": \"4.247509\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11019\",\n" +
                "            \"costo\": \"9.98048\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11020\",\n" +
                "            \"costo\": \"9.935637\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11021\",\n" +
                "            \"costo\": \"10.175893\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11024\",\n" +
                "            \"costo\": \"10.162539\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11026\",\n" +
                "            \"costo\": \"9.927319\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11027\",\n" +
                "            \"costo\": \"10.059615\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11031\",\n" +
                "            \"costo\": \"10.010008\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11059\",\n" +
                "            \"costo\": \"4.522858\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11060\",\n" +
                "            \"costo\": \"4.534494\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11062\",\n" +
                "            \"costo\": \"3.729847\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11063\",\n" +
                "            \"costo\": \"3.728993\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11083\",\n" +
                "            \"costo\": \"10.360899\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11089\",\n" +
                "            \"costo\": \"10.236201\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11090\",\n" +
                "            \"costo\": \"10.436207\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11093\",\n" +
                "            \"costo\": \"7.863282\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11102\",\n" +
                "            \"costo\": \"13.903588\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11104\",\n" +
                "            \"costo\": \"7.875848\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11111\",\n" +
                "            \"costo\": \"7.869976\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11117\",\n" +
                "            \"costo\": \"9.94979\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11118\",\n" +
                "            \"costo\": \"13.99151\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11121\",\n" +
                "            \"costo\": \"7.870074\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11122\",\n" +
                "            \"costo\": \"7.879109\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11125\",\n" +
                "            \"costo\": \"17.564014\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11132\",\n" +
                "            \"costo\": \"15.169438\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11144\",\n" +
                "            \"costo\": \"7.137121\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11150\",\n" +
                "            \"costo\": \"10.472685\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11155\",\n" +
                "            \"costo\": \"17.53223\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11162\",\n" +
                "            \"costo\": \"14.705214\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11182\",\n" +
                "            \"costo\": \"13.146542\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11183\",\n" +
                "            \"costo\": \"13.140161\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11185\",\n" +
                "            \"costo\": \"13.154289\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11186\",\n" +
                "            \"costo\": \"13.110332\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11188\",\n" +
                "            \"costo\": \"17.110873\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11191\",\n" +
                "            \"costo\": \"14.858513\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11192\",\n" +
                "            \"costo\": \"14.798009\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11194\",\n" +
                "            \"costo\": \"15.705512\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11217\",\n" +
                "            \"costo\": \"7.873269\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11220\",\n" +
                "            \"costo\": \"17.189002\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11227\",\n" +
                "            \"costo\": \"14.359559\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11232\",\n" +
                "            \"costo\": \"17.547062\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11234\",\n" +
                "            \"costo\": \"14.662216\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11239\",\n" +
                "            \"costo\": \"7.861076\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11242\",\n" +
                "            \"costo\": \"7.855547\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11247\",\n" +
                "            \"costo\": \"14.773603\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11248\",\n" +
                "            \"costo\": \"14.876146\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11249\",\n" +
                "            \"costo\": \"14.882027\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11251\",\n" +
                "            \"costo\": \"7.856748\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11252\",\n" +
                "            \"costo\": \"7.850784\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11253\",\n" +
                "            \"costo\": \"7.862168\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11256\",\n" +
                "            \"costo\": \"11.359701\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11258\",\n" +
                "            \"costo\": \"16.070063\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11261\",\n" +
                "            \"costo\": \"16.875232\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11262\",\n" +
                "            \"costo\": \"16.884274\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11263\",\n" +
                "            \"costo\": \"16.958629\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11264\",\n" +
                "            \"costo\": \"17.021719\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11269\",\n" +
                "            \"costo\": \"16.729296\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11270\",\n" +
                "            \"costo\": \"16.916122\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11271\",\n" +
                "            \"costo\": \"16.929602\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11274\",\n" +
                "            \"costo\": \"16.142127\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11281\",\n" +
                "            \"costo\": \"14.206637\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11282\",\n" +
                "            \"costo\": \"13.811889\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11283\",\n" +
                "            \"costo\": \"17.369016\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11284\",\n" +
                "            \"costo\": \"16.762349\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11289\",\n" +
                "            \"costo\": \"16.020634\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11290\",\n" +
                "            \"costo\": \"14.495444\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11293\",\n" +
                "            \"costo\": \"15.985917\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11294\",\n" +
                "            \"costo\": \"7.875129\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11297\",\n" +
                "            \"costo\": \"16.564307\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11298\",\n" +
                "            \"costo\": \"11.376722\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11299\",\n" +
                "            \"costo\": \"11.363558\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11301\",\n" +
                "            \"costo\": \"17.539675\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11302\",\n" +
                "            \"costo\": \"17.552879\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11303\",\n" +
                "            \"costo\": \"7.862602\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11304\",\n" +
                "            \"costo\": \"13.782234\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11306\",\n" +
                "            \"costo\": \"7.863569\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11310\",\n" +
                "            \"costo\": \"17.537423\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"item\": \"11311\",\n" +
                "            \"costo\": \"12.955145\"\n" +
                "        }\n" +
                "    ]";
        try {
            List<ItemDto> itemDtos = new ObjectMapper().readValue(aa, new TypeReference<List<ItemDto>>() {});
            Transaction transaction = new Transaction();
            Item item = new Item();
            item.setId(map.get("ITEM"));

            ItemDto itemDto = itemDtos.stream().filter(b -> b.getItem().equals(item.getId())).findFirst().orElse(null);
            if(itemDto != null && map.get("TIPO").equals("E") && map.get("TRANS_TIPO").equals("43")
                    || map.get("TRANS_TIPO").equals("44") || map.get("TRANS_TIPO").equals("45")) {
                item.setPrice(itemDto.getCosto());
            } else {
                item.setPrice(new BigDecimal(map.get("PU_ACTUAL").replace(",", ""))
                        .setScale(6, BigDecimal.ROUND_DOWN));
            }


            item.setQuantity(new BigDecimal(map.get("CANTIDAD").replace(",", "")));
            item.setIdentifier(option);


            if (map.get("TIPO").equals("E")) {
                transaction.setType(TransactionType.ENTRY);
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(item.getPrice());
                transaction.setDetail(buildDetail(map, option));
            } else if (map.get("TIPO").equals("S")) {
                transaction.setType(TransactionType.EGRESS);
                transaction.setPriceActual(item.getPrice());
                transaction.setPriceNeto(BigDecimal.ZERO);
                transaction.setDetail(buildDetail(map, option));
            }

            LocalDate currentDate = convertToDate(map.get("FECHA")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            transaction.setTransactionDate(currentDate);
            transaction.setItem(item);
            transaction.setProcessId(processActive.getId());
            transaction.setDetail(buildDetail(map, option));

            return transaction;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private TransactionDetail buildDetail(Map<String, String> map, TransactionOption option) {
        switch (option) {
            case PRIMA:
                return buildPrimaDetail(map);
            case REPUESTOS:
                return buildRepuestos(map);
            case PRODUCTO:
                return buildProductDetail(map);
        }
        return null;
    }

    private TransactionDetail buildProductDetail(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("ALMACEN", map.get("ALMACEN"));
        info.put("NRO_DOC", map.get("NRO_DOC"));
        info.put("SEMANA", map.get("SEMANA"));
//        info.put("TIPO_MOV", map.get("TIPO_MOV"));
        detail.setInformation(info);
        return detail;
    }

    private TransactionDetail buildRepuestos(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("SECCION", map.get("SECCION"));
        info.put("CTA", map.get("CTA"));
        detail.setInformation(info);
        return detail;
    }

    private Date convertToDate(String receivedDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = formatter.parse(receivedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private TransactionDetail buildPrimaDetail(Map<String, String> map) {
        TransactionDetail detail = new TransactionDetail();
        Map<String, String> info = new HashMap<>();
        info.put("CUENTA", map.get("CUENTA"));
        info.put("ALMACEN", map.get("ALMACEN"));
        info.put("SECCION_D", map.get("SECCION_D"));
        info.put("DESCRIPCION", map.get("DESCRIPCION"));
        info.put("TRANS_TIPO", map.get("TRANS_TIPO"));
        detail.setInformation(info);
        return detail;
    }

    private Transaction buildTransactionProcessOne(Map<String, String> map, Process processActive,
                                                   TransactionOption option) {
        Transaction transaction = new Transaction();
        Item item = new Item();
        item.setId(map.get("ITEM"));
        item.setPrice(new BigDecimal(map.get("PU_ACTUAL").replace(",", ""))
                .setScale(6, BigDecimal.ROUND_DOWN));
        item.setQuantity(new BigDecimal(map.get("CANTIDAD").replace(",", "")));
        item.setIdentifier(option);


        if (map.get("TIPO").equals("E")) {
            transaction.setType(TransactionType.ENTRY);
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(item.getPrice());
            transaction.setDetail(buildDetail(map, option));
        } else if (map.get("TIPO").equals("S")) {
            transaction.setType(TransactionType.EGRESS);
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(BigDecimal.ZERO);
            transaction.setDetail(buildDetail(map, option));
        } else if (map.get("TIPO").equals("EC")) {
            transaction.setType(TransactionType.ENTRY_BUY);
            transaction.setPriceActual(item.getPrice());
            transaction.setPriceNeto(item.getPrice());
            transaction.setDetail(buildDetail(map, option));
        } else if (map.get("TIPO").equals("CAM")) {
            transaction.setType(TransactionType.CAM);
            transaction.setDetail(buildDetail(map, option));
        }

        LocalDate currentDate = convertToDate(map.get("FECHA")).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        transaction.setTransactionDate(currentDate);
        transaction.setItem(item);
        transaction.setProcessId(processActive.getId());
        transaction.setDetail(buildDetail(map, option));

        return transaction;
    }

}
