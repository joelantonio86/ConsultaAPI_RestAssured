package br.ce.joel.rest.core;

import io.restassured.http.ContentType;

public interface Constantes {
	String APP_BASE_URL = "http://localhost";
	Integer	 APP_PORT = 8080;
	
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	//Tempo máximo que cada requisição deve responder (5 segundos);
	Long MAX_TIMEOUT = 5000L;
}
