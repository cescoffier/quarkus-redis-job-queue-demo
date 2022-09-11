package me.escoffier.quarkus.redis.supes;

import io.smallrye.mutiny.Multi;
import org.jboss.resteasy.reactive.RestStreamElementType;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/supes")
public class SupesResource {

    private final SupesService supes;

    public SupesResource(SupesService service) {
        this.supes = service;
    }

    @POST
    public FightRequest submit() {
        return supes.submitAFight();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<FightResult> fights() {
        return supes.getFightResults();
    }


}
