package org.hl7.fhir.dstu3.model.codesystems;




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

// Generated on Sat, Mar 25, 2017 21:03-0400 for FHIR v3.0.0


import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityNamePartTypeR2EnumFactory implements EnumFactory<V3EntityNamePartTypeR2> {

  public V3EntityNamePartTypeR2 fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("DEL".equals(codeString))
      return V3EntityNamePartTypeR2.DEL;
    if ("FAM".equals(codeString))
      return V3EntityNamePartTypeR2.FAM;
    if ("GIV".equals(codeString))
      return V3EntityNamePartTypeR2.GIV;
    if ("TITLE".equals(codeString))
      return V3EntityNamePartTypeR2.TITLE;
    throw new IllegalArgumentException("Unknown V3EntityNamePartTypeR2 code '"+codeString+"'");
  }

  public String toCode(V3EntityNamePartTypeR2 code) {
       if (code == V3EntityNamePartTypeR2.NULL)
           return null;
       if (code == V3EntityNamePartTypeR2.DEL)
      return "DEL";
    if (code == V3EntityNamePartTypeR2.FAM)
      return "FAM";
    if (code == V3EntityNamePartTypeR2.GIV)
      return "GIV";
    if (code == V3EntityNamePartTypeR2.TITLE)
      return "TITLE";
    return "?";
   }

    public String toSystem(V3EntityNamePartTypeR2 code) {
      return code.getSystem();
      }

}