#!/bin/sh
set -e

MYSQL_HOST="mysql"
MYSQL_USER="root"
MYSQL_PASSWORD="rootpassword"
MYSQL_DATABASE="GeotechnicalTests"
TEMP_DATABASE="GeotechnicalTempRestore"
BACKUP_DIR="/backup"
CSV_DIR="/var/lib/mysql-files"

# Find latest backup file
echo "Listing backup files in $BACKUP_DIR:"
ls -l "$BACKUP_DIR"

SQL_BACKUP_FILE=$(ls -t ${BACKUP_DIR}/mysql_data_backup_*.sql 2>/dev/null | head -n 1)
echo "Using backup file: $SQL_BACKUP_FILE"

# Tables and their primary key columns (adjust keys as needed)
# If a table’s PK is not here, defaults to 'id'
TABLES="GeotechnicalTable InSituTable EarthworksTable RocksTable ConcreteTable AggregateTable users"
declare -A PKS
PKS["GeotechnicalTable"]="id"
PKS["InSituTable"]="id"
PKS["EarthworksTable"]="id"
PKS["RocksTable"]="id"
PKS["ConcreteTable"]="id"
PKS["AggregateTable"]="id"
PKS["users"]="id"    # example: users table uses user_id instead of id

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

# Backup current live DB state before merge (just in case)
timestamp=$(date +%Y%m%d_%H%M%S)
echo "Backing up current live DB to $BACKUP_DIR/mysql_current_state_${timestamp}.sql"
mysqldump -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" > "$BACKUP_DIR/mysql_current_state_${timestamp}.sql"

if [ -z "$SQL_BACKUP_FILE" ]; then
  echo "No backup SQL file found in $BACKUP_DIR, skipping restore from backup."
else
  echo "Restoring backup file into temporary database $TEMP_DATABASE..."
  mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$TEMP_DATABASE" < "$SQL_BACKUP_FILE"
  echo "Restore to temp database complete."

  echo "Listing tables in $TEMP_DATABASE after restore:"
  mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SHOW TABLES IN $TEMP_DATABASE;"

  echo "Showing table schemas in $TEMP_DATABASE (for debug):"
  for table in $TABLES; do
    exists=$(mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -N -e \
      "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '$TEMP_DATABASE' AND table_name = '$table';")
    if [ "$exists" -eq 1 ]; then
      echo "Schema of $table:"
      mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "SHOW CREATE TABLE $TEMP_DATABASE.$table\G"
    else
      echo "Table $table not found in temp database."
    fi
  done

  echo "Merging data from backup into live database (live data has priority)..."
  for table in $TABLES; do
    exists=$(mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -N -e \
      "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '$TEMP_DATABASE' AND table_name = '$table';")

    if [ "$exists" -eq 1 ]; then
      pk="${PKS[$table]:-id}"  # Use defined PK or default to 'id'
      echo "Merging new rows from $table using primary key '$pk'..."
      mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e \
        "INSERT INTO $MYSQL_DATABASE.$table SELECT t.* FROM $TEMP_DATABASE.$table t LEFT JOIN $MYSQL_DATABASE.$table m ON t.$pk = m.$pk WHERE m.$pk IS NULL;"
      echo "Merged new rows from $table."
    else
      echo "Table $table not found in backup database, skipping."
    fi
  done
fi

# Drop temp database after merge
mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" -e "DROP DATABASE IF EXISTS $TEMP_DATABASE;"

echo "Checking CSV imports for empty tables..."
for table_name in $TABLES; do
  csv_file="$CSV_DIR/${table_name}.csv"
  if [ -f "$csv_file" ]; then
    row_count=$(mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -N -e "SELECT COUNT(*) FROM $table_name;")
    if [ "$row_count" -eq 0 ]; then
      echo "Table $table_name is empty, importing from CSV $csv_file..."
      mysql -h "$MYSQL_HOST" -u "$MYSQL_USER" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -e \
        "LOAD DATA INFILE '$csv_file' INTO TABLE $table_name FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n' IGNORE 1 LINES;"
    else
      echo "Table $table_name already has data, skipping CSV import."
    fi
  else
    echo "CSV file for $table_name not found, skipping."
  fi
done

echo "========== Merge Restore Complete =========="
