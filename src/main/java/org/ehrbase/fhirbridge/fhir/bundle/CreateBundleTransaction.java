package org.ehrbase.fhirbridge.fhir.bundle;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.GenericFhirAuditDataset;

public class CreateBundleTransaction extends FhirTransactionConfiguration<GenericFhirAuditDataset> {

    public CreateBundleTransaction() {
        super("fhir-create-condition",
                "Create Condition",
                false,
                new CreateBundleAuditStrategy(false),
                new CreateBundleAuditStrategy(true),
                FhirVersionEnum.R4,
                new CreateBundleProvider(),
                null,
                FhirTransactionValidator.NO_VALIDATION);
    }
}