package org.ehrbase.fhirbridge.camel.route;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConstants;
import org.ehrbase.fhirbridge.camel.processor.DefaultCreateResourceRequestValidator;
import org.ehrbase.fhirbridge.camel.processor.DefaultExceptionHandler;
import org.ehrbase.fhirbridge.camel.processor.PatientIdProcessor;
import org.ehrbase.fhirbridge.ehr.converter.BloodGasCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.CompositionConverterResolver;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.context.annotation.Bean;

public class BundleRoutes extends RouteBuilder {
    private final IFhirResourceDao<Bundle> bundleDao;

    private final DefaultCreateResourceRequestValidator requestValidator;

    private final PatientIdProcessor patientIdProcessor;

    private final DefaultExceptionHandler defaultExceptionHandler;

    private final CompositionConverterResolver compositionConverterResolver;

    public BundleRoutes(IFhirResourceDao<Bundle> bundleDao,
                        DefaultCreateResourceRequestValidator requestValidator,
                        PatientIdProcessor patientIdProcessor,
                        CompositionConverterResolver compositionConverterResolver,
                        DefaultExceptionHandler defaultExceptionHandler) {
        this.bundleDao = bundleDao;
        this.compositionConverterResolver = compositionConverterResolver;
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
                .setHeader(CompositionConstants.COMPOSITION_CONVERTER, method(compositionConverterResolver, "resolve(${header.CamelFhirBridgeProfile})"))
                .to("ehr-composition:compositionProducer?operation=mergeCompositionEntity")
                .setBody(header(FhirBridgeConstants.METHOD_OUTCOME));
        // @formatter:on
    }

}
