package com.icloud.demoinflearnrestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        //TODO 스프링 부트 2.3 으로 올라가면서 Jackson 라이브러리가 더이상 Array 부터 만드는 것을 허용하지 않음.
        // 그래서 errors 라는 필드로 감싼 것.
        jsonGenerator.writeFieldName("errors");
        jsonGenerator.writeStartArray();
        {
            errors.getFieldErrors()
                    .forEach(e -> {
                        try {
                            jsonGenerator.writeStartObject();
                            {
                                jsonGenerator.writeStringField("field", e.getField());
                                jsonGenerator.writeStringField("objectName", e.getObjectName());
                                jsonGenerator.writeStringField("code", e.getCode());
                                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                                Object rejectedValue = e.getRejectedValue();
                                if (rejectedValue != null) {
                                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                                }
                            }
                            jsonGenerator.writeEndObject();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
            ;

            errors.getGlobalErrors()
                    .forEach(e -> {
                        try {
                            jsonGenerator.writeStartObject();
                            {
                                jsonGenerator.writeStringField("objectName", e.getObjectName());
                                jsonGenerator.writeStringField("code", e.getCode());
                                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());

                            }
                            jsonGenerator.writeEndObject();
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    });
        }
        jsonGenerator.writeEndArray();
    }
}
