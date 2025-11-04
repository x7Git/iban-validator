package com.acme.ibanvalidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IbanControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource(delimiterString = "|", textBlock = """
            DE02120300000000202051 | Deutsche Kreditbank Berlin | BYLADEM1001
            DE02500105170137075030 | ING-DiBa | INGDDEFFXXX
            DE02100500000054540402 | Landesbank Berlin - Berliner Sparkasse | BELADEBEXXX
            DE02300209000106531065 | TARGOBANK | CMCIDEDDXXX
            DE02200505501015871393 | Hamburger Sparkasse | HASPDEHHXXX
            DE02100100100006820101 | Postbank Ndl der DB Privat- und Firmenkundenbank | PBNKDEFFXXX
            DE02300606010002474689 | apoBank | DAAEDEDDXXX
            DE02600501010002034304 | Landesbank Baden-Württemberg/Baden-Württembergische Bank | SOLADEST600
            DE02700202700010108669 | UniCredit Bank - HypoVereinsbank | HYVEDEMMXXX
            DE02700100800030876808 | Postbank Ndl der DB Privat- und Firmenkundenbank | PBNKDEFFXXX
            DE02370502990000684712 | Kreissparkasse Köln | COKSDE33XXX
            DE88100900001234567892 | Berliner Volksbank | BEVODEBBXXX
            DE02701500000000594937 | Stadtsparkasse München | SSKMDEMMXXX
            AT026000000001349870 | BAWAG P.S.K. Bank für Arbeit und Wirtschaft und Österreichische Postsparkasse Aktiengesellschaft | BAWAATWWXXX
            AT023200000000641605 | RAIFFEISENLANDESBANK NIEDERÖSTERREICH-WIEN AG | RLNWATWWXXX
            CH0209000000100013997 | PostFinance AG | POFICHBEXXX
            CH0200790016271403331 | Berner Kantonalbank AG | KBBECH22XXX
            """)
    void shouldValidateAllBanks_WithExpectedData(String iban, String expectedName, String expectedBic) throws Exception {
        String requestBody = String.format("""
            {
                "iban": "%s"
            }
            """, iban);

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.bic").value(expectedBic));
    }


    @Test
    void shouldValidateValidIban_WithSpaces() throws Exception {
        String requestBody = """
            {
                "iban": "DE02 1203 0000 0000 2020 51"
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Deutsche Kreditbank Berlin"))
                .andExpect(jsonPath("$.bic").value("BYLADEM1001"));
    }


    @Test
    void shouldReturnBadRequest_WhenIbanIsNull() throws Exception {
        String requestBody = """
            {
                "iban": null
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_WhenIbanIsEmpty() throws Exception {
        String requestBody = """
            {
                "iban": ""
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_WhenIbanIsMissing() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_WhenIbanFormatIsInvalid() throws Exception {
        String requestBody = """
            {
                "iban": "INVALID123"
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_WhenIbanTooShort() throws Exception {
        String requestBody = """
            {
                "iban": "DE02"
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_WhenRequestBodyIsInvalid() throws Exception {
        String requestBody = "not a json";

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_WhenContentTypeIsMissing() throws Exception {
        String requestBody = """
            {
                "iban": "DE02120300000000202051"
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .content(requestBody))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldValidateIban_WithLowercaseCountryCode() throws Exception {
        String requestBody = """
            {
                "iban": "de02120300000000202051"
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.bic").exists());
    }

    @Test
    void shouldHandleIbanWithMixedSpacing() throws Exception {
        String requestBody = """
            {
                "iban": "DE021203 00000000 202051"
            }
            """;

        mockMvc.perform(post("/validate-iban")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Deutsche Kreditbank Berlin"))
                .andExpect(jsonPath("$.bic").value("BYLADEM1001"));
    }
}
