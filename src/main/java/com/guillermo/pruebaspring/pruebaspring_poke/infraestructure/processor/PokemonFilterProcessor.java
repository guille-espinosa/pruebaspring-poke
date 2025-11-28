package com.guillermo.pruebaspring.pruebaspring_poke.infraestructure.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PokemonFilterProcessor implements Processor {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String filterType;

    public PokemonFilterProcessor(String filterType) {
        this.filterType = filterType;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        JsonNode jsonNode = objectMapper.convertValue(body, JsonNode.class);
        
        Integer minDefense = exchange.getIn().getHeader("minDefense", Integer.class);
        Integer minWeight = exchange.getIn().getHeader("minWeight", Integer.class);
        Integer maxWeight = exchange.getIn().getHeader("maxWeight", Integer.class);
        Integer minExp = exchange.getIn().getHeader("minExp", Integer.class);

        switch(filterType) {
            case "defense":
                
                exchange.getIn().setHeader("_minDefense", minDefense);
                break;
            case "weight":
                exchange.getIn().setHeader("_minWeight", minWeight);
                exchange.getIn().setHeader("_maxWeight", maxWeight);
                break;
            case "exp":
                exchange.getIn().setHeader("_minExp", minExp);
                break;
        }

        // Retornar el body sin procesar, el servicio lo manejará
        exchange.getIn().setBody(jsonNode);
    }
}
