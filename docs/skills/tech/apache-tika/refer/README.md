# Apache Tika 学习资料

## 官方入口

| 资料 | 链接 |
|------|------|
| Tika 文档首页 | https://tika.apache.org/2.9.2/index.html |
| 支持的格式列表 | https://tika.apache.org/2.9.2/formats.html |
| API 参考 | https://tika.apache.org/2.9.2/api/ |
| GitHub | https://github.com/apache/tika |

## 关键概念

### 架构

```
输入文件 → Detector（MIME 检测） → Parser（文本提取） → ContentHandler（内容处理）
                                           ↓
                                    Metadata（元数据）
```

### Parser 结构

```
AutoDetectParser（自动路由）
├── PDFParser          → PDF
├── OOXMLParser        → .docx / .xlsx / .pptx
├── TXTParser          → .txt / .md / .csv
├── HTMLParser         → .html / .htm
└── ...（1000+ 格式）
```

### BodyContentHandler 字符限制

```java
// -1 = 无限制（风险：大文件 OOM）
BodyContentHandler handler = new BodyContentHandler(-1);

// 限制 10 万字符（推荐生产环境）
BodyContentHandler handler = new BodyContentHandler(100_000);

// 限制 10MB
BodyContentHandler handler = new BodyContentHandler(10 * 1024 * 1024);
```

### 大文件处理最佳实践

```java
// 1. 流式解析 + 超时控制
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<String> future = executor.submit(() -> {
    return new Tika().parseToString(inputStream);
});
String content = future.get(30, TimeUnit.SECONDS); // 30 秒超时

// 2. 限制文件大小
if (fileSize > 50 * 1024 * 1024) { // 50MB
    throw new FileTooLargeException("文件超过 50MB 限制");
}
```

### 中文支持

| 格式 | 支持情况 | 备注 |
|------|---------|------|
| 中文 PDF（文字型） | ✅ | Tika 的 PDFBox 解析器支持 |
| 中文 PDF（扫描型） | ❌ | 需要 OCR（Tesseract），Tika 不内置 |
| 中文 Word (.docx) | ✅ | OOXMLParser 原生支持 |
| 中文 Excel (.xlsx) | ✅ | 支持，但合并单元格可能错位 |
| 中文 PPT | ✅ | 文本可提取，图表/图形不可 |

### 内存占用参考

| 文件大小 | 文件类型 | 提取文本大小 | 内存峰值 |
|----------|---------|-------------|---------|
| 1MB PDF | 文字型 | ~50KB | ~30MB |
| 10MB PDF | 文字型 | ~500KB | ~100MB |
| 50MB PDF | 混合型 | ~2MB | ~500MB+ |
| 1MB .docx | 文字 | ~30KB | ~20MB |

### 依赖树

`tika-parsers-standard-package` 传递引入 50+ 依赖（PDFBox、POI、BouncyCastle 等），jar 包总体积约 80MB。如只需特定格式，可只引入对应 parser：

```xml
<!-- 仅 PDF -->
<dependency>
    <groupId>org.apache.tika</groupId>
    <artifactId>tika-parser-pdf-module</artifactId>
    <version>2.9.2</version>
</dependency>
```
