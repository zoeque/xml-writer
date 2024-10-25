package zoeque.xml.applicaton;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import zoeque.xml.domain.annotation.XmlChildAnnotation;
import zoeque.xml.domain.annotation.XmlRootAnnotation;
import zoeque.xml.domain.exception.XmlWriterException;
import zoeque.xml.domain.model.AbstractXmlModel;

/**
 * This class writes with the given {@link zoeque.xml.domain.model.AbstractXmlModel} instance.
 */
@Slf4j
public class XmlWriter {

  String filePath;

  /**
   * Constructor
   */
  public XmlWriter(String filePath) {
    this.filePath = filePath;
  }

  /**
   * Write xml file to given file path
   *
   * @param model {@link AbstractXmlModel} with full set value.
   * @throws XmlWriterException Xml writing exception
   */
  public void write(AbstractXmlModel model) throws XmlWriterException {
    try {
      StringBuilder xmlBuilder = new StringBuilder();

      // Get class annotation
      Class<?> clazz = model.getClass();
      XmlRootAnnotation rootAnnotation = clazz.getAnnotation(XmlRootAnnotation.class);

      if (rootAnnotation != null) {
        xmlBuilder.append("<").append(rootAnnotation.name()).append(">\n");

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
          XmlChildAnnotation childAnnotation = field.getAnnotation(XmlChildAnnotation.class);
          if (childAnnotation != null) {
            field.setAccessible(true);
            Object value = field.get(model);
            xmlBuilder.append("  <").append(childAnnotation.name()).append(">");
            xmlBuilder.append(value != null ? value.toString() : "");
            xmlBuilder.append("</").append(childAnnotation.name()).append(">\n");
          }
        }

        xmlBuilder.append("</").append(rootAnnotation.name()).append(">");
      }

      // write XML file
      Path path = Paths.get(filePath);
      Files.write(path, xmlBuilder.toString().getBytes());
      log.info("XML has been written to {}", filePath);
    } catch (Exception e) {
      throw new XmlWriterException(e);
    }
  }

  /**
   * Return file path that set into XmlWriter instance.
   *
   * @return file path
   */
  public String getFilePath() {
    return this.filePath;
  }
}
