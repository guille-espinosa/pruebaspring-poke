package com.guillermo.pruebaspring.pruebaspring_poke.infraestructure.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.guillermo.pruebaspring.pruebaspring_poke.infraestructure.processor.PokemonProcessor;

@Component
public class PokemonRoute extends RouteBuilder {

    @Value("${pokeapi.base-url}")
    private String pokeApiBaseUrl;

    @Override
    public void configure() throws Exception {

        from("direct:getPokemon")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD(pokeApiBaseUrl + "pokemon/${header.pokemonId}")
            .unmarshal().json(JsonLibrary.Jackson, Object.class)
            .process(new PokemonProcessor());

        from("direct:getPokemonType")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD(pokeApiBaseUrl + "type/${header.typeName}")
            .unmarshal().json(JsonLibrary.Jackson, Object.class);
            
        from("direct:getPokemonListForFilter")
            .setHeader("CamelHttpMethod", constant("GET"))
            .toD(pokeApiBaseUrl + "pokemon?limit=${header.limit}&offset=${header.offset}")
            .unmarshal().json(JsonLibrary.Jackson, Object.class);
    }
}

