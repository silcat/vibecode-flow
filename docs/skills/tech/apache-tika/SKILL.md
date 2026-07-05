# Apache Tika 技能

## 是什么

Apache Tika 是通用文档解析工具，支持 1000+ 文件格式（PDF、Word、Excel、PPT、HTML、纯文本等），自动检测 MIME 类型并提取文本内容。

## 快速接入

### 依赖

```xml
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-core</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-parsers-standard-package</artifactId>
    <version>2.9.2</version>
</dependency>
```

### 核心代码片段

```java
// 自动检测类型并解析文本
Tika tika = new Tika();
String content = tika.parseToString(inputStream);

// 带元数据的解析
Parser parser = new AutoDetectParser();
BodyContentHandler handler = new BodyContentHandler(-1); // -1 = 无长度限制
Metadata metadata = new Metadata();
parser.parse(inputStream, handler, metadata, new ParseContext());

String text = handler.toString();
String mimeType = metadata.get("Content-Type");

// 限制提取量（大文件场景）
BodyContentHandler handler = new BodyContentHandler(100_000); // 限制 10 万字符
```

## 已知坑

| 坑 | 现象 | 解法 |
|----|------|------|
| 解析器缺失 | 某些格式报 NoParserFoundException | 检查是否引入 tika-parsers-standard-package |
| 大文件 OOM | PDF 大文件解析时内存爆炸 | 使用 BodyContentHandler 限制字符数 |
| 解析慢 | Word/PDF 解析耗时较长 | 异步处理 + 超时控制 |
| 中文 PDF | 某些中文 PDF 文字提取不全 | 尝试 PDFBox 代替 Tika 的 PDF 解析器 |

## 参考

| 资料 | 链接 |
|------|------|
| Tika 官方文档 | https://tika.apache.org/2.9.2/index.html |
| 支持的格式列表 | https://tika.apache.org/2.9.2/formats.html |

更多资料见 [refer/](refer/)
