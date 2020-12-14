package org.ehrbase.fhirbridge.camel.processor.validator.bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.camel.Exchange;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.CanonicalType;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.List;
import java.util.stream.Collectors;

public class BundleProfileValidator {

    public void validate(OperationOutcome operationOutcome, MessageSourceAccessor messages, Bundle bundle, FhirContext fhirContext, Exchange exchange) {
        Profile defaultProfile = Profile.getDefaultProfile(bundle.getClass());
        List<String> profiles = getBundleProfiles(bundle);
        if (profiles.isEmpty()){
            if (defaultProfile == null) {
                operationOutcome.addIssue(new OperationOutcome.OperationOutcomeIssueComponent()
                        .setSeverity(OperationOutcome.IssueSeverity.FATAL)
                        .setCode(OperationOutcome.IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.profile.defaultNotSupported", new Object[]{bundle.getClass(), Profile.getSupportedProfiles(bundle.getClass())}))
                        .addExpression(bundle.getEntry().get(0).getResource().getMeta().getProfile().get(0).getValueAsString()));
                throw new UnprocessableEntityException(fhirContext, operationOutcome);
            }
            exchange.getMessage().setHeader(FhirBridgeConstants.PROFILE, defaultProfile);
        }

    }


    private List<String> getBundleProfiles(Bundle bundle) {
        return bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(resource -> resource.getMeta().getProfile().get(0))
                .map(CanonicalType::getValue)
                .collect(Collectors.toUnmodifiableList());
    }

    private void validateProfiles() {

    }

    private void validateObservationIds() {

    }

    private void validatePatientIds() {

    }

}
