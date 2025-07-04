package org.hl7.fhir.r4b.comparison;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.r4b.comparison.ResourceComparer.MessageCounts;
import org.hl7.fhir.r4b.comparison.ValueSetComparer.ValueSetComparison;
import org.hl7.fhir.r4b.conformance.ProfileUtilities;
import org.hl7.fhir.r4b.conformance.ProfileUtilities.UnusedTracker;
import org.hl7.fhir.r4b.context.IWorkerContext;
import org.hl7.fhir.r4b.formats.IParser;
import org.hl7.fhir.r4b.model.Base;
import org.hl7.fhir.r4b.model.Coding;
import org.hl7.fhir.r4b.model.DataType;
import org.hl7.fhir.r4b.model.ElementDefinition;
import org.hl7.fhir.r4b.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.r4b.model.ElementDefinition.ElementDefinitionConstraintComponent;
import org.hl7.fhir.r4b.model.ElementDefinition.ElementDefinitionMappingComponent;
import org.hl7.fhir.r4b.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4b.model.Enumerations.BindingStrength;
import org.hl7.fhir.r4b.model.IntegerType;
import org.hl7.fhir.r4b.model.PrimitiveType;
import org.hl7.fhir.r4b.model.StringType;
import org.hl7.fhir.r4b.model.StructureDefinition;
import org.hl7.fhir.r4b.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4b.model.ValueSet;
import org.hl7.fhir.r4b.utils.DefinitionNavigator;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.MarkedToMoveToAdjunctPackage;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.i18n.RenderingI18nContext;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.validation.ValidationMessage.IssueSeverity;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Cell;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.Row;
import org.hl7.fhir.utilities.xhtml.HierarchicalTableGenerator.TableModel;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

@MarkedToMoveToAdjunctPackage
@Slf4j
public class ProfileComparer extends CanonicalResourceComparer {

  public class ProfileComparison extends CanonicalResourceComparison<StructureDefinition> {

    private StructuralMatch<ElementDefinitionNode> combined;

    public ProfileComparison(StructureDefinition left, StructureDefinition right) {
      super(left, right);
      combined = new StructuralMatch<ElementDefinitionNode>(); // base
    }

    public StructuralMatch<ElementDefinitionNode> getCombined() {
      return combined;
    }

    @Override
    protected String abbreviation() {
      return "sd";
    }

    @Override
    protected String summary() {
      return "Profile: " + left.present() + " vs " + right.present();
    }

    @Override
    protected String fhirType() {
      return "StructureDefinition";
    }

    @Override
    protected void countMessages(MessageCounts cnts) {
      super.countMessages(cnts);
      combined.countMessages(cnts);
    }

  }

  private class ElementDefinitionNode {
    private ElementDefinition def;
    private StructureDefinition src;

    private ElementDefinitionNode(StructureDefinition src, ElementDefinition def) {
      super();
      this.src = src;
      this.def = def;
    }

    public ElementDefinition getDef() {
      return def;
    }

    public StructureDefinition getSrc() {
      return src;
    }
  }

  private ProfileUtilities utilsLeft;
  private ProfileUtilities utilsRight;

  public ProfileComparer(ComparisonSession session, ProfileUtilities utilsLeft, ProfileUtilities utilsRight) {
    super(session);
    this.utilsLeft = utilsLeft;
    this.utilsRight = utilsRight;
  }

  @Override
  protected String fhirType() {
    return "StructureDefinition";
  }

  public ProfileComparison compare(StructureDefinition left, StructureDefinition right)
      throws DefinitionException, FHIRFormatError, IOException {
    check(left, "left");
    check(right, "right");

    ProfileComparison res = new ProfileComparison(left, right);
    session.identify(res);
    StructureDefinition sd = new StructureDefinition();
    res.setUnion(sd);
    session.identify(sd);
    sd.setName("Union" + left.getName() + "And" + right.getName());
    sd.setTitle("Union of " + left.getTitle() + " And " + right.getTitle());
    sd.setStatus(left.getStatus());
    sd.setDate(new Date());

    StructureDefinition sd1 = new StructureDefinition();
    res.setIntersection(sd1);
    session.identify(sd1);
    sd1.setName("Intersection" + left.getName() + "And" + right.getName());
    sd1.setTitle("Intersection of " + left.getTitle() + " And " + right.getTitle());
    sd1.setStatus(left.getStatus());
    sd1.setDate(new Date());

    compareMetadata(left, right, res.getMetadata(), res);
    comparePrimitives("fhirVersion", left.getFhirVersionElement(), right.getFhirVersionElement(), res.getMetadata(),
        IssueSeverity.WARNING, res);
    comparePrimitives("kind", left.getKindElement(), right.getKindElement(), res.getMetadata(), IssueSeverity.WARNING,
        res);
    comparePrimitives("abstract", left.getAbstractElement(), right.getAbstractElement(), res.getMetadata(),
        IssueSeverity.WARNING, res);
    comparePrimitives("type", left.getTypeElement(), right.getTypeElement(), res.getMetadata(), IssueSeverity.ERROR,
        res);
    comparePrimitives("baseDefinition", left.getBaseDefinitionElement(), right.getBaseDefinitionElement(),
        res.getMetadata(), IssueSeverity.ERROR, res);

    if (left.getType().equals(right.getType())) {
      DefinitionNavigator ln = new DefinitionNavigator(session.getContextLeft(), left);
      DefinitionNavigator rn = new DefinitionNavigator(session.getContextRight(), right);
      StructuralMatch<ElementDefinitionNode> sm = new StructuralMatch<ElementDefinitionNode>(
          new ElementDefinitionNode(left, ln.current()), new ElementDefinitionNode(right, rn.current()));
      compareElements(res, sm, ln.path(), null, ln, rn);
      res.combined = sm;
    }
    return res;
  }

  private void check(StructureDefinition sd, String name) {
    if (sd == null)
      throw new DefinitionException("No StructureDefinition provided (" + name + ": " + sd.getName() + ")");
//    if (sd.getType().equals("Extension")) {
//      throw new DefinitionException("StructureDefinition is for an extension - use ExtensionComparer instead ("+name+": "+sd.getName()+")");
//    }
    if (sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
      throw new DefinitionException(
          "StructureDefinition is not for an profile - can't be compared (" + name + ": " + sd.getName() + ")");
    }
    if (sd.getSnapshot().getElement().isEmpty())
      throw new DefinitionException("StructureDefinition snapshot is empty (" + name + ": " + sd.getName() + ")");
  }

  private void compareElements(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, String path,
      String sliceName, DefinitionNavigator left, DefinitionNavigator right)
      throws DefinitionException, FHIRFormatError, IOException {
    assert (path != null);
    assert (left != null);
    assert (right != null);
    assert (left.path().equals(right.path()));


      log.debug("Compare elements at " + path);


    // not allowed to be different:
//    ruleEqual(comp, res, left.current().getDefaultValue(), right.current().getDefaultValue(), "defaultValue", path);
//    ruleEqual(comp, res, left.current().getMeaningWhenMissingElement(), right.current().getMeaningWhenMissingElement(), "meaningWhenMissing", path);
//    ruleEqual(comp, res, left.current().getIsModifierElement(), right.current().getIsModifierElement(), "isModifier", path); - this check belongs in the core
//    ruleEqual(comp, res, left.current().getIsSummaryElement(), right.current().getIsSummaryElement(), "isSummary", path); - so does this

    // we ignore slicing right now - we're going to clone the root one anyway, and
    // then think about clones
    // simple stuff
    ElementDefinition subset = new ElementDefinition();
    subset.setPath(left.path());
    if (sliceName != null)
      subset.setSliceName(sliceName);

    subset.getRepresentation().addAll(left.current().getRepresentation()); // can't be bothered even testing this one
    subset.setDefaultValue(left.current().getDefaultValue());
    subset.setMeaningWhenMissing(left.current().getMeaningWhenMissing());
    subset.setIsModifier(left.current().getIsModifier());
    subset.setIsSummary(left.current().getIsSummary());

    // descriptive properties from ElementDefinition - merge them:
    subset.setLabel(mergeText(comp, res, path, "label", left.current().getLabel(), right.current().getLabel(), false));
    subset.setShort(mergeText(comp, res, path, "short", left.current().getShort(), right.current().getShort(), false));
    subset.setDefinition(mergeText(comp, res, path, "definition", left.current().getDefinition(),
        right.current().getDefinition(), false));
    subset.setComment(
        mergeText(comp, res, path, "comments", left.current().getComment(), right.current().getComment(), false));
    subset.setRequirements(mergeText(comp, res, path, "requirements", left.current().getRequirements(),
        right.current().getRequirements(), false));
    subset.getCode().addAll(mergeCodings(left.current().getCode(), right.current().getCode()));
    subset.getAlias().addAll(mergeStrings(left.current().getAlias(), right.current().getAlias()));
    subset.getMapping().addAll(mergeMappings(left.current().getMapping(), right.current().getMapping()));
    // left will win for example
    subset.setExample(left.current().hasExample() ? left.current().getExample() : right.current().getExample());

    if (left.current().getMustSupport() != right.current().getMustSupport()) {
      vm(IssueSeverity.WARNING, "Elements differ in definition for mustSupport: '" + left.current().getMustSupport()
          + "' vs '" + right.current().getMustSupport() + "'", path, comp.getMessages(), res.getMessages());

    }
    subset.setMustSupport(left.current().getMustSupport() || right.current().getMustSupport());
    ElementDefinition superset = subset.copy();

    // compare and intersect
    int leftMin = left.current().getMin();
    int rightMin = right.current().getMin();
    int leftMax = "*".equals(left.current().getMax()) ? Integer.MAX_VALUE : Integer.parseInt(left.current().getMax());
    int rightMax = "*".equals(right.current().getMax()) ? Integer.MAX_VALUE
        : Integer.parseInt(right.current().getMax());

    checkMinMax(comp, res, path, leftMin, rightMin, leftMax, rightMax);
    superset.setMin(unionMin(leftMin, rightMin));
    superset.setMax(unionMax(leftMax, rightMax, left.current().getMax(), right.current().getMax()));
    subset.setMin(intersectMin(leftMin, rightMin));
    subset.setMax(intersectMax(leftMax, rightMax, left.current().getMax(), right.current().getMax()));

    superset.getType().addAll(unionTypes(comp, res, path, left.current().getType(), right.current().getType()));
    subset.getType()
        .addAll(intersectTypes(comp, res, subset, path, left.current().getType(), right.current().getType()));
    rule(comp, res, !subset.getType().isEmpty() || (!left.current().hasType() && !right.current().hasType()), path,
        "Type Mismatch: " + typeCode(left) + " vs " + typeCode(right));
    // <fixed[x]><!-- ?? 0..1 * Value must be exactly this --></fixed[x]>
    // <pattern[x]><!-- ?? 0..1 * Value must have at least these property values
    // --></pattern[x]>
    superset.setMaxLengthElement(unionMaxLength(left.current().getMaxLength(), right.current().getMaxLength()));
    subset.setMaxLengthElement(intersectMaxLength(left.current().getMaxLength(), right.current().getMaxLength()));
    if (left.current().hasBinding() || right.current().hasBinding()) {
      compareBindings(comp, res, subset, superset, path, left.current(), right.current());
    }
    // note these are backwards
    superset.getConstraint()
        .addAll(intersectConstraints(path, left.current().getConstraint(), right.current().getConstraint()));
    subset.getConstraint()
        .addAll(unionConstraints(comp, res, path, left.current().getConstraint(), right.current().getConstraint()));
    comp.getIntersection().getSnapshot().getElement().add(subset);
    comp.getUnion().getSnapshot().getElement().add(superset);

    // add the children
    compareChildren(comp, res, path, left, right);
//
//    // now process the slices
//    if (left.current().hasSlicing() || right.current().hasSlicing()) {
//      assert sliceName == null;
//      if (isExtension(left.path()))
//        return compareExtensions(outcome, path, superset, subset, left, right);
//      //      return true;
//      else {
//        ElementDefinitionSlicingComponent slicingL = left.current().getSlicing();
//        ElementDefinitionSlicingComponent slicingR = right.current().getSlicing();
//        // well, this is tricky. If one is sliced, and the other is not, then in general, the union just ignores the slices, and the intersection is the slices.
//        if (left.current().hasSlicing() && !right.current().hasSlicing()) { 
//          // the super set is done. Any restrictions in the slices are irrelevant to what the super set says, except that we're going sum up the value sets if we can (for documentation purposes) (todo)
//          // the minimum set is the slicing specified in the slicer
//          subset.setSlicing(slicingL);
//          // stick everything from the right to do with the slices to the subset 
//          copySlices(outcome.subset.getSnapshot().getElement(), left.getStructure().getSnapshot().getElement(), left.slices());
//        } else if (!left.current().hasSlicing() && right.current().hasSlicing()) { 
//          // the super set is done. Any restrictions in the slices are irrelevant to what the super set says, except that we're going sum up the value sets if we can (for documentation purposes) (todo)
//          // the minimum set is the slicing specified in the slicer
//          subset.setSlicing(slicingR);
//          // stick everything from the right to do with the slices to the subset 
//          copySlices(outcome.subset.getSnapshot().getElement(), right.getStructure().getSnapshot().getElement(), right.slices());
//        } else if (isTypeSlicing(slicingL) || isTypeSlicing(slicingR)) {
//          superset.getSlicing().setRules(SlicingRules.OPEN).setOrdered(false).addDiscriminator().setType(DiscriminatorType.TYPE).setPath("$this");
//          subset.getSlicing().setRules(slicingL.getRules() == SlicingRules.CLOSED || slicingR.getRules() == SlicingRules.CLOSED ? SlicingRules.OPEN : SlicingRules.CLOSED).setOrdered(false).addDiscriminator().setType(DiscriminatorType.TYPE).setPath("$this");
//
//          // the superset is the union of the types 
//          // the subset is the intersection of them 
//          List<DefinitionNavigator> handled = new ArrayList<>();
//          for (DefinitionNavigator t : left.slices()) {
//            DefinitionNavigator r = findMatchingSlice(right.slices(), t);
//            if (r == null) {
//              copySlice(outcome.superset.getSnapshot().getElement(), left.getStructure().getSnapshot().getElement(), t);              
//            } else {
//              handled.add(r);
//              ret = compareElements(outcome, path+":"+t.current().getSliceName(), t, r, t.current().getSliceName()) && ret;
//            }
//          }
//          for (DefinitionNavigator t : right.slices()) {
//            if (!handled.contains(t)) {
//              copySlice(outcome.superset.getSnapshot().getElement(), right.getStructure().getSnapshot().getElement(), t);
//            }
//          }
//        } else if (slicingMatches(slicingL, slicingR)) {
//          // if it's the same, we can try matching the slices - though we might have to give up without getting matches correct
//          // there amy be implied consistency we can't reason about 
//          throw new DefinitionException("Slicing matches but is not handled yet at "+left.current().getId()+": ("+ProfileUtilities.summarizeSlicing(slicingL)+")");
//        } else  {
//          // if the slicing is different, we can't compare them - or can we?
//          throw new DefinitionException("Slicing doesn't match at "+left.current().getId()+": ("+ProfileUtilities.summarizeSlicing(slicingL)+" / "+ProfileUtilities.summarizeSlicing(slicingR)+")");
//        }
//      }
//      // todo: name 
//    }
//    return ret;
//
//    // TODO Auto-generated method stub
//    return null;
  }

  private void compareChildren(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, String path,
      DefinitionNavigator left, DefinitionNavigator right) throws DefinitionException, IOException, FHIRFormatError {
    List<DefinitionNavigator> lc = left.children();
    List<DefinitionNavigator> rc = right.children();
    // it's possible that one of these profiles walks into a data type and the other
    // doesn't
    // if it does, we have to load the children for that data into the profile that
    // doesn't
    // walk into it
    if (lc.isEmpty() && !rc.isEmpty() && right.current().getType().size() == 1
        && left.hasTypeChildren(right.current().getType().get(0)))
      lc = left.childrenFromType(right.current().getType().get(0));
    if (rc.isEmpty() && !lc.isEmpty() && left.current().getType().size() == 1
        && right.hasTypeChildren(left.current().getType().get(0)))
      rc = right.childrenFromType(left.current().getType().get(0));

    List<DefinitionNavigator> matchR = new ArrayList<>();
    for (DefinitionNavigator l : lc) {
      DefinitionNavigator r = findInList(rc, l);
      if (r == null) {
        comp.getUnion().getSnapshot().getElement().add(l.current().copy());
        res.getChildren()
            .add(new StructuralMatch<ElementDefinitionNode>(new ElementDefinitionNode(l.getStructure(), l.current()),
                vmI(IssueSeverity.INFORMATION, "Removed this element", path)));
      } else {
        matchR.add(r);
        StructuralMatch<ElementDefinitionNode> sm = new StructuralMatch<ElementDefinitionNode>(
            new ElementDefinitionNode(l.getStructure(), l.current()),
            new ElementDefinitionNode(r.getStructure(), r.current()));
        res.getChildren().add(sm);
        compareElements(comp, sm, l.path(), null, l, r);
      }
    }
    for (DefinitionNavigator r : rc) {
      if (!matchR.contains(r)) {
        comp.getUnion().getSnapshot().getElement().add(r.current().copy());
        res.getChildren()
            .add(new StructuralMatch<ElementDefinitionNode>(vmI(IssueSeverity.INFORMATION, "Added this element", path),
                new ElementDefinitionNode(r.getStructure(), r.current())));
      }
    }
  }

  private DefinitionNavigator findInList(List<DefinitionNavigator> rc, DefinitionNavigator l) {
    for (DefinitionNavigator t : rc) {
      if (tail(t.current().getPath()).equals(tail(l.current().getPath()))) {
        return t;
      }
    }
    return null;
  }

  private void ruleEqual(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, DataType vLeft,
      DataType vRight, String name, String path) throws IOException {
    if (vLeft == null && vRight == null) {
      // nothing
    } else if (vLeft == null) {
      vm(IssueSeverity.ERROR, "Added " + name, path, comp.getMessages(), res.getMessages());
    } else if (vRight == null) {
      vm(IssueSeverity.ERROR, "Removed " + name, path, comp.getMessages(), res.getMessages());
    } else if (!Base.compareDeep(vLeft, vRight, false)) {
      vm(IssueSeverity.ERROR,
          name + " must be the same (" + toString(vLeft, true) + "/" + toString(vRight, false) + ")", path,
          comp.getMessages(), res.getMessages());
    }
  }

  private String toString(DataType val, boolean left) throws IOException {
    if (val instanceof PrimitiveType)
      return "'" + ((PrimitiveType) val).getValueAsString() + "'";

    IParser jp = left ? session.getContextLeft().newJsonParser() : session.getContextRight().newJsonParser();
    return jp.composeString(val, "value");
  }

  private String stripLinks(String s) {
    while (s.contains("](")) {
      int i = s.indexOf("](");
      int j = s.substring(i).indexOf(")");
      if (j == -1)
        return s;
      else
        s = s.substring(0, i + 1) + s.substring(i + j + 1);
    }
    return s;
  }

  private boolean rule(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, boolean test, String path,
      String message) {
    if (!test) {
      vm(IssueSeverity.ERROR, message, path, comp.getMessages(), res.getMessages());
    }
    return test;
  }

  private String mergeText(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, String path, String name,
      String left, String right, boolean isError) {
    if (left == null && right == null)
      return null;
    if (left == null)
      return right;
    if (right == null)
      return left;
    left = stripLinks(left);
    right = stripLinks(right);
    if (left.equalsIgnoreCase(right))
      return left;
    if (path != null) {
      vm(isError ? IssueSeverity.ERROR : IssueSeverity.WARNING,
          "Elements differ in " + name + ": '" + left + "' vs '" + right + "'", path, comp.getMessages(),
          res.getMessages());
    }
    return "left: " + left + "; right: " + right;
  }

  private List<Coding> mergeCodings(List<Coding> left, List<Coding> right) {
    List<Coding> result = new ArrayList<Coding>();
    result.addAll(left);
    for (Coding c : right) {
      boolean found = false;
      for (Coding ct : left)
        if (Utilities.equals(c.getSystem(), ct.getSystem()) && Utilities.equals(c.getCode(), ct.getCode()))
          found = true;
      if (!found)
        result.add(c);
    }
    return result;
  }

  private List<StringType> mergeStrings(List<StringType> left, List<StringType> right) {
    List<StringType> result = new ArrayList<StringType>();
    result.addAll(left);
    for (StringType c : right) {
      boolean found = false;
      for (StringType ct : left)
        if (Utilities.equals(c.getValue(), ct.getValue()))
          found = true;
      if (!found)
        result.add(c);
    }
    return result;
  }

  private List<ElementDefinitionMappingComponent> mergeMappings(List<ElementDefinitionMappingComponent> left,
      List<ElementDefinitionMappingComponent> right) {
    List<ElementDefinitionMappingComponent> result = new ArrayList<ElementDefinitionMappingComponent>();
    result.addAll(left);
    for (ElementDefinitionMappingComponent c : right) {
      boolean found = false;
      for (ElementDefinitionMappingComponent ct : left)
        if (Utilities.equals(c.getIdentity(), ct.getIdentity()) && Utilities.equals(c.getLanguage(), ct.getLanguage())
            && Utilities.equals(c.getMap(), ct.getMap()))
          found = true;
      if (!found)
        result.add(c);
    }
    return result;
  }

  private int intersectMin(int left, int right) {
    if (left > right)
      return left;
    else
      return right;
  }

  private void checkMinMax(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, String path, int leftMin,
      int rightMin, int leftMax, int rightMax) {
    if (leftMin != rightMin) {
      if (leftMin == 0) {
        vm(IssueSeverity.INFORMATION, "Element minimum cardinalities differ:  '" + leftMin + "' vs '" + rightMin + "'",
            path, comp.getMessages(), res.getMessages());
      } else if (rightMin == 0) {
        vm(IssueSeverity.INFORMATION, "Element minimum cardinalities differ:  '" + leftMin + "' vs '" + rightMin + "'",
            path, comp.getMessages(), res.getMessages());
      } else {
        vm(IssueSeverity.INFORMATION, "Element minimum cardinalities differ:  '" + leftMin + "' vs '" + rightMin + "'",
            path, comp.getMessages(), res.getMessages());
      }
    }
    if (leftMax != rightMax) {
      if (leftMax == Integer.MAX_VALUE) {
        vm(IssueSeverity.INFORMATION, "Element maximum cardinalities differ:  '" + leftMax + "' vs '" + rightMax + "'",
            path, comp.getMessages(), res.getMessages());
      } else if (rightMax == Integer.MAX_VALUE) {
        vm(IssueSeverity.INFORMATION, "Element maximum cardinalities differ:  '" + leftMax + "' vs '" + rightMax + "'",
            path, comp.getMessages(), res.getMessages());
      } else {
        vm(IssueSeverity.INFORMATION, "Element maximum cardinalities differ:  '" + leftMax + "' vs '" + rightMax + "'",
            path, comp.getMessages(), res.getMessages());
      }
    }
//    rule(comp, res, subset.getMax().equals("*") || Integer.parseInt(subset.getMax()) >= subset.getMin(), path, "Cardinality Mismatch: "+card(left)+"/"+card(right));

    // cross comparison - if max > min in either direction, there can be no
    // instances that are valid against both
    if (leftMax < rightMin) {
      vm(IssueSeverity.ERROR,
          "Element minimum cardinalities conflict:  '" + leftMin + ".." + leftMax + "' vs '" + rightMin + ".."
              + rightMax + "': No instances can be valid against both profiles",
          path, comp.getMessages(), res.getMessages());
    }
    if (rightMax < leftMin) {
      vm(IssueSeverity.ERROR,
          "Element minimum cardinalities conflict:  '" + leftMin + ".." + leftMax + "' vs '" + rightMin + ".."
              + rightMax + "': No instances can be valid against both profiles",
          path, comp.getMessages(), res.getMessages());
    }
  }

  private int unionMin(int left, int right) {
    if (left > right)
      return right;
    else
      return left;
  }

  private String intersectMax(int l, int r, String left, String right) {
    if (l < r)
      return left;
    else
      return right;
  }

  private String unionMax(int l, int r, String left, String right) {
    if (l < r)
      return right;
    else
      return left;
  }

  private IntegerType intersectMaxLength(int left, int right) {
    if (left == 0)
      left = Integer.MAX_VALUE;
    if (right == 0)
      right = Integer.MAX_VALUE;
    if (left < right)
      return left == Integer.MAX_VALUE ? null : new IntegerType(left);
    else
      return right == Integer.MAX_VALUE ? null : new IntegerType(right);
  }

  private IntegerType unionMaxLength(int left, int right) {
    if (left == 0)
      left = Integer.MAX_VALUE;
    if (right == 0)
      right = Integer.MAX_VALUE;
    if (left < right)
      return right == Integer.MAX_VALUE ? null : new IntegerType(right);
    else
      return left == Integer.MAX_VALUE ? null : new IntegerType(left);
  }

  private String card(DefinitionNavigator defn) {
    return Integer.toString(defn.current().getMin()) + ".." + defn.current().getMax();
  }

  private Collection<? extends TypeRefComponent> unionTypes(ProfileComparison comp,
      StructuralMatch<ElementDefinitionNode> res, String path, List<TypeRefComponent> left,
      List<TypeRefComponent> right) throws DefinitionException, IOException, FHIRFormatError {
    List<TypeRefComponent> result = new ArrayList<TypeRefComponent>();
    for (TypeRefComponent l : left)
      checkAddTypeUnion(comp, res, path, result, l, session.getContextLeft());
    for (TypeRefComponent r : right)
      checkAddTypeUnion(comp, res, path, result, r, session.getContextRight());
    return result;
  }

  private void checkAddTypeUnion(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res, String path,
      List<TypeRefComponent> results, TypeRefComponent nw, IWorkerContext ctxt)
      throws DefinitionException, IOException, FHIRFormatError {
    boolean pfound = false;
    boolean tfound = false;
    nw = nw.copy();
    if (nw.hasAggregation())
      throw new DefinitionException("Aggregation not supported: " + path);
    for (TypeRefComponent ex : results) {
      if (Utilities.equals(ex.getWorkingCode(), nw.getWorkingCode())) {
        if (!ex.hasProfile() && !nw.hasProfile())
          pfound = true;
        else if (!ex.hasProfile()) {
          pfound = true;
        } else if (!nw.hasProfile()) {
          pfound = true;
          ex.setProfile(null);
        } else {
          // both have profiles. Is one derived from the other?
          StructureDefinition sdex = ((IWorkerContext) ex.getUserData("ctxt")).fetchResource(StructureDefinition.class,
              ex.getProfile().get(0).getValue());
          StructureDefinition sdnw = ctxt.fetchResource(StructureDefinition.class, nw.getProfile().get(0).getValue());
          if (sdex != null && sdnw != null) {
            if (sdex.getUrl().equals(sdnw.getUrl())) {
              pfound = true;
            } else if (derivesFrom(sdex, sdnw, ((IWorkerContext) ex.getUserData("ctxt")))) {
              ex.setProfile(nw.getProfile());
              pfound = true;
            } else if (derivesFrom(sdnw, sdex, ctxt)) {
              pfound = true;
            } else if (sdnw.getSnapshot().getElement().get(0).getPath()
                .equals(sdex.getSnapshot().getElement().get(0).getPath())) {
              ProfileComparison compP = (ProfileComparison) session.compare(sdex, sdnw);
              if (compP != null && compP.getUnion() != null) { // might be null if circular
                pfound = true;
                ex.addProfile("#" + compP.getId());
              }
            }
          }
        }
        if (!ex.hasTargetProfile() && !nw.hasTargetProfile())
          tfound = true;
        else if (!ex.hasTargetProfile()) {
          tfound = true;
        } else if (!nw.hasTargetProfile()) {
          tfound = true;
          ex.setTargetProfile(null);
        } else {
          // both have profiles. Is one derived from the other?
          StructureDefinition sdex = ((IWorkerContext) ex.getUserData("ctxt")).fetchResource(StructureDefinition.class,
              ex.getTargetProfile().get(0).getValue());
          StructureDefinition sdnw = ctxt.fetchResource(StructureDefinition.class,
              nw.getTargetProfile().get(0).getValue());
          if (sdex != null && sdnw != null) {
            if (matches(sdex, sdnw)) {
              tfound = true;
            } else if (derivesFrom(sdex, sdnw, ((IWorkerContext) ex.getUserData("ctxt")))) {
              ex.setTargetProfile(nw.getTargetProfile());
              tfound = true;
            } else if (derivesFrom(sdnw, sdex, ctxt)) {
              tfound = true;
            } else if (sdnw.getSnapshot().getElement().get(0).getPath()
                .equals(sdex.getSnapshot().getElement().get(0).getPath())) {
              ProfileComparison compP = (ProfileComparison) session.compare(sdex, sdnw);
              if (compP.getUnion() != null) {
                tfound = true;
                ex.addTargetProfile("#" + compP.getId());
              }
            }
          }
        }
      }
    }
    if (!tfound || !pfound) {
      nw.setUserData("ctxt", ctxt);
      results.add(nw);
    }
  }

  private boolean matches(StructureDefinition s1, StructureDefinition s2) {
    if (!s1.getUrl().equals(s2.getUrl())) {
      return false;
    }
    if (s1.getDerivation() == TypeDerivationRule.SPECIALIZATION
        && s2.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
      return true; // arbitrary; we're just not interested in pursuing cross version differences
    }
    if (s1.hasVersion()) {
      return s1.getVersion().equals(s2.getVersion());
    } else {
      return !s2.hasVersion();
    }
  }

  private boolean derivesFrom(StructureDefinition left, StructureDefinition right, IWorkerContext ctxt) {
    StructureDefinition sd = left;
    while (sd != null) {
      if (right.getUrl().equals(sd.getBaseDefinition())) {
        return true;
      }
      sd = sd.hasBaseDefinition() ? ctxt.fetchResource(StructureDefinition.class, sd.getBaseDefinition()) : null;
    }
    return false;
  }

  private Collection<? extends TypeRefComponent> intersectTypes(ProfileComparison comp,
      StructuralMatch<ElementDefinitionNode> res, ElementDefinition ed, String path, List<TypeRefComponent> left,
      List<TypeRefComponent> right) throws DefinitionException, IOException, FHIRFormatError {
    List<TypeRefComponent> result = new ArrayList<TypeRefComponent>();
    for (TypeRefComponent l : left) {
      if (l.hasAggregation())
        throw new DefinitionException("Aggregation not supported: " + path);
      boolean pfound = false;
      boolean tfound = false;
      TypeRefComponent c = l.copy();
      for (TypeRefComponent r : right) {
        if (r.hasAggregation())
          throw new DefinitionException("Aggregation not supported: " + path);
        if (!l.hasProfile() && !r.hasProfile()) {
          pfound = true;
        } else if (!r.hasProfile()) {
          pfound = true;
        } else if (!l.hasProfile()) {
          pfound = true;
          c.setProfile(r.getProfile());
        } else {
          StructureDefinition sdl = resolveProfile(comp, res, path, l.getProfile().get(0).getValue(),
              comp.getLeft().getName(), session.getContextLeft());
          StructureDefinition sdr = resolveProfile(comp, res, path, r.getProfile().get(0).getValue(),
              comp.getRight().getName(), session.getContextRight());
          if (sdl != null && sdr != null) {
            if (sdl == sdr) {
              pfound = true;
            } else if (derivesFrom(sdl, sdr, session.getContextLeft())) {
              pfound = true;
            } else if (derivesFrom(sdr, sdl, session.getContextRight())) {
              c.setProfile(r.getProfile());
              pfound = true;
            } else if (sdl.getType().equals(sdr.getType())) {
              ProfileComparison compP = (ProfileComparison) session.compare(sdl, sdr);
              if (compP != null && compP.getIntersection() != null) {
                pfound = true;
                c.addProfile("#" + compP.getId());
              }
            }
          }
        }
        if (!l.hasTargetProfile() && !r.hasTargetProfile()) {
          tfound = true;
        } else if (!r.hasTargetProfile()) {
          tfound = true;
        } else if (!l.hasTargetProfile()) {
          tfound = true;
          c.setTargetProfile(r.getTargetProfile());
        } else {
          StructureDefinition sdl = resolveProfile(comp, res, path, l.getTargetProfile().get(0).getValue(),
              comp.getLeft().getName(), session.getContextLeft());
          StructureDefinition sdr = resolveProfile(comp, res, path, r.getTargetProfile().get(0).getValue(),
              comp.getRight().getName(), session.getContextRight());
          if (sdl != null && sdr != null) {
            if (matches(sdl, sdr)) {
              tfound = true;
            } else if (derivesFrom(sdl, sdr, session.getContextLeft())) {
              tfound = true;
            } else if (derivesFrom(sdr, sdl, session.getContextRight())) {
              c.setTargetProfile(r.getTargetProfile());
              tfound = true;
            } else if (sdl.getType().equals(sdr.getType())) {
              ProfileComparison compP = (ProfileComparison) session.compare(sdl, sdr);
              if (compP != null && compP.getIntersection() != null) {
                tfound = true;
                c.addTargetProfile("#" + compP.getId());
              }
            }
          }
        }
      }
      if (pfound && tfound)
        result.add(c);
    }
    return result;
  }

  private String typeCode(DefinitionNavigator defn) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (TypeRefComponent t : defn.current().getType())
      b.append(t.getWorkingCode() + (t.hasProfile() ? "(" + t.getProfile() + ")" : "")
          + (t.hasTargetProfile() ? "(" + t.getTargetProfile() + ")" : "")); // todo: other properties
    return b.toString();
  }

  private boolean compareBindings(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res,
      ElementDefinition subset, ElementDefinition superset, String path, ElementDefinition lDef, ElementDefinition rDef)
      throws FHIRFormatError, DefinitionException, IOException {
    assert (lDef.hasBinding() || rDef.hasBinding());
    if (!lDef.hasBinding()) {
      subset.setBinding(rDef.getBinding());
      // technically, the super set is unbound, but that's not very useful - so we use
      // the provided on as an example
      superset.setBinding(rDef.getBinding().copy());
      superset.getBinding().setStrength(BindingStrength.EXAMPLE);
      return true;
    }
    if (!rDef.hasBinding()) {
      subset.setBinding(lDef.getBinding());
      superset.setBinding(lDef.getBinding().copy());
      superset.getBinding().setStrength(BindingStrength.EXAMPLE);
      return true;
    }
    ElementDefinitionBindingComponent left = lDef.getBinding();
    ElementDefinitionBindingComponent right = rDef.getBinding();
    if (Base.compareDeep(left, right, false)) {
      subset.setBinding(left);
      superset.setBinding(right);
    }

    // if they're both examples/preferred then:
    // subset: left wins if they're both the same
    // superset:
    if (isPreferredOrExample(left) && isPreferredOrExample(right)) {
      if (right.getStrength() == BindingStrength.PREFERRED && left.getStrength() == BindingStrength.EXAMPLE
          && !Base.compareDeep(left.getValueSet(), right.getValueSet(), false)) {
        vm(IssueSeverity.INFORMATION,
            "Example/preferred bindings differ at " + path + " using binding from " + comp.getRight().getName(), path,
            comp.getMessages(), res.getMessages());
        subset.setBinding(right);
        superset.setBinding(unionBindings(comp, res, path, left, right));
      } else {
        if ((right.getStrength() != BindingStrength.EXAMPLE || left.getStrength() != BindingStrength.EXAMPLE)
            && !Base.compareDeep(left.getValueSet(), right.getValueSet(), false)) {
          vm(IssueSeverity.INFORMATION,
              "Example/preferred bindings differ at " + path + " using binding from " + comp.getLeft().getName(), path,
              comp.getMessages(), res.getMessages());
        }
        subset.setBinding(left);
        superset.setBinding(unionBindings(comp, res, path, left, right));
      }
      return true;
    }
    // if either of them are extensible/required, then it wins
    if (isPreferredOrExample(left)) {
      subset.setBinding(right);
      superset.setBinding(unionBindings(comp, res, path, left, right));
      return true;
    }
    if (isPreferredOrExample(right)) {
      subset.setBinding(left);
      superset.setBinding(unionBindings(comp, res, path, left, right));
      return true;
    }

    // ok, both are extensible or required.
    ElementDefinitionBindingComponent subBinding = new ElementDefinitionBindingComponent();
    subset.setBinding(subBinding);
    ElementDefinitionBindingComponent superBinding = new ElementDefinitionBindingComponent();
    superset.setBinding(superBinding);
    subBinding.setDescription(
        mergeText(comp, res, path, "description", left.getDescription(), right.getDescription(), false));
    superBinding.setDescription(
        mergeText(comp, res, path, "description", left.getDescription(), right.getDescription(), false));
    if (left.getStrength() == BindingStrength.REQUIRED || right.getStrength() == BindingStrength.REQUIRED)
      subBinding.setStrength(BindingStrength.REQUIRED);
    else
      subBinding.setStrength(BindingStrength.EXTENSIBLE);
    if (left.getStrength() == BindingStrength.EXTENSIBLE || right.getStrength() == BindingStrength.EXTENSIBLE)
      superBinding.setStrength(BindingStrength.EXTENSIBLE);
    else
      superBinding.setStrength(BindingStrength.REQUIRED);

    if (Base.compareDeep(left.getValueSet(), right.getValueSet(), false)) {
      subBinding.setValueSet(left.getValueSet());
      superBinding.setValueSet(left.getValueSet());
      return true;
    } else if (!left.hasValueSet()) {
      vm(IssueSeverity.ERROR, "No left Value set at " + path, path, comp.getMessages(), res.getMessages());
      return true;
    } else if (!right.hasValueSet()) {
      vm(IssueSeverity.ERROR, "No right Value set at " + path, path, comp.getMessages(), res.getMessages());
      return true;
    } else {
      // ok, now we compare the value sets. This may be unresolvable.
      ValueSet lvs = resolveVS(comp.getLeft(), left.getValueSet(), session.getContextLeft());
      ValueSet rvs = resolveVS(comp.getRight(), right.getValueSet(), session.getContextRight());
      if (lvs == null) {
        vm(IssueSeverity.ERROR, "Unable to resolve left value set " + left.getValueSet().toString() + " at " + path,
            path, comp.getMessages(), res.getMessages());
        return true;
      } else if (rvs == null) {
        vm(IssueSeverity.ERROR, "Unable to resolve right value set " + right.getValueSet().toString() + " at " + path,
            path, comp.getMessages(), res.getMessages());
        return true;
      } else if (sameValueSets(lvs, rvs)) {
        subBinding.setValueSet(lvs.getUrl());
        superBinding.setValueSet(lvs.getUrl());
      } else {
        ValueSetComparison compP = (ValueSetComparison) session.compare(lvs, rvs);
        if (compP != null) {
          subBinding.setValueSet(compP.getIntersection().getUrl());
          superBinding.setValueSet(compP.getUnion().getUrl());
        }
      }
    }
    return false;
  }

  private boolean sameValueSets(ValueSet lvs, ValueSet rvs) {
    if (!lvs.getUrl().equals(rvs.getUrl())) {
      return false;
    }
    if (isCore(lvs) && isCore(rvs)) {
      return true;
    }
    if (lvs.hasVersion()) {
      if (!lvs.getVersion().equals(rvs.getVersion())) {
        return false;
      } else if (!rvs.hasVersion()) {
        return false;
      }
    }
    return true;
  }

  private boolean isCore(ValueSet vs) {
    return vs.getUrl().startsWith("http://hl7.org/fhir/ValueSet");
  }

  private List<ElementDefinitionConstraintComponent> intersectConstraints(String path,
      List<ElementDefinitionConstraintComponent> left, List<ElementDefinitionConstraintComponent> right) {
    List<ElementDefinitionConstraintComponent> result = new ArrayList<ElementDefinitionConstraintComponent>();
    for (ElementDefinitionConstraintComponent l : left) {
      boolean found = false;
      for (ElementDefinitionConstraintComponent r : right)
        if (Utilities.equals(r.getId(), l.getId())
            || (Utilities.equals(r.getXpath(), l.getXpath()) && r.getSeverity() == l.getSeverity()))
          found = true;
      if (found)
        result.add(l);
    }
    return result;
  }

  // we can't really know about constraints. We create warnings, and collate them
  private List<ElementDefinitionConstraintComponent> unionConstraints(ProfileComparison comp,
      StructuralMatch<ElementDefinitionNode> res, String path, List<ElementDefinitionConstraintComponent> left,
      List<ElementDefinitionConstraintComponent> right) {
    List<ElementDefinitionConstraintComponent> result = new ArrayList<ElementDefinitionConstraintComponent>();
    for (ElementDefinitionConstraintComponent l : left) {
      boolean found = false;
      for (ElementDefinitionConstraintComponent r : right)
        if (Utilities.equals(r.getId(), l.getId())
            || (Utilities.equals(r.getXpath(), l.getXpath()) && r.getSeverity() == l.getSeverity()))
          found = true;
      if (!found) {
        if (!Utilities.existsInList(l.getExpression(), "hasValue() or (children().count() > id.count())",
            "extension.exists() != value.exists()")) {
          vm(IssueSeverity.INFORMATION,
              "StructureDefinition " + comp.getLeft().getName() + " has a constraint that is removed in "
                  + comp.getRight().getName() + " and it is uncertain whether they are compatible (" + l.getExpression()
                  + ")",
              path, comp.getMessages(), res.getMessages());
        }
      }
      result.add(l);
    }
    for (ElementDefinitionConstraintComponent r : right) {
      boolean found = false;
      for (ElementDefinitionConstraintComponent l : left)
        if (Utilities.equals(r.getId(), l.getId())
            || (Utilities.equals(r.getXpath(), l.getXpath()) && r.getSeverity() == l.getSeverity()))
          found = true;
      if (!found) {
        if (!Utilities.existsInList(r.getExpression(), "hasValue() or (children().count() > id.count())",
            "extension.exists() != value.exists()")) {
          vm(IssueSeverity.INFORMATION,
              "StructureDefinition " + comp.getRight().getName() + " has added constraint that is not found in "
                  + comp.getLeft().getName() + " and it is uncertain whether they are compatible (" + r.getExpression()
                  + ")",
              path, comp.getMessages(), res.getMessages());
        }
      }
    }
    return result;
  }

  private StructureDefinition resolveProfile(ProfileComparison comp, StructuralMatch<ElementDefinitionNode> res,
      String path, String url, String name, IWorkerContext ctxt) {
    StructureDefinition sd = ctxt.fetchResource(StructureDefinition.class, url);
    if (sd == null) {
      ValidationMessage vm = vmI(IssueSeverity.WARNING, "Unable to resolve profile " + url + " in profile " + name,
          path);
    }
    return sd;
  }

  private boolean isPreferredOrExample(ElementDefinitionBindingComponent binding) {
    return binding.getStrength() == BindingStrength.EXAMPLE || binding.getStrength() == BindingStrength.PREFERRED;
  }

  private ElementDefinitionBindingComponent unionBindings(ProfileComparison comp,
      StructuralMatch<ElementDefinitionNode> res, String path, ElementDefinitionBindingComponent left,
      ElementDefinitionBindingComponent right) throws FHIRFormatError, DefinitionException, IOException {
    ElementDefinitionBindingComponent union = new ElementDefinitionBindingComponent();
    if (left.getStrength().compareTo(right.getStrength()) < 0)
      union.setStrength(left.getStrength());
    else
      union.setStrength(right.getStrength());
    union.setDescription(
        mergeText(comp, res, path, "binding.description", left.getDescription(), right.getDescription(), false));
    if (Base.compareDeep(left.getValueSet(), right.getValueSet(), false))
      union.setValueSet(left.getValueSet());
    else {
      ValueSet lvs = resolveVS(comp.getLeft(), left.getValueSet(), session.getContextLeft());
      ValueSet rvs = resolveVS(comp.getRight(), right.getValueSet(), session.getContextRight());
      if (lvs != null && rvs != null) {
        ValueSetComparison compP = (ValueSetComparison) session.compare(lvs, rvs);
        if (compP != null) {
          union.setValueSet(compP.getUnion().getUrl());
        }
      } else if (lvs != null) {
        union.setValueSet(lvs.getUrl());
      } else if (rvs != null) {
        union.setValueSet(rvs.getUrl());
      }
    }
    return union;
  }

  private ValueSet resolveVS(StructureDefinition ctxtLeft, String vsRef, IWorkerContext ctxt) {
    if (vsRef == null)
      return null;
    return ctxt.fetchResource(ValueSet.class, vsRef);
  }

  public XhtmlNode renderStructure(ProfileComparison comp, String id, String prefix, String corePath)
      throws FHIRException, IOException {
    HierarchicalTableGenerator gen = new HierarchicalTableGenerator(new RenderingI18nContext(), Utilities.path("[tmp]", "compare"), false, true);
    TableModel model = gen.initComparisonTable(corePath, id);
    genElementComp(null /* oome back to this later */, gen, model.getRows(), comp.combined, corePath, prefix, null,
        true);
    return gen.generate(model, prefix, 0, null);
  }

  private void genElementComp(String defPath, HierarchicalTableGenerator gen, List<Row> rows,
      StructuralMatch<ElementDefinitionNode> combined, String corePath, String prefix, Row slicingRow, boolean root)
      throws IOException {
    Row originalRow = slicingRow;
    Row typesRow = null;

    List<StructuralMatch<ElementDefinitionNode>> children = combined.getChildren();

    Row row = gen.new Row();
    rows.add(row);
    String path = combined.either().getDef().getPath();
    row.setAnchor(path);
    row.setColor(utilsRight.getRowColor(combined.either().getDef(), false));
    if (eitherHasSlicing(combined))
      row.setLineColor(1);
    else if (eitherHasSliceName(combined))
      row.setLineColor(2);
    else
      row.setLineColor(0);
    boolean ext = false;
    if (tail(path).equals("extension")) {
      if (elementIsComplex(combined))
        row.setIcon("icon_extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);
      else
        row.setIcon("icon_extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
      ext = true;
    } else if (tail(path).equals("modifierExtension")) {
      if (elementIsComplex(combined))
        row.setIcon("icon_modifier_extension_complex.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_COMPLEX);
      else
        row.setIcon("icon_modifier_extension_simple.png", HierarchicalTableGenerator.TEXT_ICON_EXTENSION_SIMPLE);
    } else if (hasChoice(combined)) {
      if (allAreReference(combined))
        row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
      else {
        row.setIcon("icon_choice.gif", HierarchicalTableGenerator.TEXT_ICON_CHOICE);
        typesRow = row;
      }
    } else if (combined.either().getDef().hasContentReference())
      row.setIcon("icon_reuse.png", HierarchicalTableGenerator.TEXT_ICON_REUSE);
    else if (isPrimitive(combined))
      row.setIcon("icon_primitive.png", HierarchicalTableGenerator.TEXT_ICON_PRIMITIVE);
    else if (hasTarget(combined))
      row.setIcon("icon_reference.png", HierarchicalTableGenerator.TEXT_ICON_REFERENCE);
    else if (isDataType(combined))
      row.setIcon("icon_datatype.gif", HierarchicalTableGenerator.TEXT_ICON_DATATYPE);
    else
      row.setIcon("icon_resource.png", HierarchicalTableGenerator.TEXT_ICON_RESOURCE);
    String ref = defPath == null ? null : defPath + combined.either().getDef().getId();
    String sName = tail(path);
    String sn = getSliceName(combined);
    if (sn != null)
      sName = sName + ":" + sn;
    UnusedTracker used = new UnusedTracker();
    Cell nc;
    String leftColor = !combined.hasLeft() ? COLOR_NO_ROW_LEFT : combined.hasErrors() ? COLOR_DIFFERENT : null;
    String rightColor = !combined.hasRight() ? COLOR_NO_ROW_LEFT : combined.hasErrors() ? COLOR_DIFFERENT : null;
    if (combined.hasLeft()) {
      nc = utilsRight.genElementNameCell(gen, combined.getLeft().getDef(), "??", true, corePath, prefix, root, false,
          false, combined.getLeft().getSrc(), typesRow, row, false, ext, used, ref, sName, null);
    } else {
      nc = utilsRight.genElementNameCell(gen, combined.getRight().getDef(), "??", true, corePath, prefix, root, false,
          false, combined.getRight().getSrc(), typesRow, row, false, ext, used, ref, sName, null);
    }
    if (combined.hasLeft()) {
      frame(
          utilsRight.genElementCells(gen, combined.getLeft().getDef(), "??", true, corePath, prefix, root, false, false,
              combined.getLeft().getSrc(), typesRow, row, true, ext, used, ref, sName, nc, false, false, null),
          leftColor);
    } else {
      frame(spacers(row, 4, gen), leftColor);
    }
    if (combined.hasRight()) {
      frame(
          utilsRight.genElementCells(gen, combined.getRight().getDef(), "??", true, corePath, prefix, root, false,
              false, combined.getRight().getSrc(), typesRow, row, true, ext, used, ref, sName, nc, false, false, null),
          rightColor);
    } else {
      frame(spacers(row, 4, gen), rightColor);
    }
    row.getCells().add(cellForMessages(gen, combined.getMessages()));

    for (StructuralMatch<ElementDefinitionNode> child : children) {
      genElementComp(defPath, gen, row.getSubRows(), child, corePath, prefix, originalRow, false);
    }
  }

  private void frame(List<Cell> cells, String color) {
    for (Cell cell : cells) {
      if (color != null) {
        cell.setStyle("background-color: " + color);
      }
    }
    cells.get(0).setStyle("border-left: 1px grey solid" + (color == null ? "" : "; background-color: " + color));
    cells.get(cells.size() - 1)
        .setStyle("border-right: 1px grey solid" + (color == null ? "" : "; background-color: " + color));
  }

  private List<Cell> spacers(Row row, int count, HierarchicalTableGenerator gen) {
    List<Cell> res = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      Cell c = gen.new Cell();
      res.add(c);
      row.getCells().add(c);
    }
    return res;
  }

  private String getSliceName(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return null;
  }

  private boolean isDataType(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean hasTarget(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isPrimitive(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean allAreReference(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean hasChoice(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean elementIsComplex(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub velement.hasType() &&
    // element.getType().get(0).hasProfile() &&
    // extensionIsComplex(element.getType().get(0).getProfile().get(0).getValue()
    return false;
  }

  private boolean eitherHasSliceName(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean eitherHasSlicing(StructuralMatch<ElementDefinitionNode> combined) {
    // TODO Auto-generated method stub
    return false;
  }

  private String tail(String path) {
    if (path.contains("."))
      return path.substring(path.lastIndexOf('.') + 1);
    else
      return path;
  }

}