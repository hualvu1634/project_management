ALTER TABLE project_management.tasks
ADD COLUMN  description TEXT;
ALTER TABLE project_management.task_logs
ADD COLUMN  file_path TEXT;