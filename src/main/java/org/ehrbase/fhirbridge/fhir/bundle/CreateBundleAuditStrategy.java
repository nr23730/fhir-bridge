package org.ehrbase.fhirbridge.fhir.bundle;

import org.hl7.fhir.r4.model.Observation;
import org.openehealth.ipf.commons.ihe.fhir.audit.GenericFhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.OperationOutcomeOperations;

import java.util.Optional;

public class CreateBundleAuditStrategy extends GenericFhirAuditStrategy<Observation> { //TODO set Bundle

    public CreateBundleAuditStrategy(boolean serverSide) {
        super(serverSide, OperationOutcomeOperations.INSTANCE, bundle -> Optional.empty());
    }
}