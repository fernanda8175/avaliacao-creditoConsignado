package br.com.itau.consignado.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.itau.consignado.domain.Client;
import br.com.itau.consignado.domain.CreditContract;
import br.com.itau.consignado.repository.ClientRepository;
import br.com.itau.consignado.repository.CreditContractRepository;
import br.com.itau.consignado.service.ClientService;
import br.com.itau.consignado.service.CreditContractService;


@Service
@Transactional
public class CreditContractServiceImpl implements CreditContractService {

	@Autowired
	CreditContractRepository creditContractRepository;
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	ClientService clientService;
	
	public Optional<?> getActiveContract(String cpf) throws Exception {
		
		List<CreditContract> contractList = creditContractRepository.findByCpfAndEndDateLessThanEqual(cpf, new Date());
		
		if(contractList.isEmpty()) {
			throw new Exception("Contrato com cpf"+ cpf +"não localizado");
		}
		
		return Optional.ofNullable(contractList);
	}

	
	public Optional<?> registerContract(CreditContract creditContract) throws Exception {
		
		List<CreditContract> contractList = creditContractRepository.findByCpfAndEndDateLessThanEqual(creditContract.getCpf(), new Date());
		Client client = clientRepository.findByCpf(creditContract.getCpf());
		//caso exista um contrato ativo, valida se pode criar um novo
		if(!contractList.isEmpty()) {
			if(client.getAutorizationDate().after(new Date())) {
				throw new Exception("Cliente não pode realizar novo empréstimo, aguarde até o dia " + client.getAutorizationDate() );
			}
		}if(client == null) {
			throw new Exception("Cliente não cadastrado");
		}
		
		Optional<?> oClient = clientService.setAutorizationDefault(client, creditContract);
		client = (Client) oClient.get();
		client.setDispatchDate(creditContract.getContractDate());
		
		creditContractRepository.save(creditContract);
		clientRepository.save(client);
		
		return Optional.ofNullable(contractList);
	}

	
}
