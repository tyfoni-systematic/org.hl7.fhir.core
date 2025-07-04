package org.hl7.fhir.r4.conformance;

/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
    
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
 */

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r4.conformance.ProfileUtilities.ProfileKnowledgeProvider.BindingResolution;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.context.IWorkerContext.ValidationResult;
import org.hl7.fhir.r4.elementmodel.ObjectConverter;
import org.hl7.fhir.r4.elementmodel.Property;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Element;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.AggregationMode;
import org.hl7.fhir.r4.model.ElementDefinition.DiscriminatorType;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBaseComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionExampleComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionSlicingComponent;
import org.hl7.fhir.r4.model.ElementDefinition.ElementDefinitionSlicingDiscriminatorComponent;
import org.hl7.fhir.r4.model.ElementDefinition.PropertyRepresentation;
import org.hl7.fhir.r4.model.ElementDefinition.SlicingRules;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Enumeration;
import org.hl7.fhir.r4.model.Enumerations.BindingStrength;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.ExtensionContextType;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionContextComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionDifferentialComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionMappingComponent;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionSnapshotComponent;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4.model.Type;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.r4.utils.NarrativeGenerator;
import org.hl7.fhir.r4.utils.ToolingExtensions;
import org.hl7.fhir.r4.utils.TranslatingUtilities;
import org.hl7.fhir.r4.utils.formats.CSVWriter;
import org.hl7.fhir.r4.utils.formats.XLSXWriter;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.FhirPublication;
import org.hl7.fhir.utilities.MarkedToMoveToAdjunctPackage;
import org.hl7.fhir.utilities.TerminologyServiceOptions;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.VersionUtilities;
import org.hl7.fhir.utilities.i18n.RenderingI18nContext;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.Source;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Cell;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Piece;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Row;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.TableGenerationMode;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.TableModel;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.hl7.fhir.utilities.xml.SchematronWriter;
import org.hl7.fhir.utilities.xml.SchematronWriter.Rule;
import org.hl7.fhir.utilities.xml.SchematronWriter.SchematronType;
import org.hl7.fhir.utilities.xml.SchematronWriter.Section;

/**
 * This class provides a set of utility operations for working with Profiles.
 * Key functionality: * getChildMap --? * getChildList * generateSnapshot: Given
 * a base (snapshot) profile structure, and a differential profile, generate a
 * new snapshot profile * closeDifferential: fill out a differential by
 * excluding anything not mentioned * generateExtensionsTable: generate the HTML
 * for a hierarchical table presentation of the extensions * generateTable:
 * generate the HTML for a hierarchical table presentation of a structure *
 * generateSpanningTable: generate the HTML for a table presentation of a
 * network of structures, starting at a nominated point * summarize: describe
 * the contents of a profile
 * 
 * note to maintainers: Do not make modifications to the snapshot generation
 * without first changing the snapshot generation test cases to demonstrate the
 * grounds for your change
 * 
 * @author Grahame
 *
 */
@MarkedToMoveToAdjunctPackage
@Slf4j
public class ProfileUtilities extends TranslatingUtilities {

  public class ElementRedirection {

    private String path;
    private ElementDefinition element;

    public ElementRedirection(ElementDefinition element, String path) {
      this.path = path;
      this.element = element;
    }

    public ElementDefinition getElement() {
      return element;
    }

    @Override
    public String toString() {
      return element.toString() + " : " + path;
    }

    public String getPath() {
      return path;
    }

  }

  public class TypeSlice {
    private ElementDefinition defn;
    private String type;

    public TypeSlice(ElementDefinition defn, String type) {
      super();
      this.defn = defn;
      this.type = type;
    }

    public ElementDefinition getDefn() {
      return defn;
    }

    public String getType() {
      return type;
    }

  }

  private static final int MAX_RECURSION_LIMIT = 10;

  public class ExtensionContext {

    private ElementDefinition element;
    private StructureDefinition defn;

    public ExtensionContext(StructureDefinition ext, ElementDefinition ed) {
      this.defn = ext;
      this.element = ed;
    }

    public ElementDefinition getElement() {
      return element;
    }

    public StructureDefinition getDefn() {
      return defn;
    }

    public String getUrl() {
      if (element == defn.getSnapshot().getElement().get(0))
        return defn.getUrl();
      else
        return element.getSliceName();
    }

    public ElementDefinition getExtensionValueDefinition() {
      int i = defn.getSnapshot().getElement().indexOf(element) + 1;
      while (i < defn.getSnapshot().getElement().size()) {
        ElementDefinition ed = defn.getSnapshot().getElement().get(i);
        if (ed.getPath().equals(element.getPath()))
          return null;
        if (ed.getPath().startsWith(element.getPath() + ".value"))
          return ed;
        i++;
      }
      return null;
    }
  }

  private static final String ROW_COLOR_ERROR = "#ffcccc";
  private static final String ROW_COLOR_FATAL = "#ff9999";
  private static final String ROW_COLOR_WARNING = "#ffebcc";
  private static final String ROW_COLOR_HINT = "#ebf5ff";
  private static final String ROW_COLOR_NOT_MUST_SUPPORT = "#d6eaf8";
  public static final int STATUS_OK = 0;
  public static final int STATUS_HINT = 1;
  public static final int STATUS_WARNING = 2;
  public static final int STATUS_ERROR = 3;
  public static final int STATUS_FATAL = 4;

  private static final String DERIVATION_EQUALS = "derivation.equals";
  public static final String DERIVATION_POINTER = "derived.pointer";
  public static final String IS_DERIVED = "derived.fact";
  public static final String UD_ERROR_STATUS = "error-status";
  private static final String GENERATED_IN_SNAPSHOT = "profileutilities.snapshot.processed";
  private final boolean ADD_REFERENCE_TO_TABLE = true;

  private boolean useTableForFixedValues = true;
  @Setter
  @Getter
  @Deprecated
  private boolean debug;

  // note that ProfileUtilities are used re-entrantly internally, so nothing with
  // process state can be here
  private final IWorkerContext context;
  private List<ValidationMessage> messages;
  private List<String> snapshotStack = new ArrayList<String>();
  private ProfileKnowledgeProvider pkp;
  private boolean igmode;
  private boolean exception;
  private TerminologyServiceOptions terminologyServiceOptions = new TerminologyServiceOptions(FhirPublication.R4);
  private boolean newSlicingProcessing;

  public ProfileUtilities(IWorkerContext context, List<ValidationMessage> messages, ProfileKnowledgeProvider pkp) {
    super();
    this.context = context;
    this.messages = messages;
    this.pkp = pkp;
  }

  private class UnusedTracker {
    private boolean used;
  }

  public boolean isIgmode() {
    return igmode;
  }

  public void setIgmode(boolean igmode) {
    this.igmode = igmode;
  }

  public interface ProfileKnowledgeProvider {
    public class BindingResolution {
      public String display;
      public String url;
    }

    public boolean isPrimitiveType(String typeSimple);

    public boolean isDatatype(String typeSimple);

    public boolean isResource(String typeSimple);

    public boolean hasLinkFor(String typeSimple);

    public String getLinkFor(String corePath, String typeSimple);

    public BindingResolution resolveBinding(StructureDefinition def, ElementDefinitionBindingComponent binding,
        String path) throws FHIRException;

    public BindingResolution resolveBinding(StructureDefinition def, String url, String path) throws FHIRException;

    public String getLinkForProfile(StructureDefinition profile, String url);

    public boolean prependLinks();

    public String getLinkForUrl(String corePath, String s);
  }

  public static List<ElementDefinition> getChildMap(StructureDefinition profile, ElementDefinition element)
      throws DefinitionException {
    if (element.getContentReference() != null) {
      for (ElementDefinition e : profile.getSnapshot().getElement()) {
        if (element.getContentReference().equals("#" + e.getId()))
          return getChildMap(profile, e);
      }
      throw new DefinitionException(
          "Unable to resolve name reference " + element.getContentReference() + " at path " + element.getPath());

    } else {
      List<ElementDefinition> res = new ArrayList<ElementDefinition>();
      List<ElementDefinition> elements = profile.getSnapshot().getElement();
      String path = element.getPath();
      for (int index = elements.indexOf(element) + 1; index < elements.size(); index++) {
        ElementDefinition e = elements.get(index);
        if (e.getPath().startsWith(path + ".")) {
          // We only want direct children, not all descendants
          if (!e.getPath().substring(path.length() + 1).contains("."))
            res.add(e);
        } else
          break;
      }
      return res;
    }
  }

  public static List<ElementDefinition> getSliceList(StructureDefinition profile, ElementDefinition element)
      throws DefinitionException {
    if (!element.hasSlicing())
      throw new Error("getSliceList should only be called when the element has slicing");

    List<ElementDefinition> res = new ArrayList<ElementDefinition>();
    List<ElementDefinition> elements = profile.getSnapshot().getElement();
    String path = element.getPath();
    for (int index = elements.indexOf(element) + 1; index < elements.size(); index++) {
      ElementDefinition e = elements.get(index);
      if (e.getPath().startsWith(path + ".") || e.getPath().equals(path)) {
        // We want elements with the same path (until we hit an element that doesn't
        // start with the same path)
        if (e.getPath().equals(element.getPath()))
          res.add(e);
      } else
        break;
    }
    return res;
  }

  /**
   * Given a Structure, navigate to the element given by the path and return the
   * direct children of that element
   *
   * @param structure The structure to navigate into
   * @param path      The path of the element within the structure to get the
   *                  children for
   * @return A List containing the element children (all of them are Elements)
   */
  public static List<ElementDefinition> getChildList(StructureDefinition profile, String path, String id) {
    return getChildList(profile, path, id, false);
  }

  public static List<ElementDefinition> getChildList(StructureDefinition profile, String path, String id,
      boolean diff) {
    List<ElementDefinition> res = new ArrayList<ElementDefinition>();

    boolean capturing = id == null;
    if (id == null && !path.contains("."))
      capturing = true;

    List<ElementDefinition> list = diff ? profile.getDifferential().getElement() : profile.getSnapshot().getElement();
    for (ElementDefinition e : list) {
      if (e == null)
        throw new Error("element = null: " + profile.getUrl());
      if (e.getId() == null)
        throw new Error("element id = null: " + e.toString() + " on " + profile.getUrl());

      if (!capturing && id != null && e.getId().equals(id)) {
        capturing = true;
      }

      // If our element is a slice, stop capturing children as soon as we see the next
      // slice
      if (capturing && e.hasId() && id != null && !e.getId().equals(id) && e.getPath().equals(path))
        break;

      if (capturing) {
        String p = e.getPath();

        if (!Utilities.noString(e.getContentReference()) && path.startsWith(p)) {
          if (path.length() > p.length())
            return getChildList(profile, e.getContentReference() + "." + path.substring(p.length() + 1), null, diff);
          else
            return getChildList(profile, e.getContentReference(), null, diff);

        } else if (p.startsWith(path + ".") && !p.equals(path)) {
          String tail = p.substring(path.length() + 1);
          if (!tail.contains(".")) {
            res.add(e);
          }
        }
      }
    }

    return res;
  }

  public static List<ElementDefinition> getChildList(StructureDefinition structure, ElementDefinition element,
      boolean diff) {
    return getChildList(structure, element.getPath(), element.getId(), diff);
  }

  public static List<ElementDefinition> getChildList(StructureDefinition structure, ElementDefinition element) {
    return getChildList(structure, element.getPath(), element.getId(), false);
  }

  public void updateMaps(StructureDefinition base, StructureDefinition derived) throws DefinitionException {
    if (base == null)
      throw new DefinitionException("no base profile provided");
    if (derived == null)
      throw new DefinitionException("no derived structure provided");

    for (StructureDefinitionMappingComponent baseMap : base.getMapping()) {
      boolean found = false;
      for (StructureDefinitionMappingComponent derivedMap : derived.getMapping()) {
        if (derivedMap.getUri() != null && derivedMap.getUri().equals(baseMap.getUri())) {
          found = true;
          break;
        }
      }
      if (!found)
        derived.getMapping().add(baseMap);
    }
  }

  /**
   * Given a base (snapshot) profile structure, and a differential profile,
   * generate a new snapshot profile
   *
   * @param base             - the base structure on which the differential will
   *                         be applied
   * @param differential     - the differential to apply to the base
   * @param url              - where the base has relative urls for profile
   *                         references, these need to be converted to absolutes
   *                         by prepending this URL (e.g. the canonical URL)
   * @param webUrl           - where the base has relative urls in markdown, these
   *                         need to be converted to absolutes by prepending this
   *                         URL (this is not the same as the canonical URL)
   * @param trimDifferential - if this is true, then the snap short generator will
   *                         remove any material in the element definitions that
   *                         is not different to the base
   * @return
   * @throws FHIRException
   * @throws DefinitionException
   * @throws Exception
   */
  public void generateSnapshot(StructureDefinition base, StructureDefinition derived, String url, String webUrl,
      String profileName) throws DefinitionException, FHIRException {
    if (base == null)
      throw new DefinitionException("no base profile provided");
    if (derived == null)
      throw new DefinitionException("no derived structure provided");

    if (snapshotStack.contains(derived.getUrl()))
      throw new DefinitionException(
          "Circular snapshot references detected; cannot generate snapshot (stack = " + snapshotStack.toString() + ")");
    snapshotStack.add(derived.getUrl());

    if (!Utilities.noString(webUrl) && !webUrl.endsWith("/"))
      webUrl = webUrl + '/';

    derived.setSnapshot(new StructureDefinitionSnapshotComponent());

    try {
      // so we have two lists - the base list, and the differential list
      // the differential list is only allowed to include things that are in the base
      // list, but
      // is allowed to include them multiple times - thereby slicing them

      // our approach is to walk through the base list, and see whether the
      // differential
      // says anything about them.
      int baseCursor = 0;
      int diffCursor = 0; // we need a diff cursor because we can only look ahead, in the bound scoped by
                          // longer paths

      if (derived.hasDifferential() && !derived.getDifferential().getElementFirstRep().getPath().contains(".")
          && !derived.getDifferential().getElementFirstRep().getType().isEmpty())
        throw new Error("type on first differential element!");

      for (ElementDefinition e : derived.getDifferential().getElement())
        e.clearUserData(GENERATED_IN_SNAPSHOT);

      // we actually delegate the work to a subroutine so we can re-enter it with a
      // different cursors
      StructureDefinitionDifferentialComponent diff = cloneDiff(derived.getDifferential()); // we make a copy here
                                                                                            // because we're sometimes
                                                                                            // going to hack the
                                                                                            // differential while
                                                                                            // processing it. Have to
                                                                                            // migrate user data back
                                                                                            // afterwards

      processPaths("", derived.getSnapshot(), base.getSnapshot(), diff, baseCursor, diffCursor,
          base.getSnapshot().getElement().size() - 1,
          derived.getDifferential().hasElement() ? derived.getDifferential().getElement().size() - 1 : -1, url, webUrl,
          derived.present(), null, null, false, base.getUrl(), null, false, new ArrayList<ElementRedirection>(), base);
      if (!derived.getSnapshot().getElementFirstRep().getType().isEmpty())
        throw new Error("type on first snapshot element for " + derived.getSnapshot().getElementFirstRep().getPath()
            + " in " + derived.getUrl() + " from " + base.getUrl());
      updateMaps(base, derived);


      log.debug("Differential: ");
        for (ElementDefinition ed : derived.getDifferential().getElement())
          log.debug("  " + ed.getPath() + " : " + typeSummaryWithProfile(ed) + "[" + ed.getMin() + ".."
              + ed.getMax() + "]" + sliceSummary(ed) + "  id = " + ed.getId() + " " + constraintSummary(ed));
      log.debug("Snapshot: ");
        for (ElementDefinition ed : derived.getSnapshot().getElement())
          log.debug("  " + ed.getPath() + " : " + typeSummaryWithProfile(ed) + "[" + ed.getMin() + ".."
              + ed.getMax() + "]" + sliceSummary(ed) + "  id = " + ed.getId() + " " + constraintSummary(ed));

      setIds(derived, false);
      // Check that all differential elements have a corresponding snapshot element
      for (ElementDefinition e : diff.getElement()) {
        if (!e.hasUserData("diff-source"))
          throw new Error("Unxpected internal condition - no source on diff element");
        else {
          if (e.hasUserData(DERIVATION_EQUALS))
            ((Base) e.getUserData("diff-source")).setUserData(DERIVATION_EQUALS, e.getUserData(DERIVATION_EQUALS));
          if (e.hasUserData(DERIVATION_POINTER))
            ((Base) e.getUserData("diff-source")).setUserData(DERIVATION_POINTER, e.getUserData(DERIVATION_POINTER));
        }
        if (!e.hasUserData(GENERATED_IN_SNAPSHOT)) {
          log.error("Error in snapshot generation: Differential for " + derived.getUrl() + " with "
              + (e.hasId() ? "id: " + e.getId() : "path: " + e.getPath())
              + " has an element that is not marked with a snapshot match");
          if (exception)
            throw new DefinitionException("Snapshot for " + derived.getUrl()
                + " does not contain an element that matches an existing differential element that has "
                + (e.hasId() ? "id: " + e.getId() : "path: " + e.getPath()));
          else
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.VALUE, url,
                "Snapshot for " + derived.getUrl()
                    + " does not contain an element that matches an existing differential element that has id: "
                    + e.getId(),
                ValidationMessage.IssueSeverity.ERROR));
        }
      }
      if (derived.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
        for (ElementDefinition ed : derived.getSnapshot().getElement()) {
          if (!ed.hasBase()) {
            ed.getBase().setPath(ed.getPath()).setMin(ed.getMin()).setMax(ed.getMax());
          }
        }
      }
    } catch (Exception e) {
      // if we had an exception generating the snapshot, make sure we don't leave any
      // half generated snapshot behind
      derived.setSnapshot(null);
      throw e;
    }
  }

  private StructureDefinitionDifferentialComponent cloneDiff(StructureDefinitionDifferentialComponent source) {
    StructureDefinitionDifferentialComponent diff = new StructureDefinitionDifferentialComponent();
    for (ElementDefinition sed : source.getElement()) {
      ElementDefinition ted = sed.copy();
      diff.getElement().add(ted);
      ted.setUserData("diff-source", sed);
    }
    return diff;
  }

  private String constraintSummary(ElementDefinition ed) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    if (ed.hasPattern())
      b.append("pattern=" + ed.getPattern().fhirType());
    if (ed.hasFixed())
      b.append("fixed=" + ed.getFixed().fhirType());
    if (ed.hasConstraint())
      b.append("constraints=" + ed.getConstraint().size());
    return b.toString();
  }

  private String sliceSummary(ElementDefinition ed) {
    if (!ed.hasSlicing() && !ed.hasSliceName())
      return "";
    if (ed.hasSliceName())
      return " (slicename = " + ed.getSliceName() + ")";

    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (ElementDefinitionSlicingDiscriminatorComponent d : ed.getSlicing().getDiscriminator()) {
      if (first)
        first = false;
      else
        b.append("|");
      b.append(d.getPath());
    }
    return " (slicing by " + b.toString() + ")";
  }

  private String typeSummary(ElementDefinition ed) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (TypeRefComponent tr : ed.getType()) {
      if (first)
        first = false;
      else
        b.append("|");
      b.append(tr.getWorkingCode());
    }
    return b.toString();
  }

  private String typeSummaryWithProfile(ElementDefinition ed) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (TypeRefComponent tr : ed.getType()) {
      if (first)
        first = false;
      else
        b.append("|");
      b.append(tr.getWorkingCode());
      if (tr.hasProfile()) {
        b.append("(");
        b.append(tr.getProfile());
        b.append(")");

      }
    }
    return b.toString();
  }

  private boolean findMatchingElement(String id, List<ElementDefinition> list) {
    for (ElementDefinition ed : list) {
      if (ed.getId().equals(id))
        return true;
      if (id.endsWith("[x]")) {
        if (ed.getId().startsWith(id.substring(0, id.length() - 3))
            && !ed.getId().substring(id.length() - 3).contains("."))
          return true;
      }
    }
    return false;
  }

  /**
   * @param trimDifferential
   * @param srcSD
   * @throws DefinitionException, FHIRException
   * @throws Exception
   */
  private ElementDefinition processPaths(String indent, StructureDefinitionSnapshotComponent result,
      StructureDefinitionSnapshotComponent base, StructureDefinitionDifferentialComponent differential, int baseCursor,
      int diffCursor, int baseLimit, int diffLimit, String url, String webUrl, String profileName,
      String contextPathSrc, String contextPathDst, boolean trimDifferential, String contextName, String resultPathBase,
      boolean slicingDone, List<ElementRedirection> redirector, StructureDefinition srcSD)
      throws FHIRException {

      log.debug(indent + "PP @ " + resultPathBase + " / " + contextPathSrc + " : base = " + baseCursor + " to "
          + baseLimit + ", diff = " + diffCursor + " to " + diffLimit + " (slicing = " + slicingDone + ", redirector = "
          + (redirector == null ? "null" : redirector.toString()) + ")");
    ElementDefinition res = null;
    List<TypeSlice> typeList = new ArrayList<>();
    // just repeat processing entries until we run out of our allowed scope (1st
    // entry, the allowed scope is all the entries)
    while (baseCursor <= baseLimit) {
      // get the current focus of the base, and decide what to do
      ElementDefinition currentBase = base.getElement().get(baseCursor);
      String cpath = fixedPathSource(contextPathSrc, currentBase.getPath(), redirector);

        log.debug(indent + " - " + cpath + ": base = " + baseCursor + " ("
            + descED(base.getElement(), baseCursor) + ") to " + baseLimit + " (" + descED(base.getElement(), baseLimit)
            + "), diff = " + diffCursor + " (" + descED(differential.getElement(), diffCursor) + ") to " + diffLimit
            + " (" + descED(differential.getElement(), diffLimit) + ") " + "(slicingDone = " + slicingDone
            + ") (diffpath= "
            + (differential.getElement().size() > diffCursor ? differential.getElement().get(diffCursor).getPath()
                : "n/a")
            + ")");
      List<ElementDefinition> diffMatches = getDiffMatches(differential, cpath, diffCursor, diffLimit, profileName); // get
                                                                                                                     // a
                                                                                                                     // list
                                                                                                                     // of
                                                                                                                     // matching
                                                                                                                     // elements
                                                                                                                     // in
                                                                                                                     // scope

      // in the simple case, source is not sliced.
      if (!currentBase.hasSlicing()) {
        if (diffMatches.isEmpty()) { // the differential doesn't say anything about this item
          // so we just copy it in
          ElementDefinition outcome = updateURLs(url, webUrl, currentBase.copy());
          outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
          updateFromBase(outcome, currentBase);
          markDerived(outcome);
          if (resultPathBase == null)
            resultPathBase = outcome.getPath();
          else if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);
          if (hasInnerDiffMatches(differential, cpath, diffCursor, diffLimit, base.getElement(), true)) {
            // well, the profile walks into this, so we need to as well
            // did we implicitly step into a new type?
            if (baseHasChildren(base, currentBase)) { // not a new type here
              processPaths(indent + "  ", result, base, differential, baseCursor + 1, diffCursor, baseLimit, diffLimit,
                  url, webUrl, profileName, contextPathSrc, contextPathDst, trimDifferential, contextName,
                  resultPathBase, false, redirector, srcSD);
              baseCursor = indexOfFirstNonChild(base, currentBase, baseCursor + 1, baseLimit);
            } else {
              if (outcome.getType().size() == 0) {
                throw new DefinitionException(diffMatches.get(0).getPath() + " has no children ("
                    + differential.getElement().get(diffCursor).getPath() + ") and no types in profile " + profileName);
              }
              if (outcome.getType().size() > 1) {
                for (TypeRefComponent t : outcome.getType()) {
                  if (!t.getWorkingCode().equals("Reference"))
                    throw new DefinitionException(diffMatches.get(0).getPath() + " has children ("
                        + differential.getElement().get(diffCursor).getPath() + ") and multiple types ("
                        + typeCode(outcome.getType()) + ") in profile " + profileName);
                }
              }
              StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
              if (dt == null)
                throw new DefinitionException(
                    "Unknown type " + outcome.getType().get(0) + " at " + diffMatches.get(0).getPath());
              contextName = dt.getUrl();
              int start = diffCursor;
              while (differential.getElement().size() > diffCursor
                  && pathStartsWith(differential.getElement().get(diffCursor).getPath(), cpath + "."))
                diffCursor++;
              processPaths(indent + "  ", result, dt.getSnapshot(), differential,
                  1 /* starting again on the data type, but skip the root */, start,
                  dt.getSnapshot().getElement().size() - 1, diffCursor - 1, url, webUrl, profileName, cpath,
                  outcome.getPath(), trimDifferential, contextName, resultPathBase, false, redirector, srcSD);
            }
          }
          baseCursor++;
        } else if (diffMatches.size() == 1
            && (slicingDone || (!isImplicitSlicing(diffMatches.get(0), cpath) && !(diffMatches.get(0).hasSlicing()
                || (isExtension(diffMatches.get(0)) && diffMatches.get(0).hasSliceName()))))) {// one matching element
                                                                                               // in the differential
          ElementDefinition template = null;
          if (diffMatches.get(0).hasType() && diffMatches.get(0).getType().size() == 1
              && diffMatches.get(0).getType().get(0).hasProfile()
              && !"Reference".equals(diffMatches.get(0).getType().get(0).getWorkingCode())) {
            CanonicalType p = diffMatches.get(0).getType().get(0).getProfile().get(0);
            StructureDefinition sd = context.fetchResource(StructureDefinition.class, p.getValue());
            if (sd != null) {
              if (!sd.hasSnapshot()) {
                StructureDefinition sdb = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
                if (sdb == null)
                  throw new DefinitionException("no base for " + sd.getBaseDefinition());
                generateSnapshot(sdb, sd, sd.getUrl(),
                    (sdb.hasUserData("path")) ? Utilities.extractBaseUrl(sdb.getUserString("path")) : webUrl,
                    sd.getName());
              }
              ElementDefinition src;
              if (p.hasExtension(ToolingExtensions.EXT_PROFILE_ELEMENT)) {
                src = null;
                String eid = p.getExtensionString(ToolingExtensions.EXT_PROFILE_ELEMENT);
                for (ElementDefinition t : sd.getSnapshot().getElement()) {
                  if (eid.equals(t.getId()))
                    src = t;
                }
                if (src == null)
                  throw new DefinitionException("Unable to find element " + eid + " in " + p.getValue());
              } else
                src = sd.getSnapshot().getElement().get(0);
              template = src.copy().setPath(currentBase.getPath());
              template.setSliceName(null);
              // temporary work around
              if (!"Extension".equals(diffMatches.get(0).getType().get(0).getCode())) {
                template.setMin(currentBase.getMin());
                template.setMax(currentBase.getMax());
              }
            }
          }
          if (template == null)
            template = currentBase.copy();
          else
            // some of what's in currentBase overrides template
            template = overWriteWithCurrent(template, currentBase);

          ElementDefinition outcome = updateURLs(url, webUrl, template);
          outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
          if (res == null)
            res = outcome;
          updateFromBase(outcome, currentBase);
          if (diffMatches.get(0).hasSliceName())
            outcome.setSliceName(diffMatches.get(0).getSliceName());
          updateFromDefinition(outcome, diffMatches.get(0), profileName, trimDifferential, url, srcSD);
//          if (outcome.getPath().endsWith("[x]") && outcome.getType().size() == 1 && !outcome.getType().get(0).getCode().equals("*") && !diffMatches.get(0).hasSlicing()) // if the base profile allows multiple types, but the profile only allows one, rename it
//            outcome.setPath(outcome.getPath().substring(0, outcome.getPath().length()-3)+Utilities.capitalize(outcome.getType().get(0).getCode()));
          outcome.setSlicing(null);
          if (resultPathBase == null)
            resultPathBase = outcome.getPath();
          else if (!outcome.getPath().startsWith(resultPathBase))
            throw new DefinitionException("Adding wrong path");
          result.getElement().add(outcome);
          baseCursor++;
          diffCursor = differential.getElement().indexOf(diffMatches.get(0)) + 1;
          if (differential.getElement().size() > diffCursor && outcome.getPath().contains(".")
              && (isDataType(outcome.getType()) || outcome.hasContentReference())) { // don't want to do this for the
                                                                                     // root, since that's base, and
                                                                                     // we're already processing it
            if (pathStartsWith(differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath() + ".")
                && !baseWalksInto(base.getElement(), baseCursor)) {
              if (outcome.getType().size() > 1) {
                if (outcome.getPath().endsWith("[x]") && !diffMatches.get(0).getPath().endsWith("[x]")) {
                  String en = tail(outcome.getPath());
                  String tn = tail(diffMatches.get(0).getPath());
                  String t = tn.substring(en.length() - 3);
                  if (isPrimitive(Utilities.uncapitalize(t)))
                    t = Utilities.uncapitalize(t);
                  List<TypeRefComponent> ntr = getByTypeName(outcome.getType(), t); // keep any additional information
                  if (ntr.isEmpty())
                    ntr.add(new TypeRefComponent().setCode(t));
                  outcome.getType().clear();
                  outcome.getType().addAll(ntr);
                }
                if (outcome.getType().size() > 1)
                  for (TypeRefComponent t : outcome.getType()) {
                    if (!t.getCode().equals("Reference")) {
                      boolean nonExtension = false;
                      for (ElementDefinition ed : diffMatches)
                        if (ed != diffMatches.get(0) && !ed.getPath().endsWith(".extension"))
                          nonExtension = true;
                      if (nonExtension)
                        throw new DefinitionException(diffMatches.get(0).getPath() + " has children ("
                            + differential.getElement().get(diffCursor).getPath() + ") and multiple types ("
                            + typeCode(outcome.getType()) + ") in profile " + profileName);
                    }
                  }
              }
              int start = diffCursor;
              while (differential.getElement().size() > diffCursor && pathStartsWith(
                  differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath() + "."))
                diffCursor++;
              if (outcome.hasContentReference()) {
                ElementDefinition tgt = getElementById(base.getElement(), outcome.getContentReference());
                if (tgt == null)
                  throw new DefinitionException("Unable to resolve reference to " + outcome.getContentReference());
                replaceFromContentReference(outcome, tgt);
                int nbc = base.getElement().indexOf(tgt) + 1;
                int nbl = nbc;
                while (nbl < base.getElement().size()
                    && base.getElement().get(nbl).getPath().startsWith(tgt.getPath() + "."))
                  nbl++;
                processPaths(indent + "  ", result, base, differential, nbc, start - 1, nbl - 1, diffCursor - 1, url,
                    webUrl, profileName, tgt.getPath(), diffMatches.get(0).getPath(), trimDifferential, contextName,
                    resultPathBase, false, redirectorStack(redirector, outcome, cpath), srcSD);
              } else {
                StructureDefinition dt = outcome.getType().size() == 1 ? getProfileForDataType(outcome.getType().get(0))
                    : getProfileForDataType("Element");
                if (dt == null)
                  throw new DefinitionException(diffMatches.get(0).getPath() + " has children ("
                      + differential.getElement().get(diffCursor).getPath() + ") for type "
                      + typeCode(outcome.getType()) + " in profile " + profileName + ", but can't find type");
                contextName = dt.getUrl();
                processPaths(indent + "  ", result, dt.getSnapshot(), differential,
                    1 /* starting again on the data type, but skip the root */, start,
                    dt.getSnapshot().getElement().size() - 1, diffCursor - 1, url, webUrl,
                    profileName + pathTail(diffMatches, 0), diffMatches.get(0).getPath(), outcome.getPath(),
                    trimDifferential, contextName, resultPathBase, false, new ArrayList<ElementRedirection>(), srcSD);
              }
            }
          }
        } else if (diffsConstrainTypes(diffMatches, cpath, typeList)) {
          int start = 0;
          int nbl = findEndOfElement(base, baseCursor);
          int ndc = differential.getElement().indexOf(diffMatches.get(0));
          ElementDefinition elementToRemove = null;
          // we come here whether they are sliced in the diff, or whether the short cut is
          // used.
          if (typeList.get(0).type != null) {
            // this is the short cut method, we've just dived in and specified a type slice.
            // in R3 (and unpatched R4, as a workaround right now...
            if (!VersionUtilities.isR4Plus(context.getVersion()) || !newSlicingProcessing) { // newSlicingProcessing is
                                                                                             // a work around for
                                                                                             // editorial loop
                                                                                             // dependency
              // we insert a cloned element with the right types at the start of the
              // diffMatches
              ElementDefinition ed = new ElementDefinition();
              ed.setPath(determineTypeSlicePath(diffMatches.get(0).getPath(), cpath));
              for (TypeSlice ts : typeList)
                ed.addType().setCode(ts.type);
              ed.setSlicing(new ElementDefinitionSlicingComponent());
              ed.getSlicing().addDiscriminator().setType(DiscriminatorType.TYPE).setPath("$this");
              ed.getSlicing().setRules(SlicingRules.CLOSED);
              ed.getSlicing().setOrdered(false);
              diffMatches.add(0, ed);
              differential.getElement().add(ndc, ed);
              elementToRemove = ed;
            } else {
              // as of R4, this changed; if there's no slice, there's no constraint on the
              // slice types, only one the type.
              // so the element we insert specifies no types (= all types) allowed in the
              // base, not just the listed type.
              // see also discussion here:
              // https://chat.fhir.org/#narrow/stream/179177-conformance/topic/Slicing.20a.20non-repeating.20element
              ElementDefinition ed = new ElementDefinition();
              ed.setPath(determineTypeSlicePath(diffMatches.get(0).getPath(), cpath));
              ed.setSlicing(new ElementDefinitionSlicingComponent());
              ed.getSlicing().addDiscriminator().setType(DiscriminatorType.TYPE).setPath("$this");
              ed.getSlicing().setRules(SlicingRules.CLOSED);
              ed.getSlicing().setOrdered(false);
              diffMatches.add(0, ed);
              differential.getElement().add(ndc, ed);
              elementToRemove = ed;
            }
          }
          int ndl = findEndOfElement(differential, ndc);
          // the first element is setting up the slicing
          if (diffMatches.get(0).getSlicing().hasRules())
            if (diffMatches.get(0).getSlicing().getRules() != SlicingRules.CLOSED)
              throw new FHIRException(
                  "Error at path " + contextPathSrc + ": Type slicing with slicing.rules != closed");
          if (diffMatches.get(0).getSlicing().hasOrdered())
            if (diffMatches.get(0).getSlicing().getOrdered())
              throw new FHIRException("Error at path " + contextPathSrc + ": Type slicing with slicing.ordered = true");
          if (diffMatches.get(0).getSlicing().hasDiscriminator()) {
            if (diffMatches.get(0).getSlicing().getDiscriminator().size() != 1)
              throw new FHIRException(
                  "Error at path " + contextPathSrc + ": Type slicing with slicing.discriminator.count() > 1");
            if (!"$this".equals(diffMatches.get(0).getSlicing().getDiscriminatorFirstRep().getPath()))
              throw new FHIRException(
                  "Error at path " + contextPathSrc + ": Type slicing with slicing.discriminator.path != '$this'");
            if (diffMatches.get(0).getSlicing().getDiscriminatorFirstRep().getType() != DiscriminatorType.TYPE)
              throw new FHIRException(
                  "Error at path " + contextPathSrc + ": Type slicing with slicing.discriminator.type != 'type'");
          }
          // check the slice names too while we're at it...
          for (TypeSlice ts : typeList)
            if (ts.type != null) {
              String tn = rootName(cpath) + Utilities.capitalize(ts.type);
              if (!ts.defn.hasSliceName())
                ts.defn.setSliceName(tn);
              else if (!ts.defn.getSliceName().equals(tn))
                throw new FHIRException(
                    "Error at path " + (!Utilities.noString(contextPathSrc) ? contextPathSrc : cpath)
                        + ": Slice name must be '" + tn + "' but is '" + ts.defn.getSliceName() + "'");
              if (!ts.defn.hasType())
                ts.defn.addType().setCode(ts.type);
              else if (ts.defn.getType().size() > 1)
                throw new FHIRException(
                    "Error at path " + (!Utilities.noString(contextPathSrc) ? contextPathSrc : cpath)
                        + ": Slice for type '" + tn + "' has more than one type '" + ts.defn.typeSummary() + "'");
              else if (!ts.defn.getType().get(0).getCode().equals(ts.type))
                throw new FHIRException(
                    "Error at path " + (!Utilities.noString(contextPathSrc) ? contextPathSrc : cpath)
                        + ": Slice for type '" + tn + "' has wrong type '" + ts.defn.typeSummary() + "'");
            }

          // ok passed the checks.
          // copy the root diff, and then process any children it has
          ElementDefinition e = processPaths(indent + "  ", result, base, differential, baseCursor, ndc, nbl, ndl, url,
              webUrl, profileName + pathTail(diffMatches, 0), contextPathSrc, contextPathDst, trimDifferential,
              contextName, resultPathBase, true, redirector, srcSD);
          if (e == null)
            throw new FHIRException("Did not find type root: " + diffMatches.get(0).getPath());
          // now set up slicing on the e (cause it was wiped by what we called.
          e.setSlicing(new ElementDefinitionSlicingComponent());
          e.getSlicing().addDiscriminator().setType(DiscriminatorType.TYPE).setPath("$this");
          e.getSlicing().setRules(SlicingRules.CLOSED);
          e.getSlicing().setOrdered(false);
          start++;
          // now process the siblings, which should each be type constrained - and may
          // also have their own children
          // now we process the base scope repeatedly for each instance of the item in the
          // differential list
          for (int i = start; i < diffMatches.size(); i++) {
            // our processing scope for the differential is the item in the list, and all
            // the items before the next one in the list
            ndc = differential.getElement().indexOf(diffMatches.get(i));
            ndl = findEndOfElement(differential, ndc);
            processPaths(indent + "  ", result, base, differential, baseCursor, ndc, nbl, ndl, url, webUrl,
                profileName + pathTail(diffMatches, i), contextPathSrc, contextPathDst, trimDifferential, contextName,
                resultPathBase, true, redirector, srcSD);
          }
          if (elementToRemove != null) {
            differential.getElement().remove(elementToRemove);
            ndl--;
          }

          // ok, done with that - next in the base list
          baseCursor = nbl + 1;
          diffCursor = ndl + 1;

        } else {
          // ok, the differential slices the item. Let's check our pre-conditions to
          // ensure that this is correct
          if (!unbounded(currentBase) && !isSlicedToOneOnly(diffMatches.get(0)))
            // you can only slice an element that doesn't repeat if the sum total of your
            // slices is limited to 1
            // (but you might do that in order to split up constraints by type)
            throw new DefinitionException("Attempt to a slice an element that does not repeat: " + currentBase.getPath()
                + "/" + currentBase.getPath() + " from " + contextName + " in " + url);
          if (!diffMatches.get(0).hasSlicing() && !isExtension(currentBase)) // well, the diff has set up a slice, but
                                                                             // hasn't defined it. this is an error
            throw new DefinitionException("Differential does not have a slice: " + currentBase.getPath() + "/ (b:"
                + baseCursor + " of " + baseLimit + " / " + diffCursor + "/ " + diffLimit + ") in profile " + url);

          // well, if it passed those preconditions then we slice the dest.
          int start = 0;
          int nbl = findEndOfElement(base, baseCursor);
//          if (diffMatches.size() > 1 && diffMatches.get(0).hasSlicing() && differential.getElement().indexOf(diffMatches.get(1)) > differential.getElement().indexOf(diffMatches.get(0))+1) {
          if (diffMatches.size() > 1 && diffMatches.get(0).hasSlicing() && (nbl > baseCursor || differential
              .getElement().indexOf(diffMatches.get(1)) > differential.getElement().indexOf(diffMatches.get(0)) + 1)) { // there's
                                                                                                                        // a
                                                                                                                        // default
                                                                                                                        // set
                                                                                                                        // before
                                                                                                                        // the
                                                                                                                        // slices
            int ndc = differential.getElement().indexOf(diffMatches.get(0));
            int ndl = findEndOfElement(differential, ndc);
            ElementDefinition e = processPaths(indent + "  ", result, base, differential, baseCursor, ndc, nbl, ndl,
                url, webUrl, profileName + pathTail(diffMatches, 0), contextPathSrc, contextPathDst, trimDifferential,
                contextName, resultPathBase, true, redirector, srcSD);
            if (e == null)
              throw new FHIRException("Did not find single slice: " + diffMatches.get(0).getPath());
            e.setSlicing(diffMatches.get(0).getSlicing());
            start++;
          } else {
            // we're just going to accept the differential slicing at face value
            ElementDefinition outcome = updateURLs(url, webUrl, currentBase.copy());
            outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
            updateFromBase(outcome, currentBase);

            if (!diffMatches.get(0).hasSlicing())
              outcome.setSlicing(makeExtensionSlicing());
            else
              outcome.setSlicing(diffMatches.get(0).getSlicing().copy());
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path");
            result.getElement().add(outcome);

            // differential - if the first one in the list has a name, we'll process it.
            // Else we'll treat it as the base definition of the slice.
            if (!diffMatches.get(0).hasSliceName()) {
              updateFromDefinition(outcome, diffMatches.get(0), profileName, trimDifferential, url, srcSD);
              if (!outcome.hasContentReference() && !outcome.hasType()) {
                throw new DefinitionException("not done yet");
              }
              start++;
              // result.getElement().remove(result.getElement().size()-1);
            } else
              checkExtensionDoco(outcome);
          }
          // now, for each entry in the diff matches, we're going to process the base item
          // our processing scope for base is all the children of the current path
          int ndc = diffCursor;
          int ndl = diffCursor;
          for (int i = start; i < diffMatches.size(); i++) {
            // our processing scope for the differential is the item in the list, and all
            // the items before the next one in the list
            ndc = differential.getElement().indexOf(diffMatches.get(i));
            ndl = findEndOfElement(differential, ndc);
            /*
             * if (skipSlicingElement && i == 0) { ndc = ndc + 1; if (ndc > ndl) continue; }
             */
            // now we process the base scope repeatedly for each instance of the item in the
            // differential list
            processPaths(indent + "  ", result, base, differential, baseCursor, ndc, nbl, ndl, url, webUrl,
                profileName + pathTail(diffMatches, i), contextPathSrc, contextPathDst, trimDifferential, contextName,
                resultPathBase, true, redirector, srcSD);
          }
          // ok, done with that - next in the base list
          baseCursor = nbl + 1;
          diffCursor = ndl + 1;
        }
      } else {
        // the item is already sliced in the base profile.
        // here's the rules
        // 1. irrespective of whether the slicing is ordered or not, the definition
        // order must be maintained
        // 2. slice element names have to match.
        // 3. new slices must be introduced at the end
        // corallory: you can't re-slice existing slices. is that ok?

        // we're going to need this:
        String path = currentBase.getPath();
        ElementDefinition original = currentBase;

        if (diffMatches.isEmpty()) { // the differential doesn't say anything about this item
          // copy across the currentbase, and all of its children and siblings
          while (baseCursor < base.getElement().size()
              && base.getElement().get(baseCursor).getPath().startsWith(path)) {
            ElementDefinition outcome = updateURLs(url, webUrl, base.getElement().get(baseCursor).copy());
            outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException(
                  "Adding wrong path in profile " + profileName + ": " + outcome.getPath() + " vs " + resultPathBase);
            result.getElement().add(outcome); // so we just copy it in
            baseCursor++;
          }
        } else {
          // first - check that the slicing is ok
          boolean closed = currentBase.getSlicing().getRules() == SlicingRules.CLOSED;
          int diffpos = 0;
          boolean isExtension = cpath.endsWith(".extension") || cpath.endsWith(".modifierExtension");
          if (diffMatches.get(0).hasSlicing()) { // it might be null if the differential doesn't want to say anything
                                                 // about slicing
//            if (!isExtension)
//              diffpos++; // if there's a slice on the first, we'll ignore any content it has
            ElementDefinitionSlicingComponent dSlice = diffMatches.get(0).getSlicing();
            ElementDefinitionSlicingComponent bSlice = currentBase.getSlicing();
            if (dSlice.hasOrderedElement() && bSlice.hasOrderedElement()
                && !orderMatches(dSlice.getOrderedElement(), bSlice.getOrderedElement()))
              throw new DefinitionException(
                  "Slicing rules on differential (" + summarizeSlicing(dSlice) + ") do not match those on base ("
                      + summarizeSlicing(bSlice) + ") - order @ " + path + " (" + contextName + ")");
            if (!discriminatorMatches(dSlice.getDiscriminator(), bSlice.getDiscriminator()))
              throw new DefinitionException(
                  "Slicing rules on differential (" + summarizeSlicing(dSlice) + ") do not match those on base ("
                      + summarizeSlicing(bSlice) + ") - discriminator @ " + path + " (" + contextName + ")");
            if (!ruleMatches(dSlice.getRules(), bSlice.getRules()))
              throw new DefinitionException(
                  "Slicing rules on differential (" + summarizeSlicing(dSlice) + ") do not match those on base ("
                      + summarizeSlicing(bSlice) + ") - rule @ " + path + " (" + contextName + ")");
          }
          ElementDefinition outcome = updateURLs(url, webUrl, currentBase.copy());
          outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
          updateFromBase(outcome, currentBase);
          if (diffMatches.get(0).hasSlicing() || !diffMatches.get(0).hasSliceName()) {
            updateFromSlicing(outcome.getSlicing(), diffMatches.get(0).getSlicing());
            updateFromDefinition(outcome, diffMatches.get(0), profileName, closed, url, srcSD); // if there's no slice,
                                                                                                // we don't want to
                                                                                                // update the unsliced
                                                                                                // description
          } else if (!diffMatches.get(0).hasSliceName())
            diffMatches.get(0).setUserData(GENERATED_IN_SNAPSHOT, outcome); // because of updateFromDefinition isn't
                                                                            // called

          result.getElement().add(outcome);

          if (!diffMatches.get(0).hasSliceName()) { // it's not real content, just the slice
            diffpos++;
          }
          if (hasInnerDiffMatches(differential, cpath, diffpos, diffLimit, base.getElement(), false)) {
            int nbl = findEndOfElement(base, baseCursor);
            int ndc = differential.getElement().indexOf(diffMatches.get(0)) + 1;
            int ndl = findEndOfElement(differential, ndc);
            processPaths(indent + "  ", result, base, differential, baseCursor + 1, ndc, nbl, ndl, url, webUrl,
                profileName + pathTail(diffMatches, 0), contextPathSrc, contextPathDst, trimDifferential, contextName,
                resultPathBase, false, null, srcSD);
//            throw new Error("Not done yet");
//          } else if (currentBase.getType().get(0).getCode().equals("BackboneElement") && diffMatches.size() > 0 && diffMatches.get(0).hasSliceName()) {
          } else if (currentBase.getType().get(0).getCode().equals("BackboneElement")) {
            // We need to copy children of the backbone element before we start messing
            // around with slices
            int nbl = findEndOfElement(base, baseCursor);
            for (int i = baseCursor + 1; i <= nbl; i++) {
              outcome = updateURLs(url, webUrl, base.getElement().get(i).copy());
              result.getElement().add(outcome);
            }
          }

          // now, we have two lists, base and diff. we're going to work through base,
          // looking for matches in diff.
          List<ElementDefinition> baseMatches = getSiblings(base.getElement(), currentBase);
          for (ElementDefinition baseItem : baseMatches) {
            baseCursor = base.getElement().indexOf(baseItem);
            outcome = updateURLs(url, webUrl, baseItem.copy());
            updateFromBase(outcome, currentBase);
            outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
            outcome.setSlicing(null);
            if (!outcome.getPath().startsWith(resultPathBase))
              throw new DefinitionException("Adding wrong path");
            if (diffpos < diffMatches.size()
                && diffMatches.get(diffpos).getSliceName().equals(outcome.getSliceName())) {
              // if there's a diff, we update the outcome with diff
              // no? updateFromDefinition(outcome, diffMatches.get(diffpos), profileName,
              // closed, url);
              // then process any children
              int nbl = findEndOfElement(base, baseCursor);
              int ndc = differential.getElement().indexOf(diffMatches.get(diffpos));
              int ndl = findEndOfElement(differential, ndc);
              // now we process the base scope repeatedly for each instance of the item in the
              // differential list
              processPaths(indent + "  ", result, base, differential, baseCursor, ndc, nbl, ndl, url, webUrl,
                  profileName + pathTail(diffMatches, diffpos), contextPathSrc, contextPathDst, closed, contextName,
                  resultPathBase, true, redirector, srcSD);
              // ok, done with that - now set the cursors for if this is the end
              baseCursor = nbl;
              diffCursor = ndl + 1;
              diffpos++;
            } else {
              result.getElement().add(outcome);
              baseCursor++;
              // just copy any children on the base
              while (baseCursor < base.getElement().size()
                  && base.getElement().get(baseCursor).getPath().startsWith(path)
                  && !base.getElement().get(baseCursor).getPath().equals(path)) {
                outcome = updateURLs(url, webUrl, base.getElement().get(baseCursor).copy());
                outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
                if (!outcome.getPath().startsWith(resultPathBase))
                  throw new DefinitionException("Adding wrong path");
                result.getElement().add(outcome);
                baseCursor++;
              }
              // Lloyd - add this for test T15
              baseCursor--;
            }
          }
          // finally, we process any remaining entries in diff, which are new (and which
          // are only allowed if the base wasn't closed
          if (closed && diffpos < diffMatches.size())
            throw new DefinitionException(
                "The base snapshot marks a slicing as closed, but the differential tries to extend it in " + profileName
                    + " at " + path + " (" + cpath + ")");
          if (diffpos == diffMatches.size()) {
//Lloyd This was causing problems w/ Telus
//            diffCursor++;
          } else {
            while (diffpos < diffMatches.size()) {
              ElementDefinition diffItem = diffMatches.get(diffpos);
              for (ElementDefinition baseItem : baseMatches)
                if (baseItem.getSliceName().equals(diffItem.getSliceName()))
                  throw new DefinitionException("Named items are out of order in the slice");
              outcome = updateURLs(url, webUrl, currentBase.copy());
              // outcome = updateURLs(url, diffItem.copy());
              outcome.setPath(fixedPathDest(contextPathDst, outcome.getPath(), redirector, contextPathSrc));
              updateFromBase(outcome, currentBase);
              outcome.setSlicing(null);
              if (!outcome.getPath().startsWith(resultPathBase))
                throw new DefinitionException("Adding wrong path");
              result.getElement().add(outcome);
              updateFromDefinition(outcome, diffItem, profileName, trimDifferential, url, srcSD);
              // --- LM Added this
              diffCursor = differential.getElement().indexOf(diffItem) + 1;
              if (!outcome.getType().isEmpty()
                  && (/* outcome.getType().get(0).getCode().equals("Extension") || */differential.getElement()
                      .size() > diffCursor)
                  && outcome.getPath().contains(".") && isDataType(outcome.getType())) { // don't want to do this for
                                                                                         // the root, since that's base,
                                                                                         // and we're already processing
                                                                                         // it
                if (!baseWalksInto(base.getElement(), baseCursor)) {
                  if (differential.getElement().size() > diffCursor && pathStartsWith(
                      differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath() + ".")) {
                    if (outcome.getType().size() > 1)
                      for (TypeRefComponent t : outcome.getType()) {
                        if (!t.getCode().equals("Reference"))
                          throw new DefinitionException(diffMatches.get(0).getPath() + " has children ("
                              + differential.getElement().get(diffCursor).getPath() + ") and multiple types ("
                              + typeCode(outcome.getType()) + ") in profile " + profileName);
                      }
                    TypeRefComponent t = outcome.getType().get(0);
                    if (t.getCode().equals("BackboneElement")) {
                      int baseStart = base.getElement().indexOf(currentBase) + 1;
                      int baseMax = baseStart + 1;
                      while (baseMax < base.getElement().size()
                          && base.getElement().get(baseMax).getPath().startsWith(currentBase.getPath() + "."))
                        baseMax++;
                      int start = diffCursor;
                      while (differential.getElement().size() > diffCursor && pathStartsWith(
                          differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath() + "."))
                        diffCursor++;
                      processPaths(indent + "  ", result, base, differential, baseStart, start - 1, baseMax - 1,
                          diffCursor - 1, url, webUrl, profileName + pathTail(diffMatches, 0),
                          base.getElement().get(0).getPath(), base.getElement().get(0).getPath(), trimDifferential,
                          contextName, resultPathBase, false, redirector, srcSD);

                    } else {
                      StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
                      // if (t.getCode().equals("Extension") && t.hasProfile() &&
                      // !t.getProfile().contains(":")) {
                      // lloydfix dt =
                      // }
                      if (dt == null)
                        throw new DefinitionException(diffMatches.get(0).getPath() + " has children ("
                            + differential.getElement().get(diffCursor).getPath() + ") for type "
                            + typeCode(outcome.getType()) + " in profile " + profileName + ", but can't find type");
                      contextName = dt.getUrl();
                      int start = diffCursor;
                      while (differential.getElement().size() > diffCursor && pathStartsWith(
                          differential.getElement().get(diffCursor).getPath(), diffMatches.get(0).getPath() + "."))
                        diffCursor++;
                      processPaths(indent + "  ", result, dt.getSnapshot(), differential,
                          1 /* starting again on the data type, but skip the root */, start - 1,
                          dt.getSnapshot().getElement().size() - 1, diffCursor - 1, url, webUrl,
                          profileName + pathTail(diffMatches, 0), diffMatches.get(0).getPath(), outcome.getPath(),
                          trimDifferential, contextName, resultPathBase, false, redirector, srcSD);
                    }
                  } else if (outcome.getType().get(0).getCode().equals("Extension")) {
                    // Force URL to appear if we're dealing with an extension. (This is a kludge -
                    // may need to drill down in other cases where we're slicing and the type has a
                    // profile declaration that could be setting the fixed value)
                    StructureDefinition dt = getProfileForDataType(outcome.getType().get(0));
                    for (ElementDefinition extEd : dt.getSnapshot().getElement()) {
                      // We only want the children that aren't the root
                      if (extEd.getPath().contains(".")) {
                        ElementDefinition extUrlEd = updateURLs(url, webUrl, extEd.copy());
                        extUrlEd.setPath(fixedPathDest(outcome.getPath(), extUrlEd.getPath(), redirector, null));
                        // updateFromBase(extUrlEd, currentBase);
                        markDerived(extUrlEd);
                        result.getElement().add(extUrlEd);
                      }
                    }
                  }
                }
              }
              // ---
              diffpos++;
            }
          }
          baseCursor++;
        }
      }
    }

    int i = 0;
    for (ElementDefinition e : result.getElement()) {
      i++;
      if (e.hasMinElement() && e.getMinElement().getValue() == null)
        throw new Error("null min");
    }
    return res;
  }

  private String descED(List<ElementDefinition> list, int index) {
    return index >= 0 && index < list.size() ? list.get(index).present() : "X";
  }

  private boolean baseHasChildren(StructureDefinitionSnapshotComponent base, ElementDefinition ed) {
    int index = base.getElement().indexOf(ed);
    if (index == -1 || index >= base.getElement().size() - 1)
      return false;
    String p = base.getElement().get(index + 1).getPath();
    return isChildOf(p, ed.getPath());
  }

  private boolean isChildOf(String sub, String focus) {
    if (focus.endsWith("[x]")) {
      focus = focus.substring(0, focus.length() - 3);
      return sub.startsWith(focus);
    } else
      return sub.startsWith(focus + ".");
  }

  private int indexOfFirstNonChild(StructureDefinitionSnapshotComponent base, ElementDefinition currentBase, int i,
      int baseLimit) {
    return baseLimit + 1;
  }

  private String rootName(String cpath) {
    String t = tail(cpath);
    return t.replace("[x]", "");
  }

  private String determineTypeSlicePath(String path, String cpath) {
    String headP = path.substring(0, path.lastIndexOf("."));
//    String tailP = path.substring(path.lastIndexOf(".")+1);
    String tailC = cpath.substring(cpath.lastIndexOf(".") + 1);
    return headP + "." + tailC;
  }

  private boolean isImplicitSlicing(ElementDefinition ed, String path) {
    if (ed == null || ed.getPath() == null || path == null)
      return false;
    if (path.equals(ed.getPath()))
      return false;
    boolean ok = path.endsWith("[x]") && ed.getPath().startsWith(path.substring(0, path.length() - 3));
    return ok;
  }

  private boolean diffsConstrainTypes(List<ElementDefinition> diffMatches, String cPath, List<TypeSlice> typeList) {
//    if (diffMatches.size() < 2)
//      return false;
    String p = diffMatches.get(0).getPath();
    if (!p.endsWith("[x]") && !cPath.endsWith("[x]"))
      return false;
    typeList.clear();
    String rn = tail(cPath);
    rn = rn.substring(0, rn.length() - 3);
    for (int i = 0; i < diffMatches.size(); i++) {
      ElementDefinition ed = diffMatches.get(i);
      String n = tail(ed.getPath());
      if (!n.startsWith(rn))
        return false;
      String s = n.substring(rn.length());
      if (!s.contains(".")) {
        if (ed.hasSliceName() && ed.getType().size() == 1) {
          typeList.add(new TypeSlice(ed, ed.getTypeFirstRep().getWorkingCode()));
        } else if (!ed.hasSliceName() && !s.equals("[x]")) {
          if (isDataType(s))
            typeList.add(new TypeSlice(ed, s));
          else if (isConstrainedDataType(s))
            typeList.add(new TypeSlice(ed, baseType(s)));
          else if (isPrimitive(Utilities.uncapitalize(s)))
            typeList.add(new TypeSlice(ed, Utilities.uncapitalize(s)));
        } else if (!ed.hasSliceName() && s.equals("[x]"))
          typeList.add(new TypeSlice(ed, null));
      }
    }
    return true;
  }

  private List<ElementRedirection> redirectorStack(List<ElementRedirection> redirector, ElementDefinition outcome,
      String path) {
    List<ElementRedirection> result = new ArrayList<ElementRedirection>();
    result.addAll(redirector);
    result.add(new ElementRedirection(outcome, path));
    return result;
  }

  private List<TypeRefComponent> getByTypeName(List<TypeRefComponent> type, String t) {
    List<TypeRefComponent> res = new ArrayList<TypeRefComponent>();
    for (TypeRefComponent tr : type) {
      if (t.equals(tr.getWorkingCode()))
        res.add(tr);
    }
    return res;
  }

  private void replaceFromContentReference(ElementDefinition outcome, ElementDefinition tgt) {
    outcome.setContentReference(null);
    outcome.getType().clear(); // though it should be clear anyway
    outcome.getType().addAll(tgt.getType());
  }

  private boolean baseWalksInto(List<ElementDefinition> elements, int cursor) {
    if (cursor >= elements.size())
      return false;
    String path = elements.get(cursor).getPath();
    String prevPath = elements.get(cursor - 1).getPath();
    return path.startsWith(prevPath + ".");
  }

  private ElementDefinition overWriteWithCurrent(ElementDefinition profile, ElementDefinition usage)
      throws FHIRFormatError {
    ElementDefinition res = profile.copy();
    if (usage.hasSliceName())
      res.setSliceName(usage.getSliceName());
    if (usage.hasLabel())
      res.setLabel(usage.getLabel());
    for (Coding c : usage.getCode())
      res.addCode(c);

    if (usage.hasDefinition())
      res.setDefinition(usage.getDefinition());
    if (usage.hasShort())
      res.setShort(usage.getShort());
    if (usage.hasComment())
      res.setComment(usage.getComment());
    if (usage.hasRequirements())
      res.setRequirements(usage.getRequirements());
    for (StringType c : usage.getAlias())
      res.addAlias(c.getValue());
    if (usage.hasMin())
      res.setMin(usage.getMin());
    if (usage.hasMax())
      res.setMax(usage.getMax());

    if (usage.hasFixed())
      res.setFixed(usage.getFixed());
    if (usage.hasPattern())
      res.setPattern(usage.getPattern());
    if (usage.hasExample())
      res.setExample(usage.getExample());
    if (usage.hasMinValue())
      res.setMinValue(usage.getMinValue());
    if (usage.hasMaxValue())
      res.setMaxValue(usage.getMaxValue());
    if (usage.hasMaxLength())
      res.setMaxLength(usage.getMaxLength());
    if (usage.hasMustSupport())
      res.setMustSupport(usage.getMustSupport());
    if (usage.hasBinding())
      res.setBinding(usage.getBinding().copy());
    for (ElementDefinitionConstraintComponent c : usage.getConstraint())
      res.addConstraint(c);
    for (Extension e : usage.getExtension()) {
      if (!res.hasExtension(e.getUrl()))
        res.addExtension(e.copy());
    }

    return res;
  }

  private boolean checkExtensionDoco(ElementDefinition base) {
    // see task 3970. For an extension, there's no point copying across all the
    // underlying definitional stuff
    boolean isExtension = base.getPath().equals("Extension") || base.getPath().endsWith(".extension")
        || base.getPath().endsWith(".modifierExtension");
    if (isExtension) {
      base.setDefinition("An Extension");
      base.setShort("Extension");
      base.setCommentElement(null);
      base.setRequirementsElement(null);
      base.getAlias().clear();
      base.getMapping().clear();
    }
    return isExtension;
  }

  private String pathTail(List<ElementDefinition> diffMatches, int i) {

    ElementDefinition d = diffMatches.get(i);
    String s = d.getPath().contains(".") ? d.getPath().substring(d.getPath().lastIndexOf(".") + 1) : d.getPath();
    return "." + s
        + (d.hasType() && d.getType().get(0).hasProfile() ? "[" + d.getType().get(0).getProfile() + "]" : "");
  }

  private void markDerived(ElementDefinition outcome) {
    for (ElementDefinitionConstraintComponent inv : outcome.getConstraint())
      inv.setUserData(IS_DERIVED, true);
  }

  private String summarizeSlicing(ElementDefinitionSlicingComponent slice) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (ElementDefinitionSlicingDiscriminatorComponent d : slice.getDiscriminator()) {
      if (first)
        first = false;
      else
        b.append(", ");
      b.append(d);
    }
    b.append("(");
    if (slice.hasOrdered())
      b.append(slice.getOrderedElement().asStringValue());
    b.append("/");
    if (slice.hasRules())
      b.append(slice.getRules().toCode());
    b.append(")");
    if (slice.hasDescription()) {
      b.append(" \"");
      b.append(slice.getDescription());
      b.append("\"");
    }
    return b.toString();
  }

  private void updateFromBase(ElementDefinition derived, ElementDefinition base) {
    if (base.hasBase()) {
      if (!derived.hasBase())
        derived.setBase(new ElementDefinitionBaseComponent());
      derived.getBase().setPath(base.getBase().getPath());
      derived.getBase().setMin(base.getBase().getMin());
      derived.getBase().setMax(base.getBase().getMax());
    } else {
      if (!derived.hasBase())
        derived.setBase(new ElementDefinitionBaseComponent());
      derived.getBase().setPath(base.getPath());
      derived.getBase().setMin(base.getMin());
      derived.getBase().setMax(base.getMax());
    }
  }

  private boolean pathStartsWith(String p1, String p2) {
    return p1.startsWith(p2);
  }

  private boolean pathMatches(String p1, String p2) {
    return p1.equals(p2) || (p2.endsWith("[x]") && p1.startsWith(p2.substring(0, p2.length() - 3))
        && !p1.substring(p2.length() - 3).contains("."));
  }

  private String fixedPathSource(String contextPath, String pathSimple, List<ElementRedirection> redirector) {
    if (contextPath == null)
      return pathSimple;
//    String ptail = pathSimple.substring(contextPath.length() + 1);
    if (redirector.size() > 0) {
      String ptail = pathSimple.substring(contextPath.length() + 1);
      return redirector.get(redirector.size() - 1).getPath() + "." + ptail;
//      return contextPath+"."+tail(redirector.getPath())+"."+ptail.substring(ptail.indexOf(".")+1);
    } else {
      String ptail = pathSimple.substring(pathSimple.indexOf(".") + 1);
      return contextPath + "." + ptail;
    }
  }

  private String fixedPathDest(String contextPath, String pathSimple, List<ElementRedirection> redirector,
      String redirectSource) {
    String s;
    if (contextPath == null)
      s = pathSimple;
    else {
      if (redirector.size() > 0) {
        String ptail = pathSimple.substring(redirectSource.length() + 1);
        // ptail = ptail.substring(ptail.indexOf(".")+1);
        s = contextPath + "." + /* tail(redirector.getPath())+"."+ */ptail;
      } else {
        String ptail = pathSimple.substring(pathSimple.indexOf(".") + 1);
        s = contextPath + "." + ptail;
      }
    }
    return s;
  }

  private StructureDefinition getProfileForDataType(TypeRefComponent type) {
    StructureDefinition sd = null;
    if (type.hasProfile()) {
      sd = context.fetchResource(StructureDefinition.class, type.getProfile().get(0).getValue());
      if (sd == null)
        log.warn("Failed to find referenced profile: " + type.getProfile());
    }
    if (sd == null)
      sd = context.fetchTypeDefinition(type.getWorkingCode());
    if (sd == null)
      log.warn("XX: failed to find profle for type: " + type.getWorkingCode()); // debug GJM
    return sd;
  }

  private StructureDefinition getProfileForDataType(String type) {
    StructureDefinition sd = context.fetchTypeDefinition(type);
    if (sd == null)
      log.warn("XX: failed to find profile for type: " + type); // debug GJM
    return sd;
  }

  public static String typeCode(List<TypeRefComponent> types) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (TypeRefComponent type : types) {
      if (first)
        first = false;
      else
        b.append(", ");
      b.append(type.getWorkingCode());
      if (type.hasTargetProfile())
        b.append("{" + type.getTargetProfile() + "}");
      else if (type.hasProfile())
        b.append("{" + type.getProfile() + "}");
    }
    return b.toString();
  }

  private boolean isDataType(List<TypeRefComponent> types) {
    if (types.isEmpty())
      return false;
    for (TypeRefComponent type : types) {
      String t = type.getWorkingCode();
      if (!isDataType(t) && !isPrimitive(t))
        return false;
    }
    return true;
  }

  /**
   * Finds internal references in an Element's Binding and StructureDefinition
   * references (in TypeRef) and bases them on the given url
   * 
   * @param url     - the base url to use to turn internal references into
   *                absolute references
   * @param element - the Element to update
   * @return - the updated Element
   */
  private ElementDefinition updateURLs(String url, String webUrl, ElementDefinition element) {
    if (element != null) {
      ElementDefinition defn = element;
      if (defn.hasBinding() && defn.getBinding().hasValueSet() && defn.getBinding().getValueSet().startsWith("#"))
        defn.getBinding().setValueSet(url + defn.getBinding().getValueSet());
      for (TypeRefComponent t : defn.getType()) {
        for (UriType u : t.getProfile()) {
          if (u.getValue().startsWith("#"))
            u.setValue(url + t.getProfile());
        }
        for (UriType u : t.getTargetProfile()) {
          if (u.getValue().startsWith("#"))
            u.setValue(url + t.getTargetProfile());
        }
      }
      if (webUrl != null) {
        // also, must touch up the markdown
        if (element.hasDefinition())
          element.setDefinition(processRelativeUrls(element.getDefinition(), webUrl));
        if (element.hasComment())
          element.setComment(processRelativeUrls(element.getComment(), webUrl));
        if (element.hasRequirements())
          element.setRequirements(processRelativeUrls(element.getRequirements(), webUrl));
        if (element.hasMeaningWhenMissing())
          element.setMeaningWhenMissing(processRelativeUrls(element.getMeaningWhenMissing(), webUrl));
      }
    }
    return element;
  }

  private String processRelativeUrls(String markdown, String webUrl) {
    StringBuilder b = new StringBuilder();
    int i = 0;
    while (i < markdown.length()) {
      if (i < markdown.length() - 3 && markdown.substring(i, i + 2).equals("](")) {
        int j = i + 2;
        while (j < markdown.length() && markdown.charAt(j) != ')')
          j++;
        if (j < markdown.length()) {
          String url = markdown.substring(i + 2, j);
          if (!Utilities.isAbsoluteUrl(url)) {
            b.append("](");
            b.append(webUrl);
            i = i + 1;
          } else
            b.append(markdown.charAt(i));
        } else
          b.append(markdown.charAt(i));
      } else {
        b.append(markdown.charAt(i));
      }
      i++;
    }
    return b.toString();
  }

  private List<ElementDefinition> getSiblings(List<ElementDefinition> list, ElementDefinition current) {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    String path = current.getPath();
    int cursor = list.indexOf(current) + 1;
    while (cursor < list.size() && list.get(cursor).getPath().length() >= path.length()) {
      if (pathMatches(list.get(cursor).getPath(), path))
        result.add(list.get(cursor));
      cursor++;
    }
    return result;
  }

  private void updateFromSlicing(ElementDefinitionSlicingComponent dst, ElementDefinitionSlicingComponent src) {
    if (src.hasOrderedElement())
      dst.setOrderedElement(src.getOrderedElement().copy());
    if (src.hasDiscriminator()) {
      // dst.getDiscriminator().addAll(src.getDiscriminator()); Can't use addAll
      // because it uses object equality, not string equality
      for (ElementDefinitionSlicingDiscriminatorComponent s : src.getDiscriminator()) {
        boolean found = false;
        for (ElementDefinitionSlicingDiscriminatorComponent d : dst.getDiscriminator()) {
          if (matches(d, s)) {
            found = true;
            break;
          }
        }
        if (!found)
          dst.getDiscriminator().add(s);
      }
    }
    if (src.hasRulesElement())
      dst.setRulesElement(src.getRulesElement().copy());
  }

  private boolean orderMatches(BooleanType diff, BooleanType base) {
    return (diff == null) || (base == null) || (diff.getValue() == base.getValue());
  }

  private boolean discriminatorMatches(List<ElementDefinitionSlicingDiscriminatorComponent> diff,
      List<ElementDefinitionSlicingDiscriminatorComponent> base) {
    if (diff.isEmpty() || base.isEmpty())
      return true;
    if (diff.size() != base.size())
      return false;
    for (int i = 0; i < diff.size(); i++)
      if (!matches(diff.get(i), base.get(i)))
        return false;
    return true;
  }

  private boolean matches(ElementDefinitionSlicingDiscriminatorComponent c1,
      ElementDefinitionSlicingDiscriminatorComponent c2) {
    return c1.getType().equals(c2.getType()) && c1.getPath().equals(c2.getPath());
  }

  private boolean ruleMatches(SlicingRules diff, SlicingRules base) {
    return (diff == null) || (base == null) || (diff == base) || (base == SlicingRules.OPEN)
        || ((diff == SlicingRules.OPENATEND && base == SlicingRules.CLOSED));
  }

  private boolean isSlicedToOneOnly(ElementDefinition e) {
    return (e.hasSlicing() && e.hasMaxElement() && e.getMax().equals("1"));
  }

  private ElementDefinitionSlicingComponent makeExtensionSlicing() {
    ElementDefinitionSlicingComponent slice = new ElementDefinitionSlicingComponent();
    slice.addDiscriminator().setPath("url").setType(DiscriminatorType.VALUE);
    slice.setOrdered(false);
    slice.setRules(SlicingRules.OPEN);
    return slice;
  }

  private boolean isExtension(ElementDefinition currentBase) {
    return currentBase.getPath().endsWith(".extension") || currentBase.getPath().endsWith(".modifierExtension");
  }

  private boolean hasInnerDiffMatches(StructureDefinitionDifferentialComponent context, String path, int start, int end,
      List<ElementDefinition> base, boolean allowSlices) throws DefinitionException {
    end = Math.min(context.getElement().size(), end);
    start = Math.max(0, start);

    for (int i = start; i <= end; i++) {
      String statedPath = context.getElement().get(i).getPath();
      if (statedPath.startsWith(path + ".")) {
        return true;
      } else if (!statedPath.endsWith(path))
        break;
    }
    return false;
  }

  private List<ElementDefinition> getDiffMatches(StructureDefinitionDifferentialComponent context, String path,
      int start, int end, String profileName) throws DefinitionException {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    for (int i = start; i <= end; i++) {
      String statedPath = context.getElement().get(i).getPath();
      if (statedPath.equals(path) || (path.endsWith("[x]") && statedPath.length() > path.length() - 2
          && statedPath.substring(0, path.length() - 3).equals(path.substring(0, path.length() - 3))
          && (statedPath.length() < path.length() || !statedPath.substring(path.length()).contains(".")))) {
        /*
         * Commenting this out because it raises warnings when profiling inherited
         * elements. For example, Error: unknown element 'Bundle.meta.profile' (or it is
         * out of order) in profile ... (looking for 'Bundle.entry') Not sure we have
         * enough information here to do the check properly. Might be better done when
         * we're sorting the profile?
         * 
         * if (i != start && result.isEmpty() &&
         * !path.startsWith(context.getElement().get(start).getPath())) messages.add(new
         * ValidationMessage(Source.ProfileValidator, IssueType.VALUE,
         * "StructureDefinition.differential.element["+Integer.toString(start)+"]",
         * "Error: unknown element '"+context.getElement().get(start).getPath()
         * +"' (or it is out of order) in profile '"+url+"' (looking for '"+path+"')",
         * IssueSeverity.WARNING));
         * 
         */
        result.add(context.getElement().get(i));
      }
    }
    return result;
  }

  private int findEndOfElement(StructureDefinitionDifferentialComponent context, int cursor) {
    int result = cursor;
    String path = context.getElement().get(cursor).getPath() + ".";
    while (result < context.getElement().size() - 1 && context.getElement().get(result + 1).getPath().startsWith(path))
      result++;
    return result;
  }

  private int findEndOfElement(StructureDefinitionSnapshotComponent context, int cursor) {
    int result = cursor;
    String path = context.getElement().get(cursor).getPath() + ".";
    while (result < context.getElement().size() - 1 && context.getElement().get(result + 1).getPath().startsWith(path))
      result++;
    return result;
  }

  private boolean unbounded(ElementDefinition definition) {
    StringType max = definition.getMaxElement();
    if (max == null)
      return false; // this is not valid
    if (max.getValue().equals("1"))
      return false;
    if (max.getValue().equals("0"))
      return false;
    return true;
  }

  private void updateFromDefinition(ElementDefinition dest, ElementDefinition source, String pn,
      boolean trimDifferential, String purl, StructureDefinition srcSD) throws DefinitionException, FHIRException {
    source.setUserData(GENERATED_IN_SNAPSHOT, dest);
    // we start with a clone of the base profile ('dest') and we copy from the
    // profile ('source')
    // over the top for anything the source has
    ElementDefinition base = dest;
    ElementDefinition derived = source;
    derived.setUserData(DERIVATION_POINTER, base);
    boolean isExtension = checkExtensionDoco(base);

    // Before applying changes, apply them to what's in the profile
    // TODO: follow Chris's rules - Done by Lloyd
    StructureDefinition profile = null;
    if (base.hasSliceName())
      profile = base.getType().size() == 1 && base.getTypeFirstRep().hasProfile()
          ? context.fetchResource(StructureDefinition.class, base.getTypeFirstRep().getProfile().get(0).getValue())
          : null;
    if (profile == null)
      profile = source.getType().size() == 1 && source.getTypeFirstRep().hasProfile()
          ? context.fetchResource(StructureDefinition.class, source.getTypeFirstRep().getProfile().get(0).getValue())
          : null;
    if (profile != null) {
      ElementDefinition e = profile.getSnapshot().getElement().get(0);
      base.setDefinition(e.getDefinition());
      base.setShort(e.getShort());
      if (e.hasCommentElement())
        base.setCommentElement(e.getCommentElement());
      if (e.hasRequirementsElement())
        base.setRequirementsElement(e.getRequirementsElement());
      base.getAlias().clear();
      base.getAlias().addAll(e.getAlias());
      base.getMapping().clear();
      base.getMapping().addAll(e.getMapping());
    }
    if (derived != null) {
      if (derived.hasSliceName()) {
        base.setSliceName(derived.getSliceName());
      }

      if (derived.hasShortElement()) {
        if (!Base.compareDeep(derived.getShortElement(), base.getShortElement(), false))
          base.setShortElement(derived.getShortElement().copy());
        else if (trimDifferential)
          derived.setShortElement(null);
        else if (derived.hasShortElement())
          derived.getShortElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasDefinitionElement()) {
        if (derived.getDefinition().startsWith("..."))
          base.setDefinition(Utilities.appendDerivedTextToBase(base.getDefinition(), derived.getDefinition()));
        else if (!Base.compareDeep(derived.getDefinitionElement(), base.getDefinitionElement(), false))
          base.setDefinitionElement(derived.getDefinitionElement().copy());
        else if (trimDifferential)
          derived.setDefinitionElement(null);
        else if (derived.hasDefinitionElement())
          derived.getDefinitionElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasCommentElement()) {
        if (derived.getComment().startsWith("..."))
          base.setComment(Utilities.appendDerivedTextToBase(base.getComment(), derived.getComment()));
        else if (derived.hasCommentElement() != base.hasCommentElement()
            || !Base.compareDeep(derived.getCommentElement(), base.getCommentElement(), false))
          base.setCommentElement(derived.getCommentElement().copy());
        else if (trimDifferential)
          base.setCommentElement(derived.getCommentElement().copy());
        else if (derived.hasCommentElement())
          derived.getCommentElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasLabelElement()) {
        if (derived.getLabel().startsWith("..."))
          base.setLabel(Utilities.appendDerivedTextToBase(base.getLabel(), derived.getLabel()));
        else if (!base.hasLabelElement() || !Base.compareDeep(derived.getLabelElement(), base.getLabelElement(), false))
          base.setLabelElement(derived.getLabelElement().copy());
        else if (trimDifferential)
          base.setLabelElement(derived.getLabelElement().copy());
        else if (derived.hasLabelElement())
          derived.getLabelElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasRequirementsElement()) {
        if (derived.getRequirements().startsWith("..."))
          base.setRequirements(Utilities.appendDerivedTextToBase(base.getRequirements(), derived.getRequirements()));
        else if (!base.hasRequirementsElement()
            || !Base.compareDeep(derived.getRequirementsElement(), base.getRequirementsElement(), false))
          base.setRequirementsElement(derived.getRequirementsElement().copy());
        else if (trimDifferential)
          base.setRequirementsElement(derived.getRequirementsElement().copy());
        else if (derived.hasRequirementsElement())
          derived.getRequirementsElement().setUserData(DERIVATION_EQUALS, true);
      }
      // sdf-9
      if (derived.hasRequirements() && !base.getPath().contains("."))
        derived.setRequirements(null);
      if (base.hasRequirements() && !base.getPath().contains("."))
        base.setRequirements(null);

      if (derived.hasAlias()) {
        if (!Base.compareDeep(derived.getAlias(), base.getAlias(), false))
          for (StringType s : derived.getAlias()) {
            if (!base.hasAlias(s.getValue()))
              base.getAlias().add(s.copy());
          }
        else if (trimDifferential)
          derived.getAlias().clear();
        else
          for (StringType t : derived.getAlias())
            t.setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMinElement()) {
        if (!Base.compareDeep(derived.getMinElement(), base.getMinElement(), false)) {
          if (derived.getMin() < base.getMin() && !derived.hasSliceName()) // in a slice, minimum cardinality rules do
                                                                           // not apply
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                pn + "." + source.getPath(),
                "Element " + base.getPath() + ": derived min (" + Integer.toString(derived.getMin())
                    + ") cannot be less than base min (" + Integer.toString(base.getMin()) + ")",
                ValidationMessage.IssueSeverity.ERROR));
          base.setMinElement(derived.getMinElement().copy());
        } else if (trimDifferential)
          derived.setMinElement(null);
        else
          derived.getMinElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMaxElement()) {
        if (!Base.compareDeep(derived.getMaxElement(), base.getMaxElement(), false)) {
          if (isLargerMax(derived.getMax(), base.getMax()))
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                pn + "." + source.getPath(),
                "Element " + base.getPath() + ": derived max (" + derived.getMax()
                    + ") cannot be greater than base max (" + base.getMax() + ")",
                ValidationMessage.IssueSeverity.ERROR));
          base.setMaxElement(derived.getMaxElement().copy());
        } else if (trimDifferential)
          derived.setMaxElement(null);
        else
          derived.getMaxElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasFixed()) {
        if (!Base.compareDeep(derived.getFixed(), base.getFixed(), true)) {
          base.setFixed(derived.getFixed().copy());
        } else if (trimDifferential)
          derived.setFixed(null);
        else
          derived.getFixed().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasPattern()) {
        if (!Base.compareDeep(derived.getPattern(), base.getPattern(), false)) {
          base.setPattern(derived.getPattern().copy());
        } else if (trimDifferential)
          derived.setPattern(null);
        else
          derived.getPattern().setUserData(DERIVATION_EQUALS, true);
      }

      for (ElementDefinitionExampleComponent ex : derived.getExample()) {
        boolean found = false;
        for (ElementDefinitionExampleComponent exS : base.getExample())
          if (Base.compareDeep(ex, exS, false))
            found = true;
        if (!found)
          base.addExample(ex.copy());
        else if (trimDifferential)
          derived.getExample().remove(ex);
        else
          ex.setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMaxLengthElement()) {
        if (!Base.compareDeep(derived.getMaxLengthElement(), base.getMaxLengthElement(), false))
          base.setMaxLengthElement(derived.getMaxLengthElement().copy());
        else if (trimDifferential)
          derived.setMaxLengthElement(null);
        else
          derived.getMaxLengthElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMaxValue()) {
        if (!Base.compareDeep(derived.getMaxValue(), base.getMaxValue(), false))
          base.setMaxValue(derived.getMaxValue().copy());
        else if (trimDifferential)
          derived.setMaxValue(null);
        else
          derived.getMaxValue().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMinValue()) {
        if (!Base.compareDeep(derived.getMinValue(), base.getMinValue(), false))
          base.setMinValue(derived.getMinValue().copy());
        else if (trimDifferential)
          derived.setMinValue(null);
        else
          derived.getMinValue().setUserData(DERIVATION_EQUALS, true);
      }

      // todo: what to do about conditions?
      // condition : id 0..*

      if (derived.hasMustSupportElement()) {
        if (!(base.hasMustSupportElement()
            && Base.compareDeep(derived.getMustSupportElement(), base.getMustSupportElement(), false)))
          base.setMustSupportElement(derived.getMustSupportElement().copy());
        else if (trimDifferential)
          derived.setMustSupportElement(null);
        else
          derived.getMustSupportElement().setUserData(DERIVATION_EQUALS, true);
      }

      // profiles cannot change : isModifier, defaultValue, meaningWhenMissing
      // but extensions can change isModifier
      if (isExtension) {
        if (derived.hasIsModifierElement() && !(base.hasIsModifierElement()
            && Base.compareDeep(derived.getIsModifierElement(), base.getIsModifierElement(), false)))
          base.setIsModifierElement(derived.getIsModifierElement().copy());
        else if (trimDifferential)
          derived.setIsModifierElement(null);
        else if (derived.hasIsModifierElement())
          derived.getIsModifierElement().setUserData(DERIVATION_EQUALS, true);
        if (derived.hasIsModifierReasonElement() && !(base.hasIsModifierReasonElement()
            && Base.compareDeep(derived.getIsModifierReasonElement(), base.getIsModifierReasonElement(), false)))
          base.setIsModifierReasonElement(derived.getIsModifierReasonElement().copy());
        else if (trimDifferential)
          derived.setIsModifierReasonElement(null);
        else if (derived.hasIsModifierReasonElement())
          derived.getIsModifierReasonElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasBinding()) {
        if (!base.hasBinding() || !Base.compareDeep(derived.getBinding(), base.getBinding(), false)) {
          if (base.hasBinding() && base.getBinding().getStrength() == BindingStrength.REQUIRED
              && derived.getBinding().getStrength() != BindingStrength.REQUIRED)
            messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                pn + "." + derived.getPath(),
                "illegal attempt to change the binding on " + derived.getPath() + " from "
                    + base.getBinding().getStrength().toCode() + " to " + derived.getBinding().getStrength().toCode(),
                ValidationMessage.IssueSeverity.ERROR));
//            throw new DefinitionException("StructureDefinition "+pn+" at "+derived.getPath()+": illegal attempt to change a binding from "+base.getBinding().getStrength().toCode()+" to "+derived.getBinding().getStrength().toCode());
          else if (base.hasBinding() && derived.hasBinding()
              && base.getBinding().getStrength() == BindingStrength.REQUIRED && base.getBinding().hasValueSet()
              && derived.getBinding().hasValueSet()) {
            ValueSet baseVs = context.fetchResource(ValueSet.class, base.getBinding().getValueSet());
            ValueSet contextVs = context.fetchResource(ValueSet.class, derived.getBinding().getValueSet());
            if (baseVs == null) {
              messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                  pn + "." + base.getPath(), "Binding " + base.getBinding().getValueSet() + " could not be located",
                  ValidationMessage.IssueSeverity.WARNING));
            } else if (contextVs == null) {
              messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                  pn + "." + derived.getPath(),
                  "Binding " + derived.getBinding().getValueSet() + " could not be located",
                  ValidationMessage.IssueSeverity.WARNING));
            } else {
              ValueSetExpansionOutcome expBase = context.expandVS(baseVs, true, false);
              ValueSetExpansionOutcome expDerived = context.expandVS(contextVs, true, false);
              if (expBase.getValueset() == null)
                messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                    pn + "." + base.getPath(), "Binding " + base.getBinding().getValueSet() + " could not be expanded",
                    ValidationMessage.IssueSeverity.WARNING));
              else if (expDerived.getValueset() == null)
                messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                    pn + "." + derived.getPath(),
                    "Binding " + derived.getBinding().getValueSet() + " could not be expanded",
                    ValidationMessage.IssueSeverity.WARNING));
              else if (!isSubset(expBase.getValueset(), expDerived.getValueset()))
                messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
                    pn + "." + derived.getPath(), "Binding " + derived.getBinding().getValueSet()
                        + " is not a subset of binding " + base.getBinding().getValueSet(),
                    ValidationMessage.IssueSeverity.ERROR));

            }
          }
          base.setBinding(derived.getBinding().copy());
        } else if (trimDifferential)
          derived.setBinding(null);
        else
          derived.getBinding().setUserData(DERIVATION_EQUALS, true);
      } // else if (base.hasBinding() && doesn't have bindable type )
        // base

      if (derived.hasIsSummaryElement()) {
        if (!Base.compareDeep(derived.getIsSummaryElement(), base.getIsSummaryElement(), false)) {
          if (base.hasIsSummary())
            throw new Error("Error in profile " + pn + " at " + derived.getPath() + ": Base isSummary = "
                + base.getIsSummaryElement().asStringValue() + ", derived isSummary = "
                + derived.getIsSummaryElement().asStringValue());
          base.setIsSummaryElement(derived.getIsSummaryElement().copy());
        } else if (trimDifferential)
          derived.setIsSummaryElement(null);
        else
          derived.getIsSummaryElement().setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasType()) {
        if (!Base.compareDeep(derived.getType(), base.getType(), false)) {
          if (base.hasType()) {
            for (TypeRefComponent ts : derived.getType()) {
//              if (!ts.hasCode()) { // ommitted in the differential; copy it over....
//                if (base.getType().size() > 1) 
//                  throw new DefinitionException("StructureDefinition "+pn+" at "+derived.getPath()+": constrained type code must be present if there are multiple types ("+base.typeSummary()+")");
//                if (base.getType().get(0).getCode() != null)
//                  ts.setCode(base.getType().get(0).getCode());
//              }
              boolean ok = false;
              CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
              String t = ts.getWorkingCode();
              for (TypeRefComponent td : base.getType()) {
                ;
                String tt = td.getWorkingCode();
                b.append(tt);
                if (td.hasCode()
                    && (tt.equals(t) || "Extension".equals(tt) || (t.equals("uri") && tt.equals("string")) || // work
                                                                                                              // around
                                                                                                              // for old
                                                                                                              // badly
                                                                                                              // generated
                                                                                                              // SDs
                        "Element".equals(tt) || "*".equals(tt)
                        || (("Resource".equals(tt) || ("DomainResource".equals(tt)) && pkp.isResource(t)))))
                  ok = true;
              }
              if (!ok)
                throw new DefinitionException("StructureDefinition " + pn + " at " + derived.getPath()
                    + ": illegal constrained type " + t + " from " + b.toString() + " in " + srcSD.getUrl());
            }
          }
          base.getType().clear();
          for (TypeRefComponent t : derived.getType()) {
            TypeRefComponent tt = t.copy();
//            tt.setUserData(DERIVATION_EQUALS, true);
            base.getType().add(tt);
          }
        } else if (trimDifferential)
          derived.getType().clear();
        else
          for (TypeRefComponent t : derived.getType())
            t.setUserData(DERIVATION_EQUALS, true);
      }

      if (derived.hasMapping()) {
        // todo: mappings are not cumulative - one replaces another
        if (!Base.compareDeep(derived.getMapping(), base.getMapping(), false)) {
          for (ElementDefinitionMappingComponent s : derived.getMapping()) {
            boolean found = false;
            for (ElementDefinitionMappingComponent d : base.getMapping()) {
              found = found || (d.getIdentity().equals(s.getIdentity()) && d.getMap().equals(s.getMap()));
            }
            if (!found)
              base.getMapping().add(s);
          }
        } else if (trimDifferential)
          derived.getMapping().clear();
        else
          for (ElementDefinitionMappingComponent t : derived.getMapping())
            t.setUserData(DERIVATION_EQUALS, true);
      }

      // todo: constraints are cumulative. there is no replacing
      for (ElementDefinitionConstraintComponent s : base.getConstraint()) {
        s.setUserData(IS_DERIVED, true);
        if (!s.hasSource())
          s.setSource(base.getId());
      }
      if (derived.hasConstraint()) {
        for (ElementDefinitionConstraintComponent s : derived.getConstraint()) {
          ElementDefinitionConstraintComponent inv = s.copy();
          base.getConstraint().add(inv);
        }
      }

      // now, check that we still have a bindable type; if not, delete the binding -
      // see task 8477
      if (dest.hasBinding() && !hasBindableType(dest))
        dest.setBinding(null);

      // finally, we copy any extensions from source to dest
      for (Extension ex : derived.getExtension()) {
        StructureDefinition sd = context.fetchResource(StructureDefinition.class, ex.getUrl());
        if (sd == null || sd.getSnapshot() == null || sd.getSnapshot().getElementFirstRep().getMax().equals("1"))
          ToolingExtensions.removeExtension(dest, ex.getUrl());
        dest.addExtension(ex.copy());
      }
    }
  }

  private boolean hasBindableType(ElementDefinition ed) {
    for (TypeRefComponent tr : ed.getType()) {
      if (Utilities.existsInList(tr.getWorkingCode(), "Coding", "CodeableConcept", "Quantity", "uri", "string", "code"))
        return true;
    }
    return false;
  }

  private boolean isLargerMax(String derived, String base) {
    if ("*".equals(base))
      return false;
    if ("*".equals(derived))
      return true;
    return Integer.parseInt(derived) > Integer.parseInt(base);
  }

  private boolean isSubset(ValueSet expBase, ValueSet expDerived) {
    return codesInExpansion(expDerived.getExpansion().getContains(), expBase.getExpansion());
  }

  private boolean codesInExpansion(List<ValueSetExpansionContainsComponent> contains,
      ValueSetExpansionComponent expansion) {
    for (ValueSetExpansionContainsComponent cc : contains) {
      if (!inExpansion(cc, expansion.getContains()))
        return false;
      if (!codesInExpansion(cc.getContains(), expansion))
        return false;
    }
    return true;
  }

  private boolean inExpansion(ValueSetExpansionContainsComponent cc,
      List<ValueSetExpansionContainsComponent> contains) {
    for (ValueSetExpansionContainsComponent cc1 : contains) {
      if (cc.getSystem().equals(cc1.getSystem()) && cc.getCode().equals(cc1.getCode()))
        return true;
      if (inExpansion(cc, cc1.getContains()))
        return true;
    }
    return false;
  }

  public void closeDifferential(StructureDefinition base, StructureDefinition derived) throws FHIRException {
    for (ElementDefinition edb : base.getSnapshot().getElement()) {
      if (isImmediateChild(edb) && !edb.getPath().endsWith(".id")) {
        ElementDefinition edm = getMatchInDerived(edb, derived.getDifferential().getElement());
        if (edm == null) {
          ElementDefinition edd = derived.getDifferential().addElement();
          edd.setPath(edb.getPath());
          edd.setMax("0");
        } else if (edb.hasSlicing()) {
          closeChildren(base, edb, derived, edm);
        }
      }
    }
    sortDifferential(base, derived, derived.getName(), new ArrayList<String>());
  }

  private void closeChildren(StructureDefinition base, ElementDefinition edb, StructureDefinition derived,
      ElementDefinition edm) {
    String path = edb.getPath() + ".";
    int baseStart = base.getSnapshot().getElement().indexOf(edb);
    int baseEnd = findEnd(base.getSnapshot().getElement(), edb, baseStart + 1);
    int diffStart = derived.getDifferential().getElement().indexOf(edm);
    int diffEnd = findEnd(derived.getDifferential().getElement(), edm, diffStart + 1);

    for (int cBase = baseStart; cBase < baseEnd; cBase++) {
      ElementDefinition edBase = base.getSnapshot().getElement().get(cBase);
      if (isImmediateChild(edBase, edb)) {
        ElementDefinition edMatch = getMatchInDerived(edBase, derived.getDifferential().getElement(), diffStart,
            diffEnd);
        if (edMatch == null) {
          ElementDefinition edd = derived.getDifferential().addElement();
          edd.setPath(edBase.getPath());
          edd.setMax("0");
        } else {
          closeChildren(base, edBase, derived, edMatch);
        }
      }
    }
  }

  private int findEnd(List<ElementDefinition> list, ElementDefinition ed, int cursor) {
    String path = ed.getPath() + ".";
    while (cursor < list.size() && list.get(cursor).getPath().startsWith(path))
      cursor++;
    return cursor;
  }

  private ElementDefinition getMatchInDerived(ElementDefinition ed, List<ElementDefinition> list) {
    for (ElementDefinition t : list)
      if (t.getPath().equals(ed.getPath()))
        return t;
    return null;
  }

  private ElementDefinition getMatchInDerived(ElementDefinition ed, List<ElementDefinition> list, int start, int end) {
    for (int i = start; i < end; i++) {
      ElementDefinition t = list.get(i);
      if (t.getPath().equals(ed.getPath()))
        return t;
    }
    return null;
  }

  private boolean isImmediateChild(ElementDefinition ed) {
    String p = ed.getPath();
    if (!p.contains("."))
      return false;
    p = p.substring(p.indexOf(".") + 1);
    return !p.contains(".");
  }

  private boolean isImmediateChild(ElementDefinition candidate, ElementDefinition base) {
    String p = candidate.getPath();
    if (!p.contains("."))
      return false;
    if (!p.startsWith(base.getPath() + "."))
      return false;
    p = p.substring(base.getPath().length() + 1);
    return !p.contains(".");
  }

  public XhtmlNode generateExtensionTable(String defFile, StructureDefinition ed, String imageFolder,
      boolean inlineGraphics, boolean full, String corePath, String imagePath, Set<String> outputTracker)
      throws IOException, FHIRException {
    HierarchicalTableGenerator gen = new HierarchicalTableGenerator(new RenderingI18nContext(), imageFolder, inlineGraphics, true);
    TableModel model = gen.initNormalTable(corePath, false, true, ed.getId(), false, TableGenerationMode.XML);

    boolean deep = false;
    String m = "";
    boolean vdeep = false;
    if (ed.getSnapshot().getElementFirstRep().getIsModifier())
      m = "modifier_";
    for (ElementDefinition eld : ed.getSnapshot().getElement()) {
      deep = deep || eld.getPath().contains("Extension.extension.");
      vdeep = vdeep || eld.getPath().contains("Extension.extension.extension.");
    }
    Row r = gen.new Row();
    model.getRows().add(r);
    String en;
    if (!full)
      en = ed.getName();
    else if (ed.getSnapshot().getElement().get(0).getIsModifier())
      en = "modifierExtension";
    else
      en = "extension";

    r.getCells().add(gen.new Cell(null, defFile == null ? "" : defFile + "-definitions.html#extension." + ed.getName(),
        en, null, null));
    r.getCells().add(gen.new Cell());
    r.getCells().add(gen.new Cell(null, null,
        describeCardinality(ed.getSnapshot().getElement().get(0), null, new UnusedTracker()), null, null));

    ElementDefinition ved = null;
    if (full || vdeep) {
      r.getCells().add(gen.new Cell("", "", "Extension", null, null));

      r.setIcon(deep ? "icon_" + m + "extension_complex.png" : "icon_extension_simple.png",
          deep ? HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX
              : HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
      List<ElementDefinition> children = getChildren(ed.getSnapshot().getElement(),
          ed.getSnapshot().getElement().get(0));
      for (ElementDefinition child : children)
        if (!child.getPath().endsWith(".id"))
          genElement(defFile == null ? "" : defFile + "-definitions.html#extension.", gen, r.getSubRows(), child,
              ed.getSnapshot().getElement(), null, true, defFile, true, full, corePath, imagePath, true, false, false,
              false, null);
    } else if (deep) {
      List<ElementDefinition> children = new ArrayList<ElementDefinition>();
      for (ElementDefinition ted : ed.getSnapshot().getElement()) {
        if (ted.getPath().equals("Extension.extension"))
          children.add(ted);
      }

      r.getCells().add(gen.new Cell("", "", "Extension", null, null));
      r.setIcon("icon_" + m + "extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);

      for (ElementDefinition c : children) {
        ved = getValueFor(ed, c);
        ElementDefinition ued = getUrlFor(ed, c);
        if (ved != null && ued != null) {
          Row r1 = gen.new Row();
          r.getSubRows().add(r1);
          r1.getCells()
              .add(gen.new Cell(null, defFile == null ? "" : defFile + "-definitions.html#extension." + ed.getName(),
                  ((UriType) ued.getFixed()).getValue(), null, null));
          r1.getCells().add(gen.new Cell());
          r1.getCells().add(gen.new Cell(null, null, describeCardinality(c, null, new UnusedTracker()), null, null));
          genTypes(gen, r1, ved, defFile, ed, corePath, imagePath);
          Cell cell = gen.new Cell();
          cell.addMarkdown(c.getDefinition());
          r1.getCells().add(cell);
          r1.setIcon("icon_" + m + "extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
        }
      }
    } else {
      for (ElementDefinition ted : ed.getSnapshot().getElement()) {
        if (ted.getPath().startsWith("Extension.value"))
          ved = ted;
      }

      genTypes(gen, r, ved, defFile, ed, corePath, imagePath);

      r.setIcon("icon_" + m + "extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
    }
    Cell c = gen.new Cell("", "", "URL = " + ed.getUrl(), null, null);
    Piece cc = gen.new Piece(null, ed.getName() + ": ", null);
    c.addPiece(gen.new Piece("br")).addPiece(cc);
    c.addMarkdown(ed.getDescription());

    if (!full && !(deep || vdeep) && ved != null && ved.hasBinding()) {
      c.addPiece(gen.new Piece("br"));
      BindingResolution br = pkp.resolveBinding(ed, ved.getBinding(), ved.getPath());
      c.getPieces().add(checkForNoChange(ved.getBinding(),
          gen.new Piece(null, translate("sd.table", "Binding") + ": ", null).addStyle("font-weight:bold")));
      c.getPieces()
          .add(checkForNoChange(ved.getBinding(),
              gen.new Piece(
                  br.url == null ? null
                      : Utilities.isAbsoluteUrl(br.url) || !pkp.prependLinks() ? br.url : corePath + br.url,
                  br.display, null)));
      if (ved.getBinding().hasStrength()) {
        c.getPieces().add(checkForNoChange(ved.getBinding(), gen.new Piece(null, " (", null)));
        c.getPieces()
            .add(checkForNoChange(ved.getBinding(),
                gen.new Piece(corePath + "terminologies.html#" + ved.getBinding().getStrength().toCode(),
                    egt(ved.getBinding().getStrengthElement()), ved.getBinding().getStrength().getDefinition())));
        c.getPieces().add(gen.new Piece(null, ")", null));
      }
    }
    c.addPiece(gen.new Piece("br")).addPiece(gen.new Piece(null, describeExtensionContext(ed), null));
    r.getCells().add(c);

    try {
      return gen.generate(model, corePath, 0, outputTracker);
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException(e.getMessage(), e);
    }
  }

  private ElementDefinition getUrlFor(StructureDefinition ed, ElementDefinition c) {
    int i = ed.getSnapshot().getElement().indexOf(c) + 1;
    while (i < ed.getSnapshot().getElement().size()
        && ed.getSnapshot().getElement().get(i).getPath().startsWith(c.getPath() + ".")) {
      if (ed.getSnapshot().getElement().get(i).getPath().equals(c.getPath() + ".url"))
        return ed.getSnapshot().getElement().get(i);
      i++;
    }
    return null;
  }

  private ElementDefinition getValueFor(StructureDefinition ed, ElementDefinition c) {
    int i = ed.getSnapshot().getElement().indexOf(c) + 1;
    while (i < ed.getSnapshot().getElement().size()
        && ed.getSnapshot().getElement().get(i).getPath().startsWith(c.getPath() + ".")) {
      if (ed.getSnapshot().getElement().get(i).getPath().startsWith(c.getPath() + ".value"))
        return ed.getSnapshot().getElement().get(i);
      i++;
    }
    return null;
  }

  private static final int AGG_NONE = 0;
  private static final int AGG_IND = 1;
  private static final int AGG_GR = 2;
  private static final boolean TABLE_FORMAT_FOR_FIXED_VALUES = false;

  private Cell genTypes(HierarchicalTableGenerator gen, Row r, ElementDefinition e, String profileBaseFileName,
      StructureDefinition profile, String corePath, String imagePath) {
    Cell c = gen.new Cell();
    r.getCells().add(c);
    List<TypeRefComponent> types = e.getType();
    if (!e.hasType()) {
      if (e.hasContentReference()) {
        return c;
      } else {
        ElementDefinition d = (ElementDefinition) e.getUserData(DERIVATION_POINTER);
        if (d != null && d.hasType()) {
          types = new ArrayList<ElementDefinition.TypeRefComponent>();
          for (TypeRefComponent tr : d.getType()) {
            TypeRefComponent tt = tr.copy();
            tt.setUserData(DERIVATION_EQUALS, true);
            types.add(tt);
          }
        } else
          return c;
      }
    }

    boolean first = true;

    TypeRefComponent tl = null;
    for (TypeRefComponent t : types) {
      if (first)
        first = false;
      else
        c.addPiece(checkForNoChange(tl, gen.new Piece(null, ", ", null)));
      tl = t;
      if (t.hasTarget()) {
        c.getPieces().add(gen.new Piece(corePath + "references.html", t.getWorkingCode(), null));
        c.getPieces().add(gen.new Piece(null, "(", null));
        boolean tfirst = true;
        for (UriType u : t.getTargetProfile()) {
          if (tfirst)
            tfirst = false;
          else
            c.addPiece(gen.new Piece(null, " | ", null));
          genTargetLink(gen, profileBaseFileName, corePath, c, t, u.getValue());
        }
        c.getPieces().add(gen.new Piece(null, ")", null));
        if (t.getAggregation().size() > 0) {
          c.getPieces().add(gen.new Piece(corePath + "valueset-resource-aggregation-mode.html", " {", null));
          boolean firstA = true;
          for (Enumeration<AggregationMode> a : t.getAggregation()) {
            if (firstA = true)
              firstA = false;
            else
              c.getPieces().add(gen.new Piece(corePath + "valueset-resource-aggregation-mode.html", ", ", null));
            c.getPieces().add(gen.new Piece(corePath + "valueset-resource-aggregation-mode.html",
                codeForAggregation(a.getValue()), hintForAggregation(a.getValue())));
          }
          c.getPieces().add(gen.new Piece(corePath + "valueset-resource-aggregation-mode.html", "}", null));
        }
      } else if (t.hasProfile() && (!t.getWorkingCode().equals("Extension") || isProfiledType(t.getProfile()))) { // a
                                                                                                                  // profiled
                                                                                                                  // type
        String ref;
        ref = pkp.getLinkForProfile(profile, t.getProfile().get(0).getValue());
        if (ref != null) {
          String[] parts = ref.split("\\|");
          if (parts[0].startsWith("http:") || parts[0].startsWith("https:")) {
//            c.addPiece(checkForNoChange(t, gen.new Piece(parts[0], "<" + parts[1] + ">", t.getCode()))); Lloyd
            c.addPiece(checkForNoChange(t, gen.new Piece(parts[0], parts[1], t.getWorkingCode())));
          } else {
//            c.addPiece(checkForNoChange(t, gen.new Piece((t.getProfile().startsWith(corePath)? corePath: "")+parts[0], "<" + parts[1] + ">", t.getCode())));
            c.addPiece(checkForNoChange(t,
                gen.new Piece(
                    (t.getProfile().get(0).getValue().startsWith(corePath + "StructureDefinition") ? corePath : "")
                        + parts[0],
                    parts[1], t.getWorkingCode())));
          }
        } else
          c.addPiece(checkForNoChange(t,
              gen.new Piece((t.getProfile().get(0).getValue().startsWith(corePath) ? corePath : "") + ref,
                  t.getWorkingCode(), null)));
      } else {
        String tc = t.getWorkingCode();
        if (pkp != null && pkp.hasLinkFor(tc)) {
          c.addPiece(checkForNoChange(t, gen.new Piece(pkp.getLinkFor(corePath, tc), tc, null)));
        } else
          c.addPiece(checkForNoChange(t, gen.new Piece(null, tc, null)));
      }
    }
    return c;
  }

  public void genTargetLink(HierarchicalTableGenerator gen, String profileBaseFileName, String corePath, Cell c,
      TypeRefComponent t, String u) {
    if (u.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, u);
      if (sd != null) {
        String disp = sd.hasTitle() ? sd.getTitle() : sd.getName();
        c.addPiece(checkForNoChange(t, gen.new Piece(checkPrepend(corePath, sd.getUserString("path")), disp, null)));
      } else {
        String rn = u.substring(40);
        c.addPiece(checkForNoChange(t, gen.new Piece(pkp.getLinkFor(corePath, rn), rn, null)));
      }
    } else if (Utilities.isAbsoluteUrl(u)) {
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, u);
      if (sd != null) {
        String disp = sd.hasTitle() ? sd.getTitle() : sd.getName();
        String ref = pkp.getLinkForProfile(null, sd.getUrl());
        if (ref.contains("|"))
          ref = ref.substring(0, ref.indexOf("|"));
        c.addPiece(checkForNoChange(t, gen.new Piece(ref, disp, null)));
      } else
        c.addPiece(checkForNoChange(t, gen.new Piece(null, u, null)));
    } else if (t.hasTargetProfile() && u.startsWith("#"))
      c.addPiece(checkForNoChange(t,
          gen.new Piece(corePath + profileBaseFileName + "." + u.substring(1).toLowerCase() + ".html", u, null)));
  }

  private boolean isProfiledType(List<CanonicalType> theProfile) {
    for (CanonicalType next : theProfile) {
      if (StringUtils.defaultString(next.getValueAsString()).contains(":")) {
        return true;
      }
    }
    return false;
  }

  private String codeForAggregation(AggregationMode a) {
    switch (a) {
    case BUNDLED:
      return "b";
    case CONTAINED:
      return "c";
    case REFERENCED:
      return "r";
    default:
      return "?";
    }
  }

  private String hintForAggregation(AggregationMode a) {
    if (a != null)
      return a.getDefinition();
    else
      return null;
  }

  private String checkPrepend(String corePath, String path) {
    if (pkp.prependLinks() && !(path.startsWith("http:") || path.startsWith("https:")))
      return corePath + path;
    else
      return path;
  }

  private ElementDefinition getElementByName(List<ElementDefinition> elements, String contentReference) {
    for (ElementDefinition ed : elements)
      if (ed.hasSliceName() && ("#" + ed.getSliceName()).equals(contentReference))
        return ed;
    return null;
  }

  private ElementDefinition getElementById(List<ElementDefinition> elements, String contentReference) {
    for (ElementDefinition ed : elements)
      if (ed.hasId() && ("#" + ed.getId()).equals(contentReference))
        return ed;
    return null;
  }

  public static String describeExtensionContext(StructureDefinition ext) {
    StringBuilder b = new StringBuilder();
    b.append("Use on ");
    for (int i = 0; i < ext.getContext().size(); i++) {
      StructureDefinitionContextComponent ec = ext.getContext().get(i);
      if (i > 0)
        b.append(i < ext.getContext().size() - 1 ? ", " : " or ");
      b.append(ec.getType().getDisplay());
      b.append(" ");
      b.append(ec.getExpression());
    }
    if (ext.hasContextInvariant()) {
      b.append(
          ", with <a href=\"structuredefinition-definitions.html#StructureDefinition.contextInvariant\">Context Invariant</a> = ");
      boolean first = true;
      for (StringType s : ext.getContextInvariant()) {
        if (first)
          first = false;
        else
          b.append(", ");
        b.append("<code>" + s.getValue() + "</code>");
      }
    }
    return b.toString();
  }

  private String describeCardinality(ElementDefinition definition, ElementDefinition fallback, UnusedTracker tracker) {
    IntegerType min = definition.hasMinElement() ? definition.getMinElement() : new IntegerType();
    StringType max = definition.hasMaxElement() ? definition.getMaxElement() : new StringType();
    if (min.isEmpty() && fallback != null)
      min = fallback.getMinElement();
    if (max.isEmpty() && fallback != null)
      max = fallback.getMaxElement();

    tracker.used = !max.isEmpty() && !max.getValue().equals("0");

    if (min.isEmpty() && max.isEmpty())
      return null;
    else
      return (!min.hasValue() ? "" : Integer.toString(min.getValue())) + ".." + (!max.hasValue() ? "" : max.getValue());
  }

  private void genCardinality(HierarchicalTableGenerator gen, ElementDefinition definition, Row row, boolean hasDef,
      UnusedTracker tracker, ElementDefinition fallback) {
    IntegerType min = !hasDef ? new IntegerType()
        : definition.hasMinElement() ? definition.getMinElement() : new IntegerType();
    StringType max = !hasDef ? new StringType()
        : definition.hasMaxElement() ? definition.getMaxElement() : new StringType();
    if (min.isEmpty() && definition.getUserData(DERIVATION_POINTER) != null) {
      ElementDefinition base = (ElementDefinition) definition.getUserData(DERIVATION_POINTER);
      if (base.hasMinElement()) {
        min = base.getMinElement().copy();
        min.setUserData(DERIVATION_EQUALS, true);
      }
    }
    if (max.isEmpty() && definition.getUserData(DERIVATION_POINTER) != null) {
      ElementDefinition base = (ElementDefinition) definition.getUserData(DERIVATION_POINTER);
      if (base.hasMaxElement()) {
        max = base.getMaxElement().copy();
        max.setUserData(DERIVATION_EQUALS, true);
      }
    }
    if (min.isEmpty() && fallback != null)
      min = fallback.getMinElement();
    if (max.isEmpty() && fallback != null)
      max = fallback.getMaxElement();

    if (!max.isEmpty())
      tracker.used = !max.getValue().equals("0");

    Cell cell = gen.new Cell(null, null, null, null, null);
    row.getCells().add(cell);
    if (!min.isEmpty() || !max.isEmpty()) {
      cell.addPiece(
          checkForNoChange(min, gen.new Piece(null, !min.hasValue() ? "" : Integer.toString(min.getValue()), null)));
      cell.addPiece(checkForNoChange(min, max, gen.new Piece(null, "..", null)));
      cell.addPiece(checkForNoChange(min, gen.new Piece(null, !max.hasValue() ? "" : max.getValue(), null)));
    }
  }

  private Piece checkForNoChange(Element source, Piece piece) {
    if (source.hasUserData(DERIVATION_EQUALS)) {
      piece.addStyle("opacity: 0.4");
    }
    return piece;
  }

  private Piece checkForNoChange(Element src1, Element src2, Piece piece) {
    if (src1.hasUserData(DERIVATION_EQUALS) && src2.hasUserData(DERIVATION_EQUALS)) {
      piece.addStyle("opacity: 0.5");
    }
    return piece;
  }

  public XhtmlNode generateTable(String defFile, StructureDefinition profile, boolean diff, String imageFolder,
      boolean inlineGraphics, String profileBaseFileName, boolean snapshot, String corePath, String imagePath,
      boolean logicalModel, boolean allInvariants, Set<String> outputTracker) throws IOException, FHIRException {
    assert (diff != snapshot);// check it's ok to get rid of one of these
    HierarchicalTableGenerator gen = new HierarchicalTableGenerator(new RenderingI18nContext(), imageFolder, inlineGraphics, true);
    TableModel model = gen.initNormalTable(corePath, false, true, profile.getId() + (diff ? "d" : "s"), false,
        TableGenerationMode.XML);
    List<ElementDefinition> list = diff ? profile.getDifferential().getElement() : profile.getSnapshot().getElement();
    List<StructureDefinition> profiles = new ArrayList<StructureDefinition>();
    profiles.add(profile);
    if (list.isEmpty()) {
      ElementDefinition root = new ElementDefinition().setPath(profile.getType());
      root.setId(profile.getType());
      list.add(root);
    }
    genElement(defFile == null ? null : defFile + "#", gen, model.getRows(), list.get(0), list, profiles, diff,
        profileBaseFileName, null, snapshot, corePath, imagePath, true, logicalModel,
        profile.getDerivation() == TypeDerivationRule.CONSTRAINT && usesMustSupport(list), allInvariants, null);
    try {
      return gen.generate(model, imagePath, 0, outputTracker);
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException("Error generating table for profile " + profile.getUrl() + ": " + e.getMessage(), e);
    }
  }

  public XhtmlNode generateGrid(String defFile, StructureDefinition profile, String imageFolder, boolean inlineGraphics,
      String profileBaseFileName, String corePath, String imagePath, Set<String> outputTracker)
      throws IOException, FHIRException {
    HierarchicalTableGenerator gen = new HierarchicalTableGenerator(new RenderingI18nContext(), imageFolder, inlineGraphics, true);
    TableModel model = gen.initGridTable(corePath, profile.getId());
    List<ElementDefinition> list = profile.getSnapshot().getElement();
    List<StructureDefinition> profiles = new ArrayList<StructureDefinition>();
    profiles.add(profile);
    genGridElement(defFile == null ? null : defFile + "#", gen, model.getRows(), list.get(0), list, profiles, true,
        profileBaseFileName, null, corePath, imagePath, true,
        profile.getDerivation() == TypeDerivationRule.CONSTRAINT && usesMustSupport(list));
    try {
      return gen.generate(model, imagePath, 1, outputTracker);
    } catch (org.hl7.fhir.exceptions.FHIRException e) {
      throw new FHIRException(e.getMessage(), e);
    }
  }

  private boolean usesMustSupport(List<ElementDefinition> list) {
    for (ElementDefinition ed : list)
      if (ed.hasMustSupport() && ed.getMustSupport())
        return true;
    return false;
  }

  private Row genElement(String defPath, HierarchicalTableGenerator gen, List<Row> rows, ElementDefinition element,
      List<ElementDefinition> all, List<StructureDefinition> profiles, boolean showMissing, String profileBaseFileName,
      Boolean extensions, boolean snapshot, String corePath, String imagePath, boolean root, boolean logicalModel,
      boolean isConstraintMode, boolean allInvariants, Row slicingRow) throws IOException, FHIRException {
    Row originalRow = slicingRow;
    StructureDefinition profile = profiles == null ? null : profiles.get(profiles.size() - 1);
    String s = tail(element.getPath());
    if (element.hasSliceName())
      s = s + ":" + element.getSliceName();
    Row typesRow = null;

    List<ElementDefinition> children = getChildren(all, element);
    boolean isExtension = (s.equals("extension") || s.equals("modifierExtension"));
//    if (!snapshot && isExtension && extensions != null && extensions != isExtension)
//      return;

    if (!onlyInformationIsMapping(all, element)) {
      Row row = gen.new Row();
      row.setAnchor(element.getPath());
      row.setColor(getRowColor(element, isConstraintMode));
      if (element.hasSlicing())
        row.setLineColor(1);
      else if (element.hasSliceName())
        row.setLineColor(2);
      else
        row.setLineColor(0);
      boolean hasDef = element != null;
      boolean ext = false;
      if (tail(element.getPath()).equals("extension")) {
        if (element.hasType() && element.getType().get(0).hasProfile()
            && extensionIsComplex(element.getType().get(0).getProfile().get(0).getValue()))
          row.setIcon("icon_extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);
        else
          row.setIcon("icon_extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
        ext = true;
      } else if (tail(element.getPath()).equals("modifierExtension")) {
        if (element.hasType() && element.getType().get(0).hasProfile()
            && extensionIsComplex(element.getType().get(0).getProfile().get(0).getValue()))
          row.setIcon("icon_modifier_extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);
        else
          row.setIcon("icon_modifier_extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
      } else if (!hasDef || element.getType().size() == 0)
        row.setIcon("icon_element.gif", HierarchicalTableGenerator.TEXT_ICON_ELEMENT);
      else if (hasDef && element.getType().size() > 1) {
        if (allAreReference(element.getType()))
          row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
        else {
          row.setIcon("icon_choice.gif", HierarchicalTableGenerator.TEXT_ICON_CHOICE);
          typesRow = row;
        }
      } else if (hasDef && element.getType().get(0).getWorkingCode() != null
          && element.getType().get(0).getWorkingCode().startsWith("@"))
        row.setIcon("icon_reuse.png", HierarchicalTableGenerator.TEXT_ICON_REUSE);
      else if (hasDef && isPrimitive(element.getType().get(0).getWorkingCode()))
        row.setIcon("icon_primitive.png", HierarchicalTableGenerator.TEXT_ICON_PRIMITIVE);
      else if (hasDef && element.getType().get(0).hasTarget())
        row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
      else if (hasDef && isDataType(element.getType().get(0).getWorkingCode()))
        row.setIcon("icon_datatype.gif", HierarchicalTableGenerator.TEXT_ICON_DATATYPE);
      else
        row.setIcon("icon_resource.png", HierarchicalTableGenerator.TEXT_ICON_RESOURCE);
      String ref = defPath == null ? null : defPath + element.getId();
      UnusedTracker used = new UnusedTracker();
      used.used = true;
      if (logicalModel && element.hasRepresentation(PropertyRepresentation.XMLATTR))
        s = "@" + s;
      Cell left = gen.new Cell(null, ref, s,
          (element.hasSliceName() ? translate("sd.table", "Slice") + " " + element.getSliceName() : "")
              + (hasDef && element.hasSliceName() ? ": " : "") + (!hasDef ? null : gt(element.getDefinitionElement())),
          null);
      row.getCells().add(left);
      Cell gc = gen.new Cell();
      row.getCells().add(gc);
      if (element != null && element.getIsModifier())
        checkForNoChange(element.getIsModifierElement(), gc
            .addStyledText(translate("sd.table", "This element is a modifier element"), "?!", null, null, null, false));
      if (element != null && element.getMustSupport())
        checkForNoChange(element.getMustSupportElement(), gc
            .addStyledText(translate("sd.table", "This element must be supported"), "S", "white", "red", null, false));
      if (element != null && element.getIsSummary())
        checkForNoChange(element.getIsSummaryElement(), gc.addStyledText(
            translate("sd.table", "This element is included in summaries"), "\u03A3", null, null, null, false));
      if (element != null && (!element.getConstraint().isEmpty() || !element.getCondition().isEmpty()))
        gc.addStyledText(translate("sd.table", "This element has or is affected by some invariants"), "I", null, null,
            null, false);

      ExtensionContext extDefn = null;
      if (ext) {
        if (element != null && element.getType().size() == 1 && element.getType().get(0).hasProfile()) {
          String eurl = element.getType().get(0).getProfile().get(0).getValue();
          extDefn = locateExtension(StructureDefinition.class, eurl);
          if (extDefn == null) {
            genCardinality(gen, element, row, hasDef, used, null);
            row.getCells().add(gen.new Cell(null, null, "?? " + element.getType().get(0).getProfile(), null, null));
            generateDescription(gen, row, element, null, used.used, profile.getUrl(), eurl, profile, corePath,
                imagePath, root, logicalModel, allInvariants, snapshot);
          } else {
            String name = urltail(eurl);
            left.getPieces().get(0).setText(name);
            // left.getPieces().get(0).setReference((String)
            // extDefn.getExtensionStructure().getTag("filename"));
            left.getPieces().get(0).setHint(translate("sd.table", "Extension URL") + " = " + extDefn.getUrl());
            genCardinality(gen, element, row, hasDef, used, extDefn.getElement());
            ElementDefinition valueDefn = extDefn.getExtensionValueDefinition();
            if (valueDefn != null && !"0".equals(valueDefn.getMax()))
              genTypes(gen, row, valueDefn, profileBaseFileName, profile, corePath, imagePath);
            else // if it's complex, we just call it nothing
              // genTypes(gen, row, extDefn.getSnapshot().getElement().get(0),
              // profileBaseFileName, profile);
              row.getCells().add(gen.new Cell(null, null, "(" + translate("sd.table", "Complex") + ")", null, null));
            generateDescription(gen, row, element, extDefn.getElement(), used.used, null, extDefn.getUrl(), profile,
                corePath, imagePath, root, logicalModel, allInvariants, valueDefn, snapshot);
          }
        } else {
          genCardinality(gen, element, row, hasDef, used, null);
          if ("0".equals(element.getMax()))
            row.getCells().add(gen.new Cell());
          else
            genTypes(gen, row, element, profileBaseFileName, profile, corePath, imagePath);
          generateDescription(gen, row, element, null, used.used, null, null, profile, corePath, imagePath, root,
              logicalModel, allInvariants, snapshot);
        }
      } else {
        genCardinality(gen, element, row, hasDef, used, null);
        if (element.hasSlicing())
          row.getCells().add(gen.new Cell(null, corePath + "profiling.html#slicing", "(Slice Definition)", null, null));
        else if (hasDef && !"0".equals(element.getMax()) && typesRow == null)
          genTypes(gen, row, element, profileBaseFileName, profile, corePath, imagePath);
        else
          row.getCells().add(gen.new Cell());
        generateDescription(gen, row, element, null, used.used, null, null, profile, corePath, imagePath, root,
            logicalModel, allInvariants, snapshot);
      }
      if (element.hasSlicing()) {
        if (standardExtensionSlicing(element)) {
          used.used = true; // doesn't matter whether we have a type, we're used if we're setting up slicing
                            // ... element.hasType() && element.getType().get(0).hasProfile();
          showMissing = false; // ?
        } else {
          row.setIcon("icon_slice.png", HierarchicalTableGenerator.TEXT_ICON_SLICE);
          slicingRow = row;
          for (Cell cell : row.getCells())
            for (Piece p : cell.getPieces()) {
              p.addStyle("font-style: italic");
            }
        }
      } else if (element.hasSliceName()) {
        row.setIcon("icon_slice_item.png", HierarchicalTableGenerator.TEXT_ICON_SLICE_ITEM);
      }
      if (used.used || showMissing)
        rows.add(row);
      if (!used.used && !element.hasSlicing()) {
        for (Cell cell : row.getCells())
          for (Piece p : cell.getPieces()) {
            p.setStyle("text-decoration:line-through");
            p.setReference(null);
          }
      } else {
        if (slicingRow != originalRow && !children.isEmpty()) {
          // we've entered a slice; we're going to create a holder row for the slice
          // children
          Row hrow = gen.new Row();
          hrow.setAnchor(element.getPath());
          hrow.setColor(getRowColor(element, isConstraintMode));
          hrow.setLineColor(1);
          hrow.setIcon("icon_element.gif", HierarchicalTableGenerator.TEXT_ICON_ELEMENT);
          hrow.getCells().add(gen.new Cell(null, null, "(All Slices)", "", null));
          hrow.getCells().add(gen.new Cell());
          hrow.getCells().add(gen.new Cell());
          hrow.getCells().add(gen.new Cell());
          hrow.getCells().add(gen.new Cell(null, null, "Content/Rules for all slices", "", null));
          row.getSubRows().add(hrow);
          row = hrow;
        }
        if (typesRow != null && !children.isEmpty()) {
          // we've entered a typing slice; we're going to create a holder row for the all
          // types children
          Row hrow = gen.new Row();
          hrow.setAnchor(element.getPath());
          hrow.setColor(getRowColor(element, isConstraintMode));
          hrow.setLineColor(1);
          hrow.setIcon("icon_element.gif", HierarchicalTableGenerator.TEXT_ICON_ELEMENT);
          hrow.getCells().add(gen.new Cell(null, null, "(All Types)", "", null));
          hrow.getCells().add(gen.new Cell());
          hrow.getCells().add(gen.new Cell());
          hrow.getCells().add(gen.new Cell());
          hrow.getCells().add(gen.new Cell(null, null, "Content/Rules for all Types", "", null));
          row.getSubRows().add(hrow);
          row = hrow;
        }

        Row currRow = row;
        for (ElementDefinition child : children) {
          if (!child.hasSliceName())
            currRow = row;
          if (logicalModel || !child.getPath().endsWith(".id") || (child.getPath().endsWith(".id") && (profile != null)
              && (profile.getDerivation() == TypeDerivationRule.CONSTRAINT)))
            currRow = genElement(defPath, gen, currRow.getSubRows(), child, all, profiles, showMissing,
                profileBaseFileName, isExtension, snapshot, corePath, imagePath, false, logicalModel, isConstraintMode,
                allInvariants, currRow);
        }
//        if (!snapshot && (extensions == null || !extensions))
//          for (ElementDefinition child : children)
//            if (child.getPath().endsWith(".extension") || child.getPath().endsWith(".modifierExtension"))
//              genElement(defPath, gen, row.getSubRows(), child, all, profiles, showMissing, profileBaseFileName, true, false, corePath, imagePath, false, logicalModel, isConstraintMode, allInvariants);
      }
      if (typesRow != null) {
        makeChoiceRows(typesRow.getSubRows(), element, gen, corePath, profileBaseFileName);
      }
    }
    return slicingRow;
  }

  private void makeChoiceRows(List<Row> subRows, ElementDefinition element, HierarchicalTableGenerator gen,
      String corePath, String profileBaseFileName) {
    // create a child for each choice
    for (TypeRefComponent tr : element.getType()) {
      Row choicerow = gen.new Row();
      String t = tr.getWorkingCode();
      if (isReference(t)) {
        choicerow.getCells()
            .add(gen.new Cell(null, null, tail(element.getPath()).replace("[x]", Utilities.capitalize(t)), null, null));
        choicerow.getCells().add(gen.new Cell());
        choicerow.getCells().add(gen.new Cell(null, null, "", null, null));
        choicerow.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
        Cell c = gen.new Cell();
        choicerow.getCells().add(c);
        if (ADD_REFERENCE_TO_TABLE) {
          if (tr.getWorkingCode().equals("canonical"))
            c.getPieces().add(gen.new Piece(corePath + "datatypes.html#canonical", "canonical", null));
          else
            c.getPieces().add(gen.new Piece(corePath + "references.html#Reference", "Reference", null));
          c.getPieces().add(gen.new Piece(null, "(", null));
        }
        boolean first = true;
        for (CanonicalType rt : tr.getTargetProfile()) {
          if (!first)
            c.getPieces().add(gen.new Piece(null, " | ", null));
          genTargetLink(gen, profileBaseFileName, corePath, c, tr, rt.getValue());
          first = false;
        }
        if (ADD_REFERENCE_TO_TABLE)
          c.getPieces().add(gen.new Piece(null, ")", null));

      } else {
        StructureDefinition sd = context.fetchTypeDefinition(t);
        if (sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE) {
          choicerow.getCells().add(gen.new Cell(null, null,
              tail(element.getPath()).replace("[x]", Utilities.capitalize(t)), sd.getDescription(), null));
          choicerow.getCells().add(gen.new Cell());
          choicerow.getCells().add(gen.new Cell(null, null, "", null, null));
          choicerow.setIcon("icon_primitive.png", HierarchicalTableGenerator.TEXT_ICON_PRIMITIVE);
          choicerow.getCells().add(gen.new Cell(null, corePath + "datatypes.html#" + t, t, null, null));
          // } else if (definitions.getConstraints().contthnsKey(t)) {
          // ProfiledType pt = definitions.getConstraints().get(t);
          // choicerow.getCells().add(gen.new Cell(null, null, e.getName().replace("[x]",
          // Utilities.capitalize(pt.getBaseType())),
          // definitions.getTypes().containsKey(t) ?
          // definitions.getTypes().get(t).getDefinition() : null, null));
          // choicerow.getCells().add(gen.new Cell());
          // choicerow.getCells().add(gen.new Cell(null, null, "", null, null));
          // choicerow.setIcon("icon_datatype.gif",
          // HierarchicalTableGenerator.TEXT_ICON_DATATYPE);
          // choicerow.getCells().add(gen.new Cell(null,
          // definitions.getSrcFile(t)+".html#"+t.replace("*", "open"), t, null, null));
        } else {
          choicerow.getCells().add(gen.new Cell(null, null,
              tail(element.getPath()).replace("[x]", Utilities.capitalize(t)), sd.getDescription(), null));
          choicerow.getCells().add(gen.new Cell());
          choicerow.getCells().add(gen.new Cell(null, null, "", null, null));
          choicerow.setIcon("icon_datatype.gif", HierarchicalTableGenerator.TEXT_ICON_DATATYPE);
          choicerow.getCells().add(gen.new Cell(null, pkp.getLinkFor(corePath, t), t, null, null));
        }
      }
      choicerow.getCells().add(gen.new Cell());
      subRows.add(choicerow);
    }
  }

  private boolean isReference(String t) {
    return t.equals("Reference") || t.equals("canonical");
  }

  private void genGridElement(String defPath, HierarchicalTableGenerator gen, List<Row> rows, ElementDefinition element,
      List<ElementDefinition> all, List<StructureDefinition> profiles, boolean showMissing, String profileBaseFileName,
      Boolean extensions, String corePath, String imagePath, boolean root, boolean isConstraintMode)
      throws IOException, FHIRException {
    StructureDefinition profile = profiles == null ? null : profiles.get(profiles.size() - 1);
    String s = tail(element.getPath());
    List<ElementDefinition> children = getChildren(all, element);
    boolean isExtension = (s.equals("extension") || s.equals("modifierExtension"));

    if (!onlyInformationIsMapping(all, element)) {
      Row row = gen.new Row();
      row.setAnchor(element.getPath());
      row.setColor(getRowColor(element, isConstraintMode));
      if (element.hasSlicing())
        row.setLineColor(1);
      else if (element.hasSliceName())
        row.setLineColor(2);
      else
        row.setLineColor(0);
      boolean hasDef = element != null;
      String ref = defPath == null ? null : defPath + element.getId();
      UnusedTracker used = new UnusedTracker();
      used.used = true;
      Cell left = gen.new Cell();
      if (element.getType().size() == 1 && element.getType().get(0).isPrimitive())
        left.getPieces().add(gen.new Piece(ref, "\u00A0\u00A0" + s, !hasDef ? null : gt(element.getDefinitionElement()))
            .addStyle("font-weight:bold"));
      else
        left.getPieces()
            .add(gen.new Piece(ref, "\u00A0\u00A0" + s, !hasDef ? null : gt(element.getDefinitionElement())));
      if (element.hasSliceName()) {
        left.getPieces().add(gen.new Piece("br"));
        String indent = StringUtils.repeat('\u00A0', 1 + 2 * (element.getPath().split("\\.").length));
        left.getPieces().add(gen.new Piece(null, indent + "(" + element.getSliceName() + ")", null));
      }
      row.getCells().add(left);

      ExtensionContext extDefn = null;
      genCardinality(gen, element, row, hasDef, used, null);
      if (hasDef && !"0".equals(element.getMax()))
        genTypes(gen, row, element, profileBaseFileName, profile, corePath, imagePath);
      else
        row.getCells().add(gen.new Cell());
      generateGridDescription(gen, row, element, null, used.used, null, null, profile, corePath, imagePath, root, null);
      /*
       * if (element.hasSlicing()) { if (standardExtensionSlicing(element)) {
       * used.used = element.hasType() && element.getType().get(0).hasProfile();
       * showMissing = false; } else { row.setIcon("icon_slice.png",
       * HierarchicalTableGenerator.TEXT_ICON_SLICE);
       * row.getCells().get(2).getPieces().clear(); for (Cell cell : row.getCells())
       * for (Piece p : cell.getPieces()) { p.addStyle("font-style: italic"); } } }
       */
      rows.add(row);
      for (ElementDefinition child : children)
        if (child.getMustSupport())
          genGridElement(defPath, gen, row.getSubRows(), child, all, profiles, showMissing, profileBaseFileName,
              isExtension, corePath, imagePath, false, isConstraintMode);
    }
  }

  private ExtensionContext locateExtension(Class<StructureDefinition> class1, String value) {
    if (value.contains("#")) {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class,
          value.substring(0, value.indexOf("#")));
      if (ext == null)
        return null;
      String tail = value.substring(value.indexOf("#") + 1);
      ElementDefinition ed = null;
      for (ElementDefinition ted : ext.getSnapshot().getElement()) {
        if (tail.equals(ted.getSliceName())) {
          ed = ted;
          return new ExtensionContext(ext, ed);
        }
      }
      return null;
    } else {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value);
      if (ext == null)
        return null;
      else
        return new ExtensionContext(ext, ext.getSnapshot().getElement().get(0));
    }
  }

  private boolean extensionIsComplex(String value) {
    if (value.contains("#")) {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class,
          value.substring(0, value.indexOf("#")));
      if (ext == null)
        return false;
      String tail = value.substring(value.indexOf("#") + 1);
      ElementDefinition ed = null;
      for (ElementDefinition ted : ext.getSnapshot().getElement()) {
        if (tail.equals(ted.getSliceName())) {
          ed = ted;
          break;
        }
      }
      if (ed == null)
        return false;
      int i = ext.getSnapshot().getElement().indexOf(ed);
      int j = i + 1;
      while (j < ext.getSnapshot().getElement().size()
          && !ext.getSnapshot().getElement().get(j).getPath().equals(ed.getPath()))
        j++;
      return j - i > 5;
    } else {
      StructureDefinition ext = context.fetchResource(StructureDefinition.class, value);
      return ext != null && ext.getSnapshot().getElement().size() > 5;
    }
  }

  private String getRowColor(ElementDefinition element, boolean isConstraintMode) {
    switch (element.getUserInt(UD_ERROR_STATUS)) {
    case STATUS_HINT:
      return ROW_COLOR_HINT;
    case STATUS_WARNING:
      return ROW_COLOR_WARNING;
    case STATUS_ERROR:
      return ROW_COLOR_ERROR;
    case STATUS_FATAL:
      return ROW_COLOR_FATAL;
    }
    if (isConstraintMode && !element.getMustSupport() && !element.getIsModifier() && element.getPath().contains("."))
      return null; // ROW_COLOR_NOT_MUST_SUPPORT;
    else
      return null;
  }

  private String urltail(String path) {
    if (path.contains("#"))
      return path.substring(path.lastIndexOf('#') + 1);
    if (path.contains("/"))
      return path.substring(path.lastIndexOf('/') + 1);
    else
      return path;

  }

  private boolean standardExtensionSlicing(ElementDefinition element) {
    String t = tail(element.getPath());
    return (t.equals("extension") || t.equals("modifierExtension"))
        && element.getSlicing().getRules() != SlicingRules.CLOSED && element.getSlicing().getDiscriminator().size() == 1
        && element.getSlicing().getDiscriminator().get(0).getPath().equals("url")
        && element.getSlicing().getDiscriminator().get(0).getType().equals(DiscriminatorType.VALUE);
  }

  private Cell generateDescription(HierarchicalTableGenerator gen, Row row, ElementDefinition definition,
      ElementDefinition fallback, boolean used, String baseURL, String url, StructureDefinition profile,
      String corePath, String imagePath, boolean root, boolean logicalModel, boolean allInvariants, boolean snapshot)
      throws IOException, FHIRException {
    return generateDescription(gen, row, definition, fallback, used, baseURL, url, profile, corePath, imagePath, root,
        logicalModel, allInvariants, null, snapshot);
  }

  private Cell generateDescription(HierarchicalTableGenerator gen, Row row, ElementDefinition definition,
      ElementDefinition fallback, boolean used, String baseURL, String url, StructureDefinition profile,
      String corePath, String imagePath, boolean root, boolean logicalModel, boolean allInvariants,
      ElementDefinition valueDefn, boolean snapshot) throws IOException, FHIRException {
    Cell c = gen.new Cell();
    row.getCells().add(c);

    if (used) {
      if (logicalModel && ToolingExtensions.hasExtension(profile,
          "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace")) {
        if (root) {
          c.getPieces().add(
              gen.new Piece(null, translate("sd.table", "XML Namespace") + ": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(profile,
              "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"), null));
        } else if (!root
            && ToolingExtensions.hasExtension(definition,
                "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace")
            && !ToolingExtensions
                .readStringExtension(definition, "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace")
                .equals(ToolingExtensions.readStringExtension(profile,
                    "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"))) {
          c.getPieces().add(
              gen.new Piece(null, translate("sd.table", "XML Namespace") + ": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, ToolingExtensions.readStringExtension(definition,
              "http://hl7.org/fhir/StructureDefinition/elementdefinition-namespace"), null));
        }
      }

      if (definition.hasContentReference()) {
        ElementDefinition ed = getElementByName(profile.getSnapshot().getElement(), definition.getContentReference());
        if (ed == null)
          c.getPieces().add(gen.new Piece(null,
              translate("sd.table", "Unknown reference to %s", definition.getContentReference()), null));
        else
          c.getPieces().add(gen.new Piece("#" + ed.getPath(), translate("sd.table", "See %s", ed.getPath()), null));
      }
      if (definition.getPath().endsWith("url") && definition.hasFixed()) {
        c.getPieces().add(checkForNoChange(definition.getFixed(),
            gen.new Piece(null, "\"" + buildJson(definition.getFixed()) + "\"", null).addStyle("color: darkgreen")));
      } else {
        if (definition != null && definition.hasShort()) {
          if (!c.getPieces().isEmpty())
            c.addPiece(gen.new Piece("br"));
          c.addPiece(checkForNoChange(definition.getShortElement(),
              gen.new Piece(null, gt(definition.getShortElement()), null)));
        } else if (fallback != null && fallback.hasShort()) {
          if (!c.getPieces().isEmpty())
            c.addPiece(gen.new Piece("br"));
          c.addPiece(
              checkForNoChange(fallback.getShortElement(), gen.new Piece(null, gt(fallback.getShortElement()), null)));
        }
        if (url != null) {
          if (!c.getPieces().isEmpty())
            c.addPiece(gen.new Piece("br"));
          String fullUrl = url.startsWith("#") ? baseURL + url : url;
          StructureDefinition ed = context.fetchResource(StructureDefinition.class, url);
          String ref = null;
          String ref2 = null;
          String fixedUrl = null;
          if (ed != null) {
            String p = ed.getUserString("path");
            if (p != null) {
              ref = p.startsWith("http:") || igmode ? p : Utilities.pathURL(corePath, p);
            }
            fixedUrl = getFixedUrl(ed);
            if (fixedUrl != null) {// if its null, we guess that it's not a profiled extension?
              if (fixedUrl.equals(url))
                fixedUrl = null;
              else {
                StructureDefinition ed2 = context.fetchResource(StructureDefinition.class, fixedUrl);
                if (ed2 != null) {
                  String p2 = ed2.getUserString("path");
                  if (p2 != null) {
                    ref2 = p2.startsWith("http:") || igmode ? p2 : Utilities.pathURL(corePath, p2);
                  }
                }
              }
            }
          }
          if (fixedUrl == null) {
            c.getPieces()
                .add(gen.new Piece(null, translate("sd.table", "URL") + ": ", null).addStyle("font-weight:bold"));
            c.getPieces().add(gen.new Piece(ref, fullUrl, null));
          } else {
            // reference to a profile take on the extension show the base URL
            c.getPieces()
                .add(gen.new Piece(null, translate("sd.table", "URL") + ": ", null).addStyle("font-weight:bold"));
            c.getPieces().add(gen.new Piece(ref2, fixedUrl, null));
            c.getPieces().add(
                gen.new Piece(null, translate("sd.table", " profiled by ") + " ", null).addStyle("font-weight:bold"));
            c.getPieces().add(gen.new Piece(ref, fullUrl, null));

          }
        }

        if (definition.hasSlicing()) {
          if (!c.getPieces().isEmpty())
            c.addPiece(gen.new Piece("br"));
          c.getPieces()
              .add(gen.new Piece(null, translate("sd.table", "Slice") + ": ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, describeSlice(definition.getSlicing()), null));
        }
        if (definition != null) {
          ElementDefinitionBindingComponent binding = null;
          if (valueDefn != null && valueDefn.hasBinding() && !valueDefn.getBinding().isEmpty())
            binding = valueDefn.getBinding();
          else if (definition.hasBinding())
            binding = definition.getBinding();
          if (binding != null && !binding.isEmpty()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            BindingResolution br = pkp.resolveBinding(profile, binding, definition.getPath());
            c.getPieces().add(checkForNoChange(binding,
                gen.new Piece(null, translate("sd.table", "Binding") + ": ", null).addStyle("font-weight:bold")));
            c.getPieces()
                .add(checkForNoChange(binding,
                    gen.new Piece(
                        br.url == null ? null
                            : Utilities.isAbsoluteUrl(br.url) || !pkp.prependLinks() ? br.url : corePath + br.url,
                        br.display, null)));
            if (binding.hasStrength()) {
              c.getPieces().add(checkForNoChange(binding, gen.new Piece(null, " (", null)));
              c.getPieces()
                  .add(checkForNoChange(binding,
                      gen.new Piece(corePath + "terminologies.html#" + binding.getStrength().toCode(),
                          egt(binding.getStrengthElement()), binding.getStrength().getDefinition())));

              c.getPieces().add(gen.new Piece(null, ")", null));
            }
            if (binding.hasExtension(ToolingExtensions.EXT_MAX_VALUESET)) {
              br = pkp.resolveBinding(profile,
                  ToolingExtensions.readStringExtension(binding, ToolingExtensions.EXT_MAX_VALUESET),
                  definition.getPath());
              c.addPiece(gen.new Piece("br"));
              c.getPieces()
                  .add(checkForNoChange(binding,
                      gen.new Piece(corePath + "extension-elementdefinition-maxvalueset.html",
                          translate("sd.table", "Max Binding") + ": ", "Max Value Set Extension")
                              .addStyle("font-weight:bold")));
              c.getPieces()
                  .add(checkForNoChange(binding,
                      gen.new Piece(
                          br.url == null ? null
                              : Utilities.isAbsoluteUrl(br.url) || !pkp.prependLinks() ? br.url : corePath + br.url,
                          br.display, null)));
            }
            if (binding.hasExtension(ToolingExtensions.EXT_MIN_VALUESET)) {
              br = pkp.resolveBinding(profile,
                  ToolingExtensions.readStringExtension(binding, ToolingExtensions.EXT_MIN_VALUESET),
                  definition.getPath());
              c.addPiece(gen.new Piece("br"));
              c.getPieces()
                  .add(checkForNoChange(binding,
                      gen.new Piece(corePath + "extension-elementdefinition-minvalueset.html",
                          translate("sd.table", "Min Binding") + ": ", "Min Value Set Extension")
                              .addStyle("font-weight:bold")));
              c.getPieces()
                  .add(checkForNoChange(binding,
                      gen.new Piece(
                          br.url == null ? null
                              : Utilities.isAbsoluteUrl(br.url) || !pkp.prependLinks() ? br.url : corePath + br.url,
                          br.display, null)));
            }
          }
          for (ElementDefinitionConstraintComponent inv : definition.getConstraint()) {
            if (!inv.hasSource() || allInvariants) {
              if (!c.getPieces().isEmpty())
                c.addPiece(gen.new Piece("br"));
              c.getPieces().add(
                  checkForNoChange(inv, gen.new Piece(null, inv.getKey() + ": ", null).addStyle("font-weight:bold")));
              c.getPieces().add(checkForNoChange(inv, gen.new Piece(null, gt(inv.getHumanElement()), null)));
            }
          }
          if ((definition.hasBase() && definition.getBase().getMax().equals("*"))
              || (definition.hasMax() && definition.getMax().equals("*"))) {
            if (c.getPieces().size() > 0)
              c.addPiece(gen.new Piece("br"));
            if (definition.hasOrderMeaning()) {
              c.getPieces()
                  .add(gen.new Piece(null, "This repeating element order: " + definition.getOrderMeaning(), null));
            } else {
              // don't show this, this it's important: c.getPieces().add(gen.new Piece(null,
              // "This repeating element has no defined order", null));
            }
          }

          if (definition.hasFixed()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getFixed(),
                gen.new Piece(null, translate("sd.table", "Fixed Value") + ": ", null).addStyle("font-weight:bold")));
            if (!useTableForFixedValues || definition.getFixed().isPrimitive()) {
              String s = buildJson(definition.getFixed());
              String link = null;
              if (Utilities.isAbsoluteUrl(s))
                link = pkp.getLinkForUrl(corePath, s);
              c.getPieces().add(
                  checkForNoChange(definition.getFixed(), gen.new Piece(link, s, null).addStyle("color: darkgreen")));
            } else {
              c.getPieces().add(checkForNoChange(definition.getFixed(),
                  gen.new Piece(null, "As shown", null).addStyle("color: darkgreen")));
              genFixedValue(gen, row, definition.getFixed(), snapshot, false, corePath);
            }
            if (isCoded(definition.getFixed()) && !hasDescription(definition.getFixed())) {
              Piece p = describeCoded(gen, definition.getFixed());
              if (p != null)
                c.getPieces().add(p);
            }
          } else if (definition.hasPattern()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces()
                .add(checkForNoChange(definition.getPattern(),
                    gen.new Piece(null, translate("sd.table", "Required Pattern") + ": ", null)
                        .addStyle("font-weight:bold")));
            if (!useTableForFixedValues || definition.getPattern().isPrimitive())
              c.getPieces().add(checkForNoChange(definition.getPattern(),
                  gen.new Piece(null, buildJson(definition.getPattern()), null).addStyle("color: darkgreen")));
            else {
              c.getPieces().add(checkForNoChange(definition.getPattern(),
                  gen.new Piece(null, "At least the following", null).addStyle("color: darkgreen")));
              genFixedValue(gen, row, definition.getPattern(), snapshot, true, corePath);
            }
          } else if (definition.hasExample()) {
            for (ElementDefinitionExampleComponent ex : definition.getExample()) {
              if (!c.getPieces().isEmpty())
                c.addPiece(gen.new Piece("br"));
              c.getPieces()
                  .add(checkForNoChange(ex,
                      gen.new Piece(null, translate("sd.table", "Example")
                          + ("".equals("General") ? "" : " " + ex.getLabel() + "'") + ": ", null)
                              .addStyle("font-weight:bold")));
              c.getPieces().add(checkForNoChange(ex,
                  gen.new Piece(null, buildJson(ex.getValue()), null).addStyle("color: darkgreen")));
            }
          }
          if (definition.hasMaxLength() && definition.getMaxLength() != 0) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getMaxLengthElement(),
                gen.new Piece(null, "Max Length: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getMaxLengthElement(),
                gen.new Piece(null, Integer.toString(definition.getMaxLength()), null).addStyle("color: darkgreen")));
          }
          if (profile != null) {
            for (StructureDefinitionMappingComponent md : profile.getMapping()) {
              if (md.hasExtension(ToolingExtensions.EXT_TABLE_NAME)) {
                ElementDefinitionMappingComponent map = null;
                for (ElementDefinitionMappingComponent m : definition.getMapping())
                  if (m.getIdentity().equals(md.getIdentity()))
                    map = m;
                if (map != null) {
                  for (int i = 0; i < definition.getMapping().size(); i++) {
                    c.addPiece(gen.new Piece("br"));
                    c.getPieces().add(
                        gen.new Piece(null, ToolingExtensions.readStringExtension(md, ToolingExtensions.EXT_TABLE_NAME)
                            + ": " + map.getMap(), null));
                  }
                }
              }
            }
          }
        }
      }
    }
    return c;
  }

  private void genFixedValue(HierarchicalTableGenerator gen, Row erow, Type value, boolean snapshot, boolean pattern,
      String corePath) {
    String ref = pkp.getLinkFor(corePath, value.fhirType());
    ref = ref.substring(0, ref.indexOf(".html")) + "-definitions.html#";
    StructureDefinition sd = context.fetchTypeDefinition(value.fhirType());

    for (org.hl7.fhir.r4.model.Property t : value.children()) {
      if (t.getValues().size() > 0 || snapshot) {
        ElementDefinition ed = findElementDefinition(sd, t.getName());
        if (t.getValues().size() == 0 || (t.getValues().size() == 1 && t.getValues().get(0).isEmpty())) {
          Row row = gen.new Row();
          erow.getSubRows().add(row);
          Cell c = gen.new Cell();
          row.getCells().add(c);
          c.addPiece(gen.new Piece((ed.getBase().getPath().equals(ed.getPath()) ? ref + ed.getPath()
              : corePath + "element-definitions.html#" + ed.getBase().getPath()), t.getName(), null));
          c = gen.new Cell();
          row.getCells().add(c);
          c.addPiece(gen.new Piece(null, null, null));
          c = gen.new Cell();
          row.getCells().add(c);
          if (!pattern) {
            c.addPiece(gen.new Piece(null, "0..0", null));
            row.setIcon("icon_fixed.gif", "Fixed Value" /* HierarchicalTableGenerator.TEXT_ICON_FIXED */);
          } else if (isPrimitive(t.getTypeCode())) {
            row.setIcon("icon_primitive.png", HierarchicalTableGenerator.TEXT_ICON_PRIMITIVE);
            c.addPiece(gen.new Piece(null,
                "0.." + (t.getMaxCardinality() == 2147483647 ? "*" : Integer.toString(t.getMaxCardinality())), null));
          } else if (isReference(t.getTypeCode())) {
            row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
            c.addPiece(gen.new Piece(null,
                "0.." + (t.getMaxCardinality() == 2147483647 ? "*" : Integer.toString(t.getMaxCardinality())), null));
          } else {
            row.setIcon("icon_datatype.gif", HierarchicalTableGenerator.TEXT_ICON_DATATYPE);
            c.addPiece(gen.new Piece(null,
                "0.." + (t.getMaxCardinality() == 2147483647 ? "*" : Integer.toString(t.getMaxCardinality())), null));
          }
          c = gen.new Cell();
          row.getCells().add(c);
          c.addPiece(gen.new Piece(pkp.getLinkFor(corePath, t.getTypeCode()), t.getTypeCode(), null));
          c = gen.new Cell();
          c.addPiece(gen.new Piece(null, ed.getShort(), null));
          row.getCells().add(c);
        } else {
          for (Base b : t.getValues()) {
            Row row = gen.new Row();
            erow.getSubRows().add(row);
            row.setIcon("icon_fixed.gif", "Fixed Value" /* HierarchicalTableGenerator.TEXT_ICON_FIXED */);

            Cell c = gen.new Cell();
            row.getCells().add(c);
            c.addPiece(gen.new Piece((ed.getBase().getPath().equals(ed.getPath()) ? ref + ed.getPath()
                : corePath + "element-definitions.html#" + ed.getBase().getPath()), t.getName(), null));

            c = gen.new Cell();
            row.getCells().add(c);
            c.addPiece(gen.new Piece(null, null, null));

            c = gen.new Cell();
            row.getCells().add(c);
            if (pattern)
              c.addPiece(gen.new Piece(null,
                  "1.." + (t.getMaxCardinality() == 2147483647 ? "*" : Integer.toString(t.getMaxCardinality())), null));
            else
              c.addPiece(gen.new Piece(null, "1..1", null));

            c = gen.new Cell();
            row.getCells().add(c);
            c.addPiece(gen.new Piece(pkp.getLinkFor(corePath, b.fhirType()), b.fhirType(), null));

            if (b.isPrimitive()) {
              c = gen.new Cell();
              row.getCells().add(c);
              c.addPiece(gen.new Piece(null, ed.getShort(), null));
              c.addPiece(gen.new Piece("br"));
              c.getPieces().add(gen.new Piece(null, "Fixed Value: ", null).addStyle("font-weight: bold"));
              String s = b.primitiveValue();
              // ok. let's see if we can find a relevant link for this
              String link = null;
              if (Utilities.isAbsoluteUrl(s))
                link = pkp.getLinkForUrl(corePath, s);
              c.getPieces().add(gen.new Piece(link, s, null).addStyle("color: darkgreen"));
            } else {
              c = gen.new Cell();
              row.getCells().add(c);
              c.addPiece(gen.new Piece(null, ed.getShort(), null));
              c.addPiece(gen.new Piece("br"));
              c.getPieces().add(gen.new Piece(null, "Fixed Value: ", null).addStyle("font-weight: bold"));
              c.getPieces().add(gen.new Piece(null, "(complex)", null).addStyle("color: darkgreen"));
              genFixedValue(gen, row, (Type) b, snapshot, pattern, corePath);
            }
          }
        }
      }
    }
  }

  private ElementDefinition findElementDefinition(StructureDefinition sd, String name) {
    String path = sd.getType() + "." + name;
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (ed.getPath().equals(path))
        return ed;
    }
    throw new FHIRException("Unable to find element " + path);
  }

  private String getFixedUrl(StructureDefinition sd) {
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (ed.getPath().equals("Extension.url")) {
        if (ed.hasFixed() && ed.getFixed() instanceof UriType)
          return ed.getFixed().primitiveValue();
      }
    }
    return null;
  }

  private Piece describeCoded(HierarchicalTableGenerator gen, Type fixed) {
    if (fixed instanceof Coding) {
      Coding c = (Coding) fixed;
      ValidationResult vr = context.validateCode(terminologyServiceOptions, c.getSystem(), c.getCode(), c.getDisplay());
      if (vr.getDisplay() != null)
        return gen.new Piece(null, " (" + vr.getDisplay() + ")", null).addStyle("color: darkgreen");
    } else if (fixed instanceof CodeableConcept) {
      CodeableConcept cc = (CodeableConcept) fixed;
      for (Coding c : cc.getCoding()) {
        ValidationResult vr = context.validateCode(terminologyServiceOptions, c.getSystem(), c.getCode(),
            c.getDisplay());
        if (vr.getDisplay() != null)
          return gen.new Piece(null, " (" + vr.getDisplay() + ")", null).addStyle("color: darkgreen");
      }
    }
    return null;
  }

  private boolean hasDescription(Type fixed) {
    if (fixed instanceof Coding) {
      return ((Coding) fixed).hasDisplay();
    } else if (fixed instanceof CodeableConcept) {
      CodeableConcept cc = (CodeableConcept) fixed;
      if (cc.hasText())
        return true;
      for (Coding c : cc.getCoding())
        if (c.hasDisplay())
          return true;
    } // (fixed instanceof CodeType) || (fixed instanceof Quantity);
    return false;
  }

  private boolean isCoded(Type fixed) {
    return (fixed instanceof Coding) || (fixed instanceof CodeableConcept) || (fixed instanceof CodeType)
        || (fixed instanceof Quantity);
  }

  private Cell generateGridDescription(HierarchicalTableGenerator gen, Row row, ElementDefinition definition,
      ElementDefinition fallback, boolean used, String baseURL, String url, StructureDefinition profile,
      String corePath, String imagePath, boolean root, ElementDefinition valueDefn) throws IOException, FHIRException {
    Cell c = gen.new Cell();
    row.getCells().add(c);

    if (used) {
      if (definition.hasContentReference()) {
        ElementDefinition ed = getElementByName(profile.getSnapshot().getElement(), definition.getContentReference());
        if (ed == null)
          c.getPieces().add(gen.new Piece(null, "Unknown reference to " + definition.getContentReference(), null));
        else
          c.getPieces().add(gen.new Piece("#" + ed.getPath(), "See " + ed.getPath(), null));
      }
      if (definition.getPath().endsWith("url") && definition.hasFixed()) {
        c.getPieces().add(checkForNoChange(definition.getFixed(),
            gen.new Piece(null, "\"" + buildJson(definition.getFixed()) + "\"", null).addStyle("color: darkgreen")));
      } else {
        if (url != null) {
          if (!c.getPieces().isEmpty())
            c.addPiece(gen.new Piece("br"));
          String fullUrl = url.startsWith("#") ? baseURL + url : url;
          StructureDefinition ed = context.fetchResource(StructureDefinition.class, url);
          String ref = null;
          if (ed != null) {
            String p = ed.getUserString("path");
            if (p != null) {
              ref = p.startsWith("http:") || igmode ? p : Utilities.pathURL(corePath, p);
            }
          }
          c.getPieces().add(gen.new Piece(null, "URL: ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(ref, fullUrl, null));
        }

        if (definition.hasSlicing()) {
          if (!c.getPieces().isEmpty())
            c.addPiece(gen.new Piece("br"));
          c.getPieces().add(gen.new Piece(null, "Slice: ", null).addStyle("font-weight:bold"));
          c.getPieces().add(gen.new Piece(null, describeSlice(definition.getSlicing()), null));
        }
        if (definition != null) {
          ElementDefinitionBindingComponent binding = null;
          if (valueDefn != null && valueDefn.hasBinding() && !valueDefn.getBinding().isEmpty())
            binding = valueDefn.getBinding();
          else if (definition.hasBinding())
            binding = definition.getBinding();
          if (binding != null && !binding.isEmpty()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            BindingResolution br = pkp.resolveBinding(profile, binding, definition.getPath());
            c.getPieces()
                .add(checkForNoChange(binding, gen.new Piece(null, "Binding: ", null).addStyle("font-weight:bold")));
            c.getPieces()
                .add(checkForNoChange(binding,
                    gen.new Piece(
                        br.url == null ? null
                            : Utilities.isAbsoluteUrl(br.url) || !pkp.prependLinks() ? br.url : corePath + br.url,
                        br.display, null)));
            if (binding.hasStrength()) {
              c.getPieces().add(checkForNoChange(binding, gen.new Piece(null, " (", null)));
              c.getPieces()
                  .add(checkForNoChange(binding,
                      gen.new Piece(corePath + "terminologies.html#" + binding.getStrength().toCode(),
                          binding.getStrength().toCode(), binding.getStrength().getDefinition())));
              c.getPieces().add(gen.new Piece(null, ")", null));
            }
          }
          for (ElementDefinitionConstraintComponent inv : definition.getConstraint()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(
                checkForNoChange(inv, gen.new Piece(null, inv.getKey() + ": ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(inv, gen.new Piece(null, inv.getHuman(), null)));
          }
          if (definition.hasFixed()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getFixed(),
                gen.new Piece(null, "Fixed Value: ", null).addStyle("font-weight:bold")));
            String s = buildJson(definition.getFixed());
            String link = null;
            if (Utilities.isAbsoluteUrl(s))
              link = pkp.getLinkForUrl(corePath, s);
            c.getPieces().add(
                checkForNoChange(definition.getFixed(), gen.new Piece(link, s, null).addStyle("color: darkgreen")));
          } else if (definition.hasPattern()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getPattern(),
                gen.new Piece(null, "Required Pattern: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getPattern(),
                gen.new Piece(null, buildJson(definition.getPattern()), null).addStyle("color: darkgreen")));
          } else if (definition.hasExample()) {
            for (ElementDefinitionExampleComponent ex : definition.getExample()) {
              if (!c.getPieces().isEmpty())
                c.addPiece(gen.new Piece("br"));
              c.getPieces().add(checkForNoChange(ex,
                  gen.new Piece(null, "Example'" + ("".equals("General") ? "" : " " + ex.getLabel() + "'") + ": ", null)
                      .addStyle("font-weight:bold")));
              c.getPieces().add(checkForNoChange(ex,
                  gen.new Piece(null, buildJson(ex.getValue()), null).addStyle("color: darkgreen")));
            }
          }
          if (definition.hasMaxLength() && definition.getMaxLength() != 0) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(checkForNoChange(definition.getMaxLengthElement(),
                gen.new Piece(null, "Max Length: ", null).addStyle("font-weight:bold")));
            c.getPieces().add(checkForNoChange(definition.getMaxLengthElement(),
                gen.new Piece(null, Integer.toString(definition.getMaxLength()), null).addStyle("color: darkgreen")));
          }
          if (profile != null) {
            for (StructureDefinitionMappingComponent md : profile.getMapping()) {
              if (md.hasExtension(ToolingExtensions.EXT_TABLE_NAME)) {
                ElementDefinitionMappingComponent map = null;
                for (ElementDefinitionMappingComponent m : definition.getMapping())
                  if (m.getIdentity().equals(md.getIdentity()))
                    map = m;
                if (map != null) {
                  for (int i = 0; i < definition.getMapping().size(); i++) {
                    c.addPiece(gen.new Piece("br"));
                    c.getPieces().add(
                        gen.new Piece(null, ToolingExtensions.readStringExtension(md, ToolingExtensions.EXT_TABLE_NAME)
                            + ": " + map.getMap(), null));
                  }
                }
              }
            }
          }
          if (definition.hasDefinition()) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(gen.new Piece(null, "Definition: ", null).addStyle("font-weight:bold"));
            c.addPiece(gen.new Piece("br"));
            c.addMarkdown(definition.getDefinition());
//            c.getPieces().add(checkForNoChange(definition.getCommentElement(), gen.new Piece(null, definition.getComment(), null)));
          }
          if (definition.getComment() != null) {
            if (!c.getPieces().isEmpty())
              c.addPiece(gen.new Piece("br"));
            c.getPieces().add(gen.new Piece(null, "Comments: ", null).addStyle("font-weight:bold"));
            c.addPiece(gen.new Piece("br"));
            c.addMarkdown(definition.getComment());
//            c.getPieces().add(checkForNoChange(definition.getCommentElement(), gen.new Piece(null, definition.getComment(), null)));
          }
        }
      }
    }
    return c;
  }

  private String buildJson(Type value) throws IOException {
    if (value instanceof PrimitiveType)
      return ((PrimitiveType) value).asStringValue();

    IParser json = context.newJsonParser();
    return json.composeString(value, null);
  }

  public String describeSlice(ElementDefinitionSlicingComponent slicing) {
    return translate("sd.table", "%s, %s by %s",
        slicing.getOrdered() ? translate("sd.table", "Ordered") : translate("sd.table", "Unordered"),
        describe(slicing.getRules()), commas(slicing.getDiscriminator()));
  }

  private String commas(List<ElementDefinitionSlicingDiscriminatorComponent> list) {
    CommaSeparatedStringBuilder c = new CommaSeparatedStringBuilder();
    for (ElementDefinitionSlicingDiscriminatorComponent id : list)
      c.append(id.getType().toCode() + ":" + id.getPath());
    return c.toString();
  }

  private String describe(SlicingRules rules) {
    if (rules == null)
      return translate("sd.table", "Unspecified");
    switch (rules) {
    case CLOSED:
      return translate("sd.table", "Closed");
    case OPEN:
      return translate("sd.table", "Open");
    case OPENATEND:
      return translate("sd.table", "Open At End");
    default:
      return "??";
    }
  }

  private boolean onlyInformationIsMapping(List<ElementDefinition> list, ElementDefinition e) {
    return (!e.hasSliceName() && !e.hasSlicing() && (onlyInformationIsMapping(e))) && getChildren(list, e).isEmpty();
  }

  private boolean onlyInformationIsMapping(ElementDefinition d) {
    return !d.hasShort() && !d.hasDefinition() && !d.hasRequirements() && !d.getAlias().isEmpty() && !d.hasMinElement()
        && !d.hasMax() && !d.getType().isEmpty() && !d.hasContentReference() && !d.hasExample() && !d.hasFixed()
        && !d.hasMaxLengthElement() && !d.getCondition().isEmpty() && !d.getConstraint().isEmpty()
        && !d.hasMustSupportElement() && !d.hasBinding();
  }

  private boolean allAreReference(List<TypeRefComponent> types) {
    for (TypeRefComponent t : types) {
      if (!t.hasTarget())
        return false;
    }
    return true;
  }

  private List<ElementDefinition> getChildren(List<ElementDefinition> all, ElementDefinition element) {
    List<ElementDefinition> result = new ArrayList<ElementDefinition>();
    int i = all.indexOf(element) + 1;
    while (i < all.size() && all.get(i).getPath().length() > element.getPath().length()) {
      if ((all.get(i).getPath().substring(0, element.getPath().length() + 1).equals(element.getPath() + "."))
          && !all.get(i).getPath().substring(element.getPath().length() + 1).contains("."))
        result.add(all.get(i));
      i++;
    }
    return result;
  }

  private String tail(String path) {
    if (path.contains("."))
      return path.substring(path.lastIndexOf('.') + 1);
    else
      return path;
  }

  private boolean isDataType(String value) {
    StructureDefinition sd = context.fetchTypeDefinition(value);
    if (sd == null) // might be running before all SDs are available
      return Utilities.existsInList(value, "Address", "Age", "Annotation", "Attachment", "CodeableConcept", "Coding",
          "ContactPoint", "Count", "Distance", "Duration", "HumanName", "Identifier", "Money", "Period", "Quantity",
          "Range", "Ratio", "Reference", "SampledData", "Signature", "Timing", "ContactDetail", "Contributor",
          "DataRequirement", "Expression", "ParameterDefinition", "RelatedArtifact", "TriggerDefinition",
          "UsageContext");
    else
      return sd.getKind() == StructureDefinitionKind.COMPLEXTYPE
          && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION;
  }

  private boolean isConstrainedDataType(String value) {
    StructureDefinition sd = context.fetchTypeDefinition(value);
    if (sd == null) // might be running before all SDs are available
      return Utilities.existsInList(value, "SimpleQuantity", "MoneyQuantity");
    else
      return sd.getKind() == StructureDefinitionKind.COMPLEXTYPE && sd.getDerivation() == TypeDerivationRule.CONSTRAINT;
  }

  private String baseType(String value) {
    StructureDefinition sd = context.fetchTypeDefinition(value);
    if (sd != null) // might be running before all SDs are available
      return sd.getType();
    if (Utilities.existsInList(value, "SimpleQuantity", "MoneyQuantity"))
      return "Quantity";
    throw new Error("Internal error -  type not known " + value);
  }

  public boolean isPrimitive(String value) {
    StructureDefinition sd = context.fetchTypeDefinition(value);
    if (sd == null) // might be running before all SDs are available
      return Utilities.existsInList(value, "base64Binary", "boolean", "canonical", "code", "date", "dateTime",
          "decimal", "id", "instant", "integer", "markdown", "oid", "positiveInt", "string", "time", "unsignedInt",
          "uri", "url", "uuid");
    else
      return sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE;
  }

//  private static String listStructures(StructureDefinition p) {
//    StringBuilder b = new StringBuilder();
//    boolean first = true;
//    for (ProfileStructureComponent s : p.getStructure()) {
//      if (first)
//        first = false;
//      else
//        b.append(", ");
//      if (pkp != null && pkp.hasLinkFor(s.getType()))
//        b.append("<a href=\""+pkp.getLinkFor(s.getType())+"\">"+s.getType()+"</a>");
//      else
//        b.append(s.getType());
//    }
//    return b.toString();
//  }

  public StructureDefinition getProfile(StructureDefinition source, String url) {
    StructureDefinition profile = null;
    String code = null;
    if (url.startsWith("#")) {
      profile = source;
      code = url.substring(1);
    } else if (context != null) {
      String[] parts = url.split("\\#");
      profile = context.fetchResource(StructureDefinition.class, parts[0]);
      code = parts.length == 1 ? null : parts[1];
    }
    if (profile == null)
      return null;
    if (code == null)
      return profile;
    for (Resource r : profile.getContained()) {
      if (r instanceof StructureDefinition && r.getId().equals(code))
        return (StructureDefinition) r;
    }
    return null;
  }

  public static class ElementDefinitionHolder {
    private String name;
    private ElementDefinition self;
    private int baseIndex = 0;
    private List<ElementDefinitionHolder> children;
    private boolean placeHolder = false;

    public ElementDefinitionHolder(ElementDefinition self, boolean isPlaceholder) {
      super();
      this.self = self;
      this.name = self.getPath();
      this.placeHolder = isPlaceholder;
      children = new ArrayList<ElementDefinitionHolder>();
    }

    public ElementDefinitionHolder(ElementDefinition self) {
      this(self, false);
    }

    public ElementDefinition getSelf() {
      return self;
    }

    public List<ElementDefinitionHolder> getChildren() {
      return children;
    }

    public int getBaseIndex() {
      return baseIndex;
    }

    public void setBaseIndex(int baseIndex) {
      this.baseIndex = baseIndex;
    }

    public boolean isPlaceHolder() {
      return this.placeHolder;
    }

    @Override
    public String toString() {
      if (self.hasSliceName())
        return self.getPath() + "(" + self.getSliceName() + ")";
      else
        return self.getPath();
    }
  }

  public static class ElementDefinitionComparer implements Comparator<ElementDefinitionHolder> {

    private boolean inExtension;
    private List<ElementDefinition> snapshot;
    private int prefixLength;
    private String base;
    private String name;
    private Set<String> errors = new HashSet<String>();

    public ElementDefinitionComparer(boolean inExtension, List<ElementDefinition> snapshot, String base,
        int prefixLength, String name) {
      this.inExtension = inExtension;
      this.snapshot = snapshot;
      this.prefixLength = prefixLength;
      this.base = base;
      this.name = name;
    }

    @Override
    public int compare(ElementDefinitionHolder o1, ElementDefinitionHolder o2) {
      if (o1.getBaseIndex() == 0)
        o1.setBaseIndex(find(o1.getSelf().getPath()));
      if (o2.getBaseIndex() == 0)
        o2.setBaseIndex(find(o2.getSelf().getPath()));
      return o1.getBaseIndex() - o2.getBaseIndex();
    }

    private int find(String path) {
      String op = path;
      int lc = 0;
      String actual = base + path.substring(prefixLength);
      for (int i = 0; i < snapshot.size(); i++) {
        String p = snapshot.get(i).getPath();
        if (p.equals(actual)) {
          return i;
        }
        if (p.endsWith("[x]") && actual.startsWith(p.substring(0, p.length() - 3)) && !(actual.endsWith("[x]"))
            && !actual.substring(p.length() - 3).contains(".")) {
          return i;
        }
        if (path.startsWith(p + ".") && snapshot.get(i).hasContentReference()) {
          String ref = snapshot.get(i).getContentReference();
          if (ref.substring(1, 2).toUpperCase().equals(ref.substring(1, 2))) {
            actual = base + (ref.substring(1) + "." + path.substring(p.length() + 1)).substring(prefixLength);
            path = actual;
          } else {
            // Older versions of FHIR (e.g. 2016May) had reference of the style #parameter
            // instead of #Parameters.parameter, so we have to handle that
            actual = base
                + (path.substring(0, path.indexOf(".") + 1) + ref.substring(1) + "." + path.substring(p.length() + 1))
                    .substring(prefixLength);
            path = actual;
          }

          i = 0;
          lc++;
          if (lc > MAX_RECURSION_LIMIT)
            throw new Error("Internal recursion detection: find() loop path recursion > " + MAX_RECURSION_LIMIT
                + " - check paths are valid (for path " + path + "/" + op + ")");
        }
      }
      if (prefixLength == 0)
        errors.add("Differential contains path " + path + " which is not found in the base");
      else
        errors.add(
            "Differential contains path " + path + " which is actually " + actual + ", which is not found in the base");
      return 0;
    }

    public void checkForErrors(List<String> errorList) {
      if (errors.size() > 0) {
//        CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
//        for (String s : errors)
//          b.append("StructureDefinition "+name+": "+s);
//        throw new DefinitionException(b.toString());
        for (String s : errors)
          if (s.startsWith("!"))
            errorList.add("!StructureDefinition " + name + ": " + s.substring(1));
          else
            errorList.add("StructureDefinition " + name + ": " + s);
      }
    }
  }

  public void sortDifferential(StructureDefinition base, StructureDefinition diff, String name, List<String> errors)
      throws FHIRException {
    final List<ElementDefinition> diffList = diff.getDifferential().getElement();
    int lastCount = diffList.size();
    // first, we move the differential elements into a tree
    if (diffList.isEmpty())
      return;

    ElementDefinitionHolder edh = null;
    int i = 0;
    if (diffList.get(0).getPath().contains(".")) {
      String newPath = diffList.get(0).getPath().split("\\.")[0];
      ElementDefinition e = new ElementDefinition(new StringType(newPath));
      edh = new ElementDefinitionHolder(e, true);
    } else {
      edh = new ElementDefinitionHolder(diffList.get(0));
      i = 1;
    }

    boolean hasSlicing = false;
    List<String> paths = new ArrayList<String>(); // in a differential, slicing may not be stated explicitly
    for (ElementDefinition elt : diffList) {
      if (elt.hasSlicing() || paths.contains(elt.getPath())) {
        hasSlicing = true;
        break;
      }
      paths.add(elt.getPath());
    }
    if (!hasSlicing) {
      // if Differential does not have slicing then safe to pre-sort the list
      // so elements and subcomponents are together
      Collections.sort(diffList, new ElementNameCompare());
    }

    processElementsIntoTree(edh, i, diff.getDifferential().getElement());

    // now, we sort the siblings throughout the tree
    ElementDefinitionComparer cmp = new ElementDefinitionComparer(true, base.getSnapshot().getElement(), "", 0, name);
    sortElements(edh, cmp, errors);

    // now, we serialise them back to a list
    diffList.clear();
    writeElements(edh, diffList);

    if (lastCount != diffList.size())
      errors.add("Sort failed: counts differ; at least one of the paths in the differential is illegal");
  }

  private int processElementsIntoTree(ElementDefinitionHolder edh, int i, List<ElementDefinition> list) {
    String path = edh.getSelf().getPath();
    final String prefix = path + ".";
    while (i < list.size() && list.get(i).getPath().startsWith(prefix)) {
      if (list.get(i).getPath().substring(prefix.length() + 1).contains(".")) {
        String newPath = prefix + list.get(i).getPath().substring(prefix.length()).split("\\.")[0];
        ElementDefinition e = new ElementDefinition(new StringType(newPath));
        ElementDefinitionHolder child = new ElementDefinitionHolder(e, true);
        edh.getChildren().add(child);
        i = processElementsIntoTree(child, i, list);

      } else {
        ElementDefinitionHolder child = new ElementDefinitionHolder(list.get(i));
        edh.getChildren().add(child);
        i = processElementsIntoTree(child, i + 1, list);
      }
    }
    return i;
  }

  private void sortElements(ElementDefinitionHolder edh, ElementDefinitionComparer cmp, List<String> errors)
      throws FHIRException {
    if (edh.getChildren().size() == 1)
      // special case - sort needsto allocate base numbers, but there'll be no sort if
      // there's only 1 child. So in that case, we just go ahead and allocated base
      // number directly
      edh.getChildren().get(0).baseIndex = cmp.find(edh.getChildren().get(0).getSelf().getPath());
    else
      Collections.sort(edh.getChildren(), cmp);
    cmp.checkForErrors(errors);

    for (ElementDefinitionHolder child : edh.getChildren()) {
      if (child.getChildren().size() > 0) {
        ElementDefinitionComparer ccmp = getComparer(cmp, child);
        if (ccmp != null)
          sortElements(child, ccmp, errors);
      }
    }
  }

  public ElementDefinitionComparer getComparer(ElementDefinitionComparer cmp, ElementDefinitionHolder child)
      throws FHIRException, Error {
    // what we have to check for here is running off the base profile into a data
    // type profile
    ElementDefinition ed = cmp.snapshot.get(child.getBaseIndex());
    ElementDefinitionComparer ccmp;
    if (ed.getType().isEmpty() || isAbstract(ed.getType().get(0).getWorkingCode())
        || ed.getType().get(0).getWorkingCode().equals(ed.getPath())) {
      ccmp = new ElementDefinitionComparer(true, cmp.snapshot, cmp.base, cmp.prefixLength, cmp.name);
    } else if (ed.getType().get(0).getWorkingCode().equals("Extension") && child.getSelf().getType().size() == 1
        && child.getSelf().getType().get(0).hasProfile()) {
      StructureDefinition profile = context.fetchResource(StructureDefinition.class,
          child.getSelf().getType().get(0).getProfile().get(0).getValue());
      if (profile == null)
        ccmp = null; // this might happen before everything is loaded. And we don't so much care
                     // about sot order in this case
      else
        ccmp = new ElementDefinitionComparer(true, profile.getSnapshot().getElement(),
            ed.getType().get(0).getWorkingCode(), child.getSelf().getPath().length(), cmp.name);
    } else if (ed.getType().size() == 1 && !ed.getType().get(0).getWorkingCode().equals("*")) {
      StructureDefinition profile = context.fetchResource(StructureDefinition.class,
          sdNs(ed.getType().get(0).getWorkingCode()));
      if (profile == null)
        throw new FHIRException(
            "Unable to resolve profile " + sdNs(ed.getType().get(0).getWorkingCode()) + " in element " + ed.getPath());
      ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(),
          ed.getType().get(0).getWorkingCode(), child.getSelf().getPath().length(), cmp.name);
    } else if (child.getSelf().getType().size() == 1) {
      StructureDefinition profile = context.fetchResource(StructureDefinition.class,
          sdNs(child.getSelf().getType().get(0).getWorkingCode()));
      if (profile == null)
        throw new FHIRException(
            "Unable to resolve profile " + sdNs(ed.getType().get(0).getWorkingCode()) + " in element " + ed.getPath());
      ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(),
          child.getSelf().getType().get(0).getWorkingCode(), child.getSelf().getPath().length(), cmp.name);
    } else if (ed.getPath().endsWith("[x]") && !child.getSelf().getPath().endsWith("[x]")) {
      String edLastNode = ed.getPath().replaceAll("(.*\\.)*(.*)", "$2");
      String childLastNode = child.getSelf().getPath().replaceAll("(.*\\.)*(.*)", "$2");
      String p = childLastNode.substring(edLastNode.length() - 3);
      if (isPrimitive(Utilities.uncapitalize(p)))
        p = Utilities.uncapitalize(p);
      StructureDefinition sd = context.fetchResource(StructureDefinition.class, sdNs(p));
      if (sd == null)
        throw new Error("Unable to find profile '" + p + "' at " + ed.getId());
      ccmp = new ElementDefinitionComparer(false, sd.getSnapshot().getElement(), p, child.getSelf().getPath().length(),
          cmp.name);
    } else if (child.getSelf().hasType() && child.getSelf().getType().get(0).getWorkingCode().equals("Reference")) {
      for (TypeRefComponent t : child.getSelf().getType()) {
        if (!t.getWorkingCode().equals("Reference")) {
          throw new Error(
              "Can't have children on an element with a polymorphic type - you must slice and constrain the types first (sortElements: "
                  + ed.getPath() + ":" + typeCode(ed.getType()) + ")");
        }
      }
      StructureDefinition profile = context.fetchResource(StructureDefinition.class,
          sdNs(ed.getType().get(0).getWorkingCode()));
      ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(),
          ed.getType().get(0).getWorkingCode(), child.getSelf().getPath().length(), cmp.name);
    } else if (!child.getSelf().hasType() && ed.getType().get(0).getWorkingCode().equals("Reference")) {
      for (TypeRefComponent t : ed.getType()) {
        if (!t.getWorkingCode().equals("Reference")) {
          throw new Error("Not handled yet (sortElements: " + ed.getPath() + ":" + typeCode(ed.getType()) + ")");
        }
      }
      StructureDefinition profile = context.fetchResource(StructureDefinition.class,
          sdNs(ed.getType().get(0).getWorkingCode()));
      ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(),
          ed.getType().get(0).getWorkingCode(), child.getSelf().getPath().length(), cmp.name);
    } else {
      // this is allowed if we only profile the extensions
      StructureDefinition profile = context.fetchResource(StructureDefinition.class, sdNs("Element"));
      if (profile == null)
        throw new FHIRException(
            "Unable to resolve profile " + sdNs(ed.getType().get(0).getWorkingCode()) + " in element " + ed.getPath());
      ccmp = new ElementDefinitionComparer(false, profile.getSnapshot().getElement(), "Element",
          child.getSelf().getPath().length(), cmp.name);
//      throw new Error("Not handled yet (sortElements: "+ed.getPath()+":"+typeCode(ed.getType())+")");
    }
    return ccmp;
  }

  private static String sdNs(String type) {
    return sdNs(type, null);
  }

  public static String sdNs(String type, String overrideVersionNs) {
    if (Utilities.isAbsoluteUrl(type))
      return type;
    else if (overrideVersionNs != null)
      return Utilities.pathURL(overrideVersionNs, type);
    else
      return "http://hl7.org/fhir/StructureDefinition/" + type;
  }

  private boolean isAbstract(String code) {
    return code.equals("Element") || code.equals("BackboneElement") || code.equals("Resource")
        || code.equals("DomainResource");
  }

  private void writeElements(ElementDefinitionHolder edh, List<ElementDefinition> list) {
    if (!edh.isPlaceHolder())
      list.add(edh.getSelf());
    for (ElementDefinitionHolder child : edh.getChildren()) {
      writeElements(child, list);
    }
  }

  /**
   * First compare element by path then by name if same
   */
  private static class ElementNameCompare implements Comparator<ElementDefinition> {

    @Override
    public int compare(ElementDefinition o1, ElementDefinition o2) {
      String path1 = normalizePath(o1);
      String path2 = normalizePath(o2);
      int cmp = path1.compareTo(path2);
      if (cmp == 0) {
        String name1 = o1.hasSliceName() ? o1.getSliceName() : "";
        String name2 = o2.hasSliceName() ? o2.getSliceName() : "";
        cmp = name1.compareTo(name2);
      }
      return cmp;
    }

    private static String normalizePath(ElementDefinition e) {
      if (!e.hasPath())
        return "";
      String path = e.getPath();
      // if sorting element names make sure onset[x] appears before onsetAge,
      // onsetDate, etc.
      // so strip off the [x] suffix when comparing the path names.
      if (path.endsWith("[x]")) {
        path = path.substring(0, path.length() - 3);
      }
      return path;
    }

  }

  // generate schematrons for the rules in a structure definition
  public void generateSchematrons(OutputStream dest, StructureDefinition structure)
      throws IOException, DefinitionException {
    if (structure.getDerivation() != TypeDerivationRule.CONSTRAINT)
      throw new DefinitionException("not the right kind of structure to generate schematrons for");
    if (!structure.hasSnapshot())
      throw new DefinitionException("needs a snapshot");

    StructureDefinition base = context.fetchResource(StructureDefinition.class, structure.getBaseDefinition());

    if (base != null) {
      SchematronWriter sch = new SchematronWriter(dest, SchematronType.PROFILE, base.getName());

      ElementDefinition ed = structure.getSnapshot().getElement().get(0);
      generateForChildren(sch, "f:" + ed.getPath(), ed, structure, base);
      sch.dump();
    }
  }

  // generate a CSV representation of the structure definition
  public void generateCsvs(OutputStream dest, StructureDefinition structure, boolean asXml)
      throws IOException, DefinitionException, Exception {
    if (!structure.hasSnapshot())
      throw new DefinitionException("needs a snapshot");

    CSVWriter csv = new CSVWriter(dest, structure, asXml);

    for (ElementDefinition child : structure.getSnapshot().getElement()) {
      csv.processElement(child);
    }
    csv.dump();
  }

  // generate an Excel representation of the structure definition
  public void generateXlsx(OutputStream dest, StructureDefinition structure, boolean asXml,
      boolean hideMustSupportFalse) throws IOException, DefinitionException, Exception {
    if (!structure.hasSnapshot())
      throw new DefinitionException("needs a snapshot");

    XLSXWriter xlsx = new XLSXWriter(dest, structure, asXml, hideMustSupportFalse);

    for (ElementDefinition child : structure.getSnapshot().getElement()) {
      xlsx.processElement(child);
    }
    xlsx.dump();
    xlsx.close();
  }

  private class Slicer extends ElementDefinitionSlicingComponent {
    String criteria = "";
    String name = "";
    boolean check;

    public Slicer(boolean cantCheck) {
      super();
      this.check = cantCheck;
    }
  }

  private Slicer generateSlicer(ElementDefinition child, ElementDefinitionSlicingComponent slicing,
      StructureDefinition structure) {
    // given a child in a structure, it's sliced. figure out the slicing xpath
    if (child.getPath().endsWith(".extension")) {
      ElementDefinition ued = getUrlFor(structure, child);
      if ((ued == null || !ued.hasFixed()) && !(child.hasType() && (child.getType().get(0).hasProfile())))
        return new Slicer(false);
      else {
        Slicer s = new Slicer(true);
        String url = (ued == null || !ued.hasFixed()) ? child.getType().get(0).getProfile().get(0).getValue()
            : ((UriType) ued.getFixed()).asStringValue();
        s.name = " with URL = '" + url + "'";
        s.criteria = "[@url = '" + url + "']";
        return s;
      }
    } else
      return new Slicer(false);
  }

  private void generateForChildren(SchematronWriter sch, String xpath, ElementDefinition ed,
      StructureDefinition structure, StructureDefinition base) throws IOException {
    // generateForChild(txt, structure, child);
    List<ElementDefinition> children = getChildList(structure, ed);
    String sliceName = null;
    ElementDefinitionSlicingComponent slicing = null;
    for (ElementDefinition child : children) {
      String name = tail(child.getPath());
      if (child.hasSlicing()) {
        sliceName = name;
        slicing = child.getSlicing();
      } else if (!name.equals(sliceName))
        slicing = null;

      ElementDefinition based = getByPath(base, child.getPath());
      boolean doMin = (child.getMin() > 0) && (based == null || (child.getMin() != based.getMin()));
      boolean doMax = child.hasMax() && !child.getMax().equals("*")
          && (based == null || (!child.getMax().equals(based.getMax())));
      Slicer slicer = slicing == null ? new Slicer(true) : generateSlicer(child, slicing, structure);
      if (slicer.check) {
        if (doMin || doMax) {
          Section s = sch.section(xpath);
          Rule r = s.rule(xpath);
          if (doMin)
            r.assrt("count(f:" + name + slicer.criteria + ") >= " + Integer.toString(child.getMin()),
                name + slicer.name + ": minimum cardinality of '" + name + "' is " + Integer.toString(child.getMin()));
          if (doMax)
            r.assrt("count(f:" + name + slicer.criteria + ") <= " + child.getMax(),
                name + slicer.name + ": maximum cardinality of '" + name + "' is " + child.getMax());
        }
      }
    }
    for (ElementDefinitionConstraintComponent inv : ed.getConstraint()) {
      if (inv.hasXpath()) {
        Section s = sch.section(ed.getPath());
        Rule r = s.rule(xpath);
        r.assrt(inv.getXpath(), (inv.hasId() ? inv.getId() + ": " : "") + inv.getHuman()
            + (inv.hasUserData(IS_DERIVED) ? " (inherited)" : ""));
      }
    }
    for (ElementDefinition child : children) {
      String name = tail(child.getPath());
      generateForChildren(sch, xpath + "/f:" + name, child, structure, base);
    }
  }

  private ElementDefinition getByPath(StructureDefinition base, String path) {
    for (ElementDefinition ed : base.getSnapshot().getElement()) {
      if (ed.getPath().equals(path))
        return ed;
      if (ed.getPath().endsWith("[x]") && ed.getPath().length() <= path.length() - 3
          && ed.getPath().substring(0, ed.getPath().length() - 3).equals(path.substring(0, ed.getPath().length() - 3)))
        return ed;
    }
    return null;
  }

  public void setIds(StructureDefinition sd, boolean checkFirst) throws DefinitionException {
    if (!checkFirst || !sd.hasDifferential() || hasMissingIds(sd.getDifferential().getElement())) {
      if (!sd.hasDifferential())
        sd.setDifferential(new StructureDefinitionDifferentialComponent());
      generateIds(sd.getDifferential().getElement(), sd.getUrl());
    }
    if (!checkFirst || !sd.hasSnapshot() || hasMissingIds(sd.getSnapshot().getElement())) {
      if (!sd.hasSnapshot())
        sd.setSnapshot(new StructureDefinitionSnapshotComponent());
      generateIds(sd.getSnapshot().getElement(), sd.getUrl());
    }
  }

  private boolean hasMissingIds(List<ElementDefinition> list) {
    for (ElementDefinition ed : list) {
      if (!ed.hasId())
        return true;
    }
    return false;
  }

  public class SliceList {

    private Map<String, String> slices = new HashMap<>();

    public void seeElement(ElementDefinition ed) {
      Iterator<Map.Entry<String, String>> iter = slices.entrySet().iterator();
      while (iter.hasNext()) {
        Map.Entry<String, String> entry = iter.next();
        if (entry.getKey().length() > ed.getPath().length() || entry.getKey().equals(ed.getPath()))
          iter.remove();
      }

      if (ed.hasSliceName())
        slices.put(ed.getPath(), ed.getSliceName());
    }

    public String[] analyse(List<String> paths) {
      String s = paths.get(0);
      String[] res = new String[paths.size()];
      res[0] = null;
      for (int i = 1; i < paths.size(); i++) {
        s = s + "." + paths.get(i);
        if (slices.containsKey(s))
          res[i] = slices.get(s);
        else
          res[i] = null;
      }
      return res;
    }

  }

  private void generateIds(List<ElementDefinition> list, String name) throws DefinitionException {
    if (list.isEmpty())
      return;

    Map<String, String> idMap = new HashMap<String, String>();
    Map<String, String> idList = new HashMap<String, String>();

    SliceList sliceInfo = new SliceList();
    // first pass, update the element ids
    for (ElementDefinition ed : list) {
      List<String> paths = new ArrayList<String>();
      if (!ed.hasPath())
        throw new DefinitionException(
            "No path on element Definition " + Integer.toString(list.indexOf(ed)) + " in " + name);
      sliceInfo.seeElement(ed);
      String[] pl = ed.getPath().split("\\.");
      for (int i = paths.size(); i < pl.length; i++) // -1 because the last path is in focus
        paths.add(pl[i]);
      String slices[] = sliceInfo.analyse(paths);

      StringBuilder b = new StringBuilder();
      b.append(paths.get(0));
      for (int i = 1; i < paths.size(); i++) {
        b.append(".");
        String s = paths.get(i);
        String p = slices[i];
        b.append(s);
        if (p != null) {
          b.append(":");
          b.append(p);
        }
      }
      String bs = b.toString();
      idMap.put(ed.hasId() ? ed.getId() : ed.getPath(), bs);
      ed.setId(bs);
      if (idList.containsKey(bs)) {
        if (exception || messages == null)
          throw new DefinitionException(
              "Same id '" + bs + "'on multiple elements " + idList.get(bs) + "/" + ed.getPath() + " in " + name);
        else
          messages.add(new ValidationMessage(Source.ProfileValidator, ValidationMessage.IssueType.BUSINESSRULE,
              name + "." + bs, "Duplicate Element id " + bs, ValidationMessage.IssueSeverity.ERROR));
      }
      idList.put(bs, ed.getPath());
      if (ed.hasContentReference()) {
        String s = ed.getContentReference().substring(1);
        if (idMap.containsKey(s))
          ed.setContentReference("#" + idMap.get(s));

      }
    }
    // second path - fix up any broken path based id references

  }

//  private String describeExtension(ElementDefinition ed) {
//    if (!ed.hasType() || !ed.getTypeFirstRep().hasProfile())
//      return "";
//    return "$"+urlTail(ed.getTypeFirstRep().getProfile());
//  }
//

  private String urlTail(String profile) {
    return profile.contains("/") ? profile.substring(profile.lastIndexOf("/") + 1) : profile;
  }

  private String checkName(String name) {
//    if (name.contains("."))
////      throw new Exception("Illegal name "+name+": no '.'");
//    if (name.contains(" "))
//      throw new Exception("Illegal name "+name+": no spaces");
    StringBuilder b = new StringBuilder();
    for (char c : name.toCharArray()) {
      if (!Utilities.existsInList(c, '.', ' ', ':', '"', '\'', '(', ')', '&', '[', ']'))
        b.append(c);
    }
    return b.toString().toLowerCase();
  }

  private int charCount(String path, char t) {
    int res = 0;
    for (char ch : path.toCharArray()) {
      if (ch == t)
        res++;
    }
    return res;
  }

//
//private void generateForChild(TextStreamWriter txt,
//    StructureDefinition structure, ElementDefinition child) {
//  // TODO Auto-generated method stub
//
//}

  private interface ExampleValueAccessor {
    Type getExampleValue(ElementDefinition ed);

    String getId();
  }

  private class BaseExampleValueAccessor implements ExampleValueAccessor {
    @Override
    public Type getExampleValue(ElementDefinition ed) {
      if (ed.hasFixed())
        return ed.getFixed();
      if (ed.hasExample())
        return ed.getExample().get(0).getValue();
      else
        return null;
    }

    @Override
    public String getId() {
      return "-genexample";
    }
  }

  private class ExtendedExampleValueAccessor implements ExampleValueAccessor {
    private String index;

    public ExtendedExampleValueAccessor(String index) {
      this.index = index;
    }

    @Override
    public Type getExampleValue(ElementDefinition ed) {
      if (ed.hasFixed())
        return ed.getFixed();
      for (Extension ex : ed.getExtension()) {
        String ndx = ToolingExtensions.readStringExtension(ex, "index");
        Type value = ToolingExtensions.getExtension(ex, "exValue").getValue();
        if (index.equals(ndx) && value != null)
          return value;
      }
      return null;
    }

    @Override
    public String getId() {
      return "-genexample-" + index;
    }
  }

  public List<org.hl7.fhir.r4.elementmodel.Element> generateExamples(StructureDefinition sd, boolean evenWhenNoExamples)
      throws FHIRException {
    List<org.hl7.fhir.r4.elementmodel.Element> examples = new ArrayList<org.hl7.fhir.r4.elementmodel.Element>();
    if (sd.hasSnapshot()) {
      if (evenWhenNoExamples || hasAnyExampleValues(sd))
        examples.add(generateExample(sd, new BaseExampleValueAccessor()));
      for (int i = 1; i <= 50; i++) {
        if (hasAnyExampleValues(sd, Integer.toString(i)))
          examples.add(generateExample(sd, new ExtendedExampleValueAccessor(Integer.toString(i))));
      }
    }
    return examples;
  }

  private org.hl7.fhir.r4.elementmodel.Element generateExample(StructureDefinition profile,
      ExampleValueAccessor accessor) throws FHIRException {
    ElementDefinition ed = profile.getSnapshot().getElementFirstRep();
    org.hl7.fhir.r4.elementmodel.Element r = new org.hl7.fhir.r4.elementmodel.Element(ed.getPath(),
        new Property(context, ed, profile));
    List<ElementDefinition> children = getChildMap(profile, ed);
    for (ElementDefinition child : children) {
      if (child.getPath().endsWith(".id")) {
        org.hl7.fhir.r4.elementmodel.Element id = new org.hl7.fhir.r4.elementmodel.Element("id",
            new Property(context, child, profile));
        id.setValue(profile.getId() + accessor.getId());
        r.getChildren().add(id);
      } else {
        org.hl7.fhir.r4.elementmodel.Element e = createExampleElement(profile, child, accessor);
        if (e != null)
          r.getChildren().add(e);
      }
    }
    return r;
  }

  private org.hl7.fhir.r4.elementmodel.Element createExampleElement(StructureDefinition profile, ElementDefinition ed,
      ExampleValueAccessor accessor) throws FHIRException {
    Type v = accessor.getExampleValue(ed);
    if (v != null) {
      return new ObjectConverter(context).convert(new Property(context, ed, profile), v);
    } else {
      org.hl7.fhir.r4.elementmodel.Element res = new org.hl7.fhir.r4.elementmodel.Element(tail(ed.getPath()),
          new Property(context, ed, profile));
      boolean hasValue = false;
      List<ElementDefinition> children = getChildMap(profile, ed);
      for (ElementDefinition child : children) {
        if (!child.hasContentReference()) {
          org.hl7.fhir.r4.elementmodel.Element e = createExampleElement(profile, child, accessor);
          if (e != null) {
            hasValue = true;
            res.getChildren().add(e);
          }
        }
      }
      if (hasValue)
        return res;
      else
        return null;
    }
  }

  private boolean hasAnyExampleValues(StructureDefinition sd, String index) {
    for (ElementDefinition ed : sd.getSnapshot().getElement())
      for (Extension ex : ed.getExtension()) {
        String ndx = ToolingExtensions.readStringExtension(ex, "index");
        Extension exv = ToolingExtensions.getExtension(ex, "exValue");
        if (exv != null) {
          Type value = exv.getValue();
          if (index.equals(ndx) && value != null)
            return true;
        }
      }
    return false;
  }

  private boolean hasAnyExampleValues(StructureDefinition sd) {
    for (ElementDefinition ed : sd.getSnapshot().getElement())
      if (ed.hasExample())
        return true;
    return false;
  }

  public void populateLogicalSnapshot(StructureDefinition sd) throws FHIRException {
    sd.getSnapshot().getElement().add(sd.getDifferential().getElementFirstRep().copy());

    if (sd.hasBaseDefinition()) {
      StructureDefinition base = context.fetchResource(StructureDefinition.class, sd.getBaseDefinition());
      if (base == null)
        throw new FHIRException(
            "Unable to find base definition for logical model: " + sd.getBaseDefinition() + " from " + sd.getUrl());
      copyElements(sd, base.getSnapshot().getElement());
    }
    copyElements(sd, sd.getDifferential().getElement());
  }

  private void copyElements(StructureDefinition sd, List<ElementDefinition> list) {
    for (ElementDefinition ed : list) {
      if (ed.getPath().contains(".")) {
        ElementDefinition n = ed.copy();
        n.setPath(sd.getSnapshot().getElementFirstRep().getPath() + "."
            + ed.getPath().substring(ed.getPath().indexOf(".") + 1));
        sd.getSnapshot().addElement(n);
      }
    }
  }

  public void cleanUpDifferential(StructureDefinition sd) {
    if (sd.getDifferential().getElement().size() > 1)
      cleanUpDifferential(sd, 1);
  }

  private void cleanUpDifferential(StructureDefinition sd, int start) {
    int level = Utilities.charCount(sd.getDifferential().getElement().get(start).getPath(), '.');
    int c = start;
    int len = sd.getDifferential().getElement().size();
    HashSet<String> paths = new HashSet<String>();
    while (c < len && Utilities.charCount(sd.getDifferential().getElement().get(c).getPath(), '.') == level) {
      ElementDefinition ed = sd.getDifferential().getElement().get(c);
      if (!paths.contains(ed.getPath())) {
        paths.add(ed.getPath());
        int ic = c + 1;
        while (ic < len && Utilities.charCount(sd.getDifferential().getElement().get(ic).getPath(), '.') > level)
          ic++;
        ElementDefinition slicer = null;
        List<ElementDefinition> slices = new ArrayList<ElementDefinition>();
        slices.add(ed);
        while (ic < len && Utilities.charCount(sd.getDifferential().getElement().get(ic).getPath(), '.') == level) {
          ElementDefinition edi = sd.getDifferential().getElement().get(ic);
          if (ed.getPath().equals(edi.getPath())) {
            if (slicer == null) {
              slicer = new ElementDefinition();
              slicer.setPath(edi.getPath());
              slicer.getSlicing().setRules(SlicingRules.OPEN);
              sd.getDifferential().getElement().add(c, slicer);
              c++;
              ic++;
            }
            slices.add(edi);
          }
          ic++;
          while (ic < len && Utilities.charCount(sd.getDifferential().getElement().get(ic).getPath(), '.') > level)
            ic++;
        }
        // now we're at the end, we're going to figure out the slicing discriminator
        if (slicer != null)
          determineSlicing(slicer, slices);
      }
      c++;
      if (c < len && Utilities.charCount(sd.getDifferential().getElement().get(c).getPath(), '.') > level) {
        cleanUpDifferential(sd, c);
        c++;
        while (c < len && Utilities.charCount(sd.getDifferential().getElement().get(c).getPath(), '.') > level)
          c++;
      }
    }
  }

  private void determineSlicing(ElementDefinition slicer, List<ElementDefinition> slices) {
    // first, name them
    int i = 0;
    for (ElementDefinition ed : slices) {
      if (ed.hasUserData("slice-name")) {
        ed.setSliceName(ed.getUserString("slice-name"));
      } else {
        i++;
        ed.setSliceName("slice-" + Integer.toString(i));
      }
    }
    // now, the hard bit, how are they differentiated?
    // right now, we hard code this...
    if (slicer.getPath().endsWith(".extension") || slicer.getPath().endsWith(".modifierExtension"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("url");
    else if (slicer.getPath().equals("DiagnosticReport.result"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("reference.code");
    else if (slicer.getPath().equals("Observation.related"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("target.reference.code");
    else if (slicer.getPath().equals("Bundle.entry"))
      slicer.getSlicing().addDiscriminator().setType(DiscriminatorType.VALUE).setPath("resource.@profile");
    else
      throw new Error("No slicing for " + slicer.getPath());
  }

  public class SpanEntry {
    private List<SpanEntry> children = new ArrayList<SpanEntry>();
    private boolean profile;
    private String id;
    private String name;
    private String resType;
    private String cardinality;
    private String description;
    private String profileLink;
    private String resLink;
    private String type;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getResType() {
      return resType;
    }

    public void setResType(String resType) {
      this.resType = resType;
    }

    public String getCardinality() {
      return cardinality;
    }

    public void setCardinality(String cardinality) {
      this.cardinality = cardinality;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public String getProfileLink() {
      return profileLink;
    }

    public void setProfileLink(String profileLink) {
      this.profileLink = profileLink;
    }

    public String getResLink() {
      return resLink;
    }

    public void setResLink(String resLink) {
      this.resLink = resLink;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public boolean isProfile() {
      return profile;
    }

    public void setProfile(boolean profile) {
      this.profile = profile;
    }

    public List<SpanEntry> getChildren() {
      return children;
    }

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

  }

  public XhtmlNode generateSpanningTable(StructureDefinition profile, String imageFolder, boolean onlyConstraints,
      String constraintPrefix, Set<String> outputTracker) throws IOException, FHIRException {
    HierarchicalTableGenerator gen = new HierarchicalTableGenerator(new RenderingI18nContext(), imageFolder, false, true);
    TableModel model = initSpanningTable(gen, "", false, profile.getId());
    Set<String> processed = new HashSet<String>();
    SpanEntry span = buildSpanningTable("(focus)", "", profile, processed, onlyConstraints, constraintPrefix);

    genSpanEntry(gen, model.getRows(), span);
    return gen.generate(model, "", 0, outputTracker);
  }

  private SpanEntry buildSpanningTable(String name, String cardinality, StructureDefinition profile,
      Set<String> processed, boolean onlyConstraints, String constraintPrefix) throws IOException {
    SpanEntry res = buildSpanEntryFromProfile(name, cardinality, profile);
    boolean wantProcess = !processed.contains(profile.getUrl());
    processed.add(profile.getUrl());
    if (wantProcess && profile.getDerivation() == TypeDerivationRule.CONSTRAINT) {
      for (ElementDefinition ed : profile.getSnapshot().getElement()) {
        if (!"0".equals(ed.getMax()) && ed.getType().size() > 0) {
          String card = getCardinality(ed, profile.getSnapshot().getElement());
          if (!card.endsWith(".0")) {
            List<String> refProfiles = listReferenceProfiles(ed);
            if (refProfiles.size() > 0) {
              String uri = refProfiles.get(0);
              if (uri != null) {
                StructureDefinition sd = context.fetchResource(StructureDefinition.class, uri);
                if (sd != null && (!onlyConstraints || (sd.getDerivation() == TypeDerivationRule.CONSTRAINT
                    && (constraintPrefix == null || sd.getUrl().startsWith(constraintPrefix))))) {
                  res.getChildren().add(
                      buildSpanningTable(nameForElement(ed), card, sd, processed, onlyConstraints, constraintPrefix));
                }
              }
            }
          }
        }
      }
    }
    return res;
  }

  private String getCardinality(ElementDefinition ed, List<ElementDefinition> list) {
    int min = ed.getMin();
    int max = !ed.hasMax() || ed.getMax().equals("*") ? Integer.MAX_VALUE : Integer.parseInt(ed.getMax());
    while (ed != null && ed.getPath().contains(".")) {
      ed = findParent(ed, list);
      if (ed.getMax().equals("0"))
        max = 0;
      else if (!ed.getMax().equals("1") && !ed.hasSlicing())
        max = Integer.MAX_VALUE;
      if (ed.getMin() == 0)
        min = 0;
    }
    return Integer.toString(min) + ".." + (max == Integer.MAX_VALUE ? "*" : Integer.toString(max));
  }

  private ElementDefinition findParent(ElementDefinition ed, List<ElementDefinition> list) {
    int i = list.indexOf(ed) - 1;
    while (i >= 0 && !ed.getPath().startsWith(list.get(i).getPath() + "."))
      i--;
    if (i == -1)
      return null;
    else
      return list.get(i);
  }

  private List<String> listReferenceProfiles(ElementDefinition ed) {
    List<String> res = new ArrayList<String>();
    for (TypeRefComponent tr : ed.getType()) {
      // code is null if we're dealing with "value" and profile is null if we just
      // have Reference()
      if (tr.hasTarget() && tr.hasTargetProfile())
        for (UriType u : tr.getTargetProfile())
          res.add(u.getValue());
    }
    return res;
  }

  private String nameForElement(ElementDefinition ed) {
    return ed.getPath().substring(ed.getPath().indexOf(".") + 1);
  }

  private SpanEntry buildSpanEntryFromProfile(String name, String cardinality, StructureDefinition profile)
      throws IOException {
    SpanEntry res = new SpanEntry();
    res.setName(name);
    res.setCardinality(cardinality);
    res.setProfileLink(profile.getUserString("path"));
    res.setResType(profile.getType());
    StructureDefinition base = context.fetchResource(StructureDefinition.class, res.getResType());
    if (base != null)
      res.setResLink(base.getUserString("path"));
    res.setId(profile.getId());
    res.setProfile(profile.getDerivation() == TypeDerivationRule.CONSTRAINT);
    StringBuilder b = new StringBuilder();
    b.append(res.getResType());
    boolean first = true;
    boolean open = false;
    if (profile.getDerivation() == TypeDerivationRule.CONSTRAINT) {
      res.setDescription(profile.getName());
      for (ElementDefinition ed : profile.getSnapshot().getElement()) {
        if (isKeyProperty(ed.getBase().getPath()) && ed.hasFixed()) {
          if (first) {
            open = true;
            first = false;
            b.append("[");
          } else {
            b.append(", ");
          }
          b.append(tail(ed.getBase().getPath()));
          b.append("=");
          b.append(summarize(ed.getFixed()));
        }
      }
      if (open)
        b.append("]");
    } else
      res.setDescription("Base FHIR " + profile.getName());
    res.setType(b.toString());
    return res;
  }

  private String summarize(Type value) throws IOException {
    if (value instanceof Coding)
      return summarizeCoding((Coding) value);
    else if (value instanceof CodeableConcept)
      return summarizeCodeableConcept((CodeableConcept) value);
    else
      return buildJson(value);
  }

  private String summarizeCoding(Coding value) {
    String uri = value.getSystem();
    String system = NarrativeGenerator.describeSystem(uri);
    if (Utilities.isURL(system)) {
      if (system.equals("http://cap.org/protocols"))
        system = "CAP Code";
    }
    return system + " " + value.getCode();
  }

  private String summarizeCodeableConcept(CodeableConcept value) {
    if (value.hasCoding())
      return summarizeCoding(value.getCodingFirstRep());
    else
      return value.getText();
  }

  private boolean isKeyProperty(String path) {
    return Utilities.existsInList(path, "Observation.code");
  }

  public TableModel initSpanningTable(HierarchicalTableGenerator gen, String prefix, boolean isLogical, String id) {
    TableModel model = gen.new TableModel(id, false);

    model.setDocoImg(prefix + "help16.png");
    model.setDocoRef(prefix + "formats.html#table"); // todo: change to graph definition
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Property", "A profiled resource", null, 0));
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Card.",
        "Minimum and Maximum # of times the the element can appear in the instance", null, 0));
    model.getTitles().add(gen.new Title(null, model.getDocoRef(), "Content", "What goes here", null, 0));
    model.getTitles()
        .add(gen.new Title(null, model.getDocoRef(), "Description", "Description of the profile", null, 0));
    return model;
  }

  private void genSpanEntry(HierarchicalTableGenerator gen, List<Row> rows, SpanEntry span) throws IOException {
    Row row = gen.new Row();
    rows.add(row);
    row.setAnchor(span.getId());
    // row.setColor(..?);
    if (span.isProfile())
      row.setIcon("icon_profile.png", HierarchicalTableGenerator.TEXT_ICON_PROFILE);
    else
      row.setIcon("icon_resource.png", HierarchicalTableGenerator.TEXT_ICON_RESOURCE);

    row.getCells().add(gen.new Cell(null, null, span.getName(), null, null));
    row.getCells().add(gen.new Cell(null, null, span.getCardinality(), null, null));
    row.getCells().add(gen.new Cell(null, span.getProfileLink(), span.getType(), null, null));
    row.getCells().add(gen.new Cell(null, null, span.getDescription(), null, null));

    for (SpanEntry child : span.getChildren())
      genSpanEntry(gen, row.getSubRows(), child);
  }

  public static ElementDefinitionSlicingDiscriminatorComponent interpretR2Discriminator(String discriminator,
      boolean isExists) {
    if (discriminator.endsWith("@pattern"))
      return makeDiscriminator(DiscriminatorType.PATTERN,
          discriminator.length() == 8 ? "" : discriminator.substring(0, discriminator.length() - 9));
    if (discriminator.endsWith("@profile"))
      return makeDiscriminator(DiscriminatorType.PROFILE,
          discriminator.length() == 8 ? "" : discriminator.substring(0, discriminator.length() - 9));
    if (discriminator.endsWith("@type"))
      return makeDiscriminator(DiscriminatorType.TYPE,
          discriminator.length() == 5 ? "" : discriminator.substring(0, discriminator.length() - 6));
    if (discriminator.endsWith("@exists"))
      return makeDiscriminator(DiscriminatorType.EXISTS,
          discriminator.length() == 7 ? "" : discriminator.substring(0, discriminator.length() - 8));
    if (isExists)
      return makeDiscriminator(DiscriminatorType.EXISTS, discriminator);
    return new ElementDefinitionSlicingDiscriminatorComponent().setType(DiscriminatorType.VALUE).setPath(discriminator);
  }

  private static ElementDefinitionSlicingDiscriminatorComponent makeDiscriminator(DiscriminatorType dType, String str) {
    return new ElementDefinitionSlicingDiscriminatorComponent().setType(dType)
        .setPath(Utilities.noString(str) ? "$this" : str);
  }

  public static String buildR2Discriminator(ElementDefinitionSlicingDiscriminatorComponent t) throws FHIRException {
    switch (t.getType()) {
    case PROFILE:
      return t.getPath() + "/@profile";
    case TYPE:
      return t.getPath() + "/@type";
    case VALUE:
      return t.getPath();
    case PATTERN:
      return t.getPath();
    case EXISTS:
      return t.getPath(); // determination of value vs. exists is based on whether there's only 2 slices -
                          // one with minOccurs=1 and other with maxOccur=0
    default:
      throw new FHIRException("Unable to represent " + t.getType().toCode() + ":" + t.getPath() + " in R2");
    }
  }

  public static StructureDefinition makeExtensionForVersionedURL(IWorkerContext context, String url) {
    String epath = url.substring(54);
    if (!epath.contains("."))
      return null;
    String type = epath.substring(0, epath.indexOf("."));
    StructureDefinition sd = context.fetchTypeDefinition(type);
    if (sd == null)
      return null;
    ElementDefinition ed = null;
    for (ElementDefinition t : sd.getSnapshot().getElement()) {
      if (t.getPath().equals(epath)) {
        ed = t;
        break;
      }
    }
    if (ed == null)
      return null;
    if ("Element".equals(ed.typeSummary()) || "BackboneElement".equals(ed.typeSummary())) {
      return null;
    } else {
      StructureDefinition template = context.fetchResource(StructureDefinition.class,
          "http://fhir-registry.smarthealthit.org/StructureDefinition/capabilities");
      StructureDefinition ext = template.copy();
      ext.setUrl(url);
      ext.setId("extension-" + epath);
      ext.setName("Extension-" + epath);
      ext.setTitle("Extension for r4 " + epath);
      ext.setStatus(sd.getStatus());
      ext.setDate(sd.getDate());
      ext.getContact().clear();
      ext.getContact().addAll(sd.getContact());
      ext.setFhirVersion(sd.getFhirVersion());
      ext.setDescription(ed.getDefinition());
      ext.getContext().clear();
      ext.addContext().setType(ExtensionContextType.ELEMENT).setExpression(epath.substring(0, epath.lastIndexOf(".")));
      ext.getDifferential().getElement().clear();
      ext.getSnapshot().getElement().get(3).setFixed(new UriType(url));
      ext.getSnapshot().getElement().set(4, ed.copy());
      ext.getSnapshot().getElement().get(4).setPath("Extension.value" + Utilities.capitalize(ed.typeSummary()));
      return ext;
    }

  }

  public boolean isThrowException() {
    return exception;
  }

  public void setThrowException(boolean exception) {
    this.exception = exception;
  }

  public TerminologyServiceOptions getTerminologyServiceOptions() {
    return terminologyServiceOptions;
  }

  public void setTerminologyServiceOptions(TerminologyServiceOptions terminologyServiceOptions) {
    this.terminologyServiceOptions = terminologyServiceOptions;
  }

  public boolean isNewSlicingProcessing() {
    return newSlicingProcessing;
  }

  public void setNewSlicingProcessing(boolean newSlicingProcessing) {
    this.newSlicingProcessing = newSlicingProcessing;
  }


  public static boolean isExtensionDefinition(StructureDefinition sd) {
    return sd.getDerivation() == TypeDerivationRule.CONSTRAINT && sd.getType().equals("Extension");
  }

  public static boolean isSimpleExtension(StructureDefinition sd) {
    if (!isExtensionDefinition(sd)) {
      return false;
    }
    ElementDefinition value = sd.getSnapshot().getElementByPath("Extension.value");
    return value != null && !value.isProhibited();
  }

  public static boolean isComplexExtension(StructureDefinition sd) {
    if (!isExtensionDefinition(sd)) {
      return false;
    }
    ElementDefinition value = sd.getSnapshot().getElementByPath("Extension.value");
    return value == null || value.isProhibited();
  }

  public static boolean isModifierExtension(StructureDefinition sd) {
    ElementDefinition defn = sd.getSnapshot().getElementByPath("Extension");
    return defn.getIsModifier();
  }

}
