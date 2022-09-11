package me.escoffier.quarkus.redis.fight;

public record FightRequest(String id, Hero hero, Villain villain) {

}
