#### REST API of Norikra ####


### TARGETS ###

# list targets
GET http://localhost:26578/api/targets

# open target
POST http://localhost:26578/api/open
{ "target" : "mytarget", "fields" : "", "auto_field" : "true" }

# close target
POST http://localhost:26578/api/close
{ "target" : "mytarget" } 


### QUERIES ###

# list queries
GET http://localhost:26578/api/queries

# add query
POST http://localhost:26578/api/register
{
    "query_name" : "my service counterX",
    "query_group" : "counter",
    "expression" : "select timestamp, count(*, appname=\"ftp\") as ftp_count,  count(*, appname=\"samba\") as samba_count,  count(*, appname=\"nginx\") as nginx_count  from syslog.win:time_batch(15 sec)"    
}

# remove query
POST http://localhost:26578/api/deregister
{ "query_name" : "my service counterX" }


More REST API functions are available from the sources:
https://github.com/norikra/norikra/blob/master/lib/norikra/webui/api.rb
