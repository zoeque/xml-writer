# XML writer
## Overview

## Usage
When used, first create an instance of the XmlWriter class and specify the output file as the argument.
The specified file path can be checked using the getFilePath() method.  

Create a class that extends AbstractXmlModel according to the XML format you wish to output.  
The class inherits from AbstractXmlModel and is given XmlRootAnnotation as a class annotation. This annotation can be set to a `name` and the specified value will be set as an attribute of the XML.

XmlChildAnnotation is given to the field as a child element of XML. This annotation can be set to a `name` as well. The value set for the field can be set as the value of the element.

Below is a sample XML model.
```java
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
```

XmlWriter class exports this model to xml below;
```xml
<TestModel>
    <field1>value1</field1>
    <field2>value2</field2>
</TestModel>
```

## Development
This application is built with the environment bellow;

- OpenJDK 17
- Spring boot 3.1.1
- IntelliJ IDEA 2023.1.2

Note that this module is needed the gradle.properties on the root directory.
The property file is used when the module is published to GitHub Package manager.  
Here is an example of the gradle.properties bellow (name must be set as your name);

```properties
GITHUB_USERNAME = zoeque
GITHUB_TOKEN = [[TOKEN]]
```

