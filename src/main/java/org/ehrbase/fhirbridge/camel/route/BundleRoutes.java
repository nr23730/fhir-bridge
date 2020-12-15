package org.ehrbase.fhirbridge.camel.route;


import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConstants;
import org.ehrbase.fhirbridge.camel.processor.DefaultExceptionHandler;
import org.ehrbase.fhirbridge.camel.processor.PatientIdProcessor;
import org.ehrbase.fhirbridge.camel.processor.validator.BundleProfileValidator;
import org.ehrbase.fhirbridge.ehr.converter.BloodGasCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.CompositionConverterResolver;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BundleRoutes extends RouteBuilder {

    private final IFhirResourceDao<Bundle> bundleDao;

    private final BundleProfileValidator requestValidator;

    private final PatientIdProcessor patientIdProcessor;

    private final CompositionConverterResolver compositionConverterResolver;

    private final DefaultExceptionHandler defaultExceptionHandler;


    public BundleRoutes(IFhirResourceDao<Bundle> bundleDao,
                        BundleProfileValidator requestValidator,
                        PatientIdProcessor patientIdProcessor,
                        CompositionConverterResolver compositionConverterResolver,
                        DefaultExceptionHandler defaultExceptionHandler) {
        this.bundleDao = bundleDao;
        this.compositionConverterResolver = compositionConverterResolver;
        this.patientIdProcessor = patientIdProcessor;
        this.requestValidator = requestValidator;
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
               // .bean(bundleDao, "create(${body})")
                .setHeader(FhirBridgeConstants.METHOD_OUTCOME, body())
                .setBody(simple("${body.resource}"))
                .process(patientIdProcessor)
                .setHeader(CompositionConstants.COMPOSITION_CONVERTER, method(compositionConverterResolver, "resolve(${header.CamelFhirBridgeProfile})"))
                //  .to("ehr-composition:compositionProducer?operation=mergeCompositionEntity")
                .to("ehr-composition:compositionProducer?operation=mergeCompositionEntity&compositionConverter=#bloodGasCompositionConverter")
                .setBody(header(FhirBridgeConstants.METHOD_OUTCOME));
        // @formatter:on
    }

    @Bean
    public BloodGasCompositionConverter bloodGasCompositionConverter() {
        return new BloodGasCompositionConverter();
    }
}