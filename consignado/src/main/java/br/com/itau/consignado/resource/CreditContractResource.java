package br.com.itau.consignado.resource;
/**
 * Crédito Consignado controller
 */

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
import org.springframework.web.bind.annotation.RestController;

import br.com.itau.consignado.domain.CreditContract;
import br.com.itau.consignado.domain.Error;
import br.com.itau.consignado.repository.CreditContractRepository;
import br.com.itau.consignado.service.CreditContractService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/loan")

public class CreditContractResource {

	@Autowired
	CreditContractService creditContractService;

	@Autowired
	CreditContractRepository creditContractRepository;

	@ApiOperation(value = "Recupera o empréstimo consignado ativo de um determinado cliente")
	@GetMapping(value = "/{cpf}")
	public ResponseEntity<?> getContract(@PathVariable(required = true, value = "cpf") String cpf) {

		try {
		Optional<?> creditValidate = creditContractService.getActiveContract(cpf);
		
		List<CreditContract> creditContract = (List<CreditContract>) creditValidate.get();

		return new ResponseEntity<List<CreditContract>>(creditContract, HttpStatus.OK);
		}catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	@ApiOperation(value = "Realiza o empréstimo consignado para um determinado cliente")
	@PostMapping("/contract")
	public ResponseEntity<?> registerContract(@Valid @RequestBody CreditContract creditContract) throws Exception {
	
		if (creditContract.getId() != null) {
			Error error = new Error(400, "Um novo contrato não deve possuir um ID");
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}

		try{
			
			creditContractService.registerContract(creditContract);
			
			
		} catch (Exception e) {
			Error error = new Error(400, e.getMessage());
			return new ResponseEntity<String>(error.getMessage(), HttpStatus.BAD_REQUEST);
		}

		CreditContract result = creditContractRepository.save(creditContract);
		
		return new ResponseEntity<CreditContract>(result, HttpStatus.OK);
	}

	

	
}
