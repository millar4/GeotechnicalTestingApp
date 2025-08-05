#!/bin/sh
set -e

BACKUP_DIR="/backup"
DATA_DIR="/var/lib/mysql"
MYSQL_HOST="mysql"
MYSQL_USER="root"
MYSQL_PASSWORD="rootpassword"
MYSQL_DATABASE="GeotechnicalTests"
CSV_DIR="/var/lib/mysql-files"

# Explicit table list from schema
TABLES="GeotechnicalTable InSituTable EarthworksTable RocksTable ConcreteTable AggregateTable users"

# Find the latest backup file
BACKUP_FILE=$(ls -t "$BACKUP_DIR"/mysql_data_backup_*.tar.gz 2>/dev/null | head -n 1)

if [ -f "$BACKUP_FILE" ]; then
  echo "Found backup: $BACKUP_FILE"
  echo "Clearing existing MySQL data..."
  rm -rf "$DATA_DIR"/*

  echo "Restoring backup..."
  tar -xzf "$BACKUP_FILE" -C "$DATA_DIR"

  echo "Setting permissions..."
  chown -R mysql:mysql "$DATA_DIR"
else
  echo "No backup file found in $BACKUP_DIR. Skipping backup restore."
fi

# Wait for MySQL to start
echo "Waiting for MySQL to start..."
until mysqladmin ping -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" --silent; do
  echo "MySQL is not ready yet, waiting..."
  sleep 3
done

echo "MySQL is up! Starting CSV import to refresh data..."

# Create GeotechnicalTests database if it doesn't exist
mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;"

# Import CSV files for defined tables
for table_name in $TABLES; do
  csv_file="$CSV_DIR/${table_name}.csv"
  if [ -f "$csv_file" ]; then
    echo "Importing $csv_file into $table_name..."
    mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" \
      -e "TRUNCATE TABLE $table_name; LOAD DATA INFILE '$csv_file' INTO TABLE $table_name FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES;"
  else
    echo "CSV file for $table_name not found, skipping..."
  fi
done

echo "Restore and CSV refresh complete."
