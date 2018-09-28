-- Set password on root user on all hosts
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password AS '*358D2E69BAE0D45CF5FD2374EE03AC44DFB05182';
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password AS '*358D2E69BAE0D45CF5FD2374EE03AC44DFB05182';

-- Create new user middleware, if exists - clear first
DROP USER IF EXISTS 'middleware'@'%';
CREATE USER IF NOT EXISTS 'middleware'@'%' IDENTIFIED WITH mysql_native_password AS '*EA5D2FBF4FADEEF2D67286CEE2F426451E27C91C';
GRANT ALL ON user_management_db.* TO 'middleware'@'%';
