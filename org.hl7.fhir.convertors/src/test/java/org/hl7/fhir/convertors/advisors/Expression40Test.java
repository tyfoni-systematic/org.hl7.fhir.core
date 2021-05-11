package org.hl7.fhir.convertors.advisors;

import org.hl7.fhir.convertors.VersionConvertor_10_40;
import org.hl7.fhir.convertors.misc.IGR2ConvertorAdvisor;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Questionnaire;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class Expression40Test {

  @Test
  @DisplayName("Test ValueExpression conversion R4 -> DSTU3, without advisor")
  public void testValueExpressionConversion40_10() throws IOException {
//    org.hl7.fhir.r4.model.Expression r4Expression =
//      new org.hl7.fhir.r4.model.Expression()
//        .setLanguage("text/cql")
//        .setExpression("(a1  <> 0) and (a2 = 1 or a2 = 3)");
//    org.hl7.fhir.r4.model.Extension r4Extension = new org.hl7.fhir.r4.model.Extension("http://example.com", r4Expression);
//
//    JsonParser r4_parser = new JsonParser();
//    JsonParser parser = new JsonParser();
//    String composeString = parser.composeString(r4Extension, "Extension");

  }

  @Test
  @DisplayName("Test ValueExpression conversion DSTU3 -> R4, with advisor")
  public void testValueExpressionConversion10_40() throws IOException {
//    org.hl7.fhir.r4.model.Expression r4Expression =
//      new org.hl7.fhir.r4.model.Expression()
//        .setLanguage("text/cql")
//        .setExpression("(a1  <> 0) and (a2 = 1 or a2 = 3)");
//    org.hl7.fhir.r4.model.Extension r4Extension = new org.hl7.fhir.r4.model.Extension("http://example.com", r4Expression);
//    org.hl7.fhir.r4.model.Questionnaire r4Questionnaire = new Questionnaire();
//    r4Questionnaire.addExtension(r4Extension);
//
////    org.hl7.fhir.dstu2.model.ValueSet dstu2 = (org.hl7.fhir.dstu2.model.ValueSet) new org.hl7.fhir.dstu2.formats.JsonParser().parse(dstu2_input);
//    VersionConvertorAdvisor40 advisor = new IGR2ConvertorAdvisor();
//    org.hl7.fhir.dstu2.model.Resource dstu2_actual = VersionConvertor_10_40.convertResource(r4Questionnaire, advisor);
//
//    org.hl7.fhir.dstu2.formats.JsonParser dstu2_parser = new org.hl7.fhir.dstu2.formats.JsonParser();
//    String composeString = dstu2_parser.composeString(dstu2_actual);

  }
}
