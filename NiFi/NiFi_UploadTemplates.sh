#!/bin/bash

#custom folder can be given by parameters
while getopts a:d:h option
do
	case "${option}"
	in
	a) NIFIADDRESS=${OPTARG}
	;;
	d) TEMPLATEFOLDER=${OPTARG}
	;;
	h)  
		echo
		echo -e "\tThis script will upload all .xml files in  given folder"
		echo -e "\tupon failure of a request this script will output http-code and content into a ResponseLog"
		echo 
		echo -e "\t-d [FOLDER] custom directory, standard is \"templates\" folder in the script's Directory"
		echo -e "\t-a [ADDRESS] Nifi-address and port. default: localhost:8090"
		echo -e "\t-h this help"
		exit 
	;;
	esac
done

##gets current directory
ABSPATH=$( realpath $0 )
SCRIPTDIR=$( dirname $ABSPATH )

#sets templatefolder to default Location, if No Argument set
: ${TEMPLATEFOLDER:=$SCRIPTDIR/templates}
#sets NiFi Address and Port to default, if No Argument set
: ${NIFIADDRESS:=http://localhost:8090}

echo uploading Templates from $TEMPLATEFOLDER
echo uploading via NiFiPort $NIFIADDRESS

#uploads Templates from $TEMPLATEFOLDER
for File in $TEMPLATEFOLDER/*.xml; do
		echo ">> uploading $File"
		RESPONSE=$(curl -s -X POST \
		 $NIFIADDRESS/nifi-api/process-groups/root/templates/upload \
		 -H 'cache-control: no-cache' \
		 -H 'content-type: multipart/form-data' \
		 -F template=@$File \
		 --write-out %{http_code} \
		 --output temp\
		 )
		 
		case "$RESPONSE"
		in
		201) echo successfully uploaded;;
		*)echo Code $RESPONSE; cat temp; echo;
		  cat temp >> ResponseLog; echo >> ResponseLog;;
		esac
		rm temp
	done 
