package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Wed, Jan 30, 2019 16:19-0500 for FHIR v4.0.0

import org.hl7.fhir.r4.model.EnumFactory;

public class MedicationdispenseStatusEnumFactory implements EnumFactory<MedicationdispenseStatus> {

  public MedicationdispenseStatus fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("preparation".equals(codeString))
      return MedicationdispenseStatus.PREPARATION;
    if ("in-progress".equals(codeString))
      return MedicationdispenseStatus.INPROGRESS;
    if ("cancelled".equals(codeString))
      return MedicationdispenseStatus.CANCELLED;
    if ("on-hold".equals(codeString))
      return MedicationdispenseStatus.ONHOLD;
    if ("completed".equals(codeString))
      return MedicationdispenseStatus.COMPLETED;
    if ("entered-in-error".equals(codeString))
      return MedicationdispenseStatus.ENTEREDINERROR;
    if ("stopped".equals(codeString))
      return MedicationdispenseStatus.STOPPED;
    if ("declined".equals(codeString))
      return MedicationdispenseStatus.DECLINED;
    if ("unknown".equals(codeString))
      return MedicationdispenseStatus.UNKNOWN;
    throw new IllegalArgumentException("Unknown MedicationdispenseStatus code '" + codeString + "'");
  }

  public String toCode(MedicationdispenseStatus code) {
       if (code == MedicationdispenseStatus.NULL)
           return null;
       if (code == MedicationdispenseStatus.PREPARATION)
      return "preparation";
    if (code == MedicationdispenseStatus.INPROGRESS)
      return "in-progress";
    if (code == MedicationdispenseStatus.CANCELLED)
      return "cancelled";
    if (code == MedicationdispenseStatus.ONHOLD)
      return "on-hold";
    if (code == MedicationdispenseStatus.COMPLETED)
      return "completed";
    if (code == MedicationdispenseStatus.ENTEREDINERROR)
      return "entered-in-error";
    if (code == MedicationdispenseStatus.STOPPED)
      return "stopped";
    if (code == MedicationdispenseStatus.DECLINED)
      return "declined";
    if (code == MedicationdispenseStatus.UNKNOWN)
      return "unknown";
    return "?";
   }

  public String toSystem(MedicationdispenseStatus code) {
    return code.getSystem();
  }

}