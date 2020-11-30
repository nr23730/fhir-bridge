package org.ehrbase.fhirbridge.camel.component.fhir.bundle;

import org.ehrbase.fhirbridge.fhir.bundle.CreateBundleTransaction;
import org.openehealth.ipf.commons.ihe.fhir.audit.GenericFhirAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.custom.CustomFhirComponent;

public class CreateBundleComponent extends CustomFhirComponent<GenericFhirAuditDataset>{
    public CreateBundleComponent() {
        super(new CreateBundleTransaction());
    }
}
