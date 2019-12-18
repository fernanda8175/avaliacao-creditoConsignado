package br.com.itau.consignado.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.itau.consignado.domain.Client;
import br.com.itau.consignado.domain.CreditContract;
import br.com.itau.consignado.domain.Parameters;
import br.com.itau.consignado.repository.ClientRepository;
import br.com.itau.consignado.service.ClientService;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

	@Autowired
	ClientRepository clientRepository;

	public Optional<?> validateCredit(String cpf) throws Exception {

		List<Client> clientTmp = new ArrayList<Client>();
		List<Client> clientList = new ArrayList<Client>();

		// recupera as informaões do cliente
		if (cpf != null) {
			// busca cliente específico
			clientTmp = clientRepository.findByCpfAndAutorizationDateLessThanEqual(cpf, new Date());
		} else {
			// busca todos os clientes
			clientTmp = clientRepository.findByAutorizationDateLessThanEqual(new Date());
		}

		if (clientTmp.isEmpty()) {
			throw new Exception("Cliente não cadastrado ou não autorizado a realizar empréstimo consignado");
		}

		Date dateNow = new Date();
		for (Client client : clientTmp) {
			Date dateAutorizaion = client.getAutorizationDate();

			// caso o dia de autorizacao seja menor do que a data atual, o cliente pode
			// retirar o empréstimo
			if (dateAutorizaion.before(dateNow)) {
				clientList.add(client);
			}
		}

		return Optional.ofNullable(clientList);
	}

	public Client getClient(String cpf) throws Exception {

		Client clientTmp = null;

		// recupera as informaões do cliente
		if (cpf != null) {
			// busca cliente específico
			clientTmp = clientRepository.findByCpf(cpf);
		}
		if (clientTmp == null) {
			throw new Exception("Cliente não cadastrado");
		}
		return clientTmp;
	}

	public List<Client> getAllClients() throws Exception {

		List<Client> clientTmp = null;

		// recupera as informaões de todos os cliente
		clientTmp = clientRepository.findAll();

		if (clientTmp.isEmpty()) {
			throw new Exception("Não existem clientes cadastrados na base de dados.");
		}
		return clientTmp;
	}

	public Optional<?> varifyClient(Client client) throws Exception {
		Client clientVeriry = clientRepository.findByCpf(client.getCpf());

		if (clientVeriry != null) {
			throw new Exception("Cliente já cadastrado");
		}
		client.setAutorizationDate(new Date());
		client.setDispatchDate(null);
		clientVeriry = clientRepository.save(client);
		return Optional.ofNullable(clientVeriry);
	}

	public Optional<?> setAutorizationDefault(Client client, CreditContract creditContract) {

		int daysPassive = Parameters.daysPassive;
		if (client.getDispatchDate() != null) {
			Date date = setDaysPassive(client.getDispatchDate());
			client.setAutorizationDate(date);
		} else {
			Date date = setDaysPassive(creditContract.getContractDate());
			client.setAutorizationDate(date);
		}
		return Optional.ofNullable(client);
	}

	public Date setDaysPassive(Date datePassive) {

		int daysPassive = Parameters.daysPassive;
		Date date = datePassive;
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		// Através do Calendar, trabalhamos a data informada e adicionamos 180 dia nela
		// (por padrão)
		c.add(Calendar.DATE, +daysPassive);

		// Obtemos a data alterada
		date = c.getTime();

		return date;
	}

	public Optional<?> benefitUnlock(String cpf) throws Exception {

		// busca o cliente
		Client client = clientRepository.findByCpf(cpf);

		if (client == null) {
			throw new Exception("Cliente não cadastrado");
		}

		LocalDate dateNow = LocalDate.now();
		LocalDate dateDispatch = client.getDispatchDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		long daysBetween = ChronoUnit.DAYS.between(dateDispatch, dateNow);

		int daysActive = Parameters.daysActive;

		// valida se é possivel realizar o desbloqueio
		if ((int) daysBetween >= daysActive) {
			client.setAutorizationDate(new Date());
		} else {
			throw new Exception("Não foi possível desbloquear o benefício. Aguarde completar " + daysActive
					+ " do último empréstimo consignado.");
		}
		return Optional.ofNullable(client);
	}

	public Optional<?> updateClient(Client client) throws Exception {
		Optional<Client> clientVeriry = clientRepository.findById(client.getId());
			
		if (!clientVeriry.isPresent()) {
			throw new Exception("Cliente não cadastrado");
		}

		client.setAutorizationDate(clientVeriry.get().getAutorizationDate());
		if(clientVeriry.get().getDispatchDate() != null) {
			client.setDispatchDate(clientVeriry.get().getDispatchDate());
		}
		
		client = clientRepository.save(client);
		return Optional.ofNullable(client);

	}

}
