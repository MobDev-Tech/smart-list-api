package com.smartlist.smartlistapi.integration;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@Tag("Layer1")
@DisplayName("Integração dos fluxos da Smart List API com H2")
class SmartListApiFlowsL1Test {

    private static final String DEVICE_ID = "device-1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void limpaBancoDeIntegracao() {
        jdbcTemplate.update("DELETE FROM shopping_items");
        jdbcTemplate.update("DELETE FROM snooze_settings");
        jdbcTemplate.update("DELETE FROM residences");
        jdbcTemplate.update("DELETE FROM user_settings");
    }

    @Test
    @DisplayName("Deve criar, atualizar, concluir e remover item persistido")
    void dadoItemDaLista_quandoCriadoAtualizadoConcluidoERemovido_entaoPersistirCadaAlteracao() throws Exception {
        long id = criaItem("Arroz", "2 kg", "MERCEARIA");

        mockMvc.perform(deviceHeader(put("/api/v1/shopping-items/{id}", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"Arroz integral","quantity":"1 kg","category":"MERCADO"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Arroz integral"))
                .andExpect(jsonPath("$.quantity").value("1 kg"))
                .andExpect(jsonPath("$.category").value("MERCADO"))
                .andExpect(jsonPath("$.completed").value(false));

        mockMvc.perform(deviceHeader(patch("/api/v1/shopping-items/{id}/toggle", id)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed").value(true));

        mockMvc.perform(deviceHeader(get("/api/v1/shopping-items")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pendingCount").value(0))
                .andExpect(jsonPath("$.completedCount").value(1))
                .andExpect(jsonPath("$.completedItems[0].name").value("Arroz integral"));

        mockMvc.perform(deviceHeader(delete("/api/v1/shopping-items/{id}", id)))
                .andExpect(status().isNoContent());

        mockMvc.perform(deviceHeader(get("/api/v1/shopping-items")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pendingCount").value(0))
                .andExpect(jsonPath("$.completedCount").value(0));
    }

    @Test
    @DisplayName("Deve rejeitar item inválido e informar recurso inexistente")
    void dadoItemInvalidoOuInexistente_quandoEnviadoPelaApi_entaoRetornarErroCorrespondente() throws Exception {
        mockMvc.perform(deviceHeader(post("/api/v1/shopping-items"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"   ","quantity":"1","category":"MERCEARIA"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.name").value("O nome do produto é obrigatório"));

        mockMvc.perform(deviceHeader(delete("/api/v1/shopping-items/{id}", 987654L)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Item não encontrado com o ID 987654 para este dispositivo."));
    }

    @Test
    @DisplayName("Deve persistir a residência e calcular pontos dentro e fora do geofence")
    void dadaResidenciaConfigurada_quandoGeofenceConsultado_entaoDistinguirPosicoesDentroEForaDoRaio() throws Exception {
        mockMvc.perform(deviceHeader(post("/api/v1/residence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(residenciaJson(true)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Rua das Flores"))
                .andExpect(jsonPath("$.radiusMeters").value(150))
                .andExpect(jsonPath("$.geofenceActive").value(true));

        mockMvc.perform(deviceHeader(get("/api/v1/residence")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Rua das Flores"));

        mockMvc.perform(deviceHeader(post("/api/v1/residence/check-geofence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"currentLatitude":-23.5,"currentLongitude":-46.6}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insidePerimeter").value(true))
                .andExpect(jsonPath("$.distanceFromHomeMeters").value(0.0));

        mockMvc.perform(deviceHeader(post("/api/v1/residence/check-geofence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"currentLatitude":-23.5,"currentLongitude":-46.61}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insidePerimeter").value(false))
                .andExpect(jsonPath("$.distanceFromHomeMeters").value(1019.72));
    }

    @Test
    @DisplayName("Deve retornar não encontrado ao avaliar geofence sem residência")
    void dadaResidenciaNaoConfigurada_quandoGeofenceConsultado_entaoRetornarNaoEncontrado() throws Exception {
        mockMvc.perform(deviceHeader(get("/api/v1/residence")))
                .andExpect(status().isNotFound());

        mockMvc.perform(deviceHeader(post("/api/v1/residence/check-geofence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"currentLatitude":-23.5,"currentLongitude":-46.6}
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve avaliar notificação com dados da lista, residência e soneca")
    void dadaListaResidenciaESoneca_quandoGatilhoDeNotificacaoAvaliado_entaoRetornarDecisaoCorrespondente() throws Exception {
        mockMvc.perform(deviceHeader(post("/api/v1/notifications/trigger-check"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coordenadasJson(-23.5, -46.61)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shouldNotify").value(false))
                .andExpect(jsonPath("$.pendingItemsCount").value(0));

        mockMvc.perform(deviceHeader(post("/api/v1/residence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(residenciaJson(true)))
                .andExpect(status().isOk());
        mockMvc.perform(deviceHeader(get("/api/v1/residence")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Rua das Flores"));
        criaItem("Leite", "1 L", "LATICINIOS");

        mockMvc.perform(deviceHeader(post("/api/v1/notifications/trigger-check"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coordenadasJson(-23.5, -46.61)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shouldNotify").value(true))
                .andExpect(jsonPath("$.pendingItemsCount").value(1))
                .andExpect(jsonPath("$.pendingItemsSummary[0].name").value("Leite"));

        mockMvc.perform(deviceHeader(post("/api/v1/notifications/snooze"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"option":"INDEFINITE"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.option").value("INDEFINITE"));

        mockMvc.perform(deviceHeader(post("/api/v1/notifications/trigger-check"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coordenadasJson(-23.5, -46.61)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shouldNotify").value(false))
                .andExpect(jsonPath("$.pendingItemsCount").value(1));

        mockMvc.perform(deviceHeader(delete("/api/v1/notifications/snooze")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));

        mockMvc.perform(deviceHeader(post("/api/v1/notifications/trigger-check"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coordenadasJson(-23.5, -46.61)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shouldNotify").value(true))
                .andExpect(jsonPath("$.pendingItemsCount").value(1));
    }

    @Test
    @DisplayName("Deve suprimir notificação quando o dispositivo ainda está em casa")
    void dadosItensPendentesEDispositivoDentroDoGeofence_quandoGatilhoAvaliado_entaoNaoNotificar() throws Exception {
        mockMvc.perform(deviceHeader(post("/api/v1/residence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(residenciaJson(true)))
                .andExpect(status().isOk());
        criaItem("Pão", "2 unidades", "PADARIA");

        mockMvc.perform(deviceHeader(post("/api/v1/notifications/trigger-check"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(coordenadasJson(-23.5, -46.6)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.shouldNotify").value(false))
                .andExpect(jsonPath("$.pendingItemsCount").value(1));
    }

    @Test
    @DisplayName("Deve persistir preferências e refletir residência no diagnóstico")
    void dadasConfiguracoesDoUsuario_quandoPreferenciasEResidenciaAtualizadas_entaoRefletirNoDiagnostico() throws Exception {
        mockMvc.perform(deviceHeader(get("/api/v1/settings")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remindOnLeave").value(true))
                .andExpect(jsonPath("$.notificationPermission").value("PERMITIDO"));

        mockMvc.perform(deviceHeader(put("/api/v1/settings"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "remindOnLeave":false,
                                  "vibrateOnNotify":false,
                                  "notificationPermission":"NEGADO",
                                  "locationPermission":"NEGADO"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remindOnLeave").value(false))
                .andExpect(jsonPath("$.vibrateOnNotify").value(false));

        mockMvc.perform(deviceHeader(get("/api/v1/settings")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.remindOnLeave").value(false))
                .andExpect(jsonPath("$.notificationPermission").value("NEGADO"));

        mockMvc.perform(deviceHeader(get("/api/v1/settings/diagnostic")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isResidenceConfigured").value(false));

        mockMvc.perform(deviceHeader(post("/api/v1/residence"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(residenciaJson(true)))
                .andExpect(status().isOk());

        mockMvc.perform(deviceHeader(get("/api/v1/settings/diagnostic")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isResidenceConfigured").value(true))
                .andExpect(jsonPath("$.isSystemReady").value(false))
                .andExpect(jsonPath("$.residenceStatusMessage").value("Monitoramento Geofence Ativo"));
    }

    @Test
    @DisplayName("Deve disponibilizar o estado de saúde da API")
    void dadoEndpointDeSaude_quandoConsultado_entaoRetornarApiDisponivel() throws Exception {
        mockMvc.perform(deviceHeader(get("/api/v1/health")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Smart List API"))
                .andExpect(jsonPath("$.database").value("PostgreSQL"));
    }

    private long criaItem(String nome, String quantidade, String categoria) throws Exception {
        MvcResult result = mockMvc.perform(deviceHeader(post("/api/v1/shopping-items"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name":"%s","quantity":"%s","category":"%s"}
                                """.formatted(nome, quantidade, categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(nome))
                .andReturn();

        Number id = JsonPath.parse(result.getResponse().getContentAsString())
                .read("$.id", Number.class);
        return id.longValue();
    }

    private MockHttpServletRequestBuilder deviceHeader(MockHttpServletRequestBuilder builder) {
        return builder.header("X-Device-ID", DEVICE_ID);
    }

    private String residenciaJson(boolean geofenceAtivo) {
        return """
                {
                  "address":"Rua das Flores",
                  "complement":"Casa 2",
                  "latitude":-23.5,
                  "longitude":-46.6,
                  "radiusMeters":150,
                  "geofenceActive":%s
                }
                """.formatted(geofenceAtivo);
    }

    private String coordenadasJson(double latitude, double longitude) {
        return """
                {"currentLatitude":%s,"currentLongitude":%s}
                """.formatted(latitude, longitude);
    }
}
