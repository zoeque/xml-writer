package zoeque.xml.application;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zoeque.xml.applicaton.XmlWriter;
import zoeque.xml.domain.model.AbstractXmlModel;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class XmlWriterTest {

  @TempDir
  public Path tempFolder;

  @Mock
  private AbstractXmlModel model;

  private File file;
  private XmlWriter xmlWriter;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    file = tempFolder.resolve("test.xml").toFile();
    xmlWriter = new XmlWriter(model, file);
  }


  @Test
  public void testWrite() throws Exception {
    // Mock settings
    Field field = TestModel.class.getDeclaredField("childField");
    field.setAccessible(true);
    when(field.get(model)).thenReturn("childValue");

    // write XML file
    xmlWriter.write();

    // result
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(file);

    Element rootElement = doc.getDocumentElement();
    assertEquals("root", rootElement.getNodeName());

    Element childElement = (Element) rootElement.getElementsByTagName("child").item(0);
    assertEquals("childValue", childElement.getTextContent());
  }
}
