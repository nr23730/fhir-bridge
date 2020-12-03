package org.ehrbase.fhirbridge.fhir.bundle;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.support.OperationOutcomeOperations;

public class CreateBundleAuditStrategy extends FhirAuditStrategy<FhirAuditDataset> {

    public CreateBundleAuditStrategy(boolean serverSide) {
        super(serverSide, OperationOutcomeOperations.INSTANCE);
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, FhirAuditDataset auditDataset) {
        return new AuditMessage[0]; //TODO later on generate/store
    }

    @Override
    public FhirAuditDataset createAuditDataset() {
        return new FhirAuditDataset(isServerSide());
    }
}