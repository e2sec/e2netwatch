#
# Docker file to build the Capture environment.
#
####################################################################################################
#
FROM docker.elastic.co/logstash/logstash:6.3.1
#
ADD filesystem/netflow.rb  /usr/share/logstash/vendor/bundle/jruby/2.3.0/gems/logstash-codec-netflow-3.14.1/lib/logstash/codecs/netflow.rb
#
RUN logstash-plugin install logstash-filter-cidr
RUN logstash-plugin install logstash-filter-translate
#