package org.hl7.fhir.convertors.conv10_30.resources10_30;

import org.hl7.fhir.convertors.context.ConversionContext10_30;
import org.hl7.fhir.convertors.conv10_30.datatypes10_30.primitivetypes10_30.Integer10_30;
import org.hl7.fhir.dstu3.model.ProcessRequest;
import org.hl7.fhir.exceptions.FHIRException;

public class ProcessRequest10_30 {
  public static org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent convertItemsComponent(org.hl7.fhir.dstu2.model.ProcessRequest.ItemsComponent src) throws FHIRException {
    if (src == null || src.isEmpty()) return null;
    org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent tgt = new org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent();
    ConversionContext10_30.INSTANCE.getVersionConvertor_10_30().copyBackboneElement(src,tgt);
    if (src.hasSequenceLinkIdElement())
      tgt.setSequenceLinkIdElement(Integer10_30.convertInteger(src.getSequenceLinkIdElement()));
    return tgt;
  }

  public static org.hl7.fhir.dstu2.model.ProcessRequest.ItemsComponent convertItemsComponent(org.hl7.fhir.dstu3.model.ProcessRequest.ItemsComponent src) throws FHIRException {
    if (src == null || src.isEmpty()) return null;
    org.hl7.fhir.dstu2.model.ProcessRequest.ItemsComponent tgt = new org.hl7.fhir.dstu2.model.ProcessRequest.ItemsComponent();
    ConversionContext10_30.INSTANCE.getVersionConvertor_10_30().copyBackboneElement(src,tgt);
    if (src.hasSequenceLinkIdElement())
      tgt.setSequenceLinkIdElement(Integer10_30.convertInteger(src.getSequenceLinkIdElement()));
    return tgt;
  }

  static public org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.ProcessRequest.ActionList> convertActionList(org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.ProcessRequest.ActionList> src) throws FHIRException {
    if (src == null || src.isEmpty()) return null;
    org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.ProcessRequest.ActionList> tgt = new org.hl7.fhir.dstu3.model.Enumeration<>(new org.hl7.fhir.dstu3.model.ProcessRequest.ActionListEnumFactory());
    ConversionContext10_30.INSTANCE.getVersionConvertor_10_30().copyElement(src, tgt);
    if (src.getValue() == null) {
    tgt.setValue(null);
} else {
      switch(src.getValue()) {
        case CANCEL:
                    tgt.setValue(ProcessRequest.ActionList.CANCEL);
                    break;
                case POLL:
                    tgt.setValue(ProcessRequest.ActionList.POLL);
                    break;
                case REPROCESS:
                    tgt.setValue(ProcessRequest.ActionList.REPROCESS);
                    break;
                case STATUS:
                    tgt.setValue(ProcessRequest.ActionList.STATUS);
                    break;
                default:
                    tgt.setValue(ProcessRequest.ActionList.NULL);
                    break;
       }
}
    return tgt;
  }

  static public org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.ProcessRequest.ActionList> convertActionList(org.hl7.fhir.dstu3.model.Enumeration<org.hl7.fhir.dstu3.model.ProcessRequest.ActionList> src) throws FHIRException {
    if (src == null || src.isEmpty()) return null;
    org.hl7.fhir.dstu2.model.Enumeration<org.hl7.fhir.dstu2.model.ProcessRequest.ActionList> tgt = new org.hl7.fhir.dstu2.model.Enumeration<>(new org.hl7.fhir.dstu2.model.ProcessRequest.ActionListEnumFactory());
    ConversionContext10_30.INSTANCE.getVersionConvertor_10_30().copyElement(src, tgt);
    if (src.getValue() == null) {
    tgt.setValue(null);
} else {
      switch(src.getValue()) {
        case CANCEL:
                    tgt.setValue(org.hl7.fhir.dstu2.model.ProcessRequest.ActionList.CANCEL);
                    break;
                case POLL:
                    tgt.setValue(org.hl7.fhir.dstu2.model.ProcessRequest.ActionList.POLL);
                    break;
                case REPROCESS:
                    tgt.setValue(org.hl7.fhir.dstu2.model.ProcessRequest.ActionList.REPROCESS);
                    break;
                case STATUS:
                    tgt.setValue(org.hl7.fhir.dstu2.model.ProcessRequest.ActionList.STATUS);
                    break;
                default:
                    tgt.setValue(org.hl7.fhir.dstu2.model.ProcessRequest.ActionList.NULL);
                    break;
       }
}
    return tgt;
  }
}
