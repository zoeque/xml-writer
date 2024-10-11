package zoeque.xml.applicaton;

import java.io.File;
import java.lang.reflect.Field;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import zoeque.xml.domain.annotation.XmlChildAnnotation;
import zoeque.xml.domain.annotation.XmlRootAnnotation;
import zoeque.xml.domain.exception.XmlWriterException;
import zoeque.xml.domain.model.AbstractXmlModel;

/**
 * This class writes with the given {@link zoeque.xml.domain.model.AbstractXmlModel} instance.
 */
@Slf4j
public class XmlWriter {
  /**
   * Constructor
   */
  public XmlWriter() {
  }

  public String write(AbstractXmlModel model) throws IllegalAccessException {
    StringBuilder xmlBuilder = new StringBuilder();

    // クラスのアノテーションを取得
    Class<?> clazz = model.getClass();
    XmlRootAnnotation rootAnnotation = clazz.getAnnotation(XmlRootAnnotation.class);

    if (rootAnnotation != null) {
      xmlBuilder.append("<").append(rootAnnotation.name()).append(">\n");

      // フィールドを取得
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

    return xmlBuilder.toString();
  }
}
