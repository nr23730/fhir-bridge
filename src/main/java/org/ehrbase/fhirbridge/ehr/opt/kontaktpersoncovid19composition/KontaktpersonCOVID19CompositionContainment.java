package org.ehrbase.fhirbridge.ehr.opt.kontaktpersoncovid19composition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.Participation;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.time.temporal.TemporalAccessor;
import org.ehrbase.client.aql.containment.Containment;
import org.ehrbase.client.aql.field.AqlFieldImp;
import org.ehrbase.client.aql.field.ListAqlFieldImp;
import org.ehrbase.client.aql.field.ListSelectAqlField;
import org.ehrbase.client.aql.field.SelectAqlField;
import org.ehrbase.fhirbridge.ehr.opt.kontaktpersoncovid19composition.definition.SarsCov2ExpositionEvaluation;
import org.ehrbase.fhirbridge.ehr.opt.kontaktpersoncovid19composition.definition.StatusDefiningcode;
import org.ehrbase.fhirbridge.ehr.opt.shareddefinition.CategoryDefiningcode;
import org.ehrbase.fhirbridge.ehr.opt.shareddefinition.Language;
import org.ehrbase.fhirbridge.ehr.opt.shareddefinition.SettingDefiningcode;
import org.ehrbase.fhirbridge.ehr.opt.shareddefinition.Territory;

public class KontaktpersonCOVID19CompositionContainment extends Containment {
  public SelectAqlField<KontaktpersonCOVID19Composition> KONTAKTPERSON_C_O_V_ID19_COMPOSITION = new AqlFieldImp<KontaktpersonCOVID19Composition>(KontaktpersonCOVID19Composition.class, "", "KontaktpersonCOVID19Composition", KontaktpersonCOVID19Composition.class, this);

  public SelectAqlField<TemporalAccessor> END_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(KontaktpersonCOVID19Composition.class, "/context/end_time|value", "endTimeValue", TemporalAccessor.class, this);

  public ListSelectAqlField<Participation> PARTICIPATIONS = new ListAqlFieldImp<Participation>(KontaktpersonCOVID19Composition.class, "/context/participations", "participations", Participation.class, this);

  public SelectAqlField<Language> LANGUAGE = new AqlFieldImp<Language>(KontaktpersonCOVID19Composition.class, "/language", "language", Language.class, this);

  public SelectAqlField<PartyIdentified> HEALTH_CARE_FACILITY = new AqlFieldImp<PartyIdentified>(KontaktpersonCOVID19Composition.class, "/context/health_care_facility", "healthCareFacility", PartyIdentified.class, this);

  public SelectAqlField<StatusDefiningcode> STATUS_DEFININGCODE = new AqlFieldImp<StatusDefiningcode>(KontaktpersonCOVID19Composition.class, "/context/other_context[at0001]/items[at0004]/value|defining_code", "statusDefiningcode", StatusDefiningcode.class, this);

  public SelectAqlField<String> KATEGORIE_VALUE = new AqlFieldImp<String>(KontaktpersonCOVID19Composition.class, "/context/other_context[at0001]/items[at0005]/value|value", "kategorieValue", String.class, this);

  public SelectAqlField<Territory> TERRITORY = new AqlFieldImp<Territory>(KontaktpersonCOVID19Composition.class, "/territory", "territory", Territory.class, this);

  public SelectAqlField<TemporalAccessor> START_TIME_VALUE = new AqlFieldImp<TemporalAccessor>(KontaktpersonCOVID19Composition.class, "/context/start_time|value", "startTimeValue", TemporalAccessor.class, this);

  public SelectAqlField<PartyProxy> COMPOSER = new AqlFieldImp<PartyProxy>(KontaktpersonCOVID19Composition.class, "/composer", "composer", PartyProxy.class, this);

  public SelectAqlField<SettingDefiningcode> SETTING_DEFININGCODE = new AqlFieldImp<SettingDefiningcode>(KontaktpersonCOVID19Composition.class, "/context/setting|defining_code", "settingDefiningcode", SettingDefiningcode.class, this);

  public SelectAqlField<FeederAudit> FEEDER_AUDIT = new AqlFieldImp<FeederAudit>(KontaktpersonCOVID19Composition.class, "/feeder_audit", "feederAudit", FeederAudit.class, this);

  public SelectAqlField<SarsCov2ExpositionEvaluation> SARS_COV2_EXPOSITION = new AqlFieldImp<SarsCov2ExpositionEvaluation>(KontaktpersonCOVID19Composition.class, "/content[openEHR-EHR-EVALUATION.infectious_exposure.v0 and name/value='SARS-CoV-2 Exposition']", "sarsCov2Exposition", SarsCov2ExpositionEvaluation.class, this);

  public SelectAqlField<String> LOCATION = new AqlFieldImp<String>(KontaktpersonCOVID19Composition.class, "/context/location", "location", String.class, this);

  public SelectAqlField<CategoryDefiningcode> CATEGORY_DEFININGCODE = new AqlFieldImp<CategoryDefiningcode>(KontaktpersonCOVID19Composition.class, "/category|defining_code", "categoryDefiningcode", CategoryDefiningcode.class, this);

  public ListSelectAqlField<Cluster> ERWEITERUNG = new ListAqlFieldImp<Cluster>(KontaktpersonCOVID19Composition.class, "/context/other_context[at0001]/items[at0002]", "erweiterung", Cluster.class, this);

  private KontaktpersonCOVID19CompositionContainment() {
    super("openEHR-EHR-COMPOSITION.registereintrag.v1");
  }

  public static KontaktpersonCOVID19CompositionContainment getInstance() {
    return new KontaktpersonCOVID19CompositionContainment();
  }
}
