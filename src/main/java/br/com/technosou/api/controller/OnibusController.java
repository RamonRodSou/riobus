package br.com.technosou.api.controller;

import br.com.technosou.core.model.Onibus;
import br.com.technosou.core.service.OnibusService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/v1/api/onibus")
public class OnibusController {

    @Inject
    OnibusService onibusService;

    @GET
    @Path("/{linha}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Onibus> buscarPorLinha(@PathParam("linha") String linha) {
        return onibusService.buscarOnibusPorLinha(linha);
    }
}
