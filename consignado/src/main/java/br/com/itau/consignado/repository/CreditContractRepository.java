package br.com.itau.consignado.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import br.com.itau.consignado.domain.CreditContract;;
/**
 * Spring Data MongoDB repository for the CreditContract entity.
 */
@Repository
public interface CreditContractRepository extends MongoRepository<CreditContract, String> {

	List<CreditContract> findByCpfAndEndDateLessThanEqual (String cpf, Date date);
	
}
