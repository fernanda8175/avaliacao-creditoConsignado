package br.com.itau.consignado.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "credit_contract")
public class CreditContract {

	@Id
	private String id;
	@NotNull
	@JsonProperty("cpf")
	private String cpf;
	@NotNull
	@JsonProperty("valorContratado")
	private BigDecimal contractedValue;
	@NotNull
	@JsonProperty("parcelas")
	private int installments;
	@NotNull
	@JsonProperty("diaVencimento")
	private int installmentDay;
	@NotNull
	@JsonProperty("dataContratacao")
	private Date contractDate;
	@NotNull
	@JsonProperty("fimContrato")
	private Date endDate;
	@NotNull
	@JsonProperty("aposentadoria")
	private int benefitNumber;

}
