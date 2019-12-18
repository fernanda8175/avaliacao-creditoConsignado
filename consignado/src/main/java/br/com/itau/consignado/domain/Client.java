package br.com.itau.consignado.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "client")
public class Client {

	@Id
	@JsonProperty("id")
	private String id;
	@JsonProperty("cpf")
	private String cpf;
	@JsonProperty("nome")
	private String name;
	@JsonProperty("margemContratacao")
	private BigDecimal margin;
	@JsonProperty("aposentadoria")
	private int benefitNumber;
	@JsonProperty(value = "dataAutorizacao", access = Access.READ_ONLY)
	private Date autorizationDate;
	@JsonProperty("dataNascimento")
	private Date birthday;
	@JsonProperty("dataDespacho")
	private Date dispatchDate;

}
