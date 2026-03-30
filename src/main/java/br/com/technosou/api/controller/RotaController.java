package br.com.technosou.api.controller;

import br.com.technosou.core.model.Rota;
import br.com.technosou.core.service.RotaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/v1/api/rotas")
@Produces(MediaType.APPLICATION_JSON)
public class RotaController {

    @Inject
    RotaService rotaService;

    @GET
    @Path("/{linha}")
    public Rota obterRota(@PathParam("linha") String linha) {
        return rotaService.buscarRotaPelaLinha(linha);
    }
}