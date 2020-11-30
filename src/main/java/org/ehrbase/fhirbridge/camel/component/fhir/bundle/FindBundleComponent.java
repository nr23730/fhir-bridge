package org.ehrbase.fhirbridge.camel.component.fhir.bundle;

import org.ehrbase.fhirbridge.fhir.bundle.FindBundleTransaction;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.custom.CustomFhirComponent;

public class FindBundleComponent extends CustomFhirComponent<FhirQueryAuditDataset> {

    public FindBundleComponent() {
        super(new FindBundleTransaction());
    }
}