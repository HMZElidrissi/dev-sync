<p align="center"><a href="#" target="_blank"><img src="src/main/resources/static/images/dev-sync.png" width="400" alt="dev-sync Logo"></a></p>

# DevSync

DevSync is a collaborative task management system designed for development teams. It provides features for creating,
assigning, and tracking tasks, with a unique token system for task modifications and deletions.

## Version 1.1.0

- User Management: Create, view, edit, and delete user accounts
- Task Management:
    - Create tasks with multiple tags
    - Assign tasks to users
    - Mark tasks as completed
    - View task details and lists
- Task Constraints:
    - Tasks cannot be created in the past
    - Tasks must have at least 2 tags
    - Tasks cannot be scheduled more than 3 days in advance
    - Tasks must be marked as completed before their due date
- Token System:
    - Users have 2 tokens per day for replacing manager-assigned tasks
    - Users have 1 token per month for deleting tasks
    - Deleting self-created tasks doesn't use tokens

## Version 1.2.0

1. Enhanced Manager Task Replacement
    - Managers must assign replaced tasks to another user
    - Replaced tasks become immutable (cannot be modified or deleted with tokens)

2. Dynamic Token System
    - Token balance doubles if a manager doesn't respond to task change requests within 12 hours

3. Automated Task Management
    - System automatically marks overdue tasks as incomplete every 24 hours

4. Manager Overview Dashboard
    - View all tasks assigned to employees
    - Task completion percentage filterable by tags, week, month, and year
    - Track token usage per user

### Technologies

- Jakarta EE 9+
- Java 11+
- TomEE 9+
- Maven
- Docker
- PostgreSQL

### Setup and Installation

1. Clone the repository:
   ```
   git clone https://github.com/HMZElidrissi/dev-sync.git
   ```

2. Navigate to the project directory:
   ```
   cd dev-sync
   ```

3. Build the project:
   ```
   mvn clean package
   ```

4. Start the project with Docker:
   ```
   docker-compose up --build
   ```

5. Access the application at `http://localhost:8084/dev-sync`

6. To deploy the build artifact to the local Maven repository:
   ```
   mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=war
   ```
   In this case:
    ```
   mvn install:install-file -Dfile=target/dev-sync.war -DgroupId=ma.hmzelidrissi -DartifactId=dev-sync -Dversion=1.1.0 -Dpackaging=war
    ```

