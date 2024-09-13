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
  private AbstractXmlModel _model;
  private File _file;

  /**
   * Constructor
   *
   * @param model The xml model with full value set
   */
  public XmlWriter(AbstractXmlModel model, File xmlFile) {
    this._model = model;
    this._file = xmlFile;
  }

  public void write() throws XmlWriterException {
    try {
      var dbFactory = DocumentBuilderFactory.newInstance();
      var dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.newDocument();

      Class<?> modelClass = _model.getClass();
      if (modelClass.isAnnotationPresent(XmlRootAnnotation.class)) {
        XmlRootAnnotation rootAnnotation = modelClass.getAnnotation(XmlRootAnnotation.class);
        Element rootElement = doc.createElement(rootAnnotation.name());
        doc.appendChild(rootElement);

        Field[] fields = modelClass.getDeclaredFields();
        for (Field field : fields) {
          if (field.isAnnotationPresent(XmlChildAnnotation.class)) {
            XmlChildAnnotation childAnnotation = field.getAnnotation(XmlChildAnnotation.class);
            field.setAccessible(true);
            String value = (String) field.get(_model);
            Element childElement = doc.createElement(childAnnotation.name());
            childElement.appendChild(doc.createTextNode(value));
            rootElement.appendChild(childElement);
          }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(_file);

        transformer.transform(source, result);
      } else {
        log.warn("Class does not have XmlRootAnnotation.");
      }
    } catch (Exception e) {
      throw new XmlWriterException(e);
    }
  }
}
