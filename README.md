
1. 项目结构
pdf-craft/
├── README.md
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .gitattributes
├── .git/
├── .mvn/
├── target/
├── .idea/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── test/
│                   └── pdf_craft/
│                       ├── common/
│                       │   └── Result.java
│                       ├── config/
│                       │   ├── AsyncConfig.java
│                       │   └── RetryRequestHolder.java
│                       ├── controller/
│                       │   └── StatementController.java
│                       ├── enums/
│                       │   └── ResultCodeEnum.java
│                       ├── job/
│                       │   └── RetrySchedulerJob.java
│                       ├── model/
│                       │   ├── info/
│                       │   │   ├── PageInfo.java
│                       │   │   ├── RetryCallInfo.java
│                       │   │   └── Transaction.java
│                       │   ├── request/
│                       │   │   ├── RetryCallRequest.java
│                       │   │   └── TemplateEngineRequest.java
│                       │   └── response/
│                       │       ├── CoreBankingResponse.java
│                       │       └── TemplateEngineResponse.java
│                       ├── service/
│                       │   ├── CoreBankingService.java
│                       │   ├── StatementService.java
│                       │   └── TemplateEngineService.java
│                       ├── utils/
│                       │   ├── DateUtils.java
│                       │   └── JSONUtils.java
│                       └── PdfCraftApplication.java
```
以下是对该项目补充相应内容后的详细说明：

```

## 2. 功能描述
该项目主要用于处理账单相关的操作，包括异步处理账单生成、生成 PDF 文件以及获取交易信息等功能。

## 3. 设计决策
- **异步处理**：对于一些耗时的操作，如调用 `coreBankingService.getTransactions` 方法，采用异步处理的方式，避免阻塞主线程，提高系统的响应速度。
- **分层架构**：采用分层架构，将控制器、服务和模型进行分离，提高代码的可维护性和可扩展性。

## 4. 安装和运行说明
### 4.1 环境要求
- Java 17
- Maven

### 4.2 安装步骤
1. 克隆项目到本地：
   ```sh
   git clone <项目仓库地址>
   cd pdf-craft
   ```
2. 构建项目：
   ```sh
   mvn clean install
   ```
3. 运行项目：
   ```sh
   mvn spring-boot:run
   ```

## 5. API 使用指南
### 5.1 异步处理账单生成
- **URL**：`/api/processStatement`
- **方法**：`POST`
- **参数**：
    - `accountNumber`：账户号
    - `fromDate`：开始日期
    - `toDate`：结束日期
- **示例**：
  ```sh
  curl -X POST "http://localhost:8080/api/processStatement?accountNumber=123456&fromDate=2024-01-01&toDate=2024-12-31"
  ```

### 5.2 生成 PDF
- **URL**：`/api/generatePdf`
- **方法**：`POST`
- **参数**：`TemplateEngineRequest` 对象的 JSON 表示
- **示例**：
  ```sh
  curl -X POST "http://localhost:8080/api/generatePdf" -H "Content-Type: application/json" -d '{"templateId": "AccountStatement", "data": []}'
  ```

## 6. 测试执行说明
该项目使用 JUnit 5 和 Mockito 进行单元测试。

### 6.1 运行所有测试
在项目根目录下执行以下命令：
```sh
mvn test
```

### 6.2 测试用例说明
- `StatementServiceTest`：测试 `StatementService` 类中的方法。
- `RetrySchedulerJobTest`：测试 `RetrySchedulerJob` 类中的方法。
- ...
```

### 3. 测试执行说明

要执行项目中的测试，可以按照以下步骤操作：

1. 确保你已经安装了 Java 17 和 Maven。
2. 打开终端，进入项目的根目录。
3. 运行以下命令来执行所有的测试：
```sh
mvn test
```

这个命令会自动编译项目并运行所有的单元测试。测试结果会在终端中显示，包括通过的测试用例数、失败的测试用例数等信息。

### 测试用例说明
- `StatementServiceTest`：该测试类主要测试 `StatementService` 类中的方法，包括异步处理账单生成、异常处理等功能。
- `RetrySchedulerTest`：该测试类主要测试 `RetryScheduler` 类中的方法，包括重试失败请求的功能。

通过以上步骤，你可以方便地对项目进行测试，确保代码的正确性和稳定性。