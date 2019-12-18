package br.com.itau.consignado.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import br.com.itau.consignado.domain.Client;
/**
 * Spring Data MongoDB repository for the Client entity.
 */
@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

	List<Client> findByCpfAndAutorizationDateLessThanEqual(String cpf, Date date);
	List<Client> findByAutorizationDateLessThanEqual(Date date);
	
	Client findByCpf(String cpf);
}
