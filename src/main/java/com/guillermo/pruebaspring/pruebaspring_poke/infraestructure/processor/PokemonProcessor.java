package com.guillermo.pruebaspring.pruebaspring_poke.infraestructure.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.dto.PokemonDTO;
import com.guillermo.pruebaspring.pruebaspring_poke.domain.entity.Stats;

import java.util.ArrayList;
import java.util.List;

public class PokemonProcessor implements Processor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void process(Exchange exchange) throws Exception {

        Object body = exchange.getIn().getBody();
        JsonNode jsonNode = objectMapper.convertValue(body, JsonNode.class);

        PokemonDTO pokemon = new PokemonDTO();
        pokemon.setId(jsonNode.get("id").asInt());
        pokemon.setName(jsonNode.get("name").asText());
        pokemon.setWeight(jsonNode.get("weight").asInt());
        pokemon.setBaseExperience(jsonNode.get("base_experience").asInt());

        List<String> types = new ArrayList<>();
        jsonNode.get("types").forEach(typeObj -> {
            types.add(typeObj.get("type").get("name").asText());
        });
        pokemon.setTypes(types);

        Stats stats = new Stats();
        jsonNode.get("stats").forEach(stat -> {
            String statName = stat.get("stat").get("name").asText();
            int baseStat = stat.get("base_stat").asInt();

            switch(statName) {
                case "hp" -> stats.setHp(baseStat);
                case "attack" -> stats.setAttack(baseStat);
                case "defense" -> {
                    stats.setDefense(baseStat);
                    pokemon.setDefense(baseStat);
                }
                case "special-attack" -> stats.setSpecialAttack(baseStat);
                case "special-defense" -> stats.setSpecialDefense(baseStat);
                case "speed" -> stats.setSpeed(baseStat);
            }
        });
        pokemon.setStats(stats);

        exchange.getIn().setBody(pokemon);
    }
}
