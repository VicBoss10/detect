package com.jade.detect.config;

import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer globalResponseCustomizer() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                ApiResponses responses = operation.getResponses();

                switch (httpMethod) {
                    case GET:
                        addGetResponses(responses);
                        break;
                    case POST:
                        addPostResponses(responses);
                        break;
                    case PUT:
                    case PATCH:
                        addPutPatchResponses(responses);
                        break;
                    case DELETE:
                        addDeleteResponses(responses);
                        break;
                    default:
                        addCommonResponses(responses);
                }
            });
        });
    }

    private void addGetResponses(ApiResponses responses) {
        responses.addApiResponse("200", new ApiResponse().description("Consulta exitosa"));
        responses.addApiResponse("204", new ApiResponse().description("No hay contenido disponible"));
        responses.addApiResponse("404", new ApiResponse().description("Recurso no encontrado"));
        addCommonResponses(responses);
    }

    private void addPostResponses(ApiResponses responses) {
        responses.addApiResponse("201", new ApiResponse().description("Recurso creado correctamente"));
        responses.addApiResponse("400", new ApiResponse().description("Solicitud inválida. Datos incompletos o malformados"));
        responses.addApiResponse("409", new ApiResponse().description("Conflicto. Ya existe un recurso similar"));
        addCommonResponses(responses);
    }

    private void addPutPatchResponses(ApiResponses responses) {
        responses.addApiResponse("200", new ApiResponse().description("Actualización realizada correctamente"));
        responses.addApiResponse("400", new ApiResponse().description("Solicitud inválida. Datos incorrectos o faltantes"));
        responses.addApiResponse("404", new ApiResponse().description("No se encontró el recurso a actualizar"));
        addCommonResponses(responses);
    }

    private void addDeleteResponses(ApiResponses responses) {
        responses.addApiResponse("200", new ApiResponse().description("Recurso eliminado correctamente"));
        responses.addApiResponse("404", new ApiResponse().description("No se encontró el recurso a eliminar"));
        addCommonResponses(responses);
    }

    private void addCommonResponses(ApiResponses responses) {
        responses.addApiResponse("401", new ApiResponse().description("No autorizado. Token faltante o inválido"));
        responses.addApiResponse("403", new ApiResponse().description("Acceso denegado. No tiene permisos suficientes"));
        responses.addApiResponse("500", new ApiResponse().description("Error interno del servidor"));
    }
}
