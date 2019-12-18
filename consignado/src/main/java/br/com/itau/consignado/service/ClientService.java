package br.com.itau.consignado.service;
/**
 * Crédito Consignado controller
 */

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.itau.consignado.domain.Client;
import br.com.itau.consignado.domain.CreditContract;

@Service
public interface ClientService {

	public Optional<?> validateCredit(String cpf) throws Exception;

	public Optional<?> varifyClient(Client client) throws Exception;
	
	public Optional<?> setAutorizationDefault(Client client, CreditContract creditContract);
	
	public Optional<?> benefitUnlock(String cpf) throws Exception;
	
	public Client getClient(String cpf) throws Exception;
	
	public List<Client> getAllClients() throws Exception;

}
