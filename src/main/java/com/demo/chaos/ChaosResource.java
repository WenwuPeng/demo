package com.demo.chaos;

import com.wordnik.swagger.annotations.*;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.concurrent.atomic.AtomicLong;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/chaos")
@Api(value = "/chaos", description = "Greeting API", position = 1)
@Produces(APPLICATION_JSON)
public class ChaosResource {

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public ChaosResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Path("/{name}")
    @ApiOperation(value = "Greeting by Name",
            notes = "Say hello to the people",
            response = SayingRepresentation.class,
            position = 0)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "No Name Provided")
    })
    @Produces(APPLICATION_JSON)
    @Timed
    public SayingRepresentation sayHello(@ApiParam(value = "name for greeting", required = true) @PathParam("name") String name) {
        return new SayingRepresentation(counter.incrementAndGet(),
                String.format(template, name != null ? name : defaultName));
    }

    @GET
    @Path("/burncpu/{ip}")
    @ApiOperation(value = "Greeting by Name",
            notes = "Burn CPU",
            response = SayingRepresentation.class,
            position = 0)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "No Name Provided")
    })
    @Produces(APPLICATION_JSON)
    @Timed
    public SayingRepresentation burnCpu(@ApiParam(value = "name for greeting", required = true) @PathParam("ip") String ip){
        BurnCpuChaos burnCpuChaos = new BurnCpuChaos(ip, "root", "ca$hc0w");
        burnCpuChaos.apply();
        return new SayingRepresentation(counter.incrementAndGet(),
                String.format(template, ip != null ? ip : defaultName));
    }
}
