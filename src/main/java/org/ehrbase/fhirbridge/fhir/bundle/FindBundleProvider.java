package org.ehrbase.fhirbridge.fhir.bundle;

import ca.uhn.fhir.rest.annotation.Search;
import org.hl7.fhir.r4.model.Procedure;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import java.util.List;

public class FindBundleProvider extends AbstractPlainProvider {
    //TODO
    @Search
    public List<Procedure> search(){
        return null;
    }
}
