package org.ehrbase.fhirbridge.ehr.converter;

import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.klinischefrailtyskalacomposition.KlinischeFrailtySkalaComposition;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodGasCompositionConverter implements CompositionConverter<KlinischeFrailtySkalaComposition, Bundle> {

    private static final Logger LOG = LoggerFactory.getLogger(BloodGasCompositionConverter.class);

    @Override
    public Bundle fromComposition(KlinischeFrailtySkalaComposition composition) {
        LOG.info("TESTTTTT: created");
        return null;
    }

    @Override
    public KlinischeFrailtySkalaComposition toComposition(Bundle object) {
        LOG.info("TESTTTTT2: created");
        return null;
    }
}
