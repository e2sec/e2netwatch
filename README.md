 # netgen                                                                                                            
 Collection of docker containers speaking to each other and the Internet using different protocols in order to generate "real world" network traffic which then can be used for netflow/sflow analysis or anything else for that matter.
 ## Why                                                                                                              
 Generating network traffic, especially netflow and/or sflow using tools is good for *on the fly* testing but in order to have the data resembling real world best way is to have real world data, or at least to be as close as possible.
 ## How it works                                                                                                     
 ### Servers                                                                                                         
 There are (for now) three servers and as many clients as you decide to start (or your host can handle). Also there is a jornalbeat container which is forwarding systemd journald logs to logstash
 using beats protocol.
 During the start process severs connect to public http/ftp server [http://speedtest.tele2.net](http://speedtest.tele2.net) to download files clients will eventually download from them.
 After the process of downloading is finished corresponding applications are started (ftp, nginx, samba).
 ### Clients                                                                                                         
 Traffic you will get running client/s:
 * ICMP 
 * SAMBA
 * FTP 
 * DNS 
 * HTTP 
 * ARP
 As said before number of clients depends on the amount of the traffic you need and your host machine can put up with.
 All clients run ```exec netgen.sh``` bash script which has hard-coded values for server names and files.            
 So if you want to add more servers or more files you will have to edit ```netgen.sh``` but also each of the servers entrypoint scripts (they download same files).  In the future this will hopefully be automated.
 ```netgen.sh```  will "randomly" try do connect to each of the servers (public http/ftp included).                  
 Each time it tries to downloads a file it will shuffle the order of files and order of servers. It will also sleep between actions (one to twenty seconds).
 Client will also try to ping some domains which are also hard-coded.                                                
 So running 5 clients will give you some randomness.                                                                 
                                                                                                                     
 ### How to run                                                                                                      
 ##### Requirement: 
 This expects to be able to connect to logstash port 5000 and 5001, so e2netwatch should be already running! 
 If you want to log firewall (iptables) to systemd-journal you will have to put some rules into DOCKER-USER chain.
 One example which I think is enough for testing: 
  ```sudo  iptables -I DOCKER-USER -p tcp --tcp-flags FIN FIN -j LOG --log-prefix "Connection ended: "```
  ```sudo iptables -I DOCKER-USER -p tcp --tcp-flags FIN FIN -j LOG --log-prefix "Connection ended: "```
  This will log only start and end of each connection made inside docker network (global). 
  If you have a really powerful server you can do something like this:
  ```sudo  iptables -I DOCKER-USER -j LOG --log-prefix "Docker every packet"```
  If you think this is not very smart, you're right! This is really an overkill! But be free to experiment with different rules, just remember to put them in front of the rule/chain you'd like to see traffic from.  
  To delete the rule, you can reboot or replace an ```-I``` with ```-D```, ie. ```sudo  iptables -D DOCKER-USER -j LOG --log-prefix "Docker every packet"```                                                                                       
 1. Follow [instructions](https://docs.docker.com/install/linux/docker-ce/ubuntu/) to get the latest (and greatest) docker-ce and [these](https://docs.docker.com/compose/install/#install-compose) for getting latest docker-compose.
 2. ```git clone https://${username}@bitbucket.e2security.de/scm/nw/netgen.git```                                    
 3. if you want 5 clients and be able to see what they are doing on stdout: ```sudo docker-compose up --scale client=5```.  This is because docker logging driver will not start if it can not connect    to collecting server. It can be changed, of course in docker-compose.yml file.
 4. if you want to demonize (you probably want) ```sudo docker-compose up -d --scale client=5```                     
 5. By default it will try to send netflow data to e2nwcapture container (this can be changed in docker-compose file ie. LOGSTASH=e2nwcapture)
                                                                                                 
 ## If you want to use nprobe from host and not container                                                            
 1. ### send live traffic with nprobe to elastic                                                                     
 * [setup nprobe](http://packages.ntop.org)                                                                          
 * start application (for example: ```sudo docker-compose up -d --scale client=5```)                                 
 * start nprobe: ```sudo nprobe -i br-0b85348c8afb -V 10  --redis localhost --elastic "nProbe;nprobe;http://127.0.0.1:9200/_bulk"  -b 1 --json-labels -t 60``` where **br-0b85348c8afb**
 is a bridge device you can find with:                                                                               
 ```ip a | grep `sudo docker network inspect netgen | awk '/Gateway/ {print $2}'|sed 's/"//g'`|awk '{print $7}'```
                                                                                                                     
                                                                                                                     
 ## Create netflow export with iptables kernel module (if you wanna play around, not needed)                         
 You will need do install and configure [ipt-netflow](https://github.com/aabc/ipt-netflow).                          
 It will enable you to connect any of the many tools for netflow/sflow import (pmacct, ntopng,...) to your linux interface (default udp 127.0.0.1:2055). It will be same as connecting on a enterprise grade router and collecting netflow  data.
                                                                                                                     
 ### TODO                                                                                                            
 Make images smaller, optimize build...

