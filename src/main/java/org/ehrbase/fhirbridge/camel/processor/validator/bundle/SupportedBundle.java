package org.ehrbase.fhirbridge.camel.processor.validator.bundle;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.ehrbase.client.openehrclient.VersionUid;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;



public abstract class SupportedBundle extends Bundle {
    protected final Bundle bundle;

    protected SupportedBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public abstract MappedBundleComposition processBundle();

    public abstract void createdLog(VersionUid versionUid);

    String getEhrUID() {
        String ehrUID = "";
        for (Bundle.BundleEntryComponent bundleEntry : bundle.getEntry()) {
            checkIfPatientRessourceNotPresent(bundleEntry);
            Observation observation = (Observation) bundleEntry.getResource();
            ehrUID = getEntryEhrUID(ehrUID, observation);

        }
        return ehrUID;
    }

    private String getEntryEhrUID(String ehrUID, Observation observation){
        if (ehrUID.equals("") ||  referencesSameEhrUID(ehrUID, observation)) {
            return observation.getSubject().getReference().split(":")[2];
        }
        throw new InternalErrorException("The subject Ids of the profile within the Bundle reference different Patient. A Blood Gas Panel must refer to one identical Patient!");

    }

    private boolean referencesSameEhrUID(String ehrUID, Observation observation){
        return ehrUID.equals(observation.getSubject().getReference().split(":")[2]);
    }

    private void checkIfPatientRessourceNotPresent(BundleEntryComponent bundle) {
        if (bundle.getResource().getResourceType().toString().equals("Patient")) {
            throw new InternalErrorException("Patient as a resource is not allowed to be contained !");
        }
    }
}