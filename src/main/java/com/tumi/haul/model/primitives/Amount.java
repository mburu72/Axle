package com.tumi.haul.model.primitives;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;

@JsonSerialize(using = AmountSerializer.class)
@JsonDeserialize(using = AmountDeserializer.class)
public class Amount implements Serializable {
    private final BigDecimal value;
    public Amount (final BigDecimal value){
        this.value = value;
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("price must be a positive value");
        }
    }
    public BigDecimal getValue() { // Add this method
        return value;
    }
    @Override
    public String toString() {
        return value.toPlainString();
    }
}
class AmountSerializer extends JsonSerializer<Amount> {


    @Override
    public void serialize(Amount amount, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(amount.toString());
    }
}
class AmountDeserializer extends JsonDeserializer<Amount>{

    @Override
    public Amount deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return new Amount(new BigDecimal(jsonParser.getValueAsString()));
    }
}
