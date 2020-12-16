package org.ehrbase.fhirbridge.camel.processor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.ehrbase.client.aql.parameter.ParameterValue;
import org.ehrbase.client.aql.query.Query;
import org.ehrbase.client.aql.record.Record1;
import org.ehrbase.client.openehrclient.OpenEhrClient;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConstants;
import org.ehrbase.fhirbridge.ehr.converter.bundle.SupportedBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.IssueType;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

public class PatientIdProcessor implements Processor, MessageSourceAware {

    private final FhirContext fhirContext;

    private final OpenEhrClient openEhrClient;

    private MessageSourceAccessor messages;

    public PatientIdProcessor(FhirContext fhirContext, OpenEhrClient openEhrClient) {
        this.fhirContext = fhirContext;
        this.openEhrClient = openEhrClient;
    }

    @Override
    public void process(Exchange exchange) {
        Resource resource = getResource(exchange);

        String patientId = extractPatientId(resource);
        Query<Record1<UUID>> query = Query.buildNativeQuery("select e/ehr_id/value from ehr e where e/ehr_status/subject/external_ref/id/value = $patientId", UUID.class);
        List<Record1<UUID>> result = openEhrClient.aqlEndpoint()
                .execute(query, new ParameterValue<>("patientId", patientId));
        if (result.isEmpty()) {
            throwEhrNotFound(resource, patientId);
        }
        exchange.getIn().setHeader(CompositionConstants.EHR_ID, result.get(0).value1());
    }


    private Resource getResource(Exchange exchange) {
        Resource resource = exchange.getIn().getBody(Resource.class);
        if (resource == null) {
            resource = ((SupportedBundle) exchange.getIn().getHeader("Bundle")).getOriginalBundle();
            exchange.getIn().setBody(resource);
        }
        return resource;
    }

    private String extractPatientId(Resource resource) {
        String patientId = null;
        ResourceType resourceType = resource.getResourceType();
        switch (resourceType) {
            case Bundle:
                patientId = getBundlePatientId((Bundle) resource, resourceType);
                break;
            case Condition:
                patientId = ((Condition) resource).getSubject().getIdentifier().getValue();
                break;
            case DiagnosticReport:
                patientId = ((DiagnosticReport) resource).getSubject().getIdentifier().getValue();
                break;
            case Observation:
                patientId = ((Observation) resource).getSubject().getIdentifier().getValue();
                break;
            case Patient:
                Patient patient = (Patient) resource;
                if (patient.hasIdentifier()) {
                    patientId = ((Patient) resource).getIdentifier().get(0).getValue();
                }
                break;
            case Procedure:
                patientId = ((Procedure) resource).getSubject().getIdentifier().getValue();
                break;
            default:
                throw new IllegalArgumentException("Unsupported resource [" + resourceType + "]");
        }

        if (patientId == null) {
            throwIdentifierRequired(resourceType);
        }
        return patientId;
    }


    private String getBundlePatientId(Bundle bundle, ResourceType resourceType) {
        List<String> patientIds = bundle.getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(this::extractPatientId)
                .collect(Collectors.toUnmodifiableList());

        checkPatientIdsIdentical(patientIds, resourceType);

        return patientIds.get(0);
    }

    private void checkPatientIdsIdentical(List<String> patientIds, ResourceType resourceType) {
        for (String id : patientIds) {
            if (!id.equals(patientIds.get(0))) {
                throwPatientIdsUnidentical(resourceType, id);
            }
        }
    }

    private void throwPatientIdsUnidentical(ResourceType resourceType, String id) {
        throw new UnprocessableEntityException(fhirContext, new OperationOutcome()
                .addIssue(new OperationOutcomeIssueComponent()
                        .setSeverity(IssueSeverity.ERROR)
                        .setCode(IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.bundle.PatientIdsNotIdentical", new Object[]{id}))
                        .addExpression(resourceType + ".entry[].subject.identifier.value")));
    }

    private void throwIdentifierRequired(ResourceType resourceType) {
        throw new UnprocessableEntityException(fhirContext, new OperationOutcome()
                .addIssue(new OperationOutcomeIssueComponent()
                        .setSeverity(IssueSeverity.ERROR)
                        .setCode(IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.subject.identifierRequired"))
                        .addExpression(resourceType + ".subject.identifier")));
    }

    private void throwEhrNotFound(Resource resource, String patientId) {
        throw new UnprocessableEntityException(fhirContext, new OperationOutcome()
                .addIssue(new OperationOutcomeIssueComponent()
                        .setSeverity(IssueSeverity.ERROR)
                        .setCode(IssueType.VALUE)
                        .setDiagnostics(messages.getMessage("validation.subject.ehrIdNotFound", new Object[]{patientId}))
                        .addExpression(resource.getResourceType() + ".subject.identifier")));
    }

    @Override
    public void setMessageSource(@NonNull MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

}
