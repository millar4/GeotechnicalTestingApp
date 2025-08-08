#!/bin/sh
set -e

MYSQL_HOST="mysql"
MYSQL_USER="root"
MYSQL_PASSWORD="rootpassword"
MYSQL_DATABASE="GeotechnicalTests"
TEMP_DATABASE="GeotechnicalTempRestore"
BACKUP_DIR="/backup"
CSV_DIR="/var/lib/mysql-files"
SQL_BACKUP_FILE=$(ls -t ${BACKUP_DIR}/mysql_data_backup_*.sql 2>/dev/null | head -n 1)

TABLES="GeotechnicalTable InSituTable EarthworksTable RocksTable ConcreteTable AggregateTable users"

echo "========== MySQL Merge Restore Script Started =========="

echo "Waiting for MySQL to start..."
until mysqladmin ping -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" --silent; do
  echo "MySQL is not ready yet, waiting..."
  sleep 3
done
echo "MySQL is up!"

# Ensure main and temp databases exist
mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "CREATE DATABASE IF NOT EXISTS $MYSQL_DATABASE;"
mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "DROP DATABASE IF EXISTS $TEMP_DATABASE; CREATE DATABASE $TEMP_DATABASE;"

# Create SQL backup of current state before any changes
timestamp=$(date +%Y%m%d_%H%M%S)
echo "Exporting current DB state to SQL before restore..."
mysqldump -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" > "$BACKUP_DIR/mysql_current_state_${timestamp}.sql"
echo "Current state backup created at $BACKUP_DIR/mysql_current_state_${timestamp}.sql"

# Restore old backup to temporary database
if [ -f "$SQL_BACKUP_FILE" ]; then
  echo "Found previous SQL backup: $SQL_BACKUP_FILE"
  echo "Restoring into temporary database $TEMP_DATABASE..."
  mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$TEMP_DATABASE" < "$SQL_BACKUP_FILE"
  echo "Restore to temp database complete."
else
  echo "SQL backup file not found. Skipping restore from SQL."
fi

# Merge each table from TEMP_DATABASE into MAIN_DATABASE
echo "Merging data from old backup into live database..."
for table in $TABLES; do
  echo "Merging table: $table"
  # Check if table exists in both databases
  exists=$(mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -N -e \
    "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '$TEMP_DATABASE' AND table_name = '$table';")

  if [ "$exists" -eq 1 ]; then
    # Try to insert old rows only if they don't exist (based on PK)
    mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e \
      "INSERT IGNORE INTO $MYSQL_DATABASE.$table SELECT * FROM $TEMP_DATABASE.$table;"
    echo "Merged: $table"
  else
    echo "Table $table does not exist in backup. Skipping..."
  fi
done

# Optionally: Drop temp database
mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "DROP DATABASE IF EXISTS $TEMP_DATABASE;"

# CSV Import if table still empty
echo "Checking CSV imports..."
for table_name in $TABLES; do
  csv_file="$CSV_DIR/${table_name}.csv"
  if [ -f "$csv_file" ]; then
    row_count=$(mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" \
      -N -e "SELECT COUNT(*) FROM $table_name;")
    
    if [ "$row_count" -eq 0 ]; then
      echo "Table $table_name is empty. Importing from $csv_file..."
      mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" \
        -e "LOAD DATA INFILE '$csv_file' INTO TABLE $table_name FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES;"
    else
      echo "Table $table_name already has data. Skipping CSV import."
    fi
  else
    echo "CSV for $table_name not found. Skipping..."
  fi
done

echo "========== Merge Restore Complete =========="
