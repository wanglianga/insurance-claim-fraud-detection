# 保险理赔反欺诈材料核验服务

## 项目简介

本项目是一个基于 Spring Boot、Redis 和 PostgreSQL 构建的保险理赔反欺诈材料核验服务系统。系统完整管理保单、出险报案、事故照片、查勘记录、医院票据、维修报价、责任认定、历史理赔、黑名单线索和赔付结论等核心业务。

## 原始需求

> 请开发保险理赔反欺诈材料核验服务，使用 Spring Boot、Redis 和 PostgreSQL 管理保单、出险报案、事故照片、查勘记录、医院票据、维修报价、责任认定、历史理赔、黑名单线索和赔付结论。被保险人提交事故时间、地点、照片、发票、伤情或维修单；查勘员上传现场坐标、损失范围、询问记录和责任判断；医院或修理厂提供费用明细、项目编码和盖章文件；理赔审核员查看重复报案、时间冲突、影像篡改、异常金额、关联人员和赔付规则。服务要处理材料补传、第三方回调、同一事故多保单、跨地区报案、票据重复使用和人工复核结论，不能只根据材料齐全就直接赔付。

## 技术栈

- **后端框架**: Spring Boot 3.2.5
- **数据库**: PostgreSQL 16
- **缓存**: Redis 7
- **持久层**: Spring Data JPA
- **缓存层**: Spring Data Redis
- **安全框架**: Spring Security
- **构建工具**: Maven
- **Java 版本**: 17

## 核心功能模块

### 1. 保单管理
- 保单信息录入和查询
- 保单有效性验证
- 多保单人关联查询

### 2. 出险报案管理
- 被保险人报案提交（事故时间、地点、照片、发票、伤情/维修单
- 报案状态跟踪
- 重复报案检测

### 3. 反欺诈检测
- **重复报案检测（相近时间、相近地点的重复报案
- **时间冲突检测（同一被保险人短时间内多起事故
- **影像篡改检测（照片哈希比对、EXIF信息验证
- **异常金额检测（索赔金额与历史均值比对
- **关联人员检测（关联人员历史欺诈记录
- **跨地区报案检测（事故地与保单签发地不一致
- **票据重复使用检测（发票号、票据哈希重复检测
- **同一事故多保单检测（同一事故多保多赔
- **黑名单检测（投保人、被保险人、第三方黑名单
- **历史欺诈模式检测（高欺诈率客户识别

### 4. 材料核验
- 事故照片上传和核验
- 医院票据提交和核验
- 维修报价提交和核验
- 责任认定书提交和核验
- 费用明细项目级核验
- 盖章文件核验
- 材料完整性自动检测

### 5. 查勘管理
- 查勘记录上传（现场坐标、损失范围、询问记录、责任判断
- 查勘地点与报案地点一致性核验
- 查勘员欺诈疑点报告

### 6. 赔付审核
- 系统自动审核
- 人工复核流程
- 赔付规则校验
- 不能仅凭材料齐全直接赔付限制
- 多维度审核结论

### 7. 特殊业务处理
- **材料补传**: 材料缺失通知、补传跟踪、补传审核
- **第三方回调**: 异步回调处理、自动重试、失败处理
- **同一事故多保单**: 同一事故涉及多保保赔赔保赔
- **跨地区报案**: 跨地区风险评估
- **票据重复使用**: 票据号、票据哈希重复检测
- **人工复核**: 高风险案件强制人工复核
- **关联关系网络分析

## 启动方式

### 前置要求

- Docker 20.10+
- Docker Compose 2.0+
- 或本地开发：
  - JDK 17+
  - Maven 3.9+
  - PostgreSQL 16+
  - Redis 7+

### Docker 一键启动（推荐）

#### 1. 启动服务

```bash
docker compose up --build
```

如需后台运行：

```bash
docker compose up --build -d
```

#### 2. 停止服务

```bash
docker compose down
```

#### 3. 查看日志

```bash
docker compose logs -f
```

#### 访问地址

- 应用服务: http://localhost:8080
- PostgreSQL: localhost:5432 (数据库: insurance_claim, 用户名: postgres, 密码: postgres
- Redis: localhost:6379

### 本地开发启动

#### 1. 安装依赖

```bash
mvn clean install
```

#### 2. 配置数据库和 Redis

确保 PostgreSQL 和 Redis 服务已启动，并修改 `src/main/resources/application.yml` 中的连接配置。

#### 3. 启动应用

```bash
mvn spring-boot:run
```

或运行打包后的 jar：

```bash
java -jar target/claim-fraud-detection-1.0.0.jar
```

#### 4. 访问地址

http://localhost:8080

## API 文档

### 主要 API 接口概览：

| 模块 | 接口路径 | 说明 |
|------|-----------|------|
| 报案管理 | POST /api/claims/submit | 提交报案 |
| | GET /api/claims/{claimNumber} | 查询报案详情 |
| | GET /api/claims/{claimNumber}/fraud-detection | 获取欺诈检测结果 |
| | POST /api/claims/{claimNumber}/refresh-fraud | 刷新欺诈检测 |
| 查勘管理 | POST /api/surveys/submit | 提交查勘记录 |
| | GET /api/surveys/claim/{claimNumber} | 查询案件查勘记录 |
| 材料管理 | POST /api/materials/photos | 上传事故照片 |
| | POST /api/materials/invoices | 提交医院票据 |
| | POST /api/materials/quotes | 提交维修报价 |
| | POST /api/materials/liabilities | 提交责任认定书 |
| | GET /api/materials/claim/{claimNumber}/photos | 查询案件照片 |
| 赔付审核 | POST /api/compensation/review | 赔付审核 |
| | POST /api/compensation/manual-review | 人工复核 |
| | GET /api/compensation/claim/{claimNumber} | 查询赔付结论 |
| | POST /api/compensation/pending-review | 查询待审核案件 |
| 材料补传 | POST /api/supplements/request | 请求材料补传 |
| | POST /api/supplements/supply | 提交补传材料 |
| 第三方回调 | POST /api/callbacks/receive | 接收第三方回调 |
| | GET /api/callbacks/claim/{claimNumber} | 查询案件回调记录 |

## 反欺诈检测规则

| 检测类型 | 风险分值 | 触发条件 |
|-----------|----------|----------|
| 重复报案 | 30-50 | 72小时内相近时间相近地点重复报案 |
| 时间冲突 | 25-45 | 60分钟内同一人多起事故 |
| 影像篡改 | 25-40 | 照片哈希重复、GPS位置异常、拍摄时间异常 |
| 异常金额 | 35-40 | 索赔金额超均值3倍以上 |
| 关联人员欺诈 | 35-50 | 关联人员有欺诈记录 |
| 跨地区报案 | 20-35 | 事故地与保单签发地不一致 |
| 票据重复使用 | 40-50 | 票据号或票据号或票据哈希重复 |
| 同一事故多保单 | 30-45 | 同一事故多保保赔 |
| 黑名单检测 | 55-60 | 投保人/被保险人在黑名单 |
| 高风险客户 | 25-40 | 历史报案欺诈率超过30% |

## 项目结构

```
src/main/java/com/insurance/claim
├── ClaimFraudDetectionApplication.java
├── config/          # 配置类
│   ├── FraudDetectionConfig.java
│   ├── JpaAuditingConfig.java
│   ├── RedisConfig.java
│   └── SecurityConfig.java
├── controller/        # 控制层
│   ├── CallbackController.java
│   ├── ClaimController.java
│   ├── CompensationController.java
│   ├── MaterialController.java
│   ├── SupplementController.java
│   └── SurveyController.java
├── dto/              # 数据传输对象
│   ├── ApiResponse.java
│   ├── ClaimSubmitRequest.java
│   ├── CompensationReviewRequest.java
│   ├── ExpenseItemRequest.java
│   ├── FraudDetectionResultDTO.java
│   ├── HospitalInvoiceSubmitRequest.java
│   ├── LiabilitySubmitRequest.java
│   ├── ManualReviewRequest.java
│   ├── MaterialSupplementRequest.java
│   ├── MaterialSupplyRequest.java
│   ├── PhotoUploadRequest.java
│   ├── RepairQuoteSubmitRequest.java
│   ├── SurveySubmitRequest.java
│   └── ThirdPartyCallbackRequest.java
├── entity/            # 实体类
│   ├── AccidentPhoto.java
│   ├── BaseEntity.java
│   ├── BlacklistClue.java
│   ├── ClaimReport.java
│   ├── CompensationConclusion.java
│   ├── ExpenseItem.java
│   ├── FraudDetectionResult.java
│   ├── HistoricalClaim.java
│   ├── HospitalInvoice.java
│   ├── InsurancePolicy.java
│   ├── LiabilityDetermination.java
│   ├── MaterialSupplement.java
│   ├── RelatedPerson.java
│   ├── RepairQuote.java
│   ├── SurveyRecord.java
│   └── ThirdPartyCallback.java
├── enums/             # 枚举类型
│   ├── AccidentType.java
│   ├── ClaimStatus.java
│   ├── FraudType.java
│   ├── LiabilityType.java
│   ├── MaterialType.java
│   ├── PolicyStatus.java
│   └── ReviewResult.java
├── exception/         # 异常处理
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java
├── repository/        # 数据访问层
│   ├── AccidentPhotoRepository.java
│   ├── BlacklistClueRepository.java
│   ├── ClaimReportRepository.java
│   ├── CompensationConclusionRepository.java
│   ├── ExpenseItemRepository.java
│   ├── FraudDetectionResultRepository.java
│   ├── HistoricalClaimRepository.java
│   ├── HospitalInvoiceRepository.java
│   ├── InsurancePolicyRepository.java
│   ├── LiabilityDeterminationRepository.java
│   ├── MaterialSupplementRepository.java
│   ├── RelatedPersonRepository.java
│   ├── RepairQuoteRepository.java
│   ├── SurveyRecordRepository.java
│   └── ThirdPartyCallbackRepository.java
├── service/           # 业务逻辑层
│   ├── ClaimService.java
│   ├── CompensationService.java
│   ├── FraudDetectionService.java
│   ├── MaterialSupplementService.java
│   ├── MaterialVerificationService.java
│   ├── SurveyService.java
│   └── ThirdPartyCallbackService.java
└── util/              # 工具类
    ├── DistanceUtil.java
    ├── HashUtil.java
    └── IdGenerator.java
```

## 注意事项

1. **赔付审核限制：系统不会仅凭材料齐全就直接赔付，必须经过完整的反欺诈检测和审核流程
2. **高风险案件强制人工复核：欺诈风险评分超过50分的案件必须经过人工复核
3. **票据重复检测：系统会自动检测票据号、票据哈希是否重复使用
4. **照片篡改检测：系统会对上传照片进行哈希比对和EXIF信息验证
5. **关联人员分析：系统会自动分析被保险人与第三方的关联关系
6. **跨地区风险评估：对跨地区报案进行风险评估
7. **同一事故多保单检测：系统自动检测同一事故是否涉及多保保赔
8. **Redis 缓存：欺诈检测结果会缓存1小时
9. **异步处理：欺诈检测采用异步执行，不阻塞报案提交流程
10. **定时任务：第三方回调失败会自动重试，最多重试3次
