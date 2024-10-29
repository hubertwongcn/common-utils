# Common Utils

## Introduction
Common Utils is a utility library that includes various commonly used utility classes and configurations, aiming to simplify the development process and improve efficiency.

## Installation and Configuration

### Prerequisites
- JDK 17
- Maven 3.x

### Clone the Repository
Run the following commands in your terminal:
```bash
git clone https://github.com/your-username/common-utils.git
cd common-utils
```
Make sure [Git](https://git-scm.com/) is installed.

### Configuration
All configurations are managed in the `src/main/resources/application.yml` file. Adjust the settings according to your specific requirements.

### Setting Up Redis
The project uses Redis. Relevant configuration files are located in the `src/main/resources/redis` directory. You can start Redis quickly using Docker Compose:
```bash
cd src/main/resources/redis
docker-compose up -d
```
Make sure Docker and Docker Compose are installed.

### Setting Up RocketMQ
The project also uses RocketMQ. Relevant configuration files are located in the `src/main/resources/rocketmq` directory. Similarly, you can start RocketMQ using Docker Compose:
```bash
cd src/main/resources/rocketmq
docker-compose up -d
```

### Configuring IntelliJ IDEA for Environment Variables and Program Arguments
To configure environment variables and program arguments in IntelliJ IDEA:
1. Open IntelliJ IDEA and select `Run` -> `Edit Configurations...` from the menu bar.
2. In the list of configurations on the left, select the one you want to modify, or click the `+` button at the top-left to create a new one.
3. In the configuration panel on the right, find the `Environment` section.
4. Click the `...` button next to `Environment Variables` to open the environment variables setup window.
5. In the environment variables setup window, click the `+` button to add new environment variables, such as:
   - Name: `REDIS_HOST`
   - Value: `your-redis-host`
     Repeat this step to add other needed environment variables:
   - `REDIS_PORT`, `REDIS_PASSWORD`, `ROCKETMQ_PRODUCER_NAME_SERVER`, `ROCKETMQ_CONSUMER_NAME_SERVER`, etc.
6. After setting all the environment variables, click `OK` to save.

If you prefer configuring using command line arguments:
1. In the `Edit Configurations...` window, find the `Program arguments` text box.
2. In the `Program arguments` text box, add startup parameters, e.g.:
```sh
--REDIS_HOST=your-redis-host --REDIS_PORT=6379 --REDIS_PASSWORD=your-password --ROCKETMQ_PRODUCER_NAME_SERVER=your-rocketmq-server --ROCKETMQ_CONSUMER_NAME_SERVER=your-rocketmq-server
```
3. After configuring, click `Apply` and then `OK` to save the settings.

After completing these settings, IntelliJ IDEA will use the specified environment variables and startup parameters when you run or debug the configuration.

### Alternative Ways to Set Environment Variables

#### Using Maven to Pass Environment Variables
If you prefer using Maven to pass environment variables:
1. Open your terminal and navigate to the project directory.
2. Use the following command to run the application with environment variables:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--REDIS_HOST=your-redis-host --REDIS_PORT=6379 --REDIS_PASSWORD=your-password --ROCKETMQ_PRODUCER_NAME_SERVER=your-rocketmq-server --ROCKETMQ_CONSUMER_NAME_SERVER=your-rocketmq-server"
```

#### Setting Environment Variables Directly in the Terminal
If you prefer setting environment variables directly in the terminal:
1. Export the required environment variables:
```sh
export REDIS_HOST=your-redis-host
export REDIS_PORT=6379
export REDIS_PASSWORD=your-password
export ROCKETMQ_PRODUCER_NAME_SERVER=your-rocketmq-server
export ROCKETMQ_CONSUMER_NAME_SERVER=your-rocketmq-server
```
2. Run the application:
```bash
mvn spring-boot:run
```

### Running the Application and Tests

#### Maven Wrapper
The project provides a [Maven Wrapper](https://github.com/takari/maven-wrapper) for easier and unified Maven version management.

To run the application:
```bash
./mvnw spring-boot:run
```

To run the unit tests:
```bash
./mvnw test
```

#### Using Maven Commands
Alternatively, you can use standard Maven commands:

To run the application:
```bash
mvn spring-boot:run
```

To run the unit tests:
```bash
mvn test
```

## Contributing
We welcome contributions to this project! To ensure a smooth process, please follow these steps:

### Branching Model
Our project uses the following branches:
- `master`: The main branch where the stable codebase resides.
- `develop`: The development directory where new features and bug fixes are integrated.
- `release`: The release directory used for preparing deployment.

### Steps to Contribute
1. **Fork the Repository**
   - Navigate to the main repository on GitHub and click the "Fork" button to create your own copy of the repository.

2. **Clone Your Forked Repository**
   - Clone your forked repository to your local machine using the following command:
   ```bash
   git clone https://github.com/your-username/common-utils.git
   cd common-utils
   ```

3. **Switch to the Latest `develop` Branch**
   - Ensure that you are working on the latest branch under the `develop` directory. Create a new feature branch based on this branch:
   ```bash
   git checkout develop/latest-branch
   git checkout -b feature/your-feature
   ```

4. **Make Your Changes**
   - Implement your changes in your local repository. Make sure to follow the project coding standards and add appropriate unit tests.

5. **Commit Your Changes**
   - After making your changes, commit them with a meaningful commit message:
   ```bash
   git add .
   git commit -m 'Add a meaningful message describing your changes'
   ```

6. **Push Your Changes to Your Forked Repository**
   - Push your feature branch to your forked repository on GitHub:
   ```bash
   git push origin feature/your-feature
   ```

7. **Create a Pull Request**
   - Navigate to the original repository on GitHub. You should see a prompt to create a pull request from your recently pushed branch. Click "Compare & pull request".
   - Make sure the pull request is directed to the correct branch under the `develop` directory in the original repository.
   - Provide a detailed description of your changes and any additional information that reviewers might need.
   - Submit the pull request.

### Additional Notes
- Ensure your code follows the project's style guidelines.
- Write clear and concise commit messages.
- Include appropriate unit tests for your changes.
- Run all tests locally to ensure no existing functionality is broken.
- Be responsive to feedback and questions from code reviewers.

### Syncing Your Fork Regularly
To keep your fork up-to-date with the latest changes from the `master` branch of the original repository:

1. Add the original repository as a remote:
   ```bash
   git remote add upstream https://github.com/hubertwongcn/common-utils.git
   ```

2. Fetch the latest changes from the original repository:
   ```bash
   git fetch upstream
   ```

3. Merge the latest changes into your local `master` branch:
   ```bash
   git checkout master
   git merge upstream/master
   ```

4. Push the updated `master` branch to your fork:
   ```bash
   git push origin master
   ```

For more complex situations, you may need to rebase your feature branch against the latest branch under the `develop` directory to resolve conflicts:
```bash
git checkout feature/your-feature
git rebase develop/latest-branch
```

By following this process, you ensure that your contributions are easily integrated and that your fork remains in sync with the original project's codebase.

Thank you for your contributions!

## License
This project is licensed under the MIT License.

## Contact
For any questions regarding this project, feel free to reach out:
- Email: [your-email@example.com](mailto:your-email@example.com)
- GitHub: [Your GitHub Profile](https://github.com/your-username)

# Additional Recommendations and Tips:
1. **Ensure that Docker and Docker Compose are correctly installed and that your user is part of the `docker` group to avoid permission issues when starting containers.**
2. **When configuring IntelliJ IDEA, double-check paths and names to ensure environment variables and arguments are set correctly.**
3. **Before contributing, run all local tests to ensure no new issues are introduced.**
4. **Regularly sync your fork with the upstream repository to keep it up to date with the latest changes and fixes.**

This completes the full documentation with simplified branch instructions and all necessary details. Let me know if you need any additional adjustments or further information!