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

public class V3Hl7CMETAttributionEnumFactory implements EnumFactory<V3Hl7CMETAttribution> {

  public V3Hl7CMETAttribution fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("contact".equals(codeString))
      return V3Hl7CMETAttribution.CONTACT;
    if ("identified".equals(codeString))
      return V3Hl7CMETAttribution.IDENTIFIED;
    if ("identified-confirmable".equals(codeString))
      return V3Hl7CMETAttribution.IDENTIFIEDCONFIRMABLE;
    if ("identified-informational".equals(codeString))
      return V3Hl7CMETAttribution.IDENTIFIEDINFORMATIONAL;
    if ("informational".equals(codeString))
      return V3Hl7CMETAttribution.INFORMATIONAL;
    if ("minimal".equals(codeString))
      return V3Hl7CMETAttribution.MINIMAL;
    if ("universal".equals(codeString))
      return V3Hl7CMETAttribution.UNIVERSAL;
    throw new IllegalArgumentException("Unknown V3Hl7CMETAttribution code '" + codeString + "'");
  }

  public String toCode(V3Hl7CMETAttribution code) {
       if (code == V3Hl7CMETAttribution.NULL)
           return null;
       if (code == V3Hl7CMETAttribution.CONTACT)
      return "contact";
    if (code == V3Hl7CMETAttribution.IDENTIFIED)
      return "identified";
    if (code == V3Hl7CMETAttribution.IDENTIFIEDCONFIRMABLE)
      return "identified-confirmable";
    if (code == V3Hl7CMETAttribution.IDENTIFIEDINFORMATIONAL)
      return "identified-informational";
    if (code == V3Hl7CMETAttribution.INFORMATIONAL)
      return "informational";
    if (code == V3Hl7CMETAttribution.MINIMAL)
      return "minimal";
    if (code == V3Hl7CMETAttribution.UNIVERSAL)
      return "universal";
    return "?";
   }

  public String toSystem(V3Hl7CMETAttribution code) {
    return code.getSystem();
  }

}