package org.ehrbase.fhirbridge.fhir.Bundle;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.ehrbase.fhirbridge.fhir.AbstractSetupIT;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class BloodGasIT extends AbstractSetupIT {

    @Test
    public void createBloodGasPanel() throws IOException {
        MethodOutcome outcome = createAndGetOutcome("Bundle/BloodGas/BloodGas.json", "Bundle");
        Assertions.assertEquals(true, outcome.getCreated());
        Assertions.assertTrue(outcome.getResource() instanceof Bundle);
        Assertions.assertNotNull(outcome.getResource());
        Assertions.assertEquals("1", outcome.getResource().getMeta().getVersionId());
    }


    @Test
    public void createBloodGasPanelOnlyPH() throws IOException {
        MethodOutcome outcome = createAndGetOutcome("Bundle/BloodGas/OnlyWithPH.json", "Bundle");
        Assertions.assertEquals(true, outcome.getCreated());
        Assertions.assertTrue(outcome.getResource() instanceof Bundle);
        Assertions.assertNotNull(outcome.getResource());
        Assertions.assertEquals("1", outcome.getResource().getMeta().getVersionId());
    }


    @Test
    public void createBloodGasPanelWrongSubjectId()  {
        InternalErrorException exception = Assertions.assertThrows(InternalErrorException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/WrongSubjectIds.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "The subject Ids of the profile within the Bundle reference different Patient. A Blood Gas Panel must refer to one identical Patient!");
    }

    @Test
    public void createBloodGasPanelIncludingInvalidProfile() {
        InternalErrorException exception = Assertions.assertThrows(InternalErrorException.class,
                () -> createAndGetOutcome(
                        "Bundle/BloodGas/AdditionalInvalidProfile.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "Blood gas panel bundle needs to contain only the profiles for the blood gas panel. Please delete profile https://www.netzwerk-universitaetsmedizin.de/fhir/StructureDefinition/inhaled-oxygen-concentration from the Bundle.");
    }

    @Test
    public void createBloodGasWithoutBloodGasPanelProfile() {
        UnprocessableEntityException exception = Assertions.assertThrows(UnprocessableEntityException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/NoBloodGasPanelProfile.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "The Bundle provided is not supported. Supported is currently only a Bundle containing the profiles for Blood Gas Panel");
    }

    @Test
    public void createBloodGasWithMissingProfiles() {
        UnprocessableEntityException exception = Assertions.assertThrows(UnprocessableEntityException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/MissingProfiles.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "Bundle Blood gas panel needs to contain at least one of the following profiles: oxygen partial pressure, carbon dioxide partial pressure, ph or oxygen saturation");
    }

    @Test
    public void createBloodGasWithDuplicatedProfile() {
        InternalErrorException exception = Assertions.assertThrows(InternalErrorException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/DuplicateProfile.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "Oxygen partial pressure profile is duplicated within the bundle, please delete one of them");
    }


    @Test
    public void createBloodGasWithDuplicatedProfile2() {
        InternalErrorException exception = Assertions.assertThrows(InternalErrorException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/DuplicateProfile2.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "Oxygen saturation profile is duplicated within the bundle, please delete one of them");
    }

    @Test
    public void createBloodGasWithWrongMembers() throws IOException {
        UnprocessableEntityException exception = Assertions.assertThrows(UnprocessableEntityException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/WrongMembers.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "BloodgasPanel references a set of Fhir resources as members, that need to be contained in this bundle. Nevertheless the id Observation/63bd2c95-baa5-4a9a-8075-80f8c22a38ff is missing.");
    }

    @Test
    public void createBloodGasWithToMuchMembers() throws IOException {
        UnprocessableEntityException exception = Assertions.assertThrows(UnprocessableEntityException.class,
                () -> createAndGetOutcome("Bundle/BloodGas/ToMuchMembers.json", "Bundle"));

        Assertions.assertEquals(OperationOutcomeUtil.getFirstIssueDetails(context, exception.getOperationOutcome()), "BloodgasPanel contains references to a resource/s that is not contained within this bundle, please check the hasMembers within the blood gas panel resource to match the amount and value of the resources contained in the bundle.");
    }

}

