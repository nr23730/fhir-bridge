package org.ehrbase.fhirbridge.camel.processor.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.ehrbase.fhirbridge.camel.FhirBridgeConstants;
import org.ehrbase.fhirbridge.camel.processor.validator.bundle.BundleProfileValidator;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.ehrbase.fhirbridge.fhir.util.ResourceUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public class ResourceProfileValidator implements Processor, MessageSourceAware {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceProfileValidator.class);

    private final FhirContext fhirContext;

    private MessageSourceAccessor messages;

    public ResourceProfileValidator(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    @Override
    @SuppressWarnings("java:S1192")
    public void process(Exchange exchange) {
        Resource resource = exchange.getIn().getBody(Resource.class);

        LOG.debug("Start validating {} resource...", resource.getResourceType());

        OperationOutcome operationOutcome = new OperationOutcome();
        Class<? extends Resource> resourceType = resource.getClass();


        List<String> profiles = ResourceUtils.getProfiles(resource);
      if(resource.getResourceType() == ResourceType.Bundle) {
          BundleProfileValidator bundleProfileValidator = new BundleProfileValidator();
          bundleProfileValidator.validate(operationOutcome, messages, (Bundle) resource, fhirContext, exchange);
      } else if (profiles.isEmpty() && resource.getResourceType() != ResourceType.Bundle) {
            Profile defaultProfile = Profile.getDefaultProfile(resourceType);
            if (defaultProfile == null) {
                operationOutcome.addIssue(new OperationOutcomeIssueComponent()
                        .setSeverity(IssueSeverity.FATAL)
                        .setCode(IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.profile.defaultNotSupported", new Object[]{resource.getResourceType(), Profile.getSupportedProfiles(resourceType)}))
                        .addExpression(resource.getResourceType() + ".meta.profile[]"));
                throw new UnprocessableEntityException(fhirContext, operationOutcome);
            }

            exchange.getMessage().setHeader(FhirBridgeConstants.PROFILE, defaultProfile);
        }  else {
            Set<Profile> supportedProfiles = Profile.resolveAll(resource);
            if (supportedProfiles.isEmpty()) {
                operationOutcome.addIssue(new OperationOutcomeIssueComponent()
                        .setSeverity(IssueSeverity.FATAL)
                        .setCode(IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.profile.missingSupported", new Object[]{resourceType, Profile.getSupportedProfiles(resourceType)}))
                        .addExpression(resource.getResourceType() + ".meta.profile[]"));
            } else if (supportedProfiles.size() > 1) {
                operationOutcome.addIssue(new OperationOutcomeIssueComponent()
                        .setSeverity(IssueSeverity.FATAL)
                        .setCode(IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.profile.moreThanOneSupported"))
                        .addExpression(resource.getResourceType() + ".meta.profile[]"));
            }

            if (operationOutcome.hasIssue()) {
                throw new UnprocessableEntityException(fhirContext, operationOutcome);
            }

            exchange.getMessage().setHeader(FhirBridgeConstants.PROFILE, supportedProfiles.iterator().next());
        }

        LOG.info("{} resource validated", resource.getResourceType());
    }


    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
