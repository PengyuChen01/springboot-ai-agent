# SearchAPI Integration 文档

## 📖 概述

本项目已成功集成SearchAPI的Google搜索功能，为AI Agent提供了实时网络搜索能力。

## 🏗️ 架构设计

### 完整的三层架构

```
┌─────────────────────────────────────────┐
│  LoveApp (AI应用层)                      │
│  └─ 使用 WebSearchTool                   │
├─────────────────────────────────────────┤
│  WebSearchTool (工具层)                  │
│  └─ @Tool注解，提供给AI的搜索接口        │
├─────────────────────────────────────────┤
│  SearchApiService (服务层)               │
│  └─ 封装HTTP调用和响应处理               │
├─────────────────────────────────────────┤
│  SearchApiConfig (配置层)                │
│  └─ 管理API key和配置参数                │
└─────────────────────────────────────────┘
```

## 📁 文件结构

```
src/main/java/com/example/pengyuAiAgent/
├── config/
│   └── SearchApiConfig.java          # 配置类
├── service/
│   └── SearchApiService.java         # API服务类
├── tools/
│   └── WebSearchTool.java            # 搜索工具类
└── app/
    └── LoveApp.java                  # 应用类（已集成）

src/main/resources/
└── application-local.yml              # 配置文件（已添加searchapi配置）

src/test/java/com/example/pengyuAiAgent/tools/
└── WebSearchToolTest.java            # 测试类
```

## ⚙️ 配置说明

### application-local.yml 配置

```yaml
searchapi:
  api-key: 4J9n6Lo48WX3eEmaLkgzbY82
  base-url: https://www.searchapi.io/api/v1/search
  engine: google
  timeout: 30000
  location: "New York,United States"
  language: en
  country: us
```

### 配置参数说明

| 参数 | 说明 | 默认值 |
|------|------|--------|
| `api-key` | SearchAPI的API密钥 | 必需 |
| `base-url` | SearchAPI基础URL | `https://www.searchapi.io/api/v1/search` |
| `engine` | 搜索引擎 | `google` |
| `timeout` | 请求超时时间（毫秒） | `30000` |
| `location` | 搜索地理位置 | `New York,United States` |
| `language` | 界面语言 | `en` |
| `country` | 国家代码 | `us` |

## 🚀 使用方法

### 1. 在AI对话中使用

WebSearchTool已经集成到LoveApp中，AI会自动在需要时调用搜索功能：

```java
// LoveApp 中的配置
@Autowired
private WebSearchTool webSearchTool;

chatClient = ChatClient.builder(dashscopeChatModel)
    .defaultSystem(SYSTEM_PROMPT)
    .defaultTools(webSearchTool)  // 添加搜索工具
    .build();
```

用户对话示例：
```
用户: "最近有什么改善恋爱关系的建议吗？"
AI: 让我帮你搜索一下最新的建议... [自动调用WebSearchTool]
AI: 根据搜索结果，这里有一些改善恋爱关系的建议...
```

### 2. 直接调用WebSearchTool

```java
@Autowired
private WebSearchTool webSearchTool;

public void search() {
    String result = webSearchTool.search("relationship communication tips");
    System.out.println(result);
}
```

### 3. 使用SearchApiService

```java
@Autowired
private SearchApiService searchApiService;

public void directSearch() {
    String result = searchApiService.search("how to improve relationship");
    System.out.println(result);
}
```

## 📊 返回格式

搜索结果以简洁文本格式返回：

```
搜索结果：relationship communication tips

1. 10 Essential Communication Tips for Relationships
   链接：https://example.com/article1
   摘要：Effective communication is the foundation of any healthy relationship...

2. How to Improve Communication in Your Relationship
   链接：https://example.com/article2
   摘要：Learn practical tips to enhance communication with your partner...

3. The Art of Relationship Communication
   链接：https://example.com/article3
   摘要：Discover the key principles of successful relationship communication...

[最多显示5条结果]
```

## 🧪 测试

运行测试类验证功能：

```bash
mvn test -Dtest=WebSearchToolTest
```

### 测试用例

1. `testSearchWithValidQuery()` - 测试有效查询
2. `testSearchWithEmptyQuery()` - 测试空查询处理
3. `testSearchWithNullQuery()` - 测试null查询处理
4. `testSearchServiceDirectly()` - 直接测试服务层
5. `testChineseQuery()` - 测试中文查询

## 🔧 依赖

已添加到pom.xml：

```xml
<!-- OkHttp for SearchAPI integration -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.12.0</version>
</dependency>
```

## 🔐 安全建议

### 当前配置
- ✅ API key配置在`application-local.yml`
- ✅ 添加请求超时控制
- ✅ 完善的错误处理

### 生产环境建议
1. **不要将API key提交到版本控制**
   ```yaml
   # 使用环境变量
   searchapi:
     api-key: ${SEARCHAPI_KEY}
   ```

2. **使用密钥管理服务**
   - Spring Cloud Config
   - HashiCorp Vault
   - AWS Secrets Manager

3. **添加请求限流**
   ```java
   @RateLimiter(name = "searchapi")
   public String search(String query) { ... }
   ```

## 📝 API文档

详细API文档请参考：
- SearchAPI官方文档: https://www.searchapi.io/docs/google
- Spring AI文档: https://docs.spring.io/spring-ai/reference/

## ❓ 常见问题

### Q1: 搜索返回错误怎么办？
A: 检查以下几点：
1. API key是否正确配置
2. 网络连接是否正常
3. 查看日志中的详细错误信息

### Q2: 如何修改返回的搜索结果数量？
A: 在`SearchApiService.formatSearchResults()`方法中修改：
```java
if (count >= 5) break; // 修改这里的数字
```

### Q3: 如何支持其他搜索引擎？
A: 修改配置文件中的`engine`参数：
```yaml
searchapi:
  engine: bing  # 或 baidu, yandex 等
```

### Q4: 搜索结果是中文还是英文？
A: 由`language`和`location`参数决定，可在配置文件中修改。

## 🎯 功能特性

- ✅ Google搜索集成
- ✅ 简洁文本格式返回
- ✅ 中英文查询支持
- ✅ 地理位置定制
- ✅ 超时控制
- ✅ 错误处理
- ✅ 日志记录
- ✅ 单元测试
- ✅ AI自动调用

## 📈 性能优化建议

1. **启用结果缓存**
   ```java
   @Cacheable("searchResults")
   public String search(String query) { ... }
   ```

2. **异步搜索**
   ```java
   @Async
   public CompletableFuture<String> searchAsync(String query) { ... }
   ```

3. **连接池优化**
   ```java
   client = new OkHttpClient.Builder()
       .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
       .build();
   ```

## 👥 贡献者

- 集成时间: 2026-02-04
- 集成版本: v1.0.0

## 📄 License

本集成遵循项目主LICENSE。

---

**需要帮助？** 请查看项目文档或联系开发团队。
