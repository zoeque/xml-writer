package zoeque.xml.application;

import org.junit.jupiter.api.Test;
import zoeque.xml.applicaton.XmlWriter;
import zoeque.xml.domain.annotation.XmlChildAnnotation;
import zoeque.xml.domain.annotation.XmlRootAnnotation;
import zoeque.xml.domain.model.AbstractXmlModel;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XmlWriterTest {

  @XmlRootAnnotation(name = "Person")
  static class TestPerson extends AbstractXmlModel {
    @XmlChildAnnotation(name = "Name")
    private String name;

    @XmlChildAnnotation(name = "Age")
    private int age;

    public TestPerson(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

  @Test
  public void testWritePerson() throws IllegalAccessException {
    // Arrange
    TestPerson person = new TestPerson("Alice", 30);
    XmlWriter xmlWriter = new XmlWriter();

    // Act
    String xmlOutput = xmlWriter.write(person);

    // Assert
    String expectedXml = "<Person>\n" +
            "  <Name>Alice</Name>\n" +
            "  <Age>30</Age>\n" +
            "</Person>";
    assertEquals(expectedXml, xmlOutput);
  }

  @Test
  public void testWritePersonWithNullValues() throws IllegalAccessException {
    // Arrange
    TestPerson person = new TestPerson(null, 0);
    XmlWriter xmlWriter = new XmlWriter();

    // Act
    String xmlOutput = xmlWriter.write(person);

    // Assert
    String expectedXml = "<Person>\n" +
            "  <Name></Name>\n" +
            "  <Age>0</Age>\n" +
            "</Person>";
    assertEquals(expectedXml, xmlOutput);
  }
}