package br.com.itau.consignado.service;
/**
 * Crédito Consignado controller
 */

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.itau.consignado.domain.CreditContract;

@Service
public interface CreditContractService {

	public Optional<?> getActiveContract(String cpf) throws Exception;
	
	public Optional<?> registerContract(CreditContract creditContract) throws Exception;

}
