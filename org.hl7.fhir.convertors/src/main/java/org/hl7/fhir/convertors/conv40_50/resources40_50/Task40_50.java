package org.hl7.fhir.convertors.conv40_50.resources40_50;

import org.hl7.fhir.convertors.context.ConversionContext40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.general40_50.Annotation40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.general40_50.CodeableConcept40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.general40_50.Identifier40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.general40_50.Period40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.primitive40_50.Canonical40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.primitive40_50.DateTime40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.primitive40_50.PositiveInt40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.primitive40_50.String40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.primitive40_50.Uri40_50;
import org.hl7.fhir.convertors.conv40_50.datatypes40_50.special40_50.Reference40_50;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r5.model.CodeableReference;
import org.hl7.fhir.r5.model.Enumeration;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Task;

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
// Generated on Sun, Feb 24, 2019 11:37+1100 for FHIR v4.0.0
public class Task40_50 {

  public static org.hl7.fhir.r5.model.Task convertTask(org.hl7.fhir.r4.model.Task src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r5.model.Task tgt = new org.hl7.fhir.r5.model.Task();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyDomainResource(src, tgt);
    for (org.hl7.fhir.r4.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(Identifier40_50.convertIdentifier(t));
    if (src.hasInstantiatesCanonical())
      tgt.setInstantiatesCanonicalElement(Canonical40_50.convertCanonical(src.getInstantiatesCanonicalElement()));
    if (src.hasInstantiatesUri())
      tgt.setInstantiatesUriElement(Uri40_50.convertUri(src.getInstantiatesUriElement()));
    for (org.hl7.fhir.r4.model.Reference t : src.getBasedOn()) tgt.addBasedOn(Reference40_50.convertReference(t));
    if (src.hasGroupIdentifier())
      tgt.setGroupIdentifier(Identifier40_50.convertIdentifier(src.getGroupIdentifier()));
    for (org.hl7.fhir.r4.model.Reference t : src.getPartOf()) tgt.addPartOf(Reference40_50.convertReference(t));
    if (src.hasStatus())
      tgt.setStatusElement(convertTaskStatus(src.getStatusElement()));
    if (src.hasStatusReason())
      tgt.setStatusReason(CodeableConcept40_50.convertCodeableConceptToCodeableReference(src.getStatusReason()));
    if (src.hasBusinessStatus())
      tgt.setBusinessStatus(CodeableConcept40_50.convertCodeableConcept(src.getBusinessStatus()));
    if (src.hasIntent())
      tgt.setIntentElement(convertTaskIntent(src.getIntentElement()));
    if (src.hasPriority())
      tgt.setPriorityElement(convertTaskPriority(src.getPriorityElement()));
    if (src.hasCode())
      tgt.setCode(CodeableConcept40_50.convertCodeableConcept(src.getCode()));
    if (src.hasDescription())
      tgt.setDescriptionElement(String40_50.convertString(src.getDescriptionElement()));
    if (src.hasFocus())
      tgt.setFocus(Reference40_50.convertReference(src.getFocus()));
    if (src.hasFor())
      tgt.setFor(Reference40_50.convertReference(src.getFor()));
    if (src.hasEncounter())
      tgt.setEncounter(Reference40_50.convertReference(src.getEncounter()));
    if (src.hasExecutionPeriod())
      tgt.setExecutionPeriod(Period40_50.convertPeriod(src.getExecutionPeriod()));
    if (src.hasAuthoredOn())
      tgt.setAuthoredOnElement(DateTime40_50.convertDateTime(src.getAuthoredOnElement()));
    if (src.hasLastModified())
      tgt.setLastModifiedElement(DateTime40_50.convertDateTime(src.getLastModifiedElement()));
    if (src.hasRequester())
      tgt.setRequester(Reference40_50.convertReference(src.getRequester()));
    for (org.hl7.fhir.r4.model.CodeableConcept t : src.getPerformerType())
      tgt.addRequestedPerformer(CodeableConcept40_50.convertCodeableConceptToCodeableReference(t));
    if (src.hasOwner())
      tgt.setOwner(Reference40_50.convertReference(src.getOwner()));
    if (src.hasLocation())
      tgt.setLocation(Reference40_50.convertReference(src.getLocation()));
    if (src.hasReasonCode())
      tgt.addReason(CodeableConcept40_50.convertCodeableConceptToCodeableReference(src.getReasonCode()));
    if (src.hasReasonReference())
      tgt.addReason(Reference40_50.convertReferenceToCodeableReference(src.getReasonReference()));
    for (org.hl7.fhir.r4.model.Reference t : src.getInsurance()) tgt.addInsurance(Reference40_50.convertReference(t));
    for (org.hl7.fhir.r4.model.Annotation t : src.getNote()) tgt.addNote(Annotation40_50.convertAnnotation(t));
    for (org.hl7.fhir.r4.model.Reference t : src.getRelevantHistory())
      tgt.addRelevantHistory(Reference40_50.convertReference(t));
    if (src.hasRestriction())
      tgt.setRestriction(convertTaskRestrictionComponent(src.getRestriction()));
    for (org.hl7.fhir.r4.model.Task.ParameterComponent t : src.getInput()) tgt.addInput(convertParameterComponent(t));
    for (org.hl7.fhir.r4.model.Task.TaskOutputComponent t : src.getOutput())
      tgt.addOutput(convertTaskOutputComponent(t));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Task convertTask(org.hl7.fhir.r5.model.Task src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.Task tgt = new org.hl7.fhir.r4.model.Task();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyDomainResource(src, tgt);
    for (org.hl7.fhir.r5.model.Identifier t : src.getIdentifier())
      tgt.addIdentifier(Identifier40_50.convertIdentifier(t));
    if (src.hasInstantiatesCanonical())
      tgt.setInstantiatesCanonicalElement(Canonical40_50.convertCanonical(src.getInstantiatesCanonicalElement()));
    if (src.hasInstantiatesUri())
      tgt.setInstantiatesUriElement(Uri40_50.convertUri(src.getInstantiatesUriElement()));
    for (org.hl7.fhir.r5.model.Reference t : src.getBasedOn()) tgt.addBasedOn(Reference40_50.convertReference(t));
    if (src.hasGroupIdentifier())
      tgt.setGroupIdentifier(Identifier40_50.convertIdentifier(src.getGroupIdentifier()));
    for (org.hl7.fhir.r5.model.Reference t : src.getPartOf()) tgt.addPartOf(Reference40_50.convertReference(t));
    if (src.hasStatus())
      tgt.setStatusElement(convertTaskStatus(src.getStatusElement()));
    if (src.hasStatusReason())
      tgt.setStatusReason(CodeableConcept40_50.convertCodeableReferenceToCodeableConcept(src.getStatusReason()));
    if (src.hasBusinessStatus())
      tgt.setBusinessStatus(CodeableConcept40_50.convertCodeableConcept(src.getBusinessStatus()));
    if (src.hasIntent())
      tgt.setIntentElement(convertTaskIntent(src.getIntentElement()));
    if (src.hasPriority())
      tgt.setPriorityElement(convertTaskPriority(src.getPriorityElement()));
    if (src.hasCode())
      tgt.setCode(CodeableConcept40_50.convertCodeableConcept(src.getCode()));
    if (src.hasDescription())
      tgt.setDescriptionElement(String40_50.convertString(src.getDescriptionElement()));
    if (src.hasFocus())
      tgt.setFocus(Reference40_50.convertReference(src.getFocus()));
    if (src.hasFor())
      tgt.setFor(Reference40_50.convertReference(src.getFor()));
    if (src.hasEncounter())
      tgt.setEncounter(Reference40_50.convertReference(src.getEncounter()));
    if (src.hasExecutionPeriod())
      tgt.setExecutionPeriod(Period40_50.convertPeriod(src.getExecutionPeriod()));
    if (src.hasAuthoredOn())
      tgt.setAuthoredOnElement(DateTime40_50.convertDateTime(src.getAuthoredOnElement()));
    if (src.hasLastModified())
      tgt.setLastModifiedElement(DateTime40_50.convertDateTime(src.getLastModifiedElement()));
    if (src.hasRequester())
      tgt.setRequester(Reference40_50.convertReference(src.getRequester()));
    for (org.hl7.fhir.r5.model.CodeableReference t : src.getRequestedPerformer())
      tgt.addPerformerType(CodeableConcept40_50.convertCodeableReferenceToCodeableConcept(t));
    if (src.hasOwner())
      tgt.setOwner(Reference40_50.convertReference(src.getOwner()));
    if (src.hasLocation())
      tgt.setLocation(Reference40_50.convertReference(src.getLocation()));
    for (CodeableReference t : src.getReason()) {
      if (t.hasConcept())
        tgt.setReasonCode(CodeableConcept40_50.convertCodeableConcept(t.getConcept()));
      else if (t.hasReference())
        tgt.setReasonReference(Reference40_50.convertReference(t.getReference()));
    }
    for (org.hl7.fhir.r5.model.Reference t : src.getInsurance()) tgt.addInsurance(Reference40_50.convertReference(t));
    for (org.hl7.fhir.r5.model.Annotation t : src.getNote()) tgt.addNote(Annotation40_50.convertAnnotation(t));
    for (org.hl7.fhir.r5.model.Reference t : src.getRelevantHistory())
      tgt.addRelevantHistory(Reference40_50.convertReference(t));
    if (src.hasRestriction())
      tgt.setRestriction(convertTaskRestrictionComponent(src.getRestriction()));
    for (org.hl7.fhir.r5.model.Task.TaskInputComponent t : src.getInput()) tgt.addInput(convertParameterComponent(t));
    for (org.hl7.fhir.r5.model.Task.TaskOutputComponent t : src.getOutput())
      tgt.addOutput(convertTaskOutputComponent(t));
    return tgt;
  }

  static public org.hl7.fhir.r5.model.Enumeration<org.hl7.fhir.r5.model.Task.TaskStatus> convertTaskStatus(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskStatus> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<Task.TaskStatus> tgt = new Enumeration<>(new Task.TaskStatusEnumFactory());
      ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case DRAFT:
                  tgt.setValue(Task.TaskStatus.DRAFT);
                  break;
              case REQUESTED:
                  tgt.setValue(Task.TaskStatus.REQUESTED);
                  break;
              case RECEIVED:
                  tgt.setValue(Task.TaskStatus.RECEIVED);
                  break;
              case ACCEPTED:
                  tgt.setValue(Task.TaskStatus.ACCEPTED);
                  break;
              case REJECTED:
                  tgt.setValue(Task.TaskStatus.REJECTED);
                  break;
              case READY:
                  tgt.setValue(Task.TaskStatus.READY);
                  break;
              case CANCELLED:
                  tgt.setValue(Task.TaskStatus.CANCELLED);
                  break;
              case INPROGRESS:
                  tgt.setValue(Task.TaskStatus.INPROGRESS);
                  break;
              case ONHOLD:
                  tgt.setValue(Task.TaskStatus.ONHOLD);
                  break;
              case FAILED:
                  tgt.setValue(Task.TaskStatus.FAILED);
                  break;
              case COMPLETED:
                  tgt.setValue(Task.TaskStatus.COMPLETED);
                  break;
              case ENTEREDINERROR:
                  tgt.setValue(Task.TaskStatus.ENTEREDINERROR);
                  break;
              default:
                  tgt.setValue(Task.TaskStatus.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskStatus> convertTaskStatus(org.hl7.fhir.r5.model.Enumeration<org.hl7.fhir.r5.model.Task.TaskStatus> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskStatus> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.Task.TaskStatusEnumFactory());
      ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case DRAFT:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.DRAFT);
                  break;
              case REQUESTED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.REQUESTED);
                  break;
              case RECEIVED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.RECEIVED);
                  break;
              case ACCEPTED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.ACCEPTED);
                  break;
              case REJECTED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.REJECTED);
                  break;
              case READY:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.READY);
                  break;
              case CANCELLED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.CANCELLED);
                  break;
              case INPROGRESS:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.INPROGRESS);
                  break;
              case ONHOLD:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.ONHOLD);
                  break;
              case FAILED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.FAILED);
                  break;
              case COMPLETED:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.COMPLETED);
                  break;
              case ENTEREDINERROR:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.ENTEREDINERROR);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskStatus.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r5.model.Enumeration<org.hl7.fhir.r5.model.Task.TaskIntent> convertTaskIntent(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskIntent> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<Task.TaskIntent> tgt = new Enumeration<>(new Task.TaskIntentEnumFactory());
      ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case UNKNOWN:
                  tgt.setValue(Task.TaskIntent.UNKNOWN);
                  break;
              case PROPOSAL:
                  tgt.setValue(Task.TaskIntent.PROPOSAL);
                  break;
              case PLAN:
                  tgt.setValue(Task.TaskIntent.PLAN);
                  break;
              case ORDER:
                  tgt.setValue(Task.TaskIntent.ORDER);
                  break;
              case ORIGINALORDER:
                  tgt.setValue(Task.TaskIntent.ORIGINALORDER);
                  break;
              case REFLEXORDER:
                  tgt.setValue(Task.TaskIntent.REFLEXORDER);
                  break;
              case FILLERORDER:
                  tgt.setValue(Task.TaskIntent.FILLERORDER);
                  break;
              case INSTANCEORDER:
                  tgt.setValue(Task.TaskIntent.INSTANCEORDER);
                  break;
              case OPTION:
                  tgt.setValue(Task.TaskIntent.OPTION);
                  break;
              default:
                  tgt.setValue(Task.TaskIntent.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskIntent> convertTaskIntent(org.hl7.fhir.r5.model.Enumeration<org.hl7.fhir.r5.model.Task.TaskIntent> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskIntent> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.Task.TaskIntentEnumFactory());
      ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case UNKNOWN:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.UNKNOWN);
                  break;
              case PROPOSAL:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.PROPOSAL);
                  break;
              case PLAN:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.PLAN);
                  break;
              case ORDER:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.ORDER);
                  break;
              case ORIGINALORDER:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.ORIGINALORDER);
                  break;
              case REFLEXORDER:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.REFLEXORDER);
                  break;
              case FILLERORDER:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.FILLERORDER);
                  break;
              case INSTANCEORDER:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.INSTANCEORDER);
                  break;
              case OPTION:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.OPTION);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskIntent.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r5.model.Enumeration<org.hl7.fhir.r5.model.Enumerations.RequestPriority> convertTaskPriority(org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskPriority> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      Enumeration<Enumerations.RequestPriority> tgt = new Enumeration<>(new Enumerations.RequestPriorityEnumFactory());
      ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case ROUTINE:
                  tgt.setValue(Enumerations.RequestPriority.ROUTINE);
                  break;
              case URGENT:
                  tgt.setValue(Enumerations.RequestPriority.URGENT);
                  break;
              case ASAP:
                  tgt.setValue(Enumerations.RequestPriority.ASAP);
                  break;
              case STAT:
                  tgt.setValue(Enumerations.RequestPriority.STAT);
                  break;
              default:
                  tgt.setValue(Enumerations.RequestPriority.NULL);
                  break;
          }
      }
      return tgt;
  }

  static public org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskPriority> convertTaskPriority(org.hl7.fhir.r5.model.Enumeration<org.hl7.fhir.r5.model.Enumerations.RequestPriority> src) throws FHIRException {
      if (src == null || src.isEmpty())
          return null;
      org.hl7.fhir.r4.model.Enumeration<org.hl7.fhir.r4.model.Task.TaskPriority> tgt = new org.hl7.fhir.r4.model.Enumeration<>(new org.hl7.fhir.r4.model.Task.TaskPriorityEnumFactory());
      ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyElement(src, tgt);
      if (src.getValue() == null) {
          tgt.setValue(null);
      } else {
          switch (src.getValue()) {
              case ROUTINE:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskPriority.ROUTINE);
                  break;
              case URGENT:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskPriority.URGENT);
                  break;
              case ASAP:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskPriority.ASAP);
                  break;
              case STAT:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskPriority.STAT);
                  break;
              default:
                  tgt.setValue(org.hl7.fhir.r4.model.Task.TaskPriority.NULL);
                  break;
          }
      }
      return tgt;
  }

  public static org.hl7.fhir.r5.model.Task.TaskRestrictionComponent convertTaskRestrictionComponent(org.hl7.fhir.r4.model.Task.TaskRestrictionComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r5.model.Task.TaskRestrictionComponent tgt = new org.hl7.fhir.r5.model.Task.TaskRestrictionComponent();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyBackboneElement(src, tgt);
    if (src.hasRepetitions())
      tgt.setRepetitionsElement(PositiveInt40_50.convertPositiveInt(src.getRepetitionsElement()));
    if (src.hasPeriod())
      tgt.setPeriod(Period40_50.convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.r4.model.Reference t : src.getRecipient()) tgt.addRecipient(Reference40_50.convertReference(t));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Task.TaskRestrictionComponent convertTaskRestrictionComponent(org.hl7.fhir.r5.model.Task.TaskRestrictionComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.Task.TaskRestrictionComponent tgt = new org.hl7.fhir.r4.model.Task.TaskRestrictionComponent();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyBackboneElement(src, tgt);
    if (src.hasRepetitions())
      tgt.setRepetitionsElement(PositiveInt40_50.convertPositiveInt(src.getRepetitionsElement()));
    if (src.hasPeriod())
      tgt.setPeriod(Period40_50.convertPeriod(src.getPeriod()));
    for (org.hl7.fhir.r5.model.Reference t : src.getRecipient()) tgt.addRecipient(Reference40_50.convertReference(t));
    return tgt;
  }

  public static org.hl7.fhir.r5.model.Task.TaskInputComponent convertParameterComponent(org.hl7.fhir.r4.model.Task.ParameterComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r5.model.Task.TaskInputComponent tgt = new org.hl7.fhir.r5.model.Task.TaskInputComponent();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyBackboneElement(src, tgt);
    if (src.hasType())
      tgt.setType(CodeableConcept40_50.convertCodeableConcept(src.getType()));
    if (src.hasValue())
      tgt.setValue(ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().convertType(src.getValue()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Task.ParameterComponent convertParameterComponent(org.hl7.fhir.r5.model.Task.TaskInputComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.Task.ParameterComponent tgt = new org.hl7.fhir.r4.model.Task.ParameterComponent();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyBackboneElement(src, tgt);
    if (src.hasType())
      tgt.setType(CodeableConcept40_50.convertCodeableConcept(src.getType()));
    if (src.hasValue())
      tgt.setValue(ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().convertType(src.getValue()));
    return tgt;
  }

  public static org.hl7.fhir.r5.model.Task.TaskOutputComponent convertTaskOutputComponent(org.hl7.fhir.r4.model.Task.TaskOutputComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r5.model.Task.TaskOutputComponent tgt = new org.hl7.fhir.r5.model.Task.TaskOutputComponent();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyBackboneElement(src, tgt);
    if (src.hasType())
      tgt.setType(CodeableConcept40_50.convertCodeableConcept(src.getType()));
    if (src.hasValue())
      tgt.setValue(ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().convertType(src.getValue()));
    return tgt;
  }

  public static org.hl7.fhir.r4.model.Task.TaskOutputComponent convertTaskOutputComponent(org.hl7.fhir.r5.model.Task.TaskOutputComponent src) throws FHIRException {
    if (src == null)
      return null;
    org.hl7.fhir.r4.model.Task.TaskOutputComponent tgt = new org.hl7.fhir.r4.model.Task.TaskOutputComponent();
    ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().copyBackboneElement(src, tgt);
    if (src.hasType())
      tgt.setType(CodeableConcept40_50.convertCodeableConcept(src.getType()));
    if (src.hasValue())
      tgt.setValue(ConversionContext40_50.INSTANCE.getVersionConvertor_40_50().convertType(src.getValue()));
    return tgt;
  }
}