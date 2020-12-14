package org.ehrbase.fhirbridge.ehr.opt.kontaktpersoncovid19composition.definition;

import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.fhirbridge.ehr.opt.shareddefinition.Language;

public class SarsCov2ExpositionEvaluationContainment extends Containment {
  public SelectAqlField<SarsCov2ExpositionEvaluation> SARS_COV2_EXPOSITION_EVALUATION = new AqlFieldImp<SarsCov2ExpositionEvaluation>(SarsCov2ExpositionEvaluation.class, "", "SarsCov2ExpositionEvaluation", SarsCov2ExpositionEvaluation.class, this);

  public SelectAqlField<String> EXPOSITION_DURCH_KONTAKT_MIT_AN_COVID19_ERKRANKTER_PERSON_VALUE = new AqlFieldImp<String>(SarsCov2ExpositionEvaluation.class, "/data[at0001]/items[at0003]/value|value", "expositionDurchKontaktMitAnCovid19ErkrankterPersonValue", String.class, this);

  public ListSelectAqlField<Cluster> ERWEITERUNG = new ListAqlFieldImp<Cluster>(SarsCov2ExpositionEvaluation.class, "/protocol[at0031]/items[at0032]", "erweiterung", Cluster.class, this);

  public SelectAqlField<PartyProxy> SUBJECT = new AqlFieldImp<PartyProxy>(SarsCov2ExpositionEvaluation.class, "/subject", "subject", PartyProxy.class, this);

  public SelectAqlField<String> INFEKTIONSERREGER_VALUE = new AqlFieldImp<String>(SarsCov2ExpositionEvaluation.class, "/data[at0001]/items[at0002]/value|value", "infektionserregerValue", String.class, this);

  public ListSelectAqlField<Cluster> ANGABEN_ZUR_EXPONIERTEN_PERSON = new ListAqlFieldImp<Cluster>(SarsCov2ExpositionEvaluation.class, "/data[at0001]/items[at0017]", "angabenZurExponiertenPerson", Cluster.class, this);

  public SelectAqlField<Language> LANGUAGE = new AqlFieldImp<Language>(SarsCov2ExpositionEvaluation.class, "/language", "language", Language.class, this);

  private SarsCov2ExpositionEvaluationContainment() {
    super("openEHR-EHR-EVALUATION.infectious_exposure.v0");
  }

  public static SarsCov2ExpositionEvaluationContainment getInstance() {
    return new SarsCov2ExpositionEvaluationContainment();
  }
}
