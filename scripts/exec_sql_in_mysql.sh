#!/bin/bash

# Execute SQL in MySQL pod
# Usage: bash exec_sql_in_mysql.sh username password database /path/from/test.sql

db_username="$1"
db_password="$2"
db_name="$3"
sql_file="$4"

sql_file_name=$(basename "${sql_file}")

function pod() {
  local selector="$1"
  local query='?(@.status.phase=="Running")'
  oc get pods --selector $selector -o jsonpath="{.items[$query].metadata.name}";
}

mpod=$(pod "deploymentconfig=mysql")

echo "Database Pod: ${mpod}"

echo "Copy ${sql_file_name} into pod..."
# Or use `oc rsync` command
# See also: https://learn.openshift.com/introduction/transferring-files/
oc cp "${sql_file}" "${mpod}:/tmp/${sql_file_name}"

echo "Execute ${sql_file_name} in database..."
oc exec "${mpod}" -- bash -c "mysql -u${db_username} -p${db_password} ${db_name} < /tmp/${sql_file_name}"

echo "Completed"