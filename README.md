# AI智能体项目
这是一个基于Spring AI框架构建的AI智能体项目，旨在展示如何结合RAG（检索增强生成）、Tool Calling、多模态内容处理（MCP）以及Agent技术来构建强大的智能应用。

# 模型一：AI学习智能体
使用RAG、Tool Calling和MCP，主要解决简单的计算机学习问题，调用知识库获取外部知识，更改SystemPrompt和知识库就可以获得一个你的专属AI
# 模型二：AI超级智能体
使用CoT思维链，ReAct思考行动，将一个问题分解回答，调用工具解决问题


## 核心技术亮点
### 1. Spring AI
本项目采用Spring AI作为核心框架，它提供了一套简洁、统一的API，用于集成各种AI模型（如OpenAI、Hugging Face等）。通过Spring AI，我们可以轻松地进行文本生成、嵌入、图像处理等操作，极大地简化了AI应用的开发。

### 2. RAG (Retrieval Augmented Generation)
为了增强AI模型的知识广度和准确性，本项目集成了RAG技术。通过RAG，AI智能体能够从外部知识库（例如项目中的SQL数据库、PDF文档等）中检索相关信息，并将其作为上下文输入给语言模型，从而生成更准确、更具信息量的回复。这对于处理特定领域知识或实时数据非常有用。

### 3. Tool Calling
智能体通过Tool Calling能力，可以根据用户的指令或当前任务的需要，动态地调用外部工具或服务来完成复杂的操作。例如，智能体可以调用数据库查询工具来获取数据，或者调用文件处理工具来解析文档。这使得智能体不仅限于文本交互，还能与外部系统进行深度集成。

### 4. MCP (Multi-modal Content Processing)
本项目支持多模态内容处理，特别是针对图片和PDF文档。通过MCP，智能体能够理解和处理不同格式的信息，例如从PDF中提取文本内容，或者对图片进行分析。这使得智能体能够处理更丰富的用户输入和输出。

### 5. Agent
本项目中的AI智能体（Agent）是核心组件，它能够理解用户意图，规划执行步骤，并利用RAG、Tool Calling和MCP等能力来完成任务。Agent的设计使得系统具备了更强的自主性和问题解决能力，能够处理更复杂的业务逻辑和用户请求。

### 项目展示
![image](https://github.com/user-attachments/assets/faac2f40-0b76-43ef-9201-2083b277ebc3)
![image](https://github.com/user-attachments/assets/ba5852c7-750b-4348-a4f9-9d6c9a0f7372)
![image](https://github.com/user-attachments/assets/ed5125c3-68f8-42a0-bd1f-3ee4201691ae)


## 项目结构概览
```
.gitignore
.mvn/
chatMemory/
pom.xml
sql/
├── table.sql
src/
├── main/
│   ├── java/
│   └── resources/
└── test/
    └── java/
tmp/
├── files/
├── pdf/
└── resource/
zxc-agent-picture-search-mcp-server/  # 可能包含图片搜索和多模态内
容处理相关服务
├── .gitignore
├── .mvn/
├── pom.xml
└── src/
    ├── main/
    └── test/
zxcAgent-Front/ # 前端项目，可能用于展示智能体交互界面
├── .gitignore
├── README.md
├── index.html
├── package-lock.json
├── package.json
├── public/
│   └── vite.svg
├── src/
│   ├── App.vue
│   ├── assets/
│   ├── components/
│   ├── main.js
│   ├── router/
│   ├── style.css
│   └── views/
└── vite.config.js
```
