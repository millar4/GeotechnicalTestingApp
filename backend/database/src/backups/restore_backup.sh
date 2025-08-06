#!/bin/sh
set -e

MYSQL_HOST="mysql"
MYSQL_USER="root"
MYSQL_PASSWORD="rootpassword"
MYSQL_DATABASE="GeotechnicalTests"
BACKUP_DIR="/backup"
CSV_DIR="/var/lib/mysql-files"
SQL_BACKUP_FILE="${BACKUP_DIR}/mysql_data_backup_20250806124925.sql"
TABLES="GeotechnicalTable InSituTable EarthworksTable RocksTable ConcreteTable AggregateTable users"

echo "========== MySQL Restore Script Started =========="

echo "Waiting for MySQL to start..."
until mysqladmin ping -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" --silent; do
  echo "MySQL is not ready yet, waiting..."
  sleep 3
done
echo "MySQL is up!"

# Create the database if it doesn't exist
mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;"

# Create a backup of current state before restoring
timestamp=$(date +%Y%m%d_%H%M%S)
echo "Creating a safety backup before restore..."
tar -czf "$BACKUP_DIR/mysql_data_safety_${timestamp}.tar.gz" -C /var/lib/mysql .
echo "Safety backup created: $BACKUP_DIR/mysql_data_safety_${timestamp}.tar.gz"

# Check if the SQL backup file exists and restore from it
if [ -f "$SQL_BACKUP_FILE" ]; then
  echo "Found SQL backup: $SQL_BACKUP_FILE"
  echo "Restoring from SQL backup..."
  mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" < "$SQL_BACKUP_FILE"
  echo "SQL restore complete."
else
  echo "SQL backup file $SQL_BACKUP_FILE not found. Skipping restore from SQL backup."
fi

# Check and import CSV files if needed
echo "Checking and importing CSV files if needed..."
for table_name in $TABLES; do
  csv_file="$CSV_DIR/${table_name}.csv"
  if [ -f "$csv_file" ]; then
    row_count=$(mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" \
      -N -e "SELECT COUNT(*) FROM $table_name 2>/dev/null || echo 0;")
    
    if [ "$row_count" -eq 0 ]; then
      echo "Table $table_name is empty. Importing from $csv_file..."
      mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" \
        -e "LOAD DATA INFILE '$csv_file' INTO TABLE $table_name FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES;"
    else
      echo "Table $table_name already has data. Skipping CSV import."
    fi
  else
    echo "CSV file for $table_name not found, skipping..."
  fi
done

echo "========== Restore Process Complete =========="
