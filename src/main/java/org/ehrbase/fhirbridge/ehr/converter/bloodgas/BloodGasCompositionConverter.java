package org.ehrbase.fhirbridge.ehr.converter.bloodgas;

import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.bundle.BloodGasPanel;
import org.ehrbase.fhirbridge.ehr.opt.befundderblutgasanalysecomposition.BefundDerBlutgasanalyseComposition;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodGasCompositionConverter implements CompositionConverter<BefundDerBlutgasanalyseComposition, Bundle> {

    private static final Logger LOG = LoggerFactory.getLogger(BloodGasCompositionConverter.class);

    @Override
    public Bundle fromComposition(BefundDerBlutgasanalyseComposition composition) {

        return new Bundle();
    }

    @Override
    public BefundDerBlutgasanalyseComposition toComposition(Bundle bundle) {
        BloodGasPanel bloodGasPanel = new BloodGasPanel(bundle);
        BlutgasAnalyseConverter blutgasAnalyseMapper = new BlutgasAnalyseConverter();
        BefundDerBlutgasanalyseComposition befundDerBlutgasanalyseComposition = blutgasAnalyseMapper.convert(bloodGasPanel);
        return befundDerBlutgasanalyseComposition;
    }
}

