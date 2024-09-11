package zoeque.xml.domain.exception;

/**
 * The exception class causes when {@link zoeque.xml.applicaton.XmlWriter} attempt to create XML files.
 */
public class XmlWriterException extends Exception {
  public XmlWriterException(Throwable e) {
    super(e);
  }
}
