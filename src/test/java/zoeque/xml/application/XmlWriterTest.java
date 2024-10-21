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
    // 一時ファイルを作成
    tempFilePath = Files.createTempFile("test", ".xml");
    xmlWriter = new XmlWriter(tempFilePath.toString());
  }

  @AfterEach
  void tearDown() throws IOException {
    // テスト後に一時ファイルを削除
    Files.deleteIfExists(tempFilePath);
  }

  @Test
  void testWrite_Success() throws IllegalAccessException, IOException {
    // テスト用モデル
    TestModel model = new TestModel();
    model.setField1("value1");
    model.setField2("value2");

    // XMLを書き込む
    xmlWriter.write(model);

    // 書き込まれたファイルの確認
    String content = Files.readString(tempFilePath);
    String expectedXml = "<TestModel>\n" +
            "  <field1>value1</field1>\n" +
            "  <field2>value2</field2>\n" +
            "</TestModel>";

    assertEquals(expectedXml, content.trim());
  }

  @Test
  void testWrite_ThrowsIOException() {
    // モックファイルパスを設定（無効なパスを使うことで例外を発生させる）
    xmlWriter = new XmlWriter("/invalid/path/test.xml");

    // モデルの準備
    TestModel model = new TestModel();

    // IOExceptionが発生することを確認
    assertThrows(IOException.class, () -> xmlWriter.write(model));
  }

  // テスト用モデルクラス
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