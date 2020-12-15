package org.ehrbase.fhirbridge.camel.processor.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.camel.processor.validator.ResourceProfileValidator;
import org.ehrbase.fhirbridge.ehr.converter.bundle.BloodGasPanel;
import org.ehrbase.fhirbridge.ehr.converter.bundle.SupportedBundle;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BundleProfileValidator implements Processor, MessageSourceAware {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceProfileValidator.class);

    private final FhirContext fhirContext;

    private MessageSourceAccessor messages;

    public BundleProfileValidator(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    @SuppressWarnings("java:S1192")
    public void process(Exchange exchange) {
        Bundle bundle = exchange.getIn().getBody(Bundle.class);

        LOG.debug("Start validating {} resource...", bundle.getResourceType());

        OperationOutcome operationOutcome = new OperationOutcome();
        Class<? extends Resource> resourceType = bundle.getClass();

        List<String> profiles = getBundleProfiles(bundle);

        if (profiles.isEmpty()) {
            defaultProfileNotSupported(resourceType, operationOutcome, bundle,exchange);
        }else{
            validateBundleEntries(resourceType, operationOutcome, bundle, exchange);
        }

        LOG.info("{} resource validated", bundle.getResourceType());

    }

    private void defaultProfileNotSupported(Class<? extends Resource> resourceType, OperationOutcome operationOutcome, Bundle bundle, Exchange exchange){
        Profile defaultProfile = Profile.getDefaultProfile(resourceType);
        if (defaultProfile == null) {
            operationOutcome.addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                    .setSeverity(OperationOutcome.IssueSeverity.FATAL)
                    .setCode(OperationOutcome.IssueType.VALUE)
                    .setDiagnostics(messages.getMessage("validation.profile.defaultNotSupported", new Object[]{bundle.getResourceType(), Profile.getSupportedProfiles(resourceType)}))
                    .addExpression(bundle.getResourceType() + ""));
            throw new UnprocessableEntityException(fhirContext, operationOutcome);
        }
        exchange.getMessage().setHeader(FhirBridgeConstants.PROFILE, defaultProfile);
    }

    private void validateBundleEntries(Class<? extends Resource> resourceType, OperationOutcome operationOutcome, Bundle bundle, Exchange exchange){
        Set<Profile> supportedProfiles = Profile.resolveAll(bundle);
        SupportedBundle supportedBundle = setBundle(resourceType, operationOutcome, bundle);
        if (operationOutcome.hasIssue()) {
            throw new UnprocessableEntityException(fhirContext, operationOutcome);
        }
        //Add this in the future when more Bundles are supported: exchange.getMessage().setHeader(FhirBridgeConstants.PROFILE, supportedProfiles.iterator().next());
        exchange.getMessage().setHeader(FhirBridgeConstants.PROFILE, supportedProfiles);
        exchange.getMessage().setHeader("Bundle", supportedBundle);

    }


    private SupportedBundle setBundle(Class<? extends Resource> resourceType, OperationOutcome operationOutcome, Bundle bundle){
        Optional<SupportedBundle> supportedBundle;
        for (Bundle.BundleEntryComponent bundleEntryComponent:bundle.getEntry()) {
            supportedBundle = determineBundleType(bundleEntryComponent.getResource().getMeta().getProfile().get(0).getValueAsString(), bundle);
            if(supportedBundle.isPresent()){
                return supportedBundle.get();
            }
        }
        return throwNotSupported(operationOutcome, bundle, resourceType);
    }

    private SupportedBundle throwNotSupported(OperationOutcome operationOutcome, Bundle bundle, Class<? extends Resource> resourceType){
        operationOutcome.addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                .setSeverity(OperationOutcome.IssueSeverity.FATAL)
                .setCode(OperationOutcome.IssueType.VALUE)
                .setDiagnostics(messages.getMessage("validation.bundle.NotSupported",  new Object[]{bundle.getResourceType(), Profile.getSupportedProfiles(resourceType)}))
                .addExpression(bundle.getEntry().get(0).getResource().getResourceType()+ ""));
        throw new UnprocessableEntityException(fhirContext, operationOutcome);
    }

    private Optional<SupportedBundle> determineBundleType(String profileUrl, Bundle bundle) {
        if(profileUrl.equals(Profile.BLOOD_GAS.getUri())) {
            return Optional.of(new BloodGasPanel(bundle));
        }
        return Optional.empty();
    }

    private List<String> getBundleProfiles(Bundle bundle) {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(resource -> resource.getMeta().getProfile().get(0))
                .map(CanonicalType::getValue)
                .collect(Collectors.toUnmodifiableList());
    }


    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}