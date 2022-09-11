package me.escoffier.quarkus.redis.fight;

public record FightResult(String id, Hero hero, Villain villain, String winner) {

}
