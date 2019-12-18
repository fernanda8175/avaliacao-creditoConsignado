package br.com.itau.consignado.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Template de resposta JSON para Erro
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Error {

	private Integer code;
	private String message;
	
}
