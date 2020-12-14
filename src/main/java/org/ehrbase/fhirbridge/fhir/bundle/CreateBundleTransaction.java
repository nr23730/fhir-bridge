package org.ehrbase.fhirbridge.fhir.bundle;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.support.BatchTransactionResourceProvider;

public class CreateBundleTransaction extends FhirTransactionConfiguration<FhirAuditDataset> {

    public CreateBundleTransaction() {
        super("fhir-create-bundle",
                "Create Bundle",
                false,
                new CreateBundleAuditStrategy(false),
                new CreateBundleAuditStrategy(true),
                FhirVersionEnum.R4,
              //  BatchTransactionResourceProvider.getInstance(),
                new CreateBundleProvider(),
                null,
                FhirTransactionValidator.NO_VALIDATION);
    }
}