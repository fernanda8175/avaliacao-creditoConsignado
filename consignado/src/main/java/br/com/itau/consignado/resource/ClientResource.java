package br.com.itau.consignado.resource;
/**
 * Controle de Clientes para crédito Consignado
 */


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.itau.consignado.domain.Client;
import br.com.itau.consignado.domain.Error;
import br.com.itau.consignado.repository.ClientRepository;
import br.com.itau.consignado.service.ClientService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/client")

public class ClientResource {

	@Autowired
	ClientService clientService;

	@Autowired
	ClientRepository clientRepository;

	@PostMapping()
	public ResponseEntity<?> registerClient(@Valid @RequestBody Client client) throws Exception {

		Optional<?> clientResult;
		if (client.getId() != null) {
			Error error = new Error(400, "Um novo cliente não deve possuir um ID");
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}

		try {
			//cadastra um novo cliente. Deve-se ter apenas um cadastro por cpf.
			clientResult = clientService.varifyClient(client);
		} catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		Client result = (Client) clientResult.get();

		return new ResponseEntity<Client>(result, HttpStatus.OK);
	}

	
	@ApiOperation(value = "Valida a possibilidade do cliente realizar um novo empréstimo consignado")
	@GetMapping(value = "/autorized/{cpf}")
	public ResponseEntity<?> validateCredit(@PathVariable(required = true, value = "cpf") String cpf) {

		Optional<?> clientValidate = null;
		try {
			clientValidate = clientService.validateCredit(cpf);
		} catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}

		Client client = (Client) clientValidate.get();

		return new ResponseEntity<Client>(client, HttpStatus.OK);

	}
	
	@ApiOperation(value = "Retorna os dados de um determinado cliente")
	@GetMapping(value = "/{cpf}")
	public ResponseEntity<?> getClient(@PathVariable(required = true, value = "cpf") String cpf) {

		Client client = null;
		try {
			client = clientService.getClient(cpf);
		} catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Client>(client, HttpStatus.OK);

	}
	
	@ApiOperation(value = "Recupera todos os clientes que podem realizar um novo empréstimo consignado (Ativo ou Passivo)")
	@GetMapping("/autorized")
	public ResponseEntity<?> validateAll() {

		Optional<?> clientValidate = null;
		try {
			//busca todos os clientes que podem contratar empréstimos novos
			clientValidate = clientService.validateCredit(null);
		} catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}

		List<Client> client = (List<Client>) clientValidate.get();

		return new ResponseEntity<List<Client>>(client, HttpStatus.OK);

	}
	
	@ApiOperation(value = "Retorna os dados todos os clientes cadastrados")
	@GetMapping()
	public ResponseEntity<?> getAllClient() {

		List<Client> clients = null;
		try {
			clients = clientService.getAllClients();
		} catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);

	}

}
