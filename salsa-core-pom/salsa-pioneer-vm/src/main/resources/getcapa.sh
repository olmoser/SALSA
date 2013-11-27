#/bin/bash
# waiting for capa
# Usage: getcapa.sh <capaId>
. /etc/salsa.variables

if [ -z "$SALSA_PIONEER_RUN" ]; then
	SALSA_PIONEER_RUN=salsa-pioneer-vm-0.0.1-SNAPSHOT.jar
fi

if [ -z "$SALSA_WORKING_DIR" ]; then
	SALSA_WORKING_DIR=/opt/salsa
fi

java -jar $SALSA_WORKING_DIR/$SALSA_PIONEER_RUN waitcapa $1


