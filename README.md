# Common Utils

## Introduction
Common Utils is a utility library that includes various commonly used utility classes and configurations, aiming to simplify the development process and improve efficiency.

## Directory Structure
```plaintext
├── main
│   ├── java
│   │   └── org
│   │       └── hubert
│   │           └── common
│   │               └── utils
│   │                   ├── CommonUtilsApplication.java
│   │                   ├── advice
│   │                   │   └── ApiResponseAdvice.java
│   │                   ├── config
│   │                   │   ├── PackageBindOpProducerConfig.java
│   │                   │   ├── RedisConfiguration.java
│   │                   │   ├── RocketMQConsumerConfig.java
│   │                   │   └── ThreadPoolConfig.java
│   │                   ├── controller
│   │                   │   └── RocketMQController.java
│   │                   ├── enums
│   │                   │   └── ResponseEnum.java
│   │                   ├── exceptions
│   │                   │   └── CustomException.java
│   │                   ├── executor
│   │                   │   └── CustomThreadPoolExecutor.java
│   │                   ├── factory
│   │                   │   └── ThreadPoolFactory.java
│   │                   ├── filter
│   │                   │   └── CustomThreadPoolMDCFilter.java
│   │                   ├── handler
│   │                   │   └── GlobalExceptionHandler.java
│   │                   ├── locks
│   │                   │   └── DistributedLock.java
│   │                   ├── properties
│   │                   │   ├── MQConsumerOperationProperties.java
│   │                   │   ├── MQProducerOperationProperties.java
│   │                   │   ├── PackageBindOpPropertiesConsumer.java
│   │                   │   ├── PackageBindOpPropertiesProducer.java
│   │                   │   └── RedisKeyProperties.java
│   │                   ├── result
│   │                   │   └── Result.java
│   │                   └── rocketmq
│   │                       ├── consumer
│   │                       │   ├── AbstractMessageConsumerHandler.java
│   │                       │   ├── MessageConsumerHandler.java
│   │                       │   └── UserDeviceOpConsumerHandler.java
│   │                       ├── msg
│   │                       │   └── UserDeviceOpMsg.java
│   │                       └── producer
│   │                           ├── AbstractMessageProducerHandler.java
│   │                           ├── DistributedLockMessageProducerHandler.java
│   │                           ├── MessageProducerHandler.java
│   │                           ├── PackageBindOpProducerHandler.java
│   │                           └── UnlockedMessageProducerHandler.java
│   └── resources
│       ├── application.yml
│       ├── redis
│       │   ├── conf
│       │   │   └── redis.conf
│       │   └── docker-compose.yml
│       └── rocketmq
│           ├── conf
│           │   ├── broker.conf
│           │   └── plain_acl.json
│           └── docker-compose.yml
└── test
    └── java
        └── org
            └── hubert
                └── common
                    └── utils
                        └── CommonUtilsApplicationTests.java
```

## Installation and Configuration

### Prerequisites
- JDK 17
- Maven 3.x

### Clone the repository
Run the following commands in your terminal:
```bash
git clone https://github.com/hubertwongcn/common-utils.git
cd common-utils
```

### Configuration
All configurations are managed in the `src/main/resources/application.yml` file. Adjust the settings according to your specific requirements.

### Setting Up Redis
The project uses Redis. Relevant configuration files are located in the `src/main/resources/redis` directory. You can start Redis quickly using Docker Compose:

```bash
cd src/main/resources/redis
docker-compose up -d
```

### Setting Up RocketMQ
The project also uses RocketMQ. Relevant configuration files are located in the `src/main/resources/rocketmq` directory. Similarly, you can start RocketMQ using Docker Compose:

```bash
cd src/main/resources/rocketmq
docker-compose up -d
```

## Usage

### Run the Application
You can run the application with the following command:

```bash
mvn spring-boot:run
```

### Run Unit Tests
You can run the unit tests in the project using the following command:

```bash
mvn test
```

## Key Components

### 1. ApiResponseAdvice
The `ApiResponseAdvice` class handles global API responses, standardizing the format of all API responses.

### 2. CustomThreadPoolExecutor
The `CustomThreadPoolExecutor` class is a custom thread pool executor designed to optimize thread management and task execution.

### 3. RedisKeyProperties
The `RedisKeyProperties` class manages Redis key configurations.

### 4. PackageBindOpProducerHandler
The `PackageBindOpProducerHandler` class deals with RocketMQ producer operations.

### 5. GlobalExceptionHandler
The `GlobalExceptionHandler` class handles global exceptions for the application, centralizing and standardizing exception handling.

### 6. DistributedLock
The `DistributedLock` class provides implementation for distributed locking, ensuring mutual exclusion in a distributed environment.

### 7. Result
The `Result` class is a general API response model that includes the response code, message, and specific data.

### 8. ThreadPoolConfig
The `ThreadPoolConfig` class configures the thread pools in the application, ensuring efficient task scheduling and execution.

## Contributing
If you wish to contribute to this project, please follow these steps:
1. Fork the repository
2. Clone your forked repository to your local machine
    ```bash
    git clone https://github.com/your-username/common-utils.git
    cd common-utils
    ```
3. Create a new feature branch (`git checkout -b feature/fooBar`)
    ```bash
    git checkout -b feature/fooBar
    ```
4. Make your changes and commit (`git commit -am 'Add some fooBar feature'`)
    ```bash
    git add .
    git commit -m 'Add some fooBar feature'
    ```
5. Push your changes to your forked repository (`git push origin feature/fooBar`)
    ```bash
    git push origin feature/fooBar
    ```
6. Create a new Pull Request on GitHub

## License
This project is licensed under the MIT License.

## Contact
For any questions regarding this project, feel free to reach out:
- Email: [hubertwongcn@gmail.com](mailto:hubertwongcn@gmail.com)
- GitHub: [Hubert Wong](https://github.com/hubertwongcn)