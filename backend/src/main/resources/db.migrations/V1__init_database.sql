

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(30) NOT NULL,
    phone_number VARCHAR(10) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    users_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    teamsize INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_projects_users FOREIGN KEY (users_id) REFERENCES users(id)
);

CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    project_role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_members_projects FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_members_users FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    priority VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    create_date DATE NOT NULL,
    assignee_id BIGINT,
    CONSTRAINT fk_tasks_projects FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_tasks_users FOREIGN KEY (assignee_id) REFERENCES users(id)
);

CREATE TABLE task_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATE NOT NULL,
    CONSTRAINT fk_task_logs_tasks FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_task_logs_users FOREIGN KEY (user_id) REFERENCES users(id)
);