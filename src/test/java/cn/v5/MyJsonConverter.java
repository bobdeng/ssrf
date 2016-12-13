package cn.v5;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.io.IOException;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
public class MyJsonConverter extends AbstractJackson2HttpMessageConverter {
    private String jsonPrefix;

    public MyJsonConverter() {
        this(Jackson2ObjectMapperBuilder.json().build());
    }

    public MyJsonConverter(ObjectMapper objectMapper) {
        super(objectMapper, new MediaType[]{MediaType.APPLICATION_JSON, new MediaType("application", "*+json"),
                MediaType.TEXT_HTML, new MediaType("text", "*+html")});
    }

    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = prefixJson?")]}\', ":null;
    }

    protected void writePrefix(JsonGenerator generator, Object object) throws IOException {
        if(this.jsonPrefix != null) {
            generator.writeRaw(this.jsonPrefix);
        }

        String jsonpFunction = object instanceof MappingJacksonValue ?((MappingJacksonValue)object).getJsonpFunction():null;
        if(jsonpFunction != null) {
            generator.writeRaw("/**/");
            generator.writeRaw(jsonpFunction + "(");
        }

    }

    protected void writeSuffix(JsonGenerator generator, Object object) throws IOException {
        String jsonpFunction = object instanceof MappingJacksonValue?((MappingJacksonValue)object).getJsonpFunction():null;
        if(jsonpFunction != null) {
            generator.writeRaw(");");
        }

    }
}
