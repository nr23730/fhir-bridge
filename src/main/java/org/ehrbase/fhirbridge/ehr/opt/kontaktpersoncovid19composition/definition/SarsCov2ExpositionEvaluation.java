package org.ehrbase.fhirbridge.ehr.opt.kontaktpersoncovid19composition.definition;

import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.PartyProxy;
import java.lang.String;
import java.util.List;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.fhirbridge.ehr.opt.shareddefinition.Language;

@Entity
@Archetype("openEHR-EHR-EVALUATION.infectious_exposure.v0")
public class SarsCov2ExpositionEvaluation {
  @Path("/data[at0001]/items[at0003]/value|value")
  private String expositionDurchKontaktMitAnCovid19ErkrankterPersonValue;

  @Path("/protocol[at0031]/items[at0032]")
  private List<Cluster> erweiterung;

  @Path("/subject")
  private PartyProxy subject;

  @Path("/data[at0001]/items[at0002]/value|value")
  private String infektionserregerValue;

  @Path("/data[at0001]/items[at0017]")
  private List<Cluster> angabenZurExponiertenPerson;

  @Path("/language")
  private Language language;

  public void setExpositionDurchKontaktMitAnCovid19ErkrankterPersonValue(
      String expositionDurchKontaktMitAnCovid19ErkrankterPersonValue) {
     this.expositionDurchKontaktMitAnCovid19ErkrankterPersonValue = expositionDurchKontaktMitAnCovid19ErkrankterPersonValue;
  }

  public String getExpositionDurchKontaktMitAnCovid19ErkrankterPersonValue() {
     return this.expositionDurchKontaktMitAnCovid19ErkrankterPersonValue ;
  }

  public void setErweiterung(List<Cluster> erweiterung) {
     this.erweiterung = erweiterung;
  }

  public List<Cluster> getErweiterung() {
     return this.erweiterung ;
  }

  public void setSubject(PartyProxy subject) {
     this.subject = subject;
  }

  public PartyProxy getSubject() {
     return this.subject ;
  }

  public void setInfektionserregerValue(String infektionserregerValue) {
     this.infektionserregerValue = infektionserregerValue;
  }

  public String getInfektionserregerValue() {
     return this.infektionserregerValue ;
  }

  public void setAngabenZurExponiertenPerson(List<Cluster> angabenZurExponiertenPerson) {
     this.angabenZurExponiertenPerson = angabenZurExponiertenPerson;
  }

  public List<Cluster> getAngabenZurExponiertenPerson() {
     return this.angabenZurExponiertenPerson ;
  }

  public void setLanguage(Language language) {
     this.language = language;
  }

  public Language getLanguage() {
     return this.language ;
  }
}
