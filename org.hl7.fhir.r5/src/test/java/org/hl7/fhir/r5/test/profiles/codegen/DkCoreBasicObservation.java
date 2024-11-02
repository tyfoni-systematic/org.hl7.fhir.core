package org.hl7.fhir.r5.test.profiles.codegen;

import java.util.List;
import java.util.ArrayList;
import javax.annotation.Nullable;
import java.util.Date;


import org.hl7.fhir.r5.context.IWorkerContext;
import org.hl7.fhir.r5.model.*;
import org.hl7.fhir.r5.profilemodel.PEBuilder;
import org.hl7.fhir.r5.profilemodel.PEInstance;
import org.hl7.fhir.r5.profilemodel.PEBuilder.PEElementPropertiesPolicy;
import org.hl7.fhir.r5.profilemodel.gen.PEGeneratedBase;
import org.hl7.fhir.r5.profilemodel.gen.Min;
import org.hl7.fhir.r5.profilemodel.gen.Max;
import org.hl7.fhir.r5.profilemodel.gen.Label;
import org.hl7.fhir.r5.profilemodel.gen.Doco;
import org.hl7.fhir.r5.profilemodel.gen.BindingStrength;
import org.hl7.fhir.r5.profilemodel.gen.ValueSet;
import org.hl7.fhir.r5.profilemodel.gen.MustSupport;
import org.hl7.fhir.r5.profilemodel.gen.Definition;


// Generated by the HAPI Java Profile Generator, 2/11/24, 10:50 pm

/**
 * Measurements and simple assertions made about a patient, device or other 
 * subject.
 *
 */
public class DkCoreBasicObservation extends PEGeneratedBase {

  public static final String CANONICAL_URL = "http://hl7.dk/fhir/core/StructureDefinition/dk-core-basic-observation|3.2.0";

  @Min("0") @Max("1") @Doco("")
  private String id;  // 

  @Min("0") @Max("*") @Doco("Additional content defined by implementations")
  @Definition("May be used to represent additional information that is not part of the basic definition of the resource. To make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.")
  private List<Extension> extensions = new ArrayList<>();  // Additional content defined by implementations

  @Min("0") @Max("*") @Doco("Extensions that cannot be ignored")
  @Definition("May be used to represent additional information that is not part of the basic definition of the resource and that modifies the understanding of the element that contains it and/or the understanding of the containing element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension. Applications processing a resource are required to check for modifier extensions.\n\nModifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot change the meaning of modifierExtension itself).")
  private List<Extension> modifierExtensions = new ArrayList<>();  // Extensions that cannot be ignored

  @Min("0") @Max("*") @Doco("Business Identifier for observation")
  @Definition("A unique identifier assigned to this observation.")
  private List<Identifier> identifiers = new ArrayList<>();  // Business Identifier for observation

  @Min("0") @Max("*") @Doco("Fulfills plan, proposal or order")
  @Definition("A plan, proposal or order that is fulfilled in whole or in part by this event.  For example, a MedicationRequest may require a patient to have laboratory test performed before  it is dispensed.")
  private List<Reference> basedOns = new ArrayList<>();  // Fulfills plan, proposal or order

  @Min("0") @Max("*") @Doco("Part of referenced event")
  @Definition("A larger event of which this particular Observation is a component or step.  For example,  an observation as part of a procedure.")
  private List<Reference> partOfs = new ArrayList<>();  // Part of referenced event

  @Min("1") @Max("1") @Doco("registered | preliminary | final | amended +")
  @BindingStrength("required") @ValueSet("http://hl7.org/fhir/ValueSet/observation-status|4.0.1")
  @Definition("The status of the result value.")
  private String status;// @NotNull  // registered | preliminary | final | amended +

  @Min("1") @Max("1") @Doco("Classification of  type of observation")
  @BindingStrength("preferred") @ValueSet("http://hl7.org/fhir/ValueSet/observation-category")
  @Definition("A code that classifies the general type of observation being made.")
  private CodeableConcept category;// @NotNull  // Classification of  type of observation

  @Min("1") @Max("1") @Doco("Type of observation (code / type)")
  @BindingStrength("example") @ValueSet("http://hl7.org/fhir/ValueSet/observation-codes")
  @Definition("Describes what was observed. Sometimes this is called the observation \"name\".")
  private CodeableConcept code;// @NotNull  // Type of observation (code / type)

  @Min("1") @Max("1") @Doco("Who and/or what the observation is about")
  @Definition("The patient, or group of patients, location, or device this observation is about and into whose record the observation is placed. If the actual focus of the observation is different from the subject (or a sample of, part, or region of the subject), the `focus` element or the `code` itself specifies the actual focus of the observation.")
  private Reference subject;// @NotNull  // Who and/or what the observation is about

  @Min("0") @Max("*") @Doco("What the observation is about, when it is not about the subject of record")
  @Definition("The actual focus of an observation when it is not the patient of record representing something or someone associated with the patient such as a spouse, parent, fetus, or donor. For example, fetus observations in a mother's record.  The focus of an observation could also be an existing condition,  an intervention, the subject's diet,  another observation of the subject,  or a body structure such as tumor or implanted device.   An example use case would be using the Observation resource to capture whether the mother is trained to change her child's tracheostomy tube. In this example, the child is the patient of record and the mother is the focus.")
  private List<Reference> focus = new ArrayList<>();  // What the observation is about, when it is not about the subject of record

  @Min("0") @Max("1") @Doco("Healthcare event during which this observation is made")
  @Definition("The healthcare event  (e.g. a patient and healthcare provider interaction) during which this observation is made.")
  private Reference encounter;  // Healthcare event during which this observation is made

  @Min("0") @Max("*") @Doco("Who is responsible for the observation")
  @Definition("Who was responsible for asserting the observed value as \"true\".")
  private List<Reference> performers = new ArrayList<>();  // Who is responsible for the observation

  @Min("0") @Max("1") @Doco("Actual result")
  @Definition("The information determined as a result of making the observation, if the information has a simple value.")
  private Quantity value;  // Actual result

  @Min("0") @Max("1") @Doco("Why the result is missing")
  @BindingStrength("extensible") @ValueSet("http://hl7.org/fhir/ValueSet/data-absent-reason")
  @Definition("Provides a reason why the expected value in the element Observation.value[x] is missing.")
  private CodeableConcept dataAbsentReason;  // Why the result is missing

  @Min("0") @Max("*") @Doco("Comments about the observation")
  @Definition("Comments about the observation or the results.")
  private List<Annotation> notes = new ArrayList<>();  // Comments about the observation

  @Min("0") @Max("1") @Doco("How it was done")
  @BindingStrength("example") @ValueSet("http://hl7.org/fhir/ValueSet/observation-methods")
  @Definition("Indicates the mechanism used to perform the observation.")
  private CodeableConcept method;  // How it was done

  @Min("0") @Max("1") @Doco("Specimen used for this observation")
  @Definition("The specimen that was used when this observation was made.")
  private Reference specimen;  // Specimen used for this observation

  @Min("0") @Max("1") @Doco("The device used for the measurement. It is recommended that when information about the device is sent, it is contained in the same Bundle as the Observation the device measured.")
  @Definition("The device used to generate the observation data.")
  private Reference device;  // The device used for the measurement. It is recommended that when information about the device is sent, it is contained in the same Bundle as the Observation the device measured.

  @Min("0") @Max("*") @Doco("Provides guide for interpretation")
  @Definition("Guidance on how to interpret the value by comparison to a normal or recommended range.  Multiple reference ranges are interpreted as an \"OR\".   In other words, to represent two distinct target populations, two `referenceRange` elements would be used.")
  private List<BackboneElement> referenceRanges = new ArrayList<>();  // Provides guide for interpretation

  @Min("0") @Max("*") @Doco("Related resource that belongs to the Observation group")
  @Definition("This observation is a group observation (e.g. a battery, a panel of tests, a set of vital sign measurements) that includes the target as a member of the group.")
  private List<Reference> hasMembers = new ArrayList<>();  // Related resource that belongs to the Observation group

  @Min("0") @Max("*") @Doco("Related measurements the observation is made from")
  @Definition("The target resource that represents a measurement from which this observation value is derived. For example, a calculated anion gap or a fetal measurement based on an ultrasound image.")
  private List<Reference> derivedFroms = new ArrayList<>();  // Related measurements the observation is made from

  @Min("0") @Max("*") @Doco("Component results")
  @Definition("Some observations have multiple component observations.  These component observations are expressed as separate code value pairs that share the same attributes.  Examples include systolic and diastolic component observations for blood pressure measurement and multiple component observations for genetics observations.")
  private List<BackboneElement> components = new ArrayList<>();  // Component results


  /**
   * Parameter-less constructor.
   *
   */
  public DkCoreBasicObservation() {
  }

  /**
   * Construct an instance of the object, and fill out all the fixed values 
   *
   */
  public DkCoreBasicObservation(IWorkerContext context) {
    workerContext = context;
    PEBuilder builder = new PEBuilder(context, PEElementPropertiesPolicy.EXTENSION, true);
    PEInstance src = builder.buildPEInstance(CANONICAL_URL, builder.createResource(CANONICAL_URL, false));
    load(src);
  }

  /**
   * Populate an instance of the object based on this source object 
   *
   */
  public static DkCoreBasicObservation fromSource(IWorkerContext context, Observation source) {
    DkCoreBasicObservation theThing = new DkCoreBasicObservation();
    theThing.workerContext = context;
    PEBuilder builder = new PEBuilder(context, PEElementPropertiesPolicy.EXTENSION, true);
    PEInstance src = builder.buildPEInstance(CANONICAL_URL, source);
    theThing.load(src);
    return theThing;
  }


  public void load(PEInstance src) {
    clear();
    if (src.hasChild("id")) {
      id = ((IdType) src.child("id").asDataType()).getValue();
    }
    for (PEInstance item : src.children("extension")) {
      extensions.add((Extension) item.asDataType());
    }
    for (PEInstance item : src.children("modifierExtension")) {
      modifierExtensions.add((Extension) item.asDataType());
    }
    for (PEInstance item : src.children("identifier")) {
      identifiers.add((Identifier) item.asDataType());
    }
    for (PEInstance item : src.children("basedOn")) {
      basedOns.add((Reference) item.asDataType());
    }
    for (PEInstance item : src.children("partOf")) {
      partOfs.add((Reference) item.asDataType());
    }
    if (src.hasChild("status")) {
      status = src.child("status").asDataType().primitiveValue();
    }
    if (src.hasChild("category")) {
      category = (CodeableConcept) src.child("category").asDataType();
    }
    if (src.hasChild("code")) {
      code = (CodeableConcept) src.child("code").asDataType();
    }
    if (src.hasChild("subject")) {
      subject = (Reference) src.child("subject").asDataType();
    }
    for (PEInstance item : src.children("focus")) {
      focus.add((Reference) item.asDataType());
    }
    if (src.hasChild("encounter")) {
      encounter = (Reference) src.child("encounter").asDataType();
    }
    for (PEInstance item : src.children("performer")) {
      performers.add((Reference) item.asDataType());
    }
    if (src.hasChild("value")) {
      value = (Quantity) src.child("value").asDataType();
    }
    if (src.hasChild("dataAbsentReason")) {
      dataAbsentReason = (CodeableConcept) src.child("dataAbsentReason").asDataType();
    }
    for (PEInstance item : src.children("note")) {
      notes.add((Annotation) item.asDataType());
    }
    if (src.hasChild("method")) {
      method = (CodeableConcept) src.child("method").asDataType();
    }
    if (src.hasChild("specimen")) {
      specimen = (Reference) src.child("specimen").asDataType();
    }
    if (src.hasChild("device")) {
      device = (Reference) src.child("device").asDataType();
    }
    for (PEInstance item : src.children("referenceRange")) {
      referenceRanges.add((BackboneElement) item.asElement());
    }
    for (PEInstance item : src.children("hasMember")) {
      hasMembers.add((Reference) item.asDataType());
    }
    for (PEInstance item : src.children("derivedFrom")) {
      derivedFroms.add((Reference) item.asDataType());
    }
    for (PEInstance item : src.children("component")) {
      components.add((BackboneElement) item.asElement());
    }

  }

  /**
   * Build a instance of the underlying object based on this wrapping object 
   *
   */
  public Observation build(IWorkerContext context) {
    workerContext = context;
    return build();
  }

  /**
   * Build a instance of the underlying object based on this wrapping object 
   *
   */
  public Observation build() {
    Observation theThing = new Observation();
    PEBuilder builder = new PEBuilder(workerContext, PEElementPropertiesPolicy.EXTENSION, true);
    PEInstance tgt = builder.buildPEInstance(CANONICAL_URL, theThing);
    save(tgt, false);
    return theThing;
  }

  /**
   * Save this profile class into an existing resource (overwriting anything that 
   * exists in the profile) 
   *
   */
  public void save(IWorkerContext context, Observation dest, boolean nulls) {
    workerContext = context;
    PEBuilder builder = new PEBuilder(context, PEElementPropertiesPolicy.EXTENSION, true);
    PEInstance tgt = builder.buildPEInstance(CANONICAL_URL, dest);
    save(tgt, nulls);
  }

  public void save(PEInstance tgt, boolean nulls) {
    tgt.clear("id");
    if (id != null) {
      tgt.makeChild("id").data().setProperty("value", new IdType(id));
    }
    tgt.clear("extension");
    for (Extension item : extensions) {
      tgt.addChild("extension", item);
    }
    tgt.clear("modifierExtension");
    for (Extension item : modifierExtensions) {
      tgt.addChild("modifierExtension", item);
    }
    tgt.clear("identifier");
    for (Identifier item : identifiers) {
      tgt.addChild("identifier", item);
    }
    tgt.clear("basedOn");
    for (Reference item : basedOns) {
      tgt.addChild("basedOn", item);
    }
    tgt.clear("partOf");
    for (Reference item : partOfs) {
      tgt.addChild("partOf", item);
    }
    tgt.clear("status");
    if (status != null) {
      tgt.makeChild("status").data().setProperty("value", new CodeType(status));
    }
    tgt.clear("category");
    if (category != null) {
      tgt.addChild("category", category);
    }
    tgt.clear("code");
    if (code != null) {
      tgt.addChild("code", code);
    }
    tgt.clear("subject");
    if (subject != null) {
      tgt.addChild("subject", subject);
    }
    tgt.clear("focus");
    for (Reference item : focus) {
      tgt.addChild("focus", item);
    }
    tgt.clear("encounter");
    if (encounter != null) {
      tgt.addChild("encounter", encounter);
    }
    tgt.clear("performer");
    for (Reference item : performers) {
      tgt.addChild("performer", item);
    }
    tgt.clear("value");
    if (value != null) {
      tgt.addChild("value", value);
    }
    tgt.clear("dataAbsentReason");
    if (dataAbsentReason != null) {
      tgt.addChild("dataAbsentReason", dataAbsentReason);
    }
    tgt.clear("note");
    for (Annotation item : notes) {
      tgt.addChild("note", item);
    }
    tgt.clear("method");
    if (method != null) {
      tgt.addChild("method", method);
    }
    tgt.clear("specimen");
    if (specimen != null) {
      tgt.addChild("specimen", specimen);
    }
    tgt.clear("device");
    if (device != null) {
      tgt.addChild("device", device);
    }
    tgt.clear("referenceRange");
    for (BackboneElement item : referenceRanges) {
      tgt.addChild("referenceRange", item);
    }
    tgt.clear("hasMember");
    for (Reference item : hasMembers) {
      tgt.addChild("hasMember", item);
    }
    tgt.clear("derivedFrom");
    for (Reference item : derivedFroms) {
      tgt.addChild("derivedFrom", item);
    }
    tgt.clear("component");
    for (BackboneElement item : components) {
      tgt.addChild("component", item);
    }

  }

  public String getId() {
    return id;
  }

  public DkCoreBasicObservation setId(String value) {
    this.id = value;
    return this;
  }

  public boolean hasId() {
    return id != null;
  }

  /**
   * May be used to represent additional information that is not part of the basic 
   * definition of the resource. To make the use of extensions safe and manageable, 
   * there is a strict set of governance  applied to the definition and use of 
   * extensions. Though any implementer can define an extension, there is a set of 
   * requirements that SHALL be met as part of the definition of the extension.
   *
   */
  public List<Extension> getExtensions() {
    if (extensions == null) { extensions = new ArrayList<>(); }
    return extensions;
  }

  public boolean hasExtensions() {
    return extensions != null && !extensions.isEmpty();
  }

  public Extension addExtension() {
    Extension theThing = new Extension();
    getExtensions().add(theThing);
    return theThing;
  }

  public boolean hasExtension(Extension item) {
    return hasExtensions() && extensions.contains(item);
  }

  public void removeExtension(Extension item) {
    if (hasExtension(item)) {
      extensions.remove(item);
    }
  }


  /**
   * May be used to represent additional information that is not part of the basic 
   * definition of the resource and that modifies the understanding of the element 
   * that contains it and/or the understanding of the containing element's 
   * descendants. Usually modifier elements provide negation or qualification. To 
   * make the use of extensions safe and manageable, there is a strict set of 
   * governance applied to the definition and use of extensions. Though any 
   * implementer is allowed to define an extension, there is a set of requirements 
   * that SHALL be met as part of the definition of the extension. Applications 
   * processing a resource are required to check for modifier extensions.
   *
   * 
   *
   * Modifier extensions SHALL NOT change the meaning of any elements on Resource or 
   * DomainResource (including cannot change the meaning of modifierExtension 
   * itself).
   *
   */
  public List<Extension> getModifierExtensions() {
    if (modifierExtensions == null) { modifierExtensions = new ArrayList<>(); }
    return modifierExtensions;
  }

  public boolean hasModifierExtensions() {
    return modifierExtensions != null && !modifierExtensions.isEmpty();
  }

  public Extension addModifierExtension() {
    Extension theThing = new Extension();
    getModifierExtensions().add(theThing);
    return theThing;
  }

  public boolean hasModifierExtension(Extension item) {
    return hasModifierExtensions() && modifierExtensions.contains(item);
  }

  public void removeModifierExtension(Extension item) {
    if (hasModifierExtension(item)) {
      modifierExtensions.remove(item);
    }
  }


  /**
   * A unique identifier assigned to this observation.
   *
   */
  public List<Identifier> getIdentifiers() {
    if (identifiers == null) { identifiers = new ArrayList<>(); }
    return identifiers;
  }

  public boolean hasIdentifiers() {
    return identifiers != null && !identifiers.isEmpty();
  }

  public Identifier addIdentifier() {
    Identifier theThing = new Identifier();
    getIdentifiers().add(theThing);
    return theThing;
  }

  public boolean hasIdentifier(Identifier item) {
    return hasIdentifiers() && identifiers.contains(item);
  }

  public void removeIdentifier(Identifier item) {
    if (hasIdentifier(item)) {
      identifiers.remove(item);
    }
  }


  /**
   * A plan, proposal or order that is fulfilled in whole or in part by this event.  
   * For example, a MedicationRequest may require a patient to have laboratory test 
   * performed before  it is dispensed.
   *
   */
  public List<Reference> getBasedOns() {
    if (basedOns == null) { basedOns = new ArrayList<>(); }
    return basedOns;
  }

  public boolean hasBasedOns() {
    return basedOns != null && !basedOns.isEmpty();
  }

  public Reference addBasedOn() {
    Reference theThing = new Reference();
    getBasedOns().add(theThing);
    return theThing;
  }

  public boolean hasBasedOn(Reference item) {
    return hasBasedOns() && basedOns.contains(item);
  }

  public void removeBasedOn(Reference item) {
    if (hasBasedOn(item)) {
      basedOns.remove(item);
    }
  }


  /**
   * A larger event of which this particular Observation is a component or step.  For 
   * example,  an observation as part of a procedure.
   *
   */
  public List<Reference> getPartOfs() {
    if (partOfs == null) { partOfs = new ArrayList<>(); }
    return partOfs;
  }

  public boolean hasPartOfs() {
    return partOfs != null && !partOfs.isEmpty();
  }

  public Reference addPartOf() {
    Reference theThing = new Reference();
    getPartOfs().add(theThing);
    return theThing;
  }

  public boolean hasPartOf(Reference item) {
    return hasPartOfs() && partOfs.contains(item);
  }

  public void removePartOf(Reference item) {
    if (hasPartOf(item)) {
      partOfs.remove(item);
    }
  }


  /**
   * The status of the result value.
   *
   */
  public String getStatus() {
    return status;
  }

  public DkCoreBasicObservation setStatus(String value) {
    this.status = value;
    return this;
  }

  public boolean hasStatus() {
    return status != null;
  }

  /**
   * A code that classifies the general type of observation being made.
   *
   */
  public CodeableConcept getCategory() {
    if (category == null) { category = new CodeableConcept(); }
    return category;
  }

  public boolean hasCategory() {
    return category != null;
  }

  /**
   * Describes what was observed. Sometimes this is called the observation "name".
   *
   */
  public CodeableConcept getCode() {
    if (code == null) { code = new CodeableConcept(); }
    return code;
  }

  public DkCoreBasicObservation setCode(CodeableConcept value) {
    this.code = value;
    return this;
  }
  public boolean hasCode() {
    return code != null;
  }

  /**
   * The patient, or group of patients, location, or device this observation is about 
   * and into whose record the observation is placed. If the actual focus of the 
   * observation is different from the subject (or a sample of, part, or region of 
   * the subject), the `focus` element or the `code` itself specifies the actual 
   * focus of the observation.
   *
   */
  public Reference getSubject() {
    if (subject == null) { subject = new Reference(); }
    return subject;
  }

  public DkCoreBasicObservation setSubject(Reference value) {
    this.subject = value;
    return this;
  }
  public boolean hasSubject() {
    return subject != null;
  }

  /**
   * The actual focus of an observation when it is not the patient of record 
   * representing something or someone associated with the patient such as a spouse, 
   * parent, fetus, or donor. For example, fetus observations in a mother's record.  
   * The focus of an observation could also be an existing condition,  an 
   * intervention, the subject's diet,  another observation of the subject,  or a 
   * body structure such as tumor or implanted device.   An example use case would be 
   * using the Observation resource to capture whether the mother is trained to 
   * change her child's tracheostomy tube. In this example, the child is the patient 
   * of record and the mother is the focus.
   *
   */
  public List<Reference> getFocus() {
    if (focus == null) { focus = new ArrayList<>(); }
    return focus;
  }

  public boolean hasFocus() {
    return focus != null && !focus.isEmpty();
  }

  public Reference addFocus() {
    Reference theThing = new Reference();
    getFocus().add(theThing);
    return theThing;
  }

  public boolean hasFocus(Reference item) {
    return hasFocus() && focus.contains(item);
  }

  public void removeFocus(Reference item) {
    if (hasFocus(item)) {
      focus.remove(item);
    }
  }


  /**
   * The healthcare event  (e.g. a patient and healthcare provider interaction) 
   * during which this observation is made.
   *
   */
  public Reference getEncounter() {
    if (encounter == null) { encounter = new Reference(); }
    return encounter;
  }

  public DkCoreBasicObservation setEncounter(Reference value) {
    this.encounter = value;
    return this;
  }
  public boolean hasEncounter() {
    return encounter != null;
  }

  /**
   * Who was responsible for asserting the observed value as "true".
   *
   */
  public List<Reference> getPerformers() {
    if (performers == null) { performers = new ArrayList<>(); }
    return performers;
  }

  public boolean hasPerformers() {
    return performers != null && !performers.isEmpty();
  }

  public Reference addPerformer() {
    Reference theThing = new Reference();
    getPerformers().add(theThing);
    return theThing;
  }

  public boolean hasPerformer(Reference item) {
    return hasPerformers() && performers.contains(item);
  }

  public void removePerformer(Reference item) {
    if (hasPerformer(item)) {
      performers.remove(item);
    }
  }


  /**
   * The information determined as a result of making the observation, if the 
   * information has a simple value.
   *
   */
  public Quantity getValue() {
    if (value == null) { value = new Quantity(); }
    return value;
  }

  public DkCoreBasicObservation setValue(Quantity value) {
    this.value = value;
    return this;
  }
  public boolean hasValue() {
    return value != null;
  }

  /**
   * Provides a reason why the expected value in the element Observation.value[x] is 
   * missing.
   *
   */
  public CodeableConcept getDataAbsentReason() {
    if (dataAbsentReason == null) { dataAbsentReason = new CodeableConcept(); }
    return dataAbsentReason;
  }

  public DkCoreBasicObservation setDataAbsentReason(CodeableConcept value) {
    this.dataAbsentReason = value;
    return this;
  }
  public boolean hasDataAbsentReason() {
    return dataAbsentReason != null;
  }

  /**
   * Comments about the observation or the results.
   *
   */
  public List<Annotation> getNotes() {
    if (notes == null) { notes = new ArrayList<>(); }
    return notes;
  }

  public boolean hasNotes() {
    return notes != null && !notes.isEmpty();
  }

  public Annotation addNote() {
    Annotation theThing = new Annotation();
    getNotes().add(theThing);
    return theThing;
  }

  public boolean hasNote(Annotation item) {
    return hasNotes() && notes.contains(item);
  }

  public void removeNote(Annotation item) {
    if (hasNote(item)) {
      notes.remove(item);
    }
  }


  /**
   * Indicates the mechanism used to perform the observation.
   *
   */
  public CodeableConcept getMethod() {
    if (method == null) { method = new CodeableConcept(); }
    return method;
  }

  public DkCoreBasicObservation setMethod(CodeableConcept value) {
    this.method = value;
    return this;
  }
  public boolean hasMethod() {
    return method != null;
  }

  /**
   * The specimen that was used when this observation was made.
   *
   */
  public Reference getSpecimen() {
    if (specimen == null) { specimen = new Reference(); }
    return specimen;
  }

  public DkCoreBasicObservation setSpecimen(Reference value) {
    this.specimen = value;
    return this;
  }
  public boolean hasSpecimen() {
    return specimen != null;
  }

  /**
   * The device used to generate the observation data.
   *
   */
  public Reference getDevice() {
    if (device == null) { device = new Reference(); }
    return device;
  }

  public DkCoreBasicObservation setDevice(Reference value) {
    this.device = value;
    return this;
  }
  public boolean hasDevice() {
    return device != null;
  }

  /**
   * Guidance on how to interpret the value by comparison to a normal or recommended 
   * range.  Multiple reference ranges are interpreted as an "OR".   In other words, 
   * to represent two distinct target populations, two `referenceRange` elements 
   * would be used.
   *
   */
  public List<BackboneElement> getReferenceRanges() {
    if (referenceRanges == null) { referenceRanges = new ArrayList<>(); }
    return referenceRanges;
  }

  public boolean hasReferenceRanges() {
    return referenceRanges != null && !referenceRanges.isEmpty();
  }

  public boolean hasReferenceRange(BackboneElement item) {
    return hasReferenceRanges() && referenceRanges.contains(item);
  }

  public void removeReferenceRange(BackboneElement item) {
    if (hasReferenceRange(item)) {
      referenceRanges.remove(item);
    }
  }


  /**
   * This observation is a group observation (e.g. a battery, a panel of tests, a set 
   * of vital sign measurements) that includes the target as a member of the group.
   *
   */
  public List<Reference> getHasMembers() {
    if (hasMembers == null) { hasMembers = new ArrayList<>(); }
    return hasMembers;
  }

  public boolean hasHasMembers() {
    return hasMembers != null && !hasMembers.isEmpty();
  }

  public Reference addHasMember() {
    Reference theThing = new Reference();
    getHasMembers().add(theThing);
    return theThing;
  }

  public boolean hasHasMember(Reference item) {
    return hasHasMembers() && hasMembers.contains(item);
  }

  public void removeHasMember(Reference item) {
    if (hasHasMember(item)) {
      hasMembers.remove(item);
    }
  }


  /**
   * The target resource that represents a measurement from which this observation 
   * value is derived. For example, a calculated anion gap or a fetal measurement 
   * based on an ultrasound image.
   *
   */
  public List<Reference> getDerivedFroms() {
    if (derivedFroms == null) { derivedFroms = new ArrayList<>(); }
    return derivedFroms;
  }

  public boolean hasDerivedFroms() {
    return derivedFroms != null && !derivedFroms.isEmpty();
  }

  public Reference addDerivedFrom() {
    Reference theThing = new Reference();
    getDerivedFroms().add(theThing);
    return theThing;
  }

  public boolean hasDerivedFrom(Reference item) {
    return hasDerivedFroms() && derivedFroms.contains(item);
  }

  public void removeDerivedFrom(Reference item) {
    if (hasDerivedFrom(item)) {
      derivedFroms.remove(item);
    }
  }


  /**
   * Some observations have multiple component observations.  These component 
   * observations are expressed as separate code value pairs that share the same 
   * attributes.  Examples include systolic and diastolic component observations for 
   * blood pressure measurement and multiple component observations for genetics 
   * observations.
   *
   */
  public List<BackboneElement> getComponents() {
    if (components == null) { components = new ArrayList<>(); }
    return components;
  }

  public boolean hasComponents() {
    return components != null && !components.isEmpty();
  }

  public boolean hasComponent(BackboneElement item) {
    return hasComponents() && components.contains(item);
  }

  public void removeComponent(BackboneElement item) {
    if (hasComponent(item)) {
      components.remove(item);
    }
  }




  public void clear() {
    id = null;
    extensions.clear();
    modifierExtensions.clear();
    identifiers.clear();
    basedOns.clear();
    partOfs.clear();
    status = null;
    category = null;
    code = null;
    subject = null;
    focus.clear();
    encounter = null;
    performers.clear();
    value = null;
    dataAbsentReason = null;
    notes.clear();
    method = null;
    specimen = null;
    device = null;
    referenceRanges.clear();
    hasMembers.clear();
    derivedFroms.clear();
    components.clear();

  }

}
