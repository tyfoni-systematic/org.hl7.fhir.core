package org.hl7.fhir.convertors.conv30_40.resources30_40;

import org.hl7.fhir.convertors.VersionConvertorConstants;
import org.hl7.fhir.convertors.context.ConversionContext30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.ContactDetail30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.Reference30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.complextypes30_40.CodeableConcept30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.complextypes30_40.Timing30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.Boolean30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.Code30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.DateTime30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.Integer30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.MarkDown30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.String30_40;
import org.hl7.fhir.convertors.conv30_40.datatypes30_40.primitivetypes30_40.Uri30_40;
import org.hl7.fhir.dstu3.model.Enumeration;
import org.hl7.fhir.dstu3.model.OperationDefinition;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Type;

public class OperationDefinition30_40 {

  public static org.hl7.fhir.r4.model.OperationDefinition convertOperationDefinition(org.hl7.fhir.dstu3.model.OperationDefinition src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.OperationDefinition tgt = new org.hl7.fhir.r4.model.OperationDefinition();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyDomainResource(src, tgt);
    if (src.hasUrl())
      tgt.setUrlElement(Uri30_40.convertUri(src.getUrlElement()));
    if (src.hasVersion())
      tgt.setVersionElement(String30_40.convertString(src.getVersionElement()));
    if (src.hasName())
      tgt.setNameElement(String30_40.convertString(src.getNameElement()));
    if (src.hasStatus())
      tgt.setStatusElement(Enumerations30_40.convertPublicationStatus(src.getStatusElement()));
    if (src.hasKind())
      tgt.setKindElement(convertOperationKind(src.getKindElement()));
    if (src.hasExperimental())
      tgt.setExperimentalElement(Boolean30_40.convertBoolean(src.getExperimentalElement()));
    if (src.hasDateElement())
      tgt.setDateElement(DateTime30_40.convertDateTime(src.getDateElement()));
    if (src.hasPublisher())
      tgt.setPublisherElement(String30_40.convertString(src.getPublisherElement()));
    for (org.hl7.fhir.dstu3.model.ContactDetail t : src.getContact())
      tgt.addContact(ContactDetail30_40.convertContactDetail(t));
    if (src.hasDescription())
      tgt.setDescriptionElement(MarkDown30_40.convertMarkdown(src.getDescriptionElement()));
    for (org.hl7.fhir.dstu3.model.UsageContext t : src.getUseContext())
      tgt.addUseContext(Timing30_40.convertUsageContext(t));
    for (org.hl7.fhir.dstu3.model.CodeableConcept t : src.getJurisdiction())
      tgt.addJurisdiction(CodeableConcept30_40.convertCodeableConcept(t));
    if (src.hasPurpose())
      tgt.setPurposeElement(MarkDown30_40.convertMarkdown(src.getPurposeElement()));
    if (src.hasIdempotent())
      tgt.setAffectsState(!src.getIdempotent());
    if (src.hasCode())
      tgt.setCodeElement(Code30_40.convertCode(src.getCodeElement()));
    if (src.hasComment())
      tgt.setComment(src.getComment());
    if (src.hasBase())
      tgt.setBaseElement(Reference30_40.convertReferenceToCanonical(src.getBase()));
    for (org.hl7.fhir.dstu3.model.CodeType t : src.getResource()) tgt.addResource(t.getValue());
    if (src.hasSystem())
      tgt.setSystemElement(Boolean30_40.convertBoolean(src.getSystemElement()));
    if (src.hasType())
      tgt.setTypeElement(Boolean30_40.convertBoolean(src.getTypeElement()));
    if (src.hasInstance())
      tgt.setInstanceElement(Boolean30_40.convertBoolean(src.getInstanceElement()));
    for (org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getParameter())
      tgt.addParameter(convertOperationDefinitionParameterComponent(t));
    for (org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionOverloadComponent t : src.getOverload())
      tgt.addOverload(convertOperationDefinitionOverloadComponent(t));
    return tgt;
  }

  public static org.hl7.fhir.dstu3.model.OperationDefinition convertOperationDefinition(org.hl7.fhir.r4.model.OperationDefinition src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition tgt = new org.hl7.fhir.dstu3.model.OperationDefinition();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyDomainResource(src, tgt);
    if (src.hasUrl())
      tgt.setUrlElement(Uri30_40.convertUri(src.getUrlElement()));
    if (src.hasVersion())
      tgt.setVersionElement(String30_40.convertString(src.getVersionElement()));
    if (src.hasName())
      tgt.setNameElement(String30_40.convertString(src.getNameElement()));
    if (src.hasStatus())
      tgt.setStatusElement(Enumerations30_40.convertPublicationStatus(src.getStatusElement()));
    if (src.hasKind())
      tgt.setKindElement(convertOperationKind(src.getKindElement()));
    if (src.hasExperimental())
      tgt.setExperimentalElement(Boolean30_40.convertBoolean(src.getExperimentalElement()));
    if (src.hasDateElement())
      tgt.setDateElement(DateTime30_40.convertDateTime(src.getDateElement()));
    if (src.hasPublisher())
      tgt.setPublisherElement(String30_40.convertString(src.getPublisherElement()));
    for (org.hl7.fhir.r4.model.ContactDetail t : src.getContact())
      tgt.addContact(ContactDetail30_40.convertContactDetail(t));
    if (src.hasDescription())
      tgt.setDescriptionElement(MarkDown30_40.convertMarkdown(src.getDescriptionElement()));
    for (org.hl7.fhir.r4.model.UsageContext t : src.getUseContext())
      tgt.addUseContext(Timing30_40.convertUsageContext(t));
    for (org.hl7.fhir.r4.model.CodeableConcept t : src.getJurisdiction())
      tgt.addJurisdiction(CodeableConcept30_40.convertCodeableConcept(t));
    if (src.hasPurpose())
      tgt.setPurposeElement(MarkDown30_40.convertMarkdown(src.getPurposeElement()));
    if (src.hasAffectsState())
      tgt.setIdempotent(!src.getAffectsState());
    if (src.hasCode())
      tgt.setCodeElement(Code30_40.convertCode(src.getCodeElement()));
    if (src.hasComment())
      tgt.setComment(src.getComment());
    if (src.hasBase())
      tgt.setBase(Reference30_40.convertCanonicalToReference(src.getBaseElement()));
    for (org.hl7.fhir.r4.model.CodeType t : src.getResource()) tgt.addResource(t.getValue());
    if (src.hasSystem())
      tgt.setSystemElement(Boolean30_40.convertBoolean(src.getSystemElement()));
    if (src.hasType())
      tgt.setTypeElement(Boolean30_40.convertBoolean(src.getTypeElement()));
    if (src.hasInstance())
      tgt.setInstanceElement(Boolean30_40.convertBoolean(src.getInstanceElement()));
    for (org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getParameter())
      tgt.addParameter(convertOperationDefinitionParameterComponent(t));
    for (org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionOverloadComponent t : src.getOverload())
      tgt.addOverload(convertOperationDefinitionOverloadComponent(t));
    return tgt;
  }

  public static org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionOverloadComponent convertOperationDefinitionOverloadComponent(org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionOverloadComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionOverloadComponent tgt = new org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionOverloadComponent();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyBackboneElement(src,tgt);
    for (org.hl7.fhir.r4.model.StringType t : src.getParameterName()) tgt.addParameterName(t.getValue());
    if (src.hasComment())
      tgt.setCommentElement(String30_40.convertString(src.getCommentElement()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionOverloadComponent convertOperationDefinitionOverloadComponent(org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionOverloadComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionOverloadComponent tgt = new org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionOverloadComponent();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyBackboneElement(src,tgt);
    for (org.hl7.fhir.dstu3.model.StringType t : src.getParameterName()) tgt.addParameterName(t.getValue());
    if (src.hasComment())
      tgt.setCommentElement(String30_40.convertString(src.getCommentElement()));
    return tgt;
  }

  public static org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent convertOperationDefinitionParameterBindingComponent(org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterBindingComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent tgt = new org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyBackboneElement(src,tgt);
    if (src.hasStrength())
      tgt.setStrengthElement(Enumerations30_40.convertBindingStrength(src.getStrengthElement()));
    if (src.hasValueSet()) {
      String vsr = VersionConvertorConstants.vsToRef(src.getValueSet());
      if (vsr != null)
        tgt.setValueSet(new org.hl7.fhir.dstu3.model.UriType(vsr));
      else
        tgt.setValueSet(new org.hl7.fhir.dstu3.model.Reference(src.getValueSet()));
    }
    return tgt;
  }

  public static org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterBindingComponent convertOperationDefinitionParameterBindingComponent(org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterBindingComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterBindingComponent tgt = new org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterBindingComponent();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyBackboneElement(src,tgt);
    if (src.hasStrength())
      tgt.setStrengthElement(Enumerations30_40.convertBindingStrength(src.getStrengthElement()));
    if (src.hasValueSet()) {
      Type t = ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().convertType(src.getValueSet());
      if (t instanceof org.hl7.fhir.r4.model.Reference)
        tgt.setValueSet(((org.hl7.fhir.r4.model.Reference) t).getReference());
      else
        tgt.setValueSet(t.primitiveValue());
      tgt.setValueSet(VersionConvertorConstants.refToVS(tgt.getValueSet()));
    }
    return tgt;
  }

  public static org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent convertOperationDefinitionParameterComponent(org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent tgt = new org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyBackboneElement(src,tgt);
    if (src.hasName())
      tgt.setNameElement(Code30_40.convertCode(src.getNameElement()));
    if (src.hasUse())
      tgt.setUseElement(convertOperationParameterUse(src.getUseElement()));
    if (src.hasMin())
      tgt.setMinElement(Integer30_40.convertInteger(src.getMinElement()));
    if (src.hasMax())
      tgt.setMaxElement(String30_40.convertString(src.getMaxElement()));
    if (src.hasDocumentation())
      tgt.setDocumentationElement(String30_40.convertString(src.getDocumentationElement()));
    if (src.hasType())
      tgt.setTypeElement(Code30_40.convertCode(src.getTypeElement()));
    if (src.hasSearchType())
      tgt.setSearchTypeElement(Enumerations30_40.convertSearchParamType(src.getSearchTypeElement()));
    for (org.hl7.fhir.r4.model.UriType t : src.getTargetProfile())
      tgt.setProfile(new org.hl7.fhir.dstu3.model.Reference(t.getValue()));
    if (src.hasBinding())
      tgt.setBinding(convertOperationDefinitionParameterBindingComponent(src.getBinding()));
    for (org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getPart())
      tgt.addPart(convertOperationDefinitionParameterComponent(t));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent convertOperationDefinitionParameterComponent(org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent tgt = new org.hl7.fhir.r4.model.OperationDefinition.OperationDefinitionParameterComponent();
    ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyBackboneElement(src,tgt);
    if (src.hasName())
      tgt.setNameElement(Code30_40.convertCode(src.getNameElement()));
    if (src.hasUse())
      tgt.setUseElement(convertOperationParameterUse(src.getUseElement()));
    if (src.hasMin())
      tgt.setMinElement(Integer30_40.convertInteger(src.getMinElement()));
    if (src.hasMax())
      tgt.setMaxElement(String30_40.convertString(src.getMaxElement()));
    if (src.hasDocumentation())
      tgt.setDocumentationElement(String30_40.convertString(src.getDocumentationElement()));
    if (src.hasType())
      tgt.setTypeElement(Code30_40.convertCode(src.getTypeElement()));
    if (src.hasSearchType())
      tgt.setSearchTypeElement(Enumerations30_40.convertSearchParamType(src.getSearchTypeElement()));
    if (src.hasProfile())
      tgt.addTargetProfile(src.getProfile().getReference());
    if (src.hasBinding())
      tgt.setBinding(convertOperationDefinitionParameterBindingComponent(src.getBinding()));
    for (org.hl7.fhir.dstu3.model.OperationDefinition.OperationDefinitionParameterComponent t : src.getPart())
      tgt.addPart(convertOperationDefinitionParameterComponent(t));
    return tgt;
  }

  static public org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind> convertOperationKind(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.OperationDefinition.OperationKind> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<OperationDefinition.OperationKind> tgt = new Enumeration<>(new OperationDefinition.OperationKindEnumFactory());
      ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case OPERATION:
                  tgt.setValue(OperationDefinition.OperationKind.OPERATION);
                  break;
              case QUERY:
                  tgt.setValue(OperationDefinition.OperationKind.QUERY);
                  break;
              default:
                  tgt.setValue(OperationDefinition.OperationKind.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.OperationDefinition.OperationKind> convertOperationKind(org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.OperationDefinition.OperationKind> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.OperationDefinition.OperationKind> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.OperationDefinition.OperationKindEnumFactory());
      ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case OPERATION:
                  tgt.setValue(org.hl7.fhir.r4.model.OperationDefinition.OperationKind.OPERATION);
                  break;
              case QUERY:
                  tgt.setValue(org.hl7.fhir.r4.model.OperationDefinition.OperationKind.QUERY);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.OperationDefinition.OperationKind.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse> convertOperationParameterUse(org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUseEnumFactory());
      ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case IN:
                  tgt.setValue(org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse.IN);
                  break;
              case OUT:
                  tgt.setValue(org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse.OUT);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.OperationDefinition.OperationParameterUse> convertOperationParameterUse(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.OperationDefinition.OperationParameterUse> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<OperationDefinition.OperationParameterUse> tgt = new Enumeration<>(new OperationDefinition.OperationParameterUseEnumFactory());
      ConversionContext30_40.INSTANCE.getVersionConvertor_30_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case IN:
                  tgt.setValue(OperationDefinition.OperationParameterUse.IN);
                  break;
              case OUT:
                  tgt.setValue(OperationDefinition.OperationParameterUse.OUT);
                  break;
              default:
                  tgt.setValue(OperationDefinition.OperationParameterUse.NULL);
                  break;
          }
      }
      return tgt;
  }
}