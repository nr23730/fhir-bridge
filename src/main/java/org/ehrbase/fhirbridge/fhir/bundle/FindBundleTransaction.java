package org.ehrbase.fhirbridge.fhir.bundle;

import ca.uhn.fhir.context.FhirVersionEnum;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionValidator;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

public class FindBundleTransaction extends FhirTransactionConfiguration<FhirQueryAuditDataset> {
    public FindBundleTransaction(){
        super("fhir-find-bundle",
                "Search Bundle",
                true,
                new FindBundleAuditStrategy(false),
                new FindBundleAuditStrategy(true),
                FhirVersionEnum.R4,
                new FindBundleProvider(),
                null,
                FhirTransactionValidator.NO_VALIDATION);
            }
}
