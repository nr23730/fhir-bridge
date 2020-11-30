package org.ehrbase.fhirbridge.camel.route;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.camel.processor.DefaultCreateResourceRequestValidator;
import org.ehrbase.fhirbridge.camel.processor.DefaultExceptionHandler;
import org.ehrbase.fhirbridge.camel.processor.PatientIdProcessor;
import org.ehrbase.fhirbridge.ehr.converter.BundleCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.DiagnosticReportLabCompositionConverter;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.context.annotation.Bean;

public class BundleRoutes extends RouteBuilder {
    private final IFhirResourceDao<Bundle> bundleDao;

    private final DefaultCreateResourceRequestValidator requestValidator;

    private final PatientIdProcessor patientIdProcessor;

    private final DefaultExceptionHandler defaultExceptionHandler;

    public BundleRoutes(IFhirResourceDao<Bundle> bundleDao,
                                  DefaultCreateResourceRequestValidator requestValidator,
                                  PatientIdProcessor patientIdProcessor,
                                  DefaultExceptionHandler defaultExceptionHandler) {
        this.bundleDao = bundleDao;
        this.requestValidator = requestValidator;
        this.patientIdProcessor = patientIdProcessor;
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    @Override
    public void configure() {
        // @formatter:off
        from("fhir-create-bundle:fhirConsumer?fhirContext=#fhirContext")
                .onException(Exception.class)
                .process(defaultExceptionHandler)
                .end()
                .process(requestValidator)
                .bean(bundleDao, "create(${body})")
                .setHeader(FhirBridgeConstants.METHOD_OUTCOME, body())
                .setBody(simple("${body.resource}"))
                .process(patientIdProcessor)
                .to("ehr-composition:compositionProducer?operation=mergeCompositionEntity&compositionConverter=#bundleCompositionConverter")
                .setBody(header(FhirBridgeConstants.METHOD_OUTCOME));
        // @formatter:on
    }

    @Bean
    public BundleCompositionConverter bundleCompositionConverter() {
        return new BundleCompositionConverter(); //TODO here should be decided which bundle is converted
    }
}
