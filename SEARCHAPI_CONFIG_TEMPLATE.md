# SearchAPI Configuration Template

## 配置文件说明

由于`application-local.yml`包含敏感信息（API keys、数据库密码等），该文件已被`.gitignore`忽略，不会提交到版本控制。

## 如何配置

在`src/main/resources/application-local.yml`文件中添加以下SearchAPI配置：

```yaml
# SearchAPI configuration
searchapi:
  api-key: YOUR_SEARCHAPI_KEY_HERE
  base-url: https://www.searchapi.io/api/v1/search
  engine: google
  timeout: 30000
  location: "New York,United States"
  language: en
  country: us
```

## 配置参数说明

| 参数 | 必需 | 说明 | 默认值 |
|------|------|------|--------|
| `api-key` | 是 | SearchAPI的API密钥 | 无 |
| `base-url` | 否 | SearchAPI基础URL | `https://www.searchapi.io/api/v1/search` |
| `engine` | 否 | 搜索引擎 | `google` |
| `timeout` | 否 | 请求超时时间（毫秒） | `30000` |
| `location` | 否 | 搜索地理位置 | `New York,United States` |
| `language` | 否 | 界面语言 | `en` |
| `country` | 否 | 国家代码 | `us` |

## 获取API Key

1. 访问 [SearchAPI官网](https://www.searchapi.io/)
2. 注册账号
3. 在Dashboard中获取你的API key
4. 将API key配置到`application-local.yml`

## 环境变量配置（推荐用于生产环境）

为了更安全，建议在生产环境使用环境变量：

```yaml
searchapi:
  api-key: ${SEARCHAPI_KEY}
  base-url: ${SEARCHAPI_BASE_URL:https://www.searchapi.io/api/v1/search}
  engine: ${SEARCHAPI_ENGINE:google}
  timeout: ${SEARCHAPI_TIMEOUT:30000}
  location: ${SEARCHAPI_LOCATION:New York,United States}
  language: ${SEARCHAPI_LANGUAGE:en}
  country: ${SEARCHAPI_COUNTRY:us}
```

然后设置环境变量：
```bash
export SEARCHAPI_KEY="your_api_key_here"
```

## 完整的application-local.yml示例

```yaml
spring:
  application:
    name: pengyu-ai-agent
  ai:
    dashscope:
      api-key: your_dashscope_key
      chat:
        options:
          model: qwen-plus

  datasource:
    url: jdbc:postgresql://your-db-host:5432/database_name
    username: your_username
    password: "your_password"

# SearchAPI configuration
searchapi:
  api-key: your_searchapi_key
  base-url: https://www.searchapi.io/api/v1/search
  engine: google
  timeout: 30000
  location: "New York,United States"
  language: en
  country: us
```

## 验证配置

启动应用后，查看日志确认配置是否正确加载：

```bash
# 应该看到类似的日志
INFO  SearchApiConfig - SearchAPI configured with base URL: https://www.searchapi.io/api/v1/search
```

## 故障排查

### 问题1: 配置未加载
- 检查文件路径是否正确
- 确认文件名为`application-local.yml`
- 检查YAML格式是否正确（注意缩进）

### 问题2: API调用失败
- 检查API key是否正确
- 确认网络连接正常
- 查看日志中的详细错误信息
- 确认SearchAPI账户是否有可用配额

## 安全注意事项

⚠️ **重要提醒:**

1. **不要提交包含真实API key的配置文件到Git**
2. **定期更换API密钥**
3. **使用环境变量或密钥管理服务存储敏感信息**
4. **设置API调用频率限制，防止滥用**
5. **监控API使用情况和配额**

## 相关文档

- [SearchAPI集成文档](./SEARCHAPI_INTEGRATION.md)
- [SearchAPI官方文档](https://www.searchapi.io/docs/google)
- [Spring Boot配置文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
