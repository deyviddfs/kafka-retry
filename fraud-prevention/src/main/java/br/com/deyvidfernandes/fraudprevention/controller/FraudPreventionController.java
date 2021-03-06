package br.com.deyvidfernandes.fraudprevention.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "transacional", tags = "transacional", description = "API de simulação do sistema de prevenção à fraude")
@RestController
@RequestMapping("/fraude")
public class FraudPreventionController {

    @ApiOperation(value = "Esta operação simula o recebimento de notificações de transação")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna Sucesso")
    })
    @RequestMapping(value = "/controle-transacional", method = RequestMethod.GET)
    public String transactionControl() {
        return "sucesso";
    }
}
