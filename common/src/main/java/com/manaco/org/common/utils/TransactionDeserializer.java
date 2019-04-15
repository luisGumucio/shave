package com.manaco.org.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.manaco.org.common.model.MateriaDetail;
import com.manaco.org.common.model.TransactionDetail;

import java.io.IOException;

public class TransactionDeserializer extends JsonDeserializer<TransactionDetail> {

    @Override
    public TransactionDetail deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);

        Class<? extends TransactionDetail> instanceClass = null;
        if (root.get("wareHouseId") != null) {
            instanceClass = MateriaDetail.class;
        } else {
            instanceClass = null;
        }

        return mapper.readValue(root.toString(), instanceClass);
    }
}
