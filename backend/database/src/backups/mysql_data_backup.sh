#!/bin/sh
set -e

TIMESTAMP=$(date +'%Y%m%d%H%M%S')
BACKUP_DIR="./local_backups"
TAR_BACKUP_FILE="mysql_data_backup_${TIMESTAMP}.tar.gz"
SQL_BACKUP_FILE="mysql_data_backup_${TIMESTAMP}.sql"

CONTAINER_NAME="GeoTechnicalDatabase"
MYSQL_USER="root"
MYSQL_PASSWORD="rootpassword"
MYSQL_DATABASE="GeotechnicalTests"

# Ensure the backup directory exists
mkdir -p "$BACKUP_DIR"

echo "Backing up MySQL data directory from running container..."
docker exec "$CONTAINER_NAME" sh -c "tar -czf - -C /var/lib/mysql ." > "$BACKUP_DIR/$TAR_BACKUP_FILE"

# Check if tar.gz backup was created
if [ -f "$BACKUP_DIR/$TAR_BACKUP_FILE" ]; then
  chmod 600 "$BACKUP_DIR/$TAR_BACKUP_FILE"
  echo "Data directory backup saved to $BACKUP_DIR/$TAR_BACKUP_FILE"
else
  echo "Backup failed. No tar.gz file created."
  exit 1
fi

echo "Creating SQL dump of current database state..."
docker exec "$CONTAINER_NAME" sh -c \
  "mysqldump -u$MYSQL_USER -p$MYSQL_PASSWORD $MYSQL_DATABASE" > "$BACKUP_DIR/$SQL_BACKUP_FILE"

# Check if SQL backup was created
if [ -f "$BACKUP_DIR/$SQL_BACKUP_FILE" ]; then
  chmod 600 "$BACKUP_DIR/$SQL_BACKUP_FILE"
  echo "SQL backup saved to $BACKUP_DIR/$SQL_BACKUP_FILE"
else
  echo "SQL dump failed. No .sql file created."
  exit 1
fi

echo "Backup complete: $TAR_BACKUP_FILE and $SQL_BACKUP_FILE"
