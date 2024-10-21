package zoeque.xml.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import zoeque.xml.applicaton.XmlWriter;
import zoeque.xml.domain.annotation.XmlChildAnnotation;
import zoeque.xml.domain.annotation.XmlRootAnnotation;
import zoeque.xml.domain.model.AbstractXmlModel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class XmlWriterTest {

  @InjectMocks
  private XmlWriter xmlWriter;

  private Path tempFilePath;  // 一時ファイルパス

  @BeforeEach
  void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    // create temporary file
    tempFilePath = Files.createTempFile("test", ".xml");
    xmlWriter = new XmlWriter(tempFilePath.toString());
  }

  @AfterEach
  void deleteTemporaryFile() throws IOException {
    // delete temporary file
    Files.deleteIfExists(tempFilePath);
  }

  @Test
  void testWrite_Success() throws IllegalAccessException, IOException {
    // Create temporary model class
    TestModel model = new TestModel();
    model.setField1("value1");
    model.setField2("value2");

    // write xml file
    xmlWriter.write(model);

    // assert expected xml model
    String content = Files.readString(tempFilePath);
    String expectedXml = "<TestModel>\n" +
            "  <field1>value1</field1>\n" +
            "  <field2>value2</field2>\n" +
            "</TestModel>";

    assertEquals(expectedXml, content.trim());
  }

  @Test
  void testWrite_ThrowsIOException() {
    // create mock path that does not exist
    xmlWriter = new XmlWriter("/invalid/path/test.xml");

    // create model, need not set value
    TestModel model = new TestModel();

    // IOException
    assertThrows(IOException.class, () -> xmlWriter.write(model));
  }

  /**
   * The test class
   */
  @XmlRootAnnotation(name = "TestModel")
  static class TestModel extends AbstractXmlModel {
    @XmlChildAnnotation(name = "field1")
    private String field1;

    @XmlChildAnnotation(name = "field2")
    private String field2;

    public void setField1(String field1) {
      this.field1 = field1;
    }

    public void setField2(String field2) {
      this.field2 = field2;
    }
  }
}