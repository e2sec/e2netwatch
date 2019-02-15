#!/bin/bash

#Can be used to start/stop ALL components in the root group
#Much shorter than the other scripts...

#use arguments
while getopts s:a:h option
do
	case "${option}"
	in
	a) NIFIADDRESS=${OPTARG}
		;;
	s) STATE=${OPTARG}
		;;	
	h)  echo -e "\n\tThis script is used to change the state of the NiFi root processor-group. This subsequently changes the state of all NiFi-Objects"
		echo
		echo -e "\t-s [STATE] default : STOPPED"
		echo -e "\t\tavailable states: RUNNING STOPPED DISABLED ENABLED"
		echo -e "\t-a [ADDRESS] Nifi-address and port. default: localhost:8090"
		echo -e "\t-h this help"
		echo
		exit 0
		;;
	esac
done


#sets NiFi Address and Port to default, if not set
: ${NIFIADDRESS:="http://localhost:8090"}

#STATE=$1  # If State not set or null, use STOPPED.
: ${STATE:="STOPPED"}

curl -X PUT \
  $NIFIADDRESS/nifi-api/flow/process-groups/root \
  -H "content-type: application/json" \
  -d "{\"id\":\"root\",\"state\":\"$STATE\"}"
echo
