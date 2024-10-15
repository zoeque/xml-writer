package zoeque.xml.applicaton;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import zoeque.xml.domain.annotation.XmlChildAnnotation;
import zoeque.xml.domain.annotation.XmlRootAnnotation;
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

  public void write(AbstractXmlModel model) throws IllegalAccessException, IOException {
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

    // XMLをファイルに書き込む
    Path path = Paths.get(filePath);
    Files.write(path, xmlBuilder.toString().getBytes());
    log.info("XML has been written to {}", filePath);
  }
}
