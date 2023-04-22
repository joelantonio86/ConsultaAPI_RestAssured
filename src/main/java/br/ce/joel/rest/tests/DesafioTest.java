package br.ce.joel.rest.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import br.ce.joel.rest.core.BaseTest;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DesafioTest extends BaseTest {
	
	@Test
	public void CT01_DeveConsultarCpfSemRestricao() {
		
		given()
		.when()
			.get("/api/v1/restricoes/44376111015")
		.then()
			.statusCode(204)
		;
	}
	
	@Test
	public void CT02_DeveConsultarCpfComRestricao() {
		
		given()
		.when()
			.get("/api/v1/restricoes/97093236014")
		.then()
			.statusCode(200)
			.body("mensagem", is("O CPF 97093236014 tem problema"))
		;
	}
	
	@Test
	public void CT03_NaoDeveConsultarSemCpf() {
		given()
		.when()
			.get("/api/v1/restricoes/")
		.then()
			.statusCode(404)
			.body("error", is("Not Found"))
			.body("message", is("No message available"))
		;
	}
	@Test
	public void CT04_DeveInserirUmaSimulacaoComSucesso() {
		
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "	  \"nome\": \"Fulano de Tal\",\r\n"
					+ "	  \"cpf\": 76448097082,\r\n"
					+ "	  \"email\": \"email@email.com\",\r\n"
					+ "	  \"valor\": 38000,\r\n"
					+ "	  \"parcelas\": 3,\r\n"
					+ "	  \"seguro\": true\r\n"
					+ "	}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(201)
			.body("nome", is("Fulano de Tal"), "cpf", is("76448097082"), "email", is("email@email.com"), "valor", is(38000), "parcelas", is(3), "seguro", is(true))
		;
	}
	
	@Test
	public void CT05_DeveTentarInserirUmaSimulacaoComCampoNomeVazio() {
		
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "	  \"nome\": null,\r\n"
					+ "	  \"cpf\": 13057000070,\r\n"
					+ "	  \"email\": \"email@email.com\",\r\n"
					+ "	  \"valor\": 1200,\r\n"
					+ "	  \"parcelas\": 3,\r\n"
					+ "	  \"seguro\": true\r\n"
					+ "	}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.nome", is("Nome não pode ser vazio"))
		;
	}
	
	@Test
	public void CT06_DeveTentarInserirUmaSimulacaoComCampoCpfVazio() {
		
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "	  \"nome\": \"Fulano de Tal\",\r\n"
					+ "	  \"cpf\": null,\r\n"
					+ "	  \"email\": \"email@email.com\",\r\n"
					+ "	  \"valor\": 1200,\r\n"
					+ "	  \"parcelas\": 3,\r\n"
					+ "	  \"seguro\": true\r\n"
					+ "	}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.cpf", is("CPF não pode ser vazio"))
		;
	}
	
	@Test
	public void CT07_DeveTentarInserirUmaSimulacaoComCampoEmailInvalido() {
		
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "	  \"nome\": \"Fulano de Tal\",\r\n"
					+ "	  \"cpf\": 70534573037,\r\n"
					+ "	  \"email\": \"@email.com\",\r\n"
					+ "	  \"valor\": 1200,\r\n"
					+ "	  \"parcelas\": 3,\r\n"
					+ "	  \"seguro\": true\r\n"
					+ "	}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.email", is("E-mail deve ser um e-mail válido"))
		;
	}
	
	@Test
	public void CT08_DeveTentarInserirUmaSimulacaoComValorMaiorQueQuarentaMilReais() {
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "	  \"nome\": \"Fulano\",\r\n"
					+ "	  \"cpf\": 82004452080,\r\n"
					+ "	  \"email\": \"test@email.com\",\r\n"
					+ "	  \"valor\": 41000,\r\n"
					+ "	  \"parcelas\": 3,\r\n"
					+ "	  \"seguro\": true\r\n"
					+ "	}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.valor", is("Valor deve ser menor ou igual a R$ 40.000"))
			.log().all()
			;
	}
	@Test
	public void CT09_DeveTentarInserirUmaSimulacaoComValorVazio() {
		
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "    \"nome\": \"Fulano de Tal\",\r\n"
					+ "    \"cpf\": 96426988030,\r\n"
					+ "    \"email\": \"test@email.com\",\r\n"
					+ "    \"valor\": null,\r\n"
					+ "    \"parcelas\": 3,\r\n"
					+ "    \"seguro\": true\r\n"
					+ "}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.valor", is("Valor não pode ser vazio"))
			.log().all();
		;
	}
	@Test
	public void CT10_DeveTentarInserirUmaSimulacaoComParcelaMenorQueDois() {
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "    \"nome\": \"Fulano de Tal\",\r\n"
					+ "    \"cpf\": 28861069096,\r\n"
					+ "    \"email\": \"test@email.com\",\r\n"
					+ "    \"valor\": 3000,\r\n"
					+ "    \"parcelas\": 1,\r\n"
					+ "    \"seguro\": true\r\n"
					+ "}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.parcelas", is("Parcelas deve ser igual ou maior que 2"))
		;
	}
	
	@Test
	public void CT11_DeveTentarInserirUmaSimulacaoComParcelaMaiorQueQuarentaeOito() {
		//(Esse teste irá falhar pois está realizando simulações com parcelas maior que 48 vezes  
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "    \"nome\": \"Fulano de Tal\",\r\n"
					+ "    \"cpf\": 28861069088,\r\n"
					+ "    \"email\": \"test@email.com\",\r\n"
					+ "    \"valor\": 3000,\r\n"
					+ "    \"parcelas\": 49,\r\n"
					+ "    \"seguro\": true\r\n"
					+ "}")
		.when()
			.post("/api/v1/simulacoes")
		.then()
			.statusCode(400)
			.body("erros.parcelas", is("A parcela nao pode ser maior que 48 vezes"))
		;
	}
	@Test
	public void CT12_DeveAlterarUmaSimulacaoExistente() {
		//  
		given()
		.log().all()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "    \"nome\": \"Fulano de Tal\",\r\n"
					+ "    \"cpf\": 65674980055,\r\n"
					+ "    \"email\": \"testalterado@email.com\",\r\n"
					+ "    \"valor\": 1200.00,\r\n"
					+ "    \"parcelas\": 30,\r\n"
					+ "    \"seguro\": false\r\n"
					+ "}")
		.when()
			.put("/api/v1/simulacoes/65674980055")
		.then()
			.statusCode(200)
			.body("nome", is("Fulano de Tal"), "cpf", is("65674980055"), "email", is("testalterado@email.com"), "parcelas", is(30), "seguro", is(false))
		;
	}
	
	@Test
	public void CT13_DeveAlterarUmaSimulacaoCpfInesistente() {
		//  
		given()
			.contentType(APP_CONTENT_TYPE)
			.body("{\r\n"
					+ "    \"nome\": \"Fulano de Tal\",\r\n"
					+ "    \"cpf\": 35269938073,\r\n"
					+ "    \"email\": \"testalterado@email.com\",\r\n"
					+ "    \"valor\": 1200.00,\r\n"
					+ "    \"parcelas\": 30,\r\n"
					+ "    \"seguro\": false\r\n"
					+ "}")
		.when()
			.put("/api/v1/simulacoes/35269938073")
		.then()
			.statusCode(404)
			.body("mensagem", is("CPF 35269938073 não encontrado"))
			.log().all()
		;
	}
	
	@Test
	public void CT14_DeverRetornarTodasSimulacoesEncontradas() {
		//  
		given()
			.contentType(APP_CONTENT_TYPE)
		.when()
			.get("/api/v1/simulacoes/")
		.then()
			.statusCode(200)
			.log().all()
		;
	}
	@Test
	public void CT15_DeverConsultarUmaSimulacaoPeloCPF() {
		//  
		given()
			.contentType(APP_CONTENT_TYPE)
		.when()
			.get("/api/v1/simulacoes/65674980055")
		.then()
			.statusCode(200)
			.body("nome", is("Fulano de Tal"), "cpf", is("65674980055"), "email", is("testalterado@email.com"), "parcelas", is(30), "seguro", is(false))
		;
	}
	@Test
	public void CT16_DeverConsultarUmaSimulacaoPeloCPFNaoCadastrado() {
		//  
		given()
			.contentType(APP_CONTENT_TYPE)
		.when()
			.get("/api/v1/simulacoes/80578484056")
		.then()
			.statusCode(404)
			.body("mensagem", is("CPF 80578484056 não encontrado"))
			.log().all()
		;
	}
	@Test
	public void CT17_DeverRemoverUmaSimulacao() {
		//  
		given()
			.contentType(APP_CONTENT_TYPE)
		.when()
			.delete("/api/v1/simulacoes/37")
		.then()
			.statusCode(200)
			.log().all()
		;
	}
	@Test
	public void CT18_DeverRemoverUmaSimulacaoIDInexistente() {
		//  Esse Teste irá falhar, pois está aceitando qualquer valor que passa como parametro ID da requisição
		given()
			.contentType(APP_CONTENT_TYPE)
		.when()
			.delete("/api/v1/simulacoes/10100")
		.then()
			.statusCode(404)
			.log().all()
		;
	}
}

