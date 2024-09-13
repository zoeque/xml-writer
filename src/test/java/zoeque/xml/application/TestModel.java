package zoeque.xml.application;

import zoeque.xml.domain.annotation.XmlChildAnnotation;
import zoeque.xml.domain.annotation.XmlRootAnnotation;

@XmlRootAnnotation(name = "root")
public class TestModel {
  @XmlChildAnnotation(name = "child")
  private String childField;
}
