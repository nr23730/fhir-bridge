package org.ehrbase.fhirbridge.fhir.bundle;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Bundle;
import org.openehealth.ipf.commons.ihe.fhir.AbstractPlainProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateBundleProvider extends AbstractPlainProvider {

    @Create
    public MethodOutcome create(@ResourceParam Bundle bundle, RequestDetails requestDetails,
                                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return requestAction(bundle, null, httpServletRequest, httpServletResponse, requestDetails);
    }
}
