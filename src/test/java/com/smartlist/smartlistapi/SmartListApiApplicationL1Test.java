package com.smartlist.smartlistapi;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Tag("Layer1")
@DisplayName("Testes de integração da aplicação Smart List")
class SmartListApiApplicationL1Test {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Deve carregar o contexto da aplicação")
	void carregaContextoDaAplicacao() {
	}

	@Test
	@DisplayName("Deve disponibilizar os endpoints de negócio no documento OpenAPI")
	void documentoOpenApiDisponibilizaEndpointsDeNegocio() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.info.title").value("Smart List API"))
			.andExpect(jsonPath("$.paths['/api/v1/shopping-items'].get").exists())
			.andExpect(jsonPath("$.paths['/api/v1/residence'].post").exists());
	}

}
