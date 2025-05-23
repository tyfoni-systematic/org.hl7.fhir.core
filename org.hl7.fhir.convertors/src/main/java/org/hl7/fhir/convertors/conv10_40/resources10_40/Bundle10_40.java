package org.hl7.fhir.convertors.conv10_40.resources10_40;

import org.hl7.fhir.convertors.advisors.impl.BaseAdvisor_10_40;
import org.hl7.fhir.convertors.context.ConversionContext10_40;
import org.hl7.fhir.convertors.conv10_40.datatypes10_40.complextypes10_40.Signature10_40;
import org.hl7.fhir.convertors.conv10_40.datatypes10_40.primitivetypes10_40.Decimal10_40;
import org.hl7.fhir.convertors.conv10_40.datatypes10_40.primitivetypes10_40.Instant10_40;
import org.hl7.fhir.convertors.conv10_40.datatypes10_40.primitivetypes10_40.String10_40;
import org.hl7.fhir.convertors.conv10_40.datatypes10_40.primitivetypes10_40.UnsignedInt10_40;
import org.hl7.fhir.convertors.conv10_40.datatypes10_40.primitivetypes10_40.Uri10_40;
import org.hl7.fhir.dstu2.model.Bundle;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.FhirPublication;

public class Bundle10_40 {

  public static org.hl7.fhir.r4.model.Bundle convertBundle(org.hl7.fhir.dstu2.model.Bundle src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.r4.model.Bundle tgt = new org.hl7.fhir.r4.model.Bundle();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyResource(src, tgt);
    if (src.hasType())
      tgt.setTypeElement(convertBundleType(src.getTypeElement()));
    if (src.hasTotal())
      tgt.setTotalElement(UnsignedInt10_40.convertUnsignedInt(src.getTotalElement()));
    for (org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent t : src.getLink())
      tgt.addLink(convertBundleLinkComponent(t));
    for (org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent t : src.getEntry())
      tgt.addEntry(convertBundleEntryComponent(t));
    if (src.hasSignature())
      tgt.setSignature(Signature10_40.convertSignature(src.getSignature()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle convertBundle(org.hl7.fhir.r4.model.Bundle src, BaseAdvisor_10_40 advisor) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu2.model.Bundle tgt = new org.hl7.fhir.dstu2.model.Bundle();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyResource(src, tgt);
    if (src.hasType())
      tgt.setTypeElement(convertBundleType(src.getTypeElement()));
    if (src.hasTotal())
      tgt.setTotalElement(UnsignedInt10_40.convertUnsignedInt(src.getTotalElement()));
    for (org.hl7.fhir.r4.model.Bundle.BundleLinkComponent t : src.getLink()) tgt.addLink(convertBundleLinkComponent(t));
    for (org.hl7.fhir.r4.model.Bundle.BundleEntryComponent t : src.getEntry())
      tgt.addEntry(convertBundleEntryComponent(t, advisor));
    if (src.hasSignature())
      tgt.setSignature(Signature10_40.convertSignature(src.getSignature()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle convertBundle(org.hl7.fhir.r4.model.Bundle src) throws FHIRException {
    return convertBundle(src, null);
  }

  public static org.hl7.fhir.r4.model.Bundle.BundleEntryComponent convertBundleEntryComponent(org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.r4.model.Bundle.BundleEntryComponent tgt = new org.hl7.fhir.r4.model.Bundle.BundleEntryComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    for (org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent t : src.getLink())
      tgt.addLink(convertBundleLinkComponent(t));
    if (src.hasFullUrlElement())
      tgt.setFullUrlElement(Uri10_40.convertUri(src.getFullUrlElement()));
    if (src.hasResource())
      tgt.setResource(ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().convertResource(src.getResource()));
    if (src.hasSearch())
      tgt.setSearch(convertBundleEntrySearchComponent(src.getSearch()));
    if (src.hasRequest())
      tgt.setRequest(convertBundleEntryRequestComponent(src.getRequest()));
    if (src.hasResponse())
      tgt.setResponse(convertBundleEntryResponseComponent(src.getResponse()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent convertBundleEntryComponent(org.hl7.fhir.r4.model.Bundle.BundleEntryComponent src, BaseAdvisor_10_40 advisor) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    if (advisor.ignoreEntry(src, FhirPublication.DSTU2))
      return null;
    org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent tgt = new org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    for (org.hl7.fhir.r4.model.Bundle.BundleLinkComponent t : src.getLink()) tgt.addLink(convertBundleLinkComponent(t));
    if (src.hasFullUrlElement())
      tgt.setFullUrlElement(Uri10_40.convertUri(src.getFullUrlElement()));
    if (src.hasResource())
      tgt.setResource(ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().convertResource(src.getResource()));
    if (src.hasSearch())
      tgt.setSearch(convertBundleEntrySearchComponent(src.getSearch()));
    if (src.hasRequest())
      tgt.setRequest(convertBundleEntryRequestComponent(src.getRequest()));
    if (src.hasResponse())
      tgt.setResponse(convertBundleEntryResponseComponent(src.getResponse()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent convertBundleEntryComponent(org.hl7.fhir.r4.model.Bundle.BundleEntryComponent src) throws FHIRException {
    return convertBundleEntryComponent(src, null);
  }

  public static org.hl7.fhir.dstu2.model.Bundle.BundleEntryRequestComponent convertBundleEntryRequestComponent(org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu2.model.Bundle.BundleEntryRequestComponent tgt = new org.hl7.fhir.dstu2.model.Bundle.BundleEntryRequestComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasMethod())
      tgt.setMethodElement(convertHTTPVerb(src.getMethodElement()));
    if (src.hasUrlElement())
      tgt.setUrlElement(Uri10_40.convertUri(src.getUrlElement()));
    if (src.hasIfNoneMatchElement())
      tgt.setIfNoneMatchElement(String10_40.convertString(src.getIfNoneMatchElement()));
    if (src.hasIfModifiedSinceElement())
      tgt.setIfModifiedSinceElement(Instant10_40.convertInstant(src.getIfModifiedSinceElement()));
    if (src.hasIfMatchElement())
      tgt.setIfMatchElement(String10_40.convertString(src.getIfMatchElement()));
    if (src.hasIfNoneExistElement())
      tgt.setIfNoneExistElement(String10_40.convertString(src.getIfNoneExistElement()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent convertBundleEntryRequestComponent(org.hl7.fhir.dstu2.model.Bundle.BundleEntryRequestComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent tgt = new org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasMethod())
      tgt.setMethodElement(convertHTTPVerb(src.getMethodElement()));
    if (src.hasUrlElement())
      tgt.setUrlElement(Uri10_40.convertUri(src.getUrlElement()));
    if (src.hasIfNoneMatchElement())
      tgt.setIfNoneMatchElement(String10_40.convertString(src.getIfNoneMatchElement()));
    if (src.hasIfModifiedSinceElement())
      tgt.setIfModifiedSinceElement(Instant10_40.convertInstant(src.getIfModifiedSinceElement()));
    if (src.hasIfMatchElement())
      tgt.setIfMatchElement(String10_40.convertString(src.getIfMatchElement()));
    if (src.hasIfNoneExistElement())
      tgt.setIfNoneExistElement(String10_40.convertString(src.getIfNoneExistElement()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle.BundleEntryResponseComponent convertBundleEntryResponseComponent(org.hl7.fhir.r4.model.Bundle.BundleEntryResponseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu2.model.Bundle.BundleEntryResponseComponent tgt = new org.hl7.fhir.dstu2.model.Bundle.BundleEntryResponseComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasStatusElement())
      tgt.setStatusElement(String10_40.convertString(src.getStatusElement()));
    if (src.hasLocationElement())
      tgt.setLocationElement(Uri10_40.convertUri(src.getLocationElement()));
    if (src.hasEtagElement())
      tgt.setEtagElement(String10_40.convertString(src.getEtagElement()));
    if (src.hasLastModifiedElement())
      tgt.setLastModifiedElement(Instant10_40.convertInstant(src.getLastModifiedElement()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Bundle.BundleEntryResponseComponent convertBundleEntryResponseComponent(org.hl7.fhir.dstu2.model.Bundle.BundleEntryResponseComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.r4.model.Bundle.BundleEntryResponseComponent tgt = new org.hl7.fhir.r4.model.Bundle.BundleEntryResponseComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasStatusElement())
      tgt.setStatusElement(String10_40.convertString(src.getStatusElement()));
    if (src.hasLocationElement())
      tgt.setLocationElement(Uri10_40.convertUri(src.getLocationElement()));
    if (src.hasEtagElement())
      tgt.setEtagElement(String10_40.convertString(src.getEtagElement()));
    if (src.hasLastModifiedElement())
      tgt.setLastModifiedElement(Instant10_40.convertInstant(src.getLastModifiedElement()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle.BundleEntrySearchComponent convertBundleEntrySearchComponent(org.hl7.fhir.r4.model.Bundle.BundleEntrySearchComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu2.model.Bundle.BundleEntrySearchComponent tgt = new org.hl7.fhir.dstu2.model.Bundle.BundleEntrySearchComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasMode())
      tgt.setModeElement(convertSearchEntryMode(src.getModeElement()));
    if (src.hasScoreElement())
      tgt.setScoreElement(Decimal10_40.convertDecimal(src.getScoreElement()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Bundle.BundleEntrySearchComponent convertBundleEntrySearchComponent(org.hl7.fhir.dstu2.model.Bundle.BundleEntrySearchComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.r4.model.Bundle.BundleEntrySearchComponent tgt = new org.hl7.fhir.r4.model.Bundle.BundleEntrySearchComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasMode())
      tgt.setModeElement(convertSearchEntryMode(src.getModeElement()));
    if (src.hasScoreElement())
      tgt.setScoreElement(Decimal10_40.convertDecimal(src.getScoreElement()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Bundle.BundleLinkComponent convertBundleLinkComponent(org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.r4.model.Bundle.BundleLinkComponent tgt = new org.hl7.fhir.r4.model.Bundle.BundleLinkComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasRelationElement())
      tgt.setRelationElement(String10_40.convertString(src.getRelationElement()));
    if (src.hasUrlElement())
      tgt.setUrlElement(Uri10_40.convertUri(src.getUrlElement()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent convertBundleLinkComponent(org.hl7.fhir.r4.model.Bundle.BundleLinkComponent src) throws FHIRException {
    if (src == null || src.isEmpty())
      return null;
    org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent tgt = new org.hl7.fhir.dstu2.model.Bundle.BundleLinkComponent();
    ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyBackboneElement(src,tgt);
    if (src.hasRelationElement())
      tgt.setRelationElement(String10_40.convertString(src.getRelationElement()));
    if (src.hasUrlElement())
      tgt.setUrlElement(Uri10_40.convertUri(src.getUrlElement()));
    return tgt;
  }

  static public org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.Bundle.BundleType> convertBundleType(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.BundleType> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<Bundle.BundleType> tgt = new Enumeration<>(new Bundle.BundleTypeEnumFactory());
      ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case DOCUMENT:
                  tgt.setValue(Bundle.BundleType.DOCUMENT);
                  break;
              case MESSAGE:
                  tgt.setValue(Bundle.BundleType.MESSAGE);
                  break;
              case TRANSACTION:
                  tgt.setValue(Bundle.BundleType.TRANSACTION);
                  break;
              case TRANSACTIONRESPONSE:
                  tgt.setValue(Bundle.BundleType.TRANSACTIONRESPONSE);
                  break;
              case BATCH:
                  tgt.setValue(Bundle.BundleType.BATCH);
                  break;
              case BATCHRESPONSE:
                  tgt.setValue(Bundle.BundleType.BATCHRESPONSE);
                  break;
              case HISTORY:
                  tgt.setValue(Bundle.BundleType.HISTORY);
                  break;
              case SEARCHSET:
                  tgt.setValue(Bundle.BundleType.SEARCHSET);
                  break;
              case COLLECTION:
                  tgt.setValue(Bundle.BundleType.COLLECTION);
                  break;
              default:
                  tgt.setValue(Bundle.BundleType.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.BundleType> convertBundleType(org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.Bundle.BundleType> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.BundleType> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.Bundle.BundleTypeEnumFactory());
      ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case DOCUMENT:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.DOCUMENT);
                  break;
              case MESSAGE:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.MESSAGE);
                  break;
              case TRANSACTION:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTION);
                  break;
              case TRANSACTIONRESPONSE:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.TRANSACTIONRESPONSE);
                  break;
              case BATCH:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.BATCH);
                  break;
              case BATCHRESPONSE:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.BATCHRESPONSE);
                  break;
              case HISTORY:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.HISTORY);
                  break;
              case SEARCHSET:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.SEARCHSET);
                  break;
              case COLLECTION:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.COLLECTION);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.BundleType.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.Bundle.HTTPVerb> convertHTTPVerb(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.HTTPVerb> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<Bundle.HTTPVerb> tgt = new Enumeration<>(new Bundle.HTTPVerbEnumFactory());
      ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case GET:
                  tgt.setValue(Bundle.HTTPVerb.GET);
                  break;
              case POST:
                  tgt.setValue(Bundle.HTTPVerb.POST);
                  break;
              case PUT:
                  tgt.setValue(Bundle.HTTPVerb.PUT);
                  break;
              case DELETE:
                  tgt.setValue(Bundle.HTTPVerb.DELETE);
                  break;
              default:
                  tgt.setValue(Bundle.HTTPVerb.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.HTTPVerb> convertHTTPVerb(org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.Bundle.HTTPVerb> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.HTTPVerb> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.Bundle.HTTPVerbEnumFactory());
      ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case GET:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.HTTPVerb.GET);
                  break;
              case POST:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.HTTPVerb.POST);
                  break;
              case PUT:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.HTTPVerb.PUT);
                  break;
              case DELETE:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.HTTPVerb.DELETE);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.HTTPVerb.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.Bundle.SearchEntryMode> convertSearchEntryMode(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.SearchEntryMode> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<Bundle.SearchEntryMode> tgt = new Enumeration<>(new Bundle.SearchEntryModeEnumFactory());
      ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case MATCH:
                  tgt.setValue(Bundle.SearchEntryMode.MATCH);
                  break;
              case INCLUDE:
                  tgt.setValue(Bundle.SearchEntryMode.INCLUDE);
                  break;
              case OUTCOME:
                  tgt.setValue(Bundle.SearchEntryMode.OUTCOME);
                  break;
              default:
                  tgt.setValue(Bundle.SearchEntryMode.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.SearchEntryMode> convertSearchEntryMode(org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.Bundle.SearchEntryMode> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Bundle.SearchEntryMode> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.Bundle.SearchEntryModeEnumFactory());
      ConversionContext10_40.INSTANCE.getVersionConvertor_10_40().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case MATCH:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.SearchEntryMode.MATCH);
                  break;
              case INCLUDE:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.SearchEntryMode.INCLUDE);
                  break;
              case OUTCOME:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.SearchEntryMode.OUTCOME);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.Bundle.SearchEntryMode.NULL);
                  break;
          }
      }
      return tgt;
  }
}