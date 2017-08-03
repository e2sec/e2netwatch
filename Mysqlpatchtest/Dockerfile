## MySQL 5.7
#
# Pull the mysql:5.7 image
#
FROM mysql:5.7
#
# The maintainer name and email
#
MAINTAINER Hrvoje Zeljko <hrvoje.zeljko@e-ito.hr
#
# Install necessary tools
#
RUN apt-get update && \
    apt-get install -y libpython-stdlib \
                       python \
			           wget
#
# Download and install mysql-connector-python and mysql-utilitis
#
RUN wget https://dev.mysql.com/get/Downloads/Connector-Python/mysql-connector-python_2.1.6-1debian8.2_all.deb
RUN dpkg -i mysql-connector-python_2.1.6-1debian8.2_all.deb 
RUN wget https://dev.mysql.com/get/Downloads/MySQLGUITools/mysql-utilities_1.6.5-1debian8_all.deb
RUN dpkg -i mysql-utilities_1.6.5-1debian8_all.deb
#
# Add the scripts
#
ADD scripts /tmp/scripts
#
####################################################################################################