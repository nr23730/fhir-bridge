package org.ehrbase.fhirbridge.ehr.converter;

import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.befundderblutgasanalysecomposition.BefundDerBlutgasanalyseComposition;
import org.ehrbase.fhirbridge.ehr.opt.klinischefrailtyskalacomposition.KlinischeFrailtySkalaComposition;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodGasCompositionConverter implements CompositionConverter<BefundDerBlutgasanalyseComposition, Bundle> {

    private static final Logger LOG = LoggerFactory.getLogger(BloodGasCompositionConverter.class);

    @Override
    public Bundle fromComposition(BefundDerBlutgasanalyseComposition composition) {
        System.out.println("TEST!");
        LOG.info("TESTTTTT: created");
        return null;
    }

    @Override
    public BefundDerBlutgasanalyseComposition toComposition(Bundle object) {
        LOG.info("TESTTTTT2: created");
        return null;
    }
}
