-- Set password on root user on all hosts
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password AS '*358D2E69BAE0D45CF5FD2374EE03AC44DFB05182';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password AS '*358D2E69BAE0D45CF5FD2374EE03AC44DFB05182';

-- Create new user middleware, if exists - clear first
DROP USER IF EXISTS 'middleware'@'%';
CREATE USER IF NOT EXISTS 'middleware'@'%' IDENTIFIED WITH mysql_native_password AS '*B6B7FE644D1C9144543F844AC153A09DC7DFEF29';
GRANT ALL ON user_management_db.* TO 'middleware'@'%';
GRANT ALL ON aql_db.* TO 'middleware'@'%';
